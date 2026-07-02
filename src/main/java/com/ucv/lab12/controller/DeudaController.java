package com.ucv.lab12.controller;

import com.ucv.lab12.model.Deuda;
import com.ucv.lab12.service.DeudaService;
import com.ucv.lab12.service.IDeudaService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;

public class DeudaController {

    @FXML private TableView<Deuda> tblDeudas;
    @FXML private TableColumn<Deuda, Integer> colIdDeuda;
    @FXML private TableColumn<Deuda, Integer> colIdDocente;
    @FXML private TableColumn<Deuda, Double> colMonto;
    @FXML private TableColumn<Deuda, String> colTipo;
    @FXML private TableColumn<Deuda, String> colEstado;
    @FXML private TableColumn<Deuda, LocalDate> colFecha;

    @FXML private TextField txtDniBuscar;
    @FXML private Button btnBuscar;

    private final IDeudaService deudaService;
    private final ObservableList<Deuda> listaDeudas;

    public DeudaController() {
        this.deudaService = new DeudaService();
        this.listaDeudas = FXCollections.observableArrayList();
    }

    @FXML
    public void initialize() {
        // Vinculación exacta de las columnas con las propiedades del modelo Deuda
        colIdDeuda.setCellValueFactory(new PropertyValueFactory<>("idDeuda"));
        colIdDocente.setCellValueFactory(new PropertyValueFactory<>("idDocente"));
        colMonto.setCellValueFactory(new PropertyValueFactory<>("monto"));
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipoDeuda"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fechaRegistro"));

        cargarDeudas();
    }

    private void cargarDeudas() {
        listaDeudas.clear();
        listaDeudas.addAll(deudaService.obtenerTodasLasDeudas());
        tblDeudas.setItems(listaDeudas);
    }

    @FXML
    private void manejarBuscar() {
        String dni = txtDniBuscar.getText().trim();
        listaDeudas.clear();
        listaDeudas.addAll(deudaService.buscarDeudasPorDni(dni));
        tblDeudas.setItems(listaDeudas);
    }

    @FXML
    private void abrirVentanaAgregarDocente() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ucv/lab12/nuevo-docente-view.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Registrar Docente - UGEL");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            // Refresca automáticamente la tabla principal por si hubo cambios o nuevas asignaciones
            cargarDeudas();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}