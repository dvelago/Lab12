package com.ucv.lab12.controller;

import com.ucv.lab12.config.DatabaseConfig;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class NuevoDocenteController {

    @FXML private TextField txtDni;
    @FXML private TextField txtNombres;
    @FXML private TextField txtApellidos;

    @FXML
    private void guardarDocente() {
        String dni = txtDni.getText().trim();
        String nombres = txtNombres.getText().trim();
        String apellidos = txtApellidos.getText().trim();

        if (dni.isEmpty() || nombres.isEmpty() || apellidos.isEmpty()) {
            mostrarAlerta("Error", "Todos los campos son obligatorios.", Alert.AlertType.ERROR);
            return;
        }

        String query = "INSERT INTO Docente (dni, nombres, apellidos) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, dni);
            ps.setString(2, nombres);
            ps.setString(3, apellidos);

            int filas = ps.executeUpdate();
            if (filas > 0) {
                mostrarAlerta("Éxito", "Docente registrado correctamente.", Alert.AlertType.INFORMATION);
                // Cerrar la ventanita al terminar
                Stage stage = (Stage) txtDni.getScene().getWindow();
                stage.close();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            mostrarAlerta("Error de BD", "No se pudo registrar el docente: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}