package com.ucv.lab12.repository;

import com.ucv.lab12.config.DatabaseConfig;
import com.ucv.lab12.model.Deuda;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DeudaRepository implements IDeudaRepository {

    @Override
    public boolean registrar(Deuda deuda) {
        String query = "INSERT INTO Deuda (idDocente, monto, tipoDeuda, estado, fechaRegistro) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, deuda.getIdDocente());
            ps.setDouble(2, deuda.getMonto());
            ps.setString(3, deuda.getTipoDeuda());
            ps.setString(4, deuda.getEstado());
            ps.setDate(5, Date.valueOf(deuda.getFechaRegistro()));

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean modificar(Deuda deuda) {
        String query = "UPDATE Deuda SET monto = ?, tipoDeuda = ?, estado = ? WHERE idDeuda = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setDouble(1, deuda.getMonto());
            ps.setString(2, deuda.getTipoDeuda());
            ps.setString(3, deuda.getEstado());
            ps.setInt(4, deuda.getIdDeuda());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Deuda> listarTodas() {
        List<Deuda> lista = new ArrayList<>();
        String query = "SELECT idDeuda, idDocente, monto, tipoDeuda, estado, fechaRegistro FROM Deuda";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Deuda d = new Deuda(
                        rs.getInt("idDeuda"),
                        rs.getInt("idDocente"),
                        rs.getDouble("monto"),
                        rs.getString("tipoDeuda"),
                        rs.getString("estado"),
                        rs.getDate("fechaRegistro").toLocalDate()
                );
                lista.add(d);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    @Override
    public List<Deuda> buscarPorDniDocente(String dni) {
        List<Deuda> lista = new ArrayList<>();
        String query = "SELECT d.* FROM Deuda d INNER JOIN Docente doc ON d.idDocente = doc.idDocente WHERE doc.dni = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, dni);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Deuda d = new Deuda(
                            rs.getInt("idDeuda"),
                            rs.getInt("idDocente"),
                            rs.getDouble("monto"),
                            rs.getString("tipoDeuda"),
                            rs.getString("estado"),
                            rs.getDate("fechaRegistro").toLocalDate()
                    );
                    lista.add(d);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}