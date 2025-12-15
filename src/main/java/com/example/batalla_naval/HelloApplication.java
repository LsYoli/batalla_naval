package com.example.batalla_naval;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Punto de entrada de la aplicación JavaFX que carga la vista principal.
 */
public class HelloApplication extends Application {
    /**
     * Inicia la aplicación cargando la vista principal.
     *
     * @param stage escenario principal donde se muestra la escena.
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("principal.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        stage.setTitle("Batalla Naval - Fundamentos de POE");
        stage.setScene(scene);
        stage.show();
    }
}
