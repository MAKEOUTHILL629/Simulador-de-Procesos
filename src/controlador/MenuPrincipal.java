package controlador;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import javafx.application.Platform;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.ObservableValueBase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import modelo.*;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class MenuPrincipal implements Initializable, Observer {
    public TableView<ProcesoTiempoEjecucion> tabla_procesos_nuevos;
    public Button boton_agregar_proceso;
    public TableView<ProcesoTiempoEjecucion> tabla_procesos_listos;
    public TableView<ProcesoTiempoEjecucion> tabla_proceso_ejecucion;
    public TableView<ProcesoTiempoEjecucion> tabla_procesos_bloqueados;
    public TableView<ProcesoTiempoEjecucion> tabla_procesos_terminados;
    public TableView<RecursoTiempoEjecucion> tabla_recursos;
    public Button boton_agregar_recurso;
    public Button boton_iniciar_simulacion;
    public TableColumn<ProcesoTiempoEjecucion, String> col_id_nuevos;
    public TableColumn<ProcesoTiempoEjecucion, String> col_nombre_nuevos;
    public TableColumn<ProcesoTiempoEjecucion, String> col_id_listos;
    public TableColumn<ProcesoTiempoEjecucion, String> col_nombre_listos;
    public TableColumn<ProcesoTiempoEjecucion, String> col_tamanio_listos;
    public TableColumn<ProcesoTiempoEjecucion, String> col_id_ejecucion;
    public TableColumn<ProcesoTiempoEjecucion, String> col_nombre_ejecucion;
    public TableColumn<ProcesoTiempoEjecucion, String> col_tamanio_ejecucion;
    public TableColumn<ProcesoTiempoEjecucion, String> col_id_bloqueados;
    public TableColumn<ProcesoTiempoEjecucion, String> col_nombres_bloqueados;
    public TableColumn<ProcesoTiempoEjecucion, String> col_id_terminados;
    public TableColumn<ProcesoTiempoEjecucion, String> col_nombre_terminados;
    public TableColumn<ProcesoTiempoEjecucion, String> col_tiempo_terminados;
    public TableColumn<RecursoTiempoEjecucion, String> col_id_recursos;
    public TableColumn<RecursoTiempoEjecucion, String> col_nombre_recursos;
    public TableColumn<RecursoTiempoEjecucion, String> col_tipo_recursos;
    public TableColumn<RecursoTiempoEjecucion, String> col_estado_recursos;


    //Segunda pestania
    public TableView<ProcesoTiempoEjecucion> tabla_procesos_generales;
    public TableColumn<ProcesoTiempoEjecucion, String> col_id_general;
    public TableColumn<ProcesoTiempoEjecucion, String> col_nombre_general;
    public TableColumn<ProcesoTiempoEjecucion, String> col_tamanio_inicial_general;
    public TableColumn<ProcesoTiempoEjecucion, String> col_estado_general;
    public TableColumn<ProcesoTiempoEjecucion, String> col_tamanio_actual_general;
    public TableColumn<ProcesoTiempoEjecucion, String> col_tiempo_sistema_general;
    public TableColumn<ProcesoTiempoEjecucion, String> col_recursos_general;
    public Label label_cantidad_procesos;
    public Label label_promedio_procesos;
    public Label label_tiempo_cpu;
    public Label label_tiempo_bloqueados;


    private SistemaOperativo sistemaOperativo;

    private List<ProcesoTiempoEjecucion> procesosDelSistema;

    private ObservableList<ProcesoTiempoEjecucion> procesosNuevos;
    private ObservableList<ProcesoTiempoEjecucion> procesosListos;
    private ObservableList<ProcesoTiempoEjecucion> procesosBloqueos;
    private ObservableList<ProcesoTiempoEjecucion> procesosTerminados;
    private ObservableList<ProcesoTiempoEjecucion> procesosEjecucion;
    private ObservableList<ProcesoTiempoEjecucion> procesosEnElSistema;

    private ObservableList<RecursoTiempoEjecucion> recursosDelSistema;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.sistemaOperativo = new SistemaOperativo();
        this.sistemaOperativo.addObserver(this);

        this.procesosDelSistema = this.sistemaOperativo.getProcesosSistema();
        this.recursosDelSistema = FXCollections.observableArrayList(this.sistemaOperativo.getRecursos());

        this.procesosNuevos = FXCollections.observableArrayList(this.sistemaOperativo.obtenerProcesosEstadoNuevo());
        this.procesosBloqueos = FXCollections.observableArrayList(this.sistemaOperativo.obtenerProcesosEstadoBloqueado());
        this.procesosListos = FXCollections.observableArrayList(this.sistemaOperativo.obtenerProcesosEstadoListo());
        this.procesosTerminados = FXCollections.observableArrayList(this.sistemaOperativo.obtenerProcesosEstadoTerminado());
        this.procesosEjecucion = FXCollections.observableArrayList(this.sistemaOperativo.obtenerProcesosEstadoEjecucion());

        this.procesosEnElSistema = FXCollections.observableArrayList(this.sistemaOperativo.getProcesosSistema());

        this.tabla_procesos_nuevos.setItems(this.procesosNuevos);
        this.tabla_procesos_nuevos.setPlaceholder(new Label("Sin procesos nuevos"));
        this.tabla_procesos_listos.setItems(this.procesosListos);
        this.tabla_procesos_listos.setPlaceholder(new Label("Sin procesos listos"));
        this.tabla_procesos_bloqueados.setItems(this.procesosBloqueos);
        this.tabla_procesos_bloqueados.setPlaceholder(new Label("Sin procesos bloqueados"));
        this.tabla_proceso_ejecucion.setItems(this.procesosEjecucion);
        this.tabla_proceso_ejecucion.setPlaceholder(new Label("Sin procesos en ejecucion"));
        this.tabla_procesos_terminados.setItems(this.procesosTerminados);
        this.tabla_procesos_terminados.setPlaceholder(new Label("Sin procesos en finalizados"));
        this.tabla_recursos.setItems(this.recursosDelSistema);
        this.tabla_recursos.setPlaceholder(new Label("Sin recursos"));
        this.tabla_procesos_generales.setItems(this.procesosEnElSistema);
        this.tabla_procesos_generales.setPlaceholder(new Label("No hay procesos en el sistema"));
        //Asignando las variables a cada columna

        this.col_estado_recursos.setCellValueFactory(data -> new ObservableValueBase<String>() {
            @Override
            public String getValue() {
                return data.getValue().getIdProcesoEjecucion() > 0 ? data.getValue().getIdProcesoEjecucion() + "" : "DESOCUPADO";
            }
        });

        this.col_id_bloqueados.setCellValueFactory(data -> new ObservableValueBase<String>() {
            @Override
            public String getValue() {
                return data.getValue().getProceso().getId() + "";
            }
        });
        this.col_id_ejecucion.setCellValueFactory(data -> new ObservableValueBase<String>() {
            @Override
            public String getValue() {
                return data.getValue().getProceso().getId() + "";
            }
        });
        this.col_id_listos.setCellValueFactory(data -> new ObservableValueBase<String>() {
            @Override
            public String getValue() {
                return data.getValue().getProceso().getId() + "";
            }
        });
        this.col_id_nuevos.setCellValueFactory(data -> new ObservableValueBase<String>() {
            @Override
            public String getValue() {
                return data.getValue().getProceso().getId() + "";
            }
        });
        this.col_id_terminados.setCellValueFactory(data -> new ObservableValueBase<String>() {
            @Override
            public String getValue() {
                return data.getValue().getProceso().getId() + "";
            }
        });

        this.col_id_recursos.setCellValueFactory(data -> new ObservableValueBase<String>() {
            @Override
            public String getValue() {
                return data.getValue().getId() + "";
            }
        });
        this.col_nombre_ejecucion.setCellValueFactory(data -> new ObservableValueBase<String>() {
            @Override
            public String getValue() {
                return data.getValue().getProceso().getNombre();
            }
        });
        this.col_nombre_listos.setCellValueFactory(data -> new ObservableValueBase<String>() {
            @Override
            public String getValue() {
                return data.getValue().getProceso().getNombre();
            }
        });
        this.col_nombre_nuevos.setCellValueFactory(data -> new ObservableValueBase<String>() {
            @Override
            public String getValue() {
                return data.getValue().getProceso().getNombre();
            }
        });
        this.col_nombre_terminados.setCellValueFactory(data -> new ObservableValueBase<String>() {
            @Override
            public String getValue() {
                return data.getValue().getProceso().getNombre();
            }
        });
        this.col_tiempo_terminados.setCellValueFactory(data -> new ObservableValueBase<String>() {
            @Override
            public String getValue() {
                return (data.getValue().getTiempoSistiempoSistem() / 1000) + "segundos";
            }
        });

        this.col_nombres_bloqueados.setCellValueFactory(data -> new ObservableValueBase<String>() {
            @Override
            public String getValue() {
                return data.getValue().getProceso().getNombre();
            }
        });
        this.col_nombre_recursos.setCellValueFactory(data -> new ObservableValueBase<String>() {
            @Override
            public String getValue() {
                return data.getValue().getNombre();
            }
        });
        this.col_tamanio_ejecucion.setCellValueFactory(data -> new ObservableValueBase<String>() {
            @Override
            public String getValue() {
                return data.getValue().getTamanio() + "";
            }
        });
        this.col_tamanio_listos.setCellValueFactory(data -> new ObservableValueBase<String>() {
            @Override
            public String getValue() {
                return data.getValue().getTamanio() + "";
            }
        });
        this.col_tipo_recursos.setCellValueFactory(data -> new ObservableValueBase<String>() {
            @Override
            public String getValue() {
                return data.getValue().getTipo();
            }
        });


        this.col_id_general.setCellValueFactory(data -> new ObservableValueBase<String>() {
            @Override
            public String getValue() {
                return data.getValue().getProceso().getId() + " ";
            }
        });

        this.col_nombre_general.setCellValueFactory(data -> new ObservableValueBase<String>() {
            @Override
            public String getValue() {
                return data.getValue().getProceso().getNombre();
            }
        });

        this.col_tamanio_inicial_general.setCellValueFactory(data -> new ObservableValueBase<String>() {
            @Override
            public String getValue() {
                return data.getValue().getProceso().getTamanio() + "";
            }
        });

        this.col_estado_general.setCellValueFactory(data -> new ObservableValueBase<String>() {
            @Override
            public String getValue() {
                return data.getValue().getEstado() + "";
            }
        });

        this.col_tamanio_actual_general.setCellValueFactory(data -> new ObservableValueBase<String>() {
            @Override
            public String getValue() {
                return data.getValue().getTamanio() < 0 ? 0 + "" : data.getValue().getTamanio() + "";
            }
        });

        this.col_tiempo_sistema_general.setCellValueFactory(data -> new ObservableValueBase<String>() {
            @Override
            public String getValue() {
                return (data.getValue().getTiempoSistiempoSistem() / 1000) + " segundos";
            }
        });

        this.col_recursos_general.setCellValueFactory(data -> new ObservableValueBase<String>() {
            @Override
            public String getValue() {
                return data.getValue().getProceso().getRecursosNecesitados("");
            }
        });

    }

    public void agregarRecurso(ActionEvent actionEvent) {
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

            if (recursoAux != null) {
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

    public void agregarProceso(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/agregar_proceso.fxml"));//Cargando la vista
            Parent root = loader.load();//Esta cargando el padre

            AgregarProceso agregarProcesoControlador = loader.getController();
            agregarProcesoControlador.initAtributtes(this.recursosDelSistema, FXCollections.observableArrayList(this.procesosDelSistema));//inicializando los atributos
            Scene scene = new Scene(root);
            Stage stage = new Stage();

            stage.initModality(Modality.APPLICATION_MODAL);//Cuando yo abra no me deja volver a la ventana anterior
            stage.setScene(scene);
            scene.getStylesheets().add("vista/static/css/table_view.css");
            stage.showAndWait();


            ProcesoEntrada procesoAux = agregarProcesoControlador.getProcesoEntrada();

            if (procesoAux != null) {
                this.tabla_procesos_nuevos.getItems().clear();
                this.sistemaOperativo.crearProceso(procesoAux);
                System.out.println(this.sistemaOperativo.getProcesosSistema());
                this.procesosNuevos.addAll(this.sistemaOperativo.getProcesosSistema().stream().filter(proceso -> proceso.getEstado() == Estado.NUEVO).collect(Collectors.toList()));
                //this.tabla_procesos_nuevos.setItems(this.sistemaOperativo.getProcesosSistema().stream().filter(proceso -> proceso.getEstado() == Estado.NUEVO).collect(Collectors.));

//                //En caso de que se agrege una persona, y el nombre se agrege a la lista que se filtra
//                if(personaAux.getNombre().toLowerCase().contains(this.txtFiltrarNombre.getText().toLowerCase())){
//                    this.filtroPersonas.add(personaAux);
//                }

                this.tabla_procesos_nuevos.refresh();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void iniciarSimulacion(ActionEvent actionEvent) {
        if (this.sistemaOperativo.getProcesosSistema().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setTitle("Error");
            alert.setContentText("Por favor ingrese los procesos");
            alert.showAndWait();
        } else if (this.sistemaOperativo.getRecursos().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setTitle("Error");
            alert.setContentText("Por favor ingrese los recursos");
            alert.showAndWait();

        } else {
            Thread hiloSistemaOperativo = new Thread(this.sistemaOperativo);
            hiloSistemaOperativo.start();
            this.boton_agregar_proceso.setDisable(true);
            this.boton_agregar_recurso.setDisable(true);
            this.boton_iniciar_simulacion.setDisable(true);
        }
    }

    private void actualizarTablas() {
        this.tabla_procesos_nuevos.getItems().clear();
        this.tabla_procesos_listos.getItems().clear();
        this.tabla_procesos_bloqueados.getItems().clear();
        this.tabla_proceso_ejecucion.getItems().clear();
        this.tabla_procesos_terminados.getItems().clear();
        this.tabla_recursos.getItems().clear();
        this.tabla_procesos_generales.getItems().clear();


        System.out.println("Se estan actualizando las tablas");
        this.tabla_procesos_nuevos.getItems().addAll(this.sistemaOperativo.obtenerProcesosEstadoNuevo());
        this.tabla_procesos_listos.getItems().addAll(this.sistemaOperativo.obtenerProcesosEstadoListo());
        this.tabla_procesos_bloqueados.getItems().addAll(this.sistemaOperativo.obtenerProcesosEstadoBloqueado());
        this.tabla_proceso_ejecucion.getItems().addAll(this.sistemaOperativo.obtenerProcesosEstadoEjecucion());
        this.tabla_procesos_terminados.getItems().addAll(this.sistemaOperativo.obtenerProcesosEstadoTerminado());
        this.tabla_recursos.getItems().addAll(this.sistemaOperativo.getRecursos());
        this.tabla_procesos_generales.getItems().addAll(this.sistemaOperativo.getProcesosSistema());
        System.out.println("Se actualizaron las tablas");


        if (!this.sistemaOperativo.terminaronLosProcesos()) {

            Platform.runLater(() -> {

                this.label_cantidad_procesos.setText(this.sistemaOperativo.getProcesosSistema().size() + "");
                this.label_promedio_procesos.setText((this.sistemaOperativo.getTiempoPromedioProcesos() / 1000)+ " segundos");
                this.label_tiempo_bloqueados.setText(this.sistemaOperativo.getTiempoBloqueado() + " segundos");
                this.label_tiempo_cpu.setText(this.sistemaOperativo.getTiempoCPUOcupado() + " segundos");
            });

        }

    }

    @Override
    public void update(Observable o, Object arg) {
        actualizarTablas();
    }
}
