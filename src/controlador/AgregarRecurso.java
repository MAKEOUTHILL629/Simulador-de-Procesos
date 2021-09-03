package controlador;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import modelo.Recurso;
import modelo.RecursoTiempoEjecucion;

import java.net.URL;
import java.util.ResourceBundle;

public class AgregarRecurso implements Initializable {
    public TextField field_nombre;
    public TextField field_tipo;
    public Button boton_guardar;
    public Button boton_Salir;
    private Recurso recurso;
    private ObservableList<RecursoTiempoEjecucion> recursos;
    private static int acumulador = 0;

    @Override

    public void initialize(URL location, ResourceBundle resources) {

    }

    public void guardarRecurso(ActionEvent actionEvent) {
        acumulador++;
        Recurso recursoAux = new Recurso(acumulador, this.field_nombre.getText(), this.field_tipo.getText());

        if (recursos.stream().filter(r -> r.getNombre().equals(recursoAux.getNombre())).count() == 0) {
            //modificicar
            if (this.recurso != null) {
                this.recurso.setNombre(recursoAux.getNombre());
                this.recurso.setTipo(recursoAux.getTipo());
                this.recurso.setId(recursoAux.getId());

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText(null);
                alert.setTitle("Informacion");
                alert.setContentText("El recurso se modifico correctamente");
                alert.showAndWait();

            } else {
                this.recurso = recursoAux;
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText(null);
                alert.setTitle("Informacion");
                alert.setContentText("El recurso se agrego correctamente");
                alert.showAndWait();
            }


            Stage stage = (Stage) this.boton_guardar.getScene().getWindow();//para cerrar la ventana apenas se agrege la persona
            stage.close();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setTitle("Error");
            alert.setContentText("El recurso existe");
            alert.showAndWait();

        }
    }

    public void initAtributtes(ObservableList<RecursoTiempoEjecucion> recursos) {
        this.recursos = recursos;
    }

    public void salir(ActionEvent actionEvent) {
        this.recurso = null;
        Stage stage = (Stage) this.boton_guardar.getScene().getWindow();//para cerrar la ventana apenas se agrege el recurso
        stage.close();
    }

    public Recurso getRecurso() {
        return recurso;
    }
}
