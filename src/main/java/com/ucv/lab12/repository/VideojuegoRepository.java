package com.ucv.lab12.repository;

import com.ucv.lab12.config.DatabaseConfig;
import com.ucv.lab12.model.Videojuego;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class VideojuegoRepository implements IVideojuegoRepository {

    private final DatabaseConfig dbConfig;

    public VideojuegoRepository(DatabaseConfig dbConfig) {
        this.dbConfig = dbConfig;
    }

    @Override
    public List<Videojuego> findAll() {
        return findByFilters("", "", "");
    }

    @Override
    public List<Videojuego> findByFilters(String nombre, String consola, String genero) {
        String sql = """
                SELECT IdVideojuego, Consola, Nombre, Genero, Clasificacion, Descripcion,
                       IDdesarrollador, IDdistribuidor, Precio, Stock
                FROM Videojuego
                WHERE Nombre LIKE ?
                  AND Consola LIKE ?
                  AND Genero LIKE ?
                ORDER BY Nombre
                """;

        List<Videojuego> list = new ArrayList<>();
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, "%" + normalize(nombre) + "%");
            ps.setString(2, "%" + normalize(consola) + "%");
            ps.setString(3, "%" + normalize(genero) + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al consultar videojuegos", e);
        }
        return list;
    }

    @Override
    public Optional<Videojuego> findById(int id) {
        String sql = """
                SELECT IdVideojuego, Consola, Nombre, Genero, Clasificacion, Descripcion,
                       IDdesarrollador, IDdistribuidor, Precio, Stock
                FROM Videojuego
                WHERE IdVideojuego = ?
                """;

        try (Connection conn = dbConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar videojuego", e);
        }
        return Optional.empty();
    }

    @Override
    public void save(Videojuego videojuego) {
        String sql = """
                INSERT INTO Videojuego
                    (Consola, Nombre, Genero, Clasificacion, Descripcion,
                     IDdesarrollador, IDdistribuidor, Precio, Stock)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection conn = dbConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            bind(ps, videojuego);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al insertar videojuego", e);
        }
    }

    @Override
    public void update(Videojuego videojuego) {
        String sql = """
                UPDATE Videojuego
                SET Consola = ?,
                    Nombre = ?,
                    Genero = ?,
                    Clasificacion = ?,
                    Descripcion = ?,
                    IDdesarrollador = ?,
                    IDdistribuidor = ?,
                    Precio = ?,
                    Stock = ?
                WHERE IdVideojuego = ?
                """;

        try (Connection conn = dbConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            bind(ps, videojuego);
            ps.setInt(10, videojuego.getIdVideojuego());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar videojuego", e);
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM Videojuego WHERE IdVideojuego = ?";
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar videojuego", e);
        }
    }

    private Videojuego mapRow(ResultSet rs) throws SQLException {
        return new Videojuego(
                rs.getInt("IdVideojuego"),
                rs.getString("Consola"),
                rs.getString("Nombre"),
                rs.getString("Genero"),
                rs.getString("Clasificacion"),
                rs.getString("Descripcion"),
                rs.getInt("IDdesarrollador"),
                rs.getInt("IDdistribuidor"),
                rs.getBigDecimal("Precio") != null ? rs.getBigDecimal("Precio").doubleValue() : 0d,
                rs.getInt("Stock")
        );
    }

    private void bind(PreparedStatement ps, Videojuego videojuego) throws SQLException {
        ps.setString(1, nvl(videojuego.getConsola()));
        ps.setString(2, nvl(videojuego.getNombre()));
        ps.setString(3, nvl(videojuego.getGenero()));
        ps.setString(4, nvl(videojuego.getClasificacion()));
        ps.setString(5, nvl(videojuego.getDescripcion()));
        ps.setInt(6, videojuego.getIdDesarrollador());
        ps.setInt(7, videojuego.getIdDistribuidor());
        ps.setBigDecimal(8, BigDecimal.valueOf(videojuego.getPrecio()));
        ps.setInt(9, videojuego.getStock());
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim();
    }

    private String nvl(String value) {
        return value == null ? "" : value.trim();
    }
}
