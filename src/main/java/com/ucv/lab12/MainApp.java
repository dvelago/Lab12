package com.ucv.lab12;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        // Inicializa cargando el login-view primero
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ucv/lab12/login-view.fxml"));

        Scene scene = new Scene(loader.load());

        stage.setTitle("Iniciar Sesión - UGEL");
        stage.setScene(scene);
        stage.setResizable(false); // Fija el tamaño del Login
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}