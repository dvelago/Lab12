package com.ucv.lab12.util;

import com.ucv.lab12.model.ConsultaDeudaFiltro;
import com.ucv.lab12.model.Deuda;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public final class PdfExportUtil {
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private PdfExportUtil() {
    }

    public static void exportarDeudas(Path destino, List<Deuda> deudas, ConsultaDeudaFiltro filtro, String usuario) throws IOException {
        List<String> lines = new ArrayList<>();
        lines.add("UGEL ILO - REPORTE DE DEUDAS DOCENTES");
        lines.add("Generado: " + LocalDateTime.now().format(DATE_FORMAT));
        lines.add("Usuario: " + usuario);
        lines.add("Filtros: " + (filtro == null ? "Sin filtros" : "Filtrado por DNI"));
        lines.add("");

        lines.add(String.format("%-10s %-12s %-15s %-20s %-15s %-15s", "ID DEUDA", "ID DOCENTE", "MONTO", "TIPO DEUDA", "ESTADO", "FECHA REGISTRO"));
        lines.add("-".repeat(95));

        BigDecimal total = BigDecimal.ZERO;
        for (Deuda deuda : deudas) {
            lines.add(String.format("%-10d %-12d S/. %-11.2f %-20s %-15s %-15s",
                    deuda.getIdDeuda(),
                    deuda.getIdDocente(),
                    deuda.getMonto(),
                    deuda.getTipoDeuda(),
                    deuda.getEstado(),
                    deuda.getFechaRegistro().toString()
            ));
            total = total.add(BigDecimal.valueOf(deuda.getMonto()));
        }

        lines.add("-".repeat(95));
        lines.add("Monto Total Acumulado: S/. " + total);

        Files.write(destino, lines);
    }
}
