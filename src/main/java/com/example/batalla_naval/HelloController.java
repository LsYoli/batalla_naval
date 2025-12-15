package com.example.batalla_naval;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * Controlador de la vista de bienvenida generada por la plantilla de JavaFX.
 */
public class HelloController {
    /** Etiqueta que se actualiza al presionar el botón de saludo. */
    @FXML
    private Label welcomeText;

    /**
     * Maneja el clic del botón de saludo mostrando un mensaje por defecto.
     */
    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}
