package com.ucv.lab12.security;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

public final class CryptoUtil {

    private static final String TRANSFORMATION = "AES/GCM/NoPadding";
    private static final int IV_LENGTH = 12;
    private static final int TAG_LENGTH = 128;
    private static final SecureRandom RANDOM = new SecureRandom();

    private CryptoUtil() {
    }

    public static String encrypt(String value) {
        if (value == null || value.isBlank()) {
            return "";
        }

        try {
            byte[] iv = new byte[IV_LENGTH];
            RANDOM.nextBytes(iv);

            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, resolveKey(), new GCMParameterSpec(TAG_LENGTH, iv));
            byte[] encrypted = cipher.doFinal(value.getBytes(StandardCharsets.UTF_8));

            ByteBuffer buffer = ByteBuffer.allocate(iv.length + encrypted.length);
            buffer.put(iv);
            buffer.put(encrypted);
            return Base64.getEncoder().encodeToString(buffer.array());
        } catch (Exception e) {
            throw new IllegalStateException("No se pudo cifrar el dato", e);
        }
    }

    public static String decrypt(String value) {
        if (value == null || value.isBlank()) {
            return "";
        }

        try {
            byte[] combined = Base64.getDecoder().decode(value);
            byte[] iv = new byte[IV_LENGTH];
            byte[] encrypted = new byte[combined.length - IV_LENGTH];
            System.arraycopy(combined, 0, iv, 0, IV_LENGTH);
            System.arraycopy(combined, IV_LENGTH, encrypted, 0, encrypted.length);

            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, resolveKey(), new GCMParameterSpec(TAG_LENGTH, iv));
            byte[] decrypted = cipher.doFinal(encrypted);
            return new String(decrypted, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new IllegalStateException("No se pudo descifrar el dato", e);
        }
    }

    private static SecretKey resolveKey() throws Exception {
        String secret = System.getenv("UGEL_APP_SECRET");
        if (secret == null || secret.isBlank()) {
            secret = System.getProperty("ugel.app.secret", "ugel-ilo-desarrollo-2026");
        }

        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] keyBytes = digest.digest(secret.getBytes(StandardCharsets.UTF_8));
        return new SecretKeySpec(keyBytes, "AES");
    }
}
