package com.ucv.lab12.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    @FXML private TextField txtUsuario;
    @FXML private PasswordField txtPassword;
    @FXML private Button btnLogin;

    @FXML
    private void manejarLogin() {
        String usuario = txtUsuario.getText().trim();
        String password = txtPassword.getText().trim();

        // Validamos las credenciales solicitadas
        if (usuario.equalsIgnoreCase("admin") && password.equals("123")) {
            try {
                // 1. Cargar la ventana principal (la que ya tenías)
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ucv/lab12/main-view.fxml"));
                Parent root = loader.load();

                Stage stagePrincipal = new Stage();
                stagePrincipal.setTitle("Sistema de Gestión de Deudas - UGEL");
                stagePrincipal.setScene(new Scene(root, 800, 500));
                stagePrincipal.setMinWidth(800);
                stagePrincipal.setMinHeight(500);

                // 2. Mostrar la ventana principal
                stagePrincipal.show();

                // 3. Cerrar la ventana actual de Login
                Stage stageLogin = (Stage) btnLogin.getScene().getWindow();
                stageLogin.close();

            } catch (IOException e) {
                e.printStackTrace();
                mostrarAlerta("Error", "No se pudo cargar la vista principal.", Alert.AlertType.ERROR);
            }
        } else {
            mostrarAlerta("Acceso Denegado", "Usuario o contraseña incorrectos.", Alert.AlertType.ERROR);
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