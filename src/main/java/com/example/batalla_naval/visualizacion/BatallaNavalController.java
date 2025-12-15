package com.example.batalla_naval.controlador;

import com.example.batalla_naval.logica.Barco;
import com.example.batalla_naval.logica.Coordenada;
import com.example.batalla_naval.logica.EnumOrientacion;
import com.example.batalla_naval.logica.Tablero;
import com.example.batalla_naval.logica.PosicionInvalidaException;
import com.example.batalla_naval.vista.BarcoFX; // Importa la representación visual

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Alert; // Necesario para mostrar mensajes de error

public class BatallaNavalController {

    @FXML
    private AnchorPane tableroVista;

    private Tablero tableroLogica = new Tablero();

    // CONSTANTE UNIFICADA: Tamaño real de la celda de la vista (35.0).
    private final double TAMANO_CELDA = 35.0;

    /**
     * Maneja el clic en el tablero para intentar colocar un barco.
     */
    public void manejarClickColocacion(Barco barcoSeleccionado, int filaClicada, int columnaClicada, EnumOrientacion orientacionActual) {

        Coordenada inicio = new Coordenada(filaClicada, columnaClicada);

        try {
            tableroLogica.colocarBarco(barcoSeleccionado, inicio, orientacionActual);
            BarcoFX barcoFX = new BarcoFX(barcoSeleccionado);
            colocarBarcoVisual(barcoFX);

        } catch (PosicionInvalidaException e) {
            System.out.println("Error de colocación: " + e.getMessage());
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setTitle("Error de Colocación");
            alerta.setHeaderText(null);
            alerta.setContentText("No se puede colocar el barco aquí: " + e.getMessage());
            alerta.showAndWait();
        }
    }

    /**
     * Posiciona el objeto BarcoFX en el AnchorPane del tablero.
     * Este método utiliza la fórmula más simple y comprobada para la rotación.
     */
    private void colocarBarcoVisual(BarcoFX barcoFX) {
        Barco barcoLogica = barcoFX.getBarcoLogica();
        double margen = barcoFX.getMargenSobresaliente(); // Obtiene el margen (17.5)

        // 1. Resetear transformaciones
        barcoFX.setRotate(0);
        barcoFX.setTranslateX(0);
        barcoFX.setTranslateY(0);

        // 2. Cálculo de Posicionamiento inicial (X, Y)
        double desfase = TAMANO_CELDA;

        // X: Columna * 27.0. Asumimos que la Columna 0 es X=0.0
        double x = (barcoLogica.getColumnaInicial() * TAMANO_CELDA) - desfase;

        // Y: Compensa el margen superior para la torreta
        double y = (barcoLogica.getFilaInicial() * TAMANO_CELDA) - margen - desfase;

        barcoFX.setLayoutX(x);
        barcoFX.setLayoutY(y);


        // 3. Manejo de la Rotación y Traslación (Fórmula Estándar de Centrado)
        if (barcoLogica.getOrientacion() == EnumOrientacion.VERTICAL) {
            barcoFX.setRotate(90); // Rota 90 grados

            // Largo total del barco en píxeles (N * 27.0)
            double tamanoTotal = barcoLogica.getTamano() * TAMANO_CELDA;

            // Ajuste: La mitad de la diferencia entre el largo total y el ancho de una celda.
            double ajuste = (tamanoTotal - TAMANO_CELDA) / 2.0;

            barcoFX.setTranslateX(ajuste + margen); // Traslación X: Mueve a la derecha por el ajuste, más el margen

            barcoFX.setTranslateY(-ajuste - margen); // Traslación Y: Mueve hacia arriba por el ajuste, más el margen
        }

        // 4. Añadir a la Vista
        tableroVista.getChildren().add(barcoFX);
    }
}