package com.ucv.lab12.repository;

import com.ucv.lab12.config.DatabaseConfig;
import com.ucv.lab12.model.CompraVideojuego;
import com.ucv.lab12.model.Videojuego;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Statement;
import java.time.LocalDateTime;

public class CompraVideojuegoRepository implements ICompraVideojuegoRepository {

    private final DatabaseConfig dbConfig;

    public CompraVideojuegoRepository(DatabaseConfig dbConfig) {
        this.dbConfig = dbConfig;
    }

    @Override
    public CompraVideojuego registrarCompra(Videojuego videojuego, String comprador, String correo,
                                            int cantidad, String metodoPago) {
        String selectSql = """
                SELECT Nombre, Precio
                FROM Videojuego
                WHERE IdVideojuego = ?
                """;
        String updateSql = """
                UPDATE Videojuego
                SET Stock = Stock - ?
                WHERE IdVideojuego = ?
                  AND Stock >= ?
                """;
        String insertSql = """
                INSERT INTO CompraVideojuego
                    (IdVideojuego, NombreVideojuego, Comprador, Correo, Cantidad,
                     PrecioUnitario, Total, MetodoPago, FechaCompra)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection conn = dbConfig.getConnection()) {
            conn.setAutoCommit(false);
            try {
                String nombre;
                double precio;

                try (PreparedStatement ps = conn.prepareStatement(selectSql)) {
                    ps.setInt(1, videojuego.getIdVideojuego());
                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) {
                            throw new IllegalArgumentException("El videojuego seleccionado no existe.");
                        }
                        nombre = rs.getString("Nombre");
                        precio = rs.getBigDecimal("Precio").doubleValue();
                    }
                }

                int updatedRows;
                try (PreparedStatement ps = conn.prepareStatement(updateSql)) {
                    ps.setInt(1, cantidad);
                    ps.setInt(2, videojuego.getIdVideojuego());
                    ps.setInt(3, cantidad);
                    updatedRows = ps.executeUpdate();
                }

                if (updatedRows == 0) {
                    int stockActual = obtenerStockActual(conn, videojuego.getIdVideojuego());
                    throw new IllegalStateException("Stock insuficiente. Disponibles: " + stockActual);
                }

                LocalDateTime fecha = LocalDateTime.now();
                double total = precio * cantidad;

                CompraVideojuego compra = new CompraVideojuego();
                compra.setIdVideojuego(videojuego.getIdVideojuego());
                compra.setNombreVideojuego(nombre);
                compra.setComprador(comprador);
                compra.setCorreo(correo);
                compra.setCantidad(cantidad);
                compra.setPrecioUnitario(precio);
                compra.setTotal(total);
                compra.setMetodoPago(metodoPago);
                compra.setFechaCompra(fecha);

                try (PreparedStatement ps = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setInt(1, compra.getIdVideojuego());
                    ps.setString(2, compra.getNombreVideojuego());
                    ps.setString(3, compra.getComprador());
                    ps.setString(4, compra.getCorreo());
                    ps.setInt(5, compra.getCantidad());
                    ps.setBigDecimal(6, java.math.BigDecimal.valueOf(compra.getPrecioUnitario()));
                    ps.setBigDecimal(7, java.math.BigDecimal.valueOf(compra.getTotal()));
                    ps.setString(8, compra.getMetodoPago());
                    ps.setTimestamp(9, Timestamp.valueOf(compra.getFechaCompra()));
                    ps.executeUpdate();

                    try (ResultSet keys = ps.getGeneratedKeys()) {
                        if (keys.next()) {
                            compra.setIdCompra(keys.getInt(1));
                        }
                    }
                }

                conn.commit();
                return compra;
            } catch (Exception e) {
                try {
                    conn.rollback();
                } catch (SQLException rollbackEx) {
                    e.addSuppressed(rollbackEx);
                }
                if (e instanceof RuntimeException runtimeException) {
                    throw runtimeException;
                }
                throw new RuntimeException("Error al registrar la compra", e);
            } finally {
                try {
                    conn.setAutoCommit(true);
                } catch (SQLException ignored) {
                    // No se pudo restaurar el modo auto-commit, pero la conexion se cerrara al salir.
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al registrar la compra", e);
        }
    }

    private int obtenerStockActual(Connection conn, int idVideojuego) throws SQLException {
        String sql = """
                SELECT Stock
                FROM Videojuego
                WHERE IdVideojuego = ?
                """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idVideojuego);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("Stock");
                }
            }
        }
        throw new IllegalArgumentException("El videojuego seleccionado no existe.");
    }
}
