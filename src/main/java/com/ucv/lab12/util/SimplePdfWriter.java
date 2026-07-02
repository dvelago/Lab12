package com.ucv.lab12.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public final class SimplePdfWriter {

    private static final int PAGE_WIDTH = 595;
    private static final int PAGE_HEIGHT = 842;
    private static final int LEFT_MARGIN = 40;
    private static final int TOP_MARGIN = 42;
    private static final int LEADING = 14;
    private static final int MAX_LINES_PER_PAGE = 52;

    private SimplePdfWriter() {
    }

    public static void writeTextReport(Path output, List<String> lines) throws IOException {
        if (output.getParent() != null) {
            Files.createDirectories(output.getParent());
        }

        List<List<String>> pages = paginate(lines);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        List<Long> offsets = new ArrayList<>();
        offsets.add(0L);

        write(bytes, "%PDF-1.4\n");

        writeObject(bytes, offsets, 1, "<< /Type /Catalog /Pages 2 0 R >>\n");
        StringBuilder kids = new StringBuilder();
        int firstPageObject = 4;
        for (int i = 0; i < pages.size(); i++) {
            if (i > 0) {
                kids.append(' ');
            }
            kids.append(firstPageObject + (i * 2)).append(" 0 R");
        }
        writeObject(bytes, offsets, 2, "<< /Type /Pages /Kids [" + kids + "] /Count " + pages.size() + " >>\n");
        writeObject(bytes, offsets, 3, "<< /Type /Font /Subtype /Type1 /BaseFont /Courier >>\n");

        for (int i = 0; i < pages.size(); i++) {
            int pageObject = firstPageObject + (i * 2);
            int contentObject = pageObject + 1;
            String stream = buildContentStream(pages.get(i));
            byte[] streamBytes = stream.getBytes(StandardCharsets.ISO_8859_1);
            writeObject(bytes, offsets, pageObject, String.format(
                    "<< /Type /Page /Parent 2 0 R /MediaBox [0 0 %d %d] " +
                            "/Resources << /Font << /F1 3 0 R >> >> /Contents %d 0 R >>\n",
                    PAGE_WIDTH, PAGE_HEIGHT, contentObject));
            writeObject(bytes, offsets, contentObject, "<< /Length " + streamBytes.length + " >>\nstream\n"
                    + stream
                    + "endstream\n");
        }

        long xrefStart = bytes.size();
        write(bytes, "xref\n");
        write(bytes, "0 " + (offsets.size()) + "\n");
        write(bytes, "0000000000 65535 f \n");
        for (int i = 1; i < offsets.size(); i++) {
            write(bytes, String.format("%010d 00000 n \n", offsets.get(i)));
        }
        write(bytes, "trailer\n<< /Size " + offsets.size() + " /Root 1 0 R >>\n");
        write(bytes, "startxref\n" + xrefStart + "\n%%EOF");

        Files.write(output, bytes.toByteArray());
    }

    private static List<List<String>> paginate(List<String> lines) {
        List<List<String>> pages = new ArrayList<>();
        if (lines == null || lines.isEmpty()) {
            pages.add(List.of(""));
            return pages;
        }

        for (int i = 0; i < lines.size(); i += MAX_LINES_PER_PAGE) {
            int end = Math.min(lines.size(), i + MAX_LINES_PER_PAGE);
            pages.add(new ArrayList<>(lines.subList(i, end)));
        }
        return pages;
    }

    private static String buildContentStream(List<String> lines) {
        StringBuilder sb = new StringBuilder();
        sb.append("BT\n");
        sb.append("/F1 10 Tf\n");
        sb.append(LEADING).append(" TL\n");
        sb.append(LEFT_MARGIN).append(' ').append(PAGE_HEIGHT - TOP_MARGIN).append(" Td\n");

        for (int i = 0; i < lines.size(); i++) {
            String line = escape(lines.get(i));
            sb.append('(').append(line).append(") Tj\n");
            if (i < lines.size() - 1) {
                sb.append("T*\n");
            }
        }
        sb.append("\nET\n");
        return sb.toString();
    }

    private static String escape(String value) {
        if (value == null) {
            return "";
        }
        return value
                .replace("\\", "\\\\")
                .replace("(", "\\(")
                .replace(")", "\\)")
                .replace("\r", "")
                .replace("\n", " ");
    }

    private static void writeObject(ByteArrayOutputStream bytes, List<Long> offsets, int objectNumber, String body) {
        offsets.add((long) bytes.size());
        write(bytes, objectNumber + " 0 obj\n" + body + "endobj\n");
    }

    private static void write(ByteArrayOutputStream bytes, String text) {
        bytes.writeBytes(text.getBytes(StandardCharsets.ISO_8859_1));
    }
}
