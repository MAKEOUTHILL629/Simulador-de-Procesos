package controlador;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import modelo.ProcesoEntrada;
import modelo.ProcesoTiempoEjecucion;
import modelo.Recurso;
import modelo.RecursoTiempoEjecucion;

import java.net.URL;
import java.util.ArrayList;
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
    private static int acumulador = 0;
    private ObservableList<RecursoTiempoEjecucion> recursos;
    private ProcesoEntrada procesoEntrada;
    private ArrayList<Recurso> recursosNecesitados;
    private ObservableList<Recurso> recursosTabla;
    private ObservableList<ProcesoTiempoEjecucion> procesosEnElSistema;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.recursosNecesitados = new ArrayList<>();
        this.recursosTabla = FXCollections.observableArrayList(this.recursosNecesitados);
        this.tabla_recursos_necesarios.setItems(this.recursosTabla);

        this.col_recursos_nombre.setCellValueFactory(new PropertyValueFactory("nombre"));
        this.col_tipo_recursos.setCellValueFactory(new PropertyValueFactory("tipo"));
    }

    public void guardarProceso(ActionEvent actionEvent) {
        acumulador++;
        ProcesoEntrada procesoEntradaAux = new ProcesoEntrada(acumulador, this.field_nombre.getText(), Long.parseLong(this.field_tamanio.getText()), recursosNecesitados);

        if (this.procesosEnElSistema.stream().filter(p -> p.getProceso().getNombre().equals(procesoEntradaAux.getNombre())).count() == 0) {
            //modificicar
            if (this.procesoEntrada != null) {
                this.procesoEntrada.setId(acumulador);
                this.procesoEntrada.setNombre(procesoEntradaAux.getNombre());
                this.procesoEntrada.setRecursosNecesitados(this.recursosNecesitados);
                this.procesoEntrada.setTamanio(procesoEntradaAux.getTamanio());

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText(null);
                alert.setTitle("Informacion");
                alert.setContentText("El proceso se modifico correctamente");
                alert.showAndWait();

            } else {
                this.procesoEntrada = procesoEntradaAux;
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText(null);
                alert.setTitle("Informacion");
                alert.setContentText("El proceso se agrego correctamente");
                alert.showAndWait();
            }


            Stage stage = (Stage) this.boton_guardar.getScene().getWindow();//para cerrar la ventana apenas se agrege la persona
            stage.close();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setTitle("Error");
            alert.setContentText("El proceso existe");
            alert.showAndWait();

        }
    }

    public void initAtributtes(ObservableList<RecursoTiempoEjecucion> recursos, ObservableList<ProcesoTiempoEjecucion> procesosSistema) {
        this.recursos = recursos;
        this.procesosEnElSistema = procesosSistema;
        this.combo_recursos.setItems(recursos);
    }


    public void salir(ActionEvent actionEvent) {
        Stage stage = (Stage) this.boton_guardar.getScene().getWindow();//para cerrar la ventana apenas se agrege el recurso
        stage.close();
    }

    public ProcesoEntrada getProcesoEntrada() {
        return procesoEntrada;
    }

    public void agregarRecursoNecesitado(ActionEvent actionEvent) {
        RecursoTiempoEjecucion recursoObtenido = (RecursoTiempoEjecucion) this.combo_recursos.getValue();
        Recurso recursoAgregado = new Recurso(recursoObtenido.getId(), recursoObtenido.getNombre(), recursoObtenido.getTipo());
        this.recursosNecesitados.add(recursoAgregado);
        this.tabla_recursos_necesarios.getItems().clear();
        this.recursosTabla.addAll(this.recursosNecesitados);
        this.tabla_recursos_necesarios.refresh();

    }
}
