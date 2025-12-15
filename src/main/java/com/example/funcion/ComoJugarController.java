package com.example.funcion;

import com.example.batalla_naval.HelloApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Controlador que gestiona la navegación desde la vista de instrucciones
 * hacia la pantalla principal.
 */
public class ComoJugarController {
    /**
     * Regresa a la pantalla principal cargando la vista {@code principal.fxml}.
     *
     * @param event evento del botón presionado
     * @throws IOException si la vista no puede cargarse
     */
    @FXML
/**
 * Descripción para manejarBotonRegresar.
 * @param event parámetro de entrada.
 */
    protected void manejarBotonRegresar(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("principal.fxml"));
        Parent root = loader.load();
        Scene escena = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(escena);
        stage.show();
    }
}
