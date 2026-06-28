package com.ucv.lab12.config;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;

public class DatabaseConfig implements AutoCloseable {

    /*
     * authenticationScheme=NTLM  → autenticación Windows pura Java (sin DLL nativa).
     * trustServerCertificate=true → evita error de certificado SSL en entornos locales.
     *
     * Si sigue fallando por TCP/IP deshabilitado en SQL Express:
     *   1. Abre "Administrador de configuración de SQL Server"
     *   2. Configuración de red de SQL Server -> Protocolos de SQLEXPRESS
     *   3. Habilita "TCP/IP" y reinicia el servicio SQL Server (SQLEXPRESS).
     */
    private static final String DEFAULT_URL =
            "jdbc:sqlserver://localhost:53322;"
            + "instanceName=.\\SQLEXPRESS;"
            + "databaseName=lab12;"
            + "trustServerCertificate=true;"
            + "encrypt=false;";

    private static final String URL =
            System.getenv().getOrDefault("LAB12_DB_URL", DEFAULT_URL);

    private static final String USER =
            System.getenv("LAB12_DB_USER");

    private static final String PASSWORD =
            System.getenv("LAB12_DB_PASSWORD");

    public Connection getConnection() throws SQLException {
        if (USER != null && !USER.isBlank()) {
            return DriverManager.getConnection(URL, USER, PASSWORD == null ? "" : PASSWORD);
        }
        return DriverManager.getConnection(URL);
    }

    @Override
    public void close() {
        // Deregistrar drivers registrados por DriverManager para evitar fugas al parar la app
        try {
            Enumeration<Driver> drivers = DriverManager.getDrivers();
            while (drivers.hasMoreElements()) {
                Driver driver = drivers.nextElement();
                try {
                    DriverManager.deregisterDriver(driver);
                } catch (SQLException ignored) {
                    // Ignorar errores individuales al cerrar drivers
                }
            }
        } catch (Exception ignored) {
            // No se pudo limpiar drivers, pero la aplicación puede continuar
        }
    }
}
