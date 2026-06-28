package com.ucv.lab12.controller;

import com.ucv.lab12.config.AppContext;
import com.ucv.lab12.model.Videojuego;
import com.ucv.lab12.service.IVideojuegoService;
import com.ucv.lab12.util.AlertUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class VideojuegoController implements Initializable {

    @FXML private TextField txtNombre;
    @FXML private TextField txtConsola;
    @FXML private TextField txtGenero;
    @FXML private Button btnBuscar;
    @FXML private Button btnNuevo;
    @FXML private Button btnRecargar;
    @FXML private Label lblTotal;

    @FXML private TableView<Videojuego> tableView;
    @FXML private TableColumn<Videojuego, Integer> colId;
    @FXML private TableColumn<Videojuego, String> colNombre;
    @FXML private TableColumn<Videojuego, String> colConsola;
    @FXML private TableColumn<Videojuego, String> colGenero;
    @FXML private TableColumn<Videojuego, String> colClasificacion;
    @FXML private TableColumn<Videojuego, Integer> colDesarrollador;
    @FXML private TableColumn<Videojuego, Integer> colDistribuidor;
    @FXML private TableColumn<Videojuego, Number> colPrecio;
    @FXML private TableColumn<Videojuego, Integer> colStock;
    @FXML private TableColumn<Videojuego, Void> colAcciones;

    private final IVideojuegoService service;
    private final ObservableList<Videojuego> data = FXCollections.observableArrayList();

    public VideojuegoController(IVideojuegoService service) {
        this.service = service;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        configurarColumnas();
        cargarDatos("", "", "");

        txtNombre.setOnAction(e -> onBuscar());
        txtConsola.setOnAction(e -> onBuscar());
        txtGenero.setOnAction(e -> onBuscar());
    }

    private void configurarColumnas() {
        colId.setCellValueFactory(new PropertyValueFactory<>("idVideojuego"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colConsola.setCellValueFactory(new PropertyValueFactory<>("consola"));
        colGenero.setCellValueFactory(new PropertyValueFactory<>("genero"));
        colClasificacion.setCellValueFactory(new PropertyValueFactory<>("clasificacion"));
        colDesarrollador.setCellValueFactory(new PropertyValueFactory<>("idDesarrollador"));
        colDistribuidor.setCellValueFactory(new PropertyValueFactory<>("idDistribuidor"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        colStock.setCellValueFactory(new PropertyValueFactory<>("stock"));

        colPrecio.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Number item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : String.format("S/ %.2f", item.doubleValue()));
            }
        });

        colStock.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                    return;
                }
                setText(String.valueOf(item));
                if (item <= 0) {
                    setStyle("-fx-text-fill:#c62828; -fx-font-weight:bold;");
                } else if (item <= 5) {
                    setStyle("-fx-text-fill:#ef6c00; -fx-font-weight:bold;");
                } else {
                    setStyle("-fx-text-fill:#2e7d32; -fx-font-weight:bold;");
                }
            }
        });

        colAcciones.setCellFactory(crearCeldaAcciones());
        colAcciones.setSortable(false);

        tableView.setItems(data);
    }

    private Callback<TableColumn<Videojuego, Void>, TableCell<Videojuego, Void>> crearCeldaAcciones() {
        return col -> new TableCell<>() {
            private final Button btnComprar = new Button("Comprar");
            private final Button btnEditar = new Button("Editar");
            private final Button btnEliminar = new Button("Eliminar");
            private final HBox box = new HBox(6, btnComprar, btnEditar, btnEliminar);

            {
                box.setAlignment(Pos.CENTER);
                btnComprar.setStyle("-fx-background-color:#2E7D32;-fx-text-fill:white;-fx-cursor:hand;-fx-font-size:11px;");
                btnEditar.setStyle("-fx-background-color:#1976D2;-fx-text-fill:white;-fx-cursor:hand;-fx-font-size:11px;");
                btnEliminar.setStyle("-fx-background-color:#C62828;-fx-text-fill:white;-fx-cursor:hand;-fx-font-size:11px;");

                btnComprar.setOnAction(e -> {
                    Videojuego videojuego = getTableView().getItems().get(getIndex());
                    abrirCompra(videojuego);
                });
                btnEditar.setOnAction(e -> {
                    Videojuego videojuego = getTableView().getItems().get(getIndex());
                    abrirFormulario(videojuego);
                });
                btnEliminar.setOnAction(e -> {
                    Videojuego videojuego = getTableView().getItems().get(getIndex());
                    confirmarEliminar(videojuego);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : box);
            }
        };
    }

    private void cargarDatos(String nombre, String consola, String genero) {
        try {
            List<Videojuego> lista = service.buscar(nombre, consola, genero);
            data.setAll(lista);
            lblTotal.setText("Total: " + data.size() + " videojuego(s)");
        } catch (Exception e) {
            AlertUtil.error("Error de conexion", "No se pudo cargar el catalogo:\n" + e.getMessage());
        }
    }

    @FXML
    private void onBuscar() {
        cargarDatos(txtNombre.getText(), txtConsola.getText(), txtGenero.getText());
    }

    @FXML
    private void onNuevo() {
        abrirFormulario(null);
    }

    @FXML
    private void onRecargar() {
        txtNombre.clear();
        txtConsola.clear();
        txtGenero.clear();
        cargarDatos("", "", "");
    }

    private void confirmarEliminar(Videojuego videojuego) {
        boolean ok = AlertUtil.confirmar("Eliminar videojuego",
                "Desea eliminar: " + videojuego.getNombre() + "?");
        if (!ok) {
            return;
        }

        try {
            service.eliminar(videojuego.getIdVideojuego());
            cargarDatos(txtNombre.getText(), txtConsola.getText(), txtGenero.getText());
        } catch (Exception e) {
            AlertUtil.error("Error", "No se pudo eliminar:\n" + e.getMessage());
        }
    }

    private void abrirFormulario(Videojuego videojuego) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ucv/lab12/videojuego-form.fxml"));
            loader.setControllerFactory(AppContext.getInstance()::getController);
            Parent root = loader.load();

            VideojuegoFormController controller = loader.getController();
            controller.setVideojuego(videojuego);
            controller.setOnGuardar(() -> cargarDatos(txtNombre.getText(), txtConsola.getText(), txtGenero.getText()));

            Stage modal = new Stage();
            modal.setTitle(videojuego == null ? "Nuevo Videojuego" : "Editar Videojuego");
            modal.setScene(new Scene(root));
            modal.initModality(Modality.APPLICATION_MODAL);
            modal.setResizable(false);
            modal.showAndWait();
        } catch (IOException e) {
            AlertUtil.error("Error", "No se pudo abrir el formulario:\n" + e.getMessage());
        }
    }

    private void abrirCompra(Videojuego videojuego) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ucv/lab12/compra-videojuego-form.fxml"));
            loader.setControllerFactory(AppContext.getInstance()::getController);
            Parent root = loader.load();

            CompraVideojuegoFormController controller = loader.getController();
            controller.setVideojuego(videojuego);
            controller.setOnComprado(() -> cargarDatos(txtNombre.getText(), txtConsola.getText(), txtGenero.getText()));

            Stage modal = new Stage();
            modal.setTitle("Comprar Videojuego");
            modal.setScene(new Scene(root));
            modal.initModality(Modality.APPLICATION_MODAL);
            modal.setResizable(false);
            modal.showAndWait();
        } catch (IOException e) {
            AlertUtil.error("Error", "No se pudo abrir la compra:\n" + e.getMessage());
        }
    }
}
