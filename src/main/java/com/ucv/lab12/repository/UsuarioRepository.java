package com.ucv.lab12.repository;

import com.ucv.lab12.config.DatabaseConfig;
import com.ucv.lab12.model.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

public class UsuarioRepository implements IUsuarioRepository {

    private final DatabaseConfig dbConfig;

    public UsuarioRepository(DatabaseConfig dbConfig) {
        this.dbConfig = dbConfig;
    }

    @Override
    public Optional<Usuario> findByUsuario(String usuario) {
        String sql = """
                SELECT IdUsuario, Usuario, NombreCompleto, HashClave, SaltClave, Rol, Activo,
                       UltimoAcceso, FechaCreacion
                FROM Usuario
                WHERE Usuario = ?
                """;

        try (Connection conn = dbConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, usuario == null ? "" : usuario.trim());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al consultar el usuario", e);
        }
        return Optional.empty();
    }

    @Override
    public void save(Usuario usuario) {
        String sql = """
                INSERT INTO Usuario (Usuario, NombreCompleto, HashClave, SaltClave, Rol, Activo, FechaCreacion)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection conn = dbConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, usuario.getUsuario());
            ps.setString(2, usuario.getNombreCompleto());
            ps.setString(3, usuario.getHashClave());
            ps.setString(4, usuario.getSaltClave());
            ps.setString(5, usuario.getRol());
            ps.setBoolean(6, usuario.isActivo());
            ps.setTimestamp(7, Timestamp.valueOf(valueOrNow(usuario.getFechaCreacion())));
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    usuario.setIdUsuario(keys.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al insertar el usuario", e);
        }
    }

    @Override
    public void updateUltimoAcceso(int idUsuario) {
        String sql = """
                UPDATE Usuario
                SET UltimoAcceso = SYSDATETIME()
                WHERE IdUsuario = ?
                """;

        try (Connection conn = dbConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar el ultimo acceso", e);
        }
    }

    private Usuario mapRow(ResultSet rs) throws SQLException {
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(rs.getInt("IdUsuario"));
        usuario.setUsuario(rs.getString("Usuario"));
        usuario.setNombreCompleto(rs.getString("NombreCompleto"));
        usuario.setHashClave(rs.getString("HashClave"));
        usuario.setSaltClave(rs.getString("SaltClave"));
        usuario.setRol(rs.getString("Rol"));
        usuario.setActivo(rs.getBoolean("Activo"));

        Timestamp ultimoAcceso = rs.getTimestamp("UltimoAcceso");
        if (ultimoAcceso != null) {
            usuario.setUltimoAcceso(ultimoAcceso.toLocalDateTime());
        }

        Timestamp fechaCreacion = rs.getTimestamp("FechaCreacion");
        if (fechaCreacion != null) {
            usuario.setFechaCreacion(fechaCreacion.toLocalDateTime());
        }
        return usuario;
    }

    private LocalDateTime valueOrNow(LocalDateTime value) {
        return value == null ? LocalDateTime.now() : value;
    }
}
