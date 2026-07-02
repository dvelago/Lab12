package com.ucv.lab12.security;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.util.Base64;

public final class PasswordUtil {

    private static final String ALGORITHM = "PBKDF2WithHmacSHA256";
    private static final int ITERATIONS = 120_000;
    private static final int KEY_LENGTH = 256;
    private static final SecureRandom RANDOM = new SecureRandom();

    private PasswordUtil() {
    }

    public static PasswordHash generarHash(String clave) {
        String salt = generarSalt();
        return new PasswordHash(salt, hash(clave, salt));
    }

    public static boolean verificar(String clave, String saltBase64, String hashEsperado) {
        if (clave == null || saltBase64 == null || hashEsperado == null) {
            return false;
        }
        String hashCalculado = hash(clave, saltBase64);
        return hashCalculado.equals(hashEsperado);
    }

    private static String generarSalt() {
        byte[] salt = new byte[16];
        RANDOM.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    private static String hash(String clave, String saltBase64) {
        try {
            byte[] salt = Base64.getDecoder().decode(saltBase64);
            PBEKeySpec spec = new PBEKeySpec(clave.toCharArray(), salt, ITERATIONS, KEY_LENGTH);
            SecretKeyFactory factory = SecretKeyFactory.getInstance(ALGORITHM);
            byte[] hash = factory.generateSecret(spec).getEncoded();
            return Base64.getEncoder().encodeToString(hash);
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("No se pudo generar el hash de la clave", e);
        }
    }

    public record PasswordHash(String salt, String hash) {
    }
}
