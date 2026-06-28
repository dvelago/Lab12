package com.ucv.lab12.controller;

import com.ucv.lab12.model.Videojuego;
import com.ucv.lab12.service.IVideojuegoService;
import com.ucv.lab12.util.AlertUtil;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class VideojuegoFormController implements Initializable {

    @FXML private Label lblTitulo;
    @FXML private TextField txtConsola;
    @FXML private TextField txtNombre;
    @FXML private TextField txtGenero;
    @FXML private TextField txtClasificacion;
    @FXML private TextArea txtDescripcion;
    @FXML private TextField txtIdDesarrollador;
    @FXML private TextField txtIdDistribuidor;
    @FXML private TextField txtPrecio;
    @FXML private TextField txtStock;
    @FXML private Button btnGuardar;
    @FXML private Button btnCancelar;

    private final IVideojuegoService service;
    private Videojuego videojuego;
    private Runnable onGuardar;

    public VideojuegoFormController(IVideojuegoService service) {
        this.service = service;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        limitarLongitud(txtConsola, 45);
        limitarLongitud(txtNombre, 45);
        limitarLongitud(txtGenero, 45);
        limitarLongitud(txtClasificacion, 45);
        limitarLongitud(txtDescripcion, 255);
        limitarLongitud(txtIdDesarrollador, 10);
        limitarLongitud(txtIdDistribuidor, 10);
        limitarLongitud(txtPrecio, 12);
        limitarLongitud(txtStock, 10);
    }

    public void setVideojuego(Videojuego videojuego) {
        this.videojuego = videojuego;
        if (videojuego == null) {
            lblTitulo.setText("Nuevo Videojuego");
            return;
        }

        lblTitulo.setText("Editar Videojuego");
        txtConsola.setText(nvl(videojuego.getConsola()));
        txtNombre.setText(nvl(videojuego.getNombre()));
        txtGenero.setText(nvl(videojuego.getGenero()));
        txtClasificacion.setText(nvl(videojuego.getClasificacion()));
        txtDescripcion.setText(nvl(videojuego.getDescripcion()));
        txtIdDesarrollador.setText(String.valueOf(videojuego.getIdDesarrollador()));
        txtIdDistribuidor.setText(String.valueOf(videojuego.getIdDistribuidor()));
        txtPrecio.setText(String.valueOf(videojuego.getPrecio()));
        txtStock.setText(String.valueOf(videojuego.getStock()));
    }

    public void setOnGuardar(Runnable onGuardar) {
        this.onGuardar = onGuardar;
    }

    @FXML
    private void onGuardar() {
        try {
            Videojuego v = videojuego != null ? videojuego : new Videojuego();
            v.setConsola(txtConsola.getText().trim());
            v.setNombre(txtNombre.getText().trim());
            v.setGenero(txtGenero.getText().trim());
            v.setClasificacion(txtClasificacion.getText().trim());
            v.setDescripcion(txtDescripcion.getText().trim());
            v.setIdDesarrollador(parseEntero(txtIdDesarrollador.getText(), "ID del desarrollador"));
            v.setIdDistribuidor(parseEntero(txtIdDistribuidor.getText(), "ID del distribuidor"));
            v.setPrecio(parseDecimal(txtPrecio.getText(), "precio"));
            v.setStock(parseEntero(txtStock.getText(), "stock"));

            if (videojuego == null) {
                service.crear(v);
                AlertUtil.info("Exito", "Videojuego creado correctamente.");
            } else {
                service.actualizar(v);
                AlertUtil.info("Exito", "Videojuego actualizado correctamente.");
            }

            if (onGuardar != null) {
                onGuardar.run();
            }
            cerrar();
        } catch (IllegalArgumentException ex) {
            AlertUtil.advertencia("Validacion", ex.getMessage());
        } catch (Exception ex) {
            AlertUtil.error("Error", "No se pudo guardar:\n" + ex.getMessage());
        }
    }

    @FXML
    private void onCancelar() {
        cerrar();
    }

    private void cerrar() {
        ((Stage) btnCancelar.getScene().getWindow()).close();
    }

    private void limitarLongitud(TextInputControl control, int max) {
        control.textProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null && newValue.length() > max) {
                control.setText(oldValue);
            }
        });
    }

    private String nvl(String value) {
        return value == null ? "" : value;
    }

    private int parseEntero(String value, String campo) {
        try {
            return Integer.parseInt(value.trim());
        } catch (Exception e) {
            throw new IllegalArgumentException("El campo " + campo + " debe ser numerico.");
        }
    }

    private double parseDecimal(String value, String campo) {
        try {
            return Double.parseDouble(value.trim().replace(',', '.'));
        } catch (Exception e) {
            throw new IllegalArgumentException("El campo " + campo + " debe ser numerico.");
        }
    }
}
