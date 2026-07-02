module com.ucv.lab12 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    // Abrir paquetes para que JavaFX pueda leerlos
    opens com.ucv.lab12 to javafx.fxml;
    opens com.ucv.lab12.controller to javafx.fxml;
    opens com.ucv.lab12.model to javafx.base; // Esto permite que las tablas carguen los datos

    // Exportar paquetes
    exports com.ucv.lab12;
    exports com.ucv.lab12.controller;
}