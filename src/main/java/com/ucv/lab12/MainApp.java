package com.ucv.lab12;

import com.ucv.lab12.config.AppContext;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        AppContext context = AppContext.getInstance();

        FXMLLoader loader = new FXMLLoader(
            getClass().getResource("/com/ucv/lab12/distribuidor-view.fxml")
        );
        loader.setControllerFactory(context::getController);

        Scene scene = new Scene(loader.load(), 1100, 620);
        stage.setTitle("Mantenimiento de Distribuidores");
        stage.setScene(scene);
        stage.setMinWidth(900);
        stage.setMinHeight(500);
        stage.show();
    }

    @Override
    public void stop() {
        AppContext.getInstance().destroy();
    }
}
