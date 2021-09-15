package controlador;

import javafx.scene.control.Alert;

public class Notificaciones {

    public static void alertaError(String title, String mensaje){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setTitle(title);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    public static void alertaInformacion(String title, String mensaje){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setTitle(title);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }


}
