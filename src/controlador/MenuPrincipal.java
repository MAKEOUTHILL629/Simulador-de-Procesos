package controlador;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import modelo.*;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MenuPrincipal implements Initializable {
    public TableView<ProcesoTiempoEjecucion> tabla_procesos_nuevos;
    public Button boton_agregar_proceso;
    public TableView<ProcesoTiempoEjecucion> tabla_procesos_listos;
    public TableView<ProcesoTiempoEjecucion> tabla_proceso_ejecucion;
    public TableView<ProcesoTiempoEjecucion> tabla_procesos_bloqueados;
    public TableView<ProcesoTiempoEjecucion> tabla_procesos_terminados;
    public TableView<RecursoTiempoEjecucion> tabla_recursos;
    public Button boton_agregar_recurso;
    public Button boton_iniciar_simulacion;
    public TableColumn col_id_nuevos;
    public TableColumn col_nombre_nuevos;
    public TableColumn col_id_listos;
    public TableColumn col_nombre_listos;
    public TableColumn col_tamanio_listos;
    public TableColumn col_id_ejecucion;
    public TableColumn col_nombre_ejecucion;
    public TableColumn col_tamanio_ejecucion;
    public TableColumn col_id_bloqueados;
    public TableColumn col_nombres_bloqueados;
    public TableColumn col_id_terminados;
    public TableColumn col_nombre_terminados;
    public TableColumn col_id_recursos;
    public TableColumn col_nombre_recursos;
    public TableColumn col_tipo_recursos;
    public TableColumn col_estado_recursos;
    private SistemaOperativo sistemaOperativo;
    private ObservableList<ProcesoTiempoEjecucion> procesosDelSistema;
    private ObservableList<RecursoTiempoEjecucion> recursosDelSistema;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.sistemaOperativo = new SistemaOperativo();
        this.procesosDelSistema = FXCollections.observableArrayList(this.sistemaOperativo.getProcesosSistema());
        this.recursosDelSistema = FXCollections.observableArrayList(this.sistemaOperativo.getRecursos());

        this.tabla_procesos_nuevos.setItems(this.procesosDelSistema.filtered(proceso -> proceso.getEstado() == Estado.NUEVO));
        this.tabla_procesos_listos.setItems(this.procesosDelSistema.filtered(proceso -> proceso.getEstado() == Estado.LISTO));
        this.tabla_procesos_bloqueados.setItems(this.procesosDelSistema.filtered(proceso -> proceso.getEstado() == Estado.BLOQUEADO));
        this.tabla_proceso_ejecucion.setItems(this.procesosDelSistema.filtered(proceso -> proceso.getEstado() == Estado.EJECUCION));
        this.tabla_procesos_terminados.setItems(this.procesosDelSistema.filtered(proceso -> proceso.getEstado() == Estado.TERMINADO));
        this.tabla_recursos.setItems(this.recursosDelSistema);

        this.col_estado_recursos.setCellValueFactory(new PropertyValueFactory("idProcesoEjecucion"));
        this.col_id_bloqueados.setCellValueFactory(new PropertyValueFactory("proceso.id"));
        this.col_id_ejecucion.setCellValueFactory(new PropertyValueFactory("proceso.id"));
        this.col_id_listos.setCellValueFactory(new PropertyValueFactory("proceso.id"));
        this.col_id_nuevos.setCellValueFactory(new PropertyValueFactory("proceso.id"));
        this.col_id_terminados.setCellValueFactory(new PropertyValueFactory("proceso.id"));
        this.col_id_recursos.setCellValueFactory(new PropertyValueFactory("id"));
        this.col_nombre_ejecucion.setCellValueFactory(new PropertyValueFactory("proceso.nombre"));
        this.col_nombre_listos.setCellValueFactory(new PropertyValueFactory("proceso.nombre"));
        this.col_nombre_nuevos.setCellValueFactory(new PropertyValueFactory("proceso.nombre"));
        this.col_nombre_terminados.setCellValueFactory(new PropertyValueFactory("proceso.nombre"));
        this.col_nombres_bloqueados.setCellValueFactory(new PropertyValueFactory("proceso.nombre"));
        this.col_nombre_recursos.setCellValueFactory(new PropertyValueFactory("nombre"));
        this.col_tamanio_ejecucion.setCellValueFactory(new PropertyValueFactory("tamanio"));
        this.col_tamanio_listos.setCellValueFactory(new PropertyValueFactory("tamanio"));
        this.col_tipo_recursos.setCellValueFactory(new PropertyValueFactory("tipo"));

    }

    public void agregarRecurso(ActionEvent actionEvent){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/agregar_recurso.fxml"));//Cargando la vista
            Parent root = loader.load();//Esta cargando el padre

            AgregarRecurso agregarRecursoControlador = loader.getController();
            agregarRecursoControlador.initAtributtes(this.recursosDelSistema);//inicializando los atributos
            Scene scene = new Scene(root);
            Stage stage = new Stage();

            stage.initModality(Modality.APPLICATION_MODAL);//Cuando yo abra no me deja volver a la ventana anterior
            stage.setScene(scene);
            stage.showAndWait();


            Recurso recursoAux = agregarRecursoControlador.getRecurso();

            if(recursoAux != null){
                this.tabla_recursos.getItems().clear();
                this.sistemaOperativo.agregarRecurso(recursoAux);
                System.out.println(this.sistemaOperativo.getRecursos());

                this.recursosDelSistema.addAll(this.sistemaOperativo.getRecursos());

//                //En caso de que se agrege una persona, y el nombre se agrege a la lista que se filtra
//                if(personaAux.getNombre().toLowerCase().contains(this.txtFiltrarNombre.getText().toLowerCase())){
//                    this.filtroPersonas.add(personaAux);
//                }

                this.tabla_recursos.refresh();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void agregarProceso(ActionEvent actionEvent){
        System.out.println("Hola Mundo");
    }

    public void iniciarSimulacion(ActionEvent actionEvent){
        System.out.println("Hola Mundo");
    }
}
