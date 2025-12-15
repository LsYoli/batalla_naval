package com.example.funcion; // paquete del controlador de instrucciones

import com.example.batalla_naval.HelloApplication; // importa clase para cargar recursos
import javafx.event.ActionEvent; // import de eventos
import javafx.fxml.FXML; // anotación FXML
import javafx.fxml.FXMLLoader; // cargador de vistas
import javafx.scene.Node; // nodo genérico
import javafx.scene.Parent; // padre de la escena
import javafx.scene.Scene; // escena
import javafx.stage.Stage; // ventana

import java.io.IOException; // manejo de errores de entrada salida

public class ComoJugarController { // controlador que maneja la vista de instrucciones
    @FXML // indica que es llamado desde FXML
    protected void manejarBotonRegresar(ActionEvent event) throws IOException { // método para el botón de regreso
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("principal.fxml")); // carga la vista principal
        Parent root = loader.load(); // carga el contenido
        Scene escena = new Scene(root); // crea la escena
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow(); // obtiene la ventana actual
        stage.setScene(escena); // cambia la escena a la principal
        stage.show(); // muestra la escena
    } // cierra manejarBotonRegresar
} // cierra la clase ComoJugarController
