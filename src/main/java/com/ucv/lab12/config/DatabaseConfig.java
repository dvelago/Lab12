package com.ucv.lab12.config;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;

public class DatabaseConfig implements AutoCloseable {

    private static final String URL =
            "jdbc:sqlserver://localhost:53322;"
                    + "instanceName=.\\SQLEXPRESS;"
                    + "databaseName=Pruebalogro;"
                    + "user=DarisV;"
                    + "password=123456789;"
                    + "trustServerCertificate=true;"
                    + "encrypt=false;";

    // AGREGADO: Se añadió 'static' para que se pueda usar directamente en los repositorios
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    @Override
    public void close() {
        try {
            Enumeration<Driver> drivers = DriverManager.getDrivers();
            while (drivers.hasMoreElements()) {
                Driver driver = drivers.nextElement();
                try {
                    DriverManager.deregisterDriver(driver);
                } catch (SQLException ignored) {
                }
            }
        } catch (Exception ignored) {
        }
    }
}