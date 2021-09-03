package controlador;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

public class AgregarProceso implements Initializable {
    public TableView tabla_recursos_necesarios;
    public TableColumn col_recursos_nombre;
    public TableColumn col_tipo_recursos;
    public ComboBox combo_recursos;
    public Button boton_agregar_recurso;
    public Button boton_guardar;
    public Button boton_Salir;
    public TextField field_nombre;
    public TextField field_tamanio;
    public TextField field_privilegios;
    public TextField field_hilos;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void guardarProceso(ActionEvent actionEvent) {
    }

    public void salir(ActionEvent actionEvent) {
    }
}
