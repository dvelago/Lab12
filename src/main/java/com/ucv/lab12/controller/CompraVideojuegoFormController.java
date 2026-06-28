package com.ucv.lab12.controller;

import com.ucv.lab12.model.CompraVideojuego;
import com.ucv.lab12.model.Videojuego;
import com.ucv.lab12.service.IVideojuegoService;
import com.ucv.lab12.util.AlertUtil;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class CompraVideojuegoFormController implements Initializable {

    @FXML private Label lblVideojuego;
    @FXML private Label lblPrecioUnitario;
    @FXML private Label lblTotal;
    @FXML private TextField txtComprador;
    @FXML private TextField txtCorreo;
    @FXML private Spinner<Integer> spnCantidad;
    @FXML private ComboBox<String> cmbMetodoPago;
    @FXML private Button btnComprar;
    @FXML private Button btnCancelar;

    private final IVideojuegoService service;
    private Videojuego videojuego;
    private Runnable onComprado;

    public CompraVideojuegoFormController(IVideojuegoService service) {
        this.service = service;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        spnCantidad.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 999, 1));
        cmbMetodoPago.getItems().setAll("Efectivo", "Tarjeta", "Transferencia");
        cmbMetodoPago.getSelectionModel().selectFirst();

        spnCantidad.valueProperty().addListener((obs, oldValue, newValue) -> actualizarTotal());
        txtComprador.textProperty().addListener((obs, oldValue, newValue) -> {});
    }

    public void setVideojuego(Videojuego videojuego) {
        this.videojuego = videojuego;
        if (videojuego == null) {
            lblVideojuego.setText("Sin videojuego seleccionado");
            lblPrecioUnitario.setText("S/ 0.00");
            lblTotal.setText("S/ 0.00");
            return;
        }

        lblVideojuego.setText(videojuego.getNombre() + " - " + videojuego.getConsola());
        lblPrecioUnitario.setText(String.format("S/ %.2f", videojuego.getPrecio()));
        actualizarTotal();
    }

    public void setOnComprado(Runnable onComprado) {
        this.onComprado = onComprado;
    }

    @FXML
    private void onComprar() {
        if (videojuego == null) {
            AlertUtil.advertencia("Validacion", "No hay videojuego seleccionado.");
            return;
        }

        try {
            int cantidad = spnCantidad.getValue();
            CompraVideojuego compra = service.comprar(
                    videojuego.getIdVideojuego(),
                    txtComprador.getText(),
                    txtCorreo.getText(),
                    cantidad,
                    cmbMetodoPago.getValue()
            );

            AlertUtil.info("Compra exitosa",
                    "Se registraron " + compra.getCantidad() + " unidad(es) de "
                            + compra.getNombreVideojuego() + ".\nTotal: "
                            + String.format("S/ %.2f", compra.getTotal()));

            if (onComprado != null) {
                onComprado.run();
            }
            cerrar();
        } catch (IllegalArgumentException | IllegalStateException ex) {
            AlertUtil.advertencia("Compra", ex.getMessage());
        } catch (Exception ex) {
            AlertUtil.error("Error", "No se pudo registrar la compra:\n" + ex.getMessage());
        }
    }

    @FXML
    private void onCancelar() {
        cerrar();
    }

    private void actualizarTotal() {
        if (videojuego == null) {
            return;
        }
        int cantidad = spnCantidad.getValue() == null ? 1 : spnCantidad.getValue();
        lblTotal.setText(String.format("S/ %.2f", videojuego.getPrecio() * cantidad));
    }

    private void cerrar() {
        ((Stage) btnCancelar.getScene().getWindow()).close();
    }
}
