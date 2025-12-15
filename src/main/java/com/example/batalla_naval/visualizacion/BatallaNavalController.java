package com.example.batalla_naval.controlador;

import com.example.batalla_naval.logica.Barco;
import com.example.batalla_naval.logica.Coordenada;
import com.example.batalla_naval.logica.EnumOrientacion;
import com.example.batalla_naval.logica.Tablero;
import com.example.batalla_naval.logica.PosicionInvalidaException;
import com.example.batalla_naval.visualizacion.BarcoFX;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Alert;

/**
 * Clase BatallaNavalController de la aplicación Batalla Naval.
 */
public class BatallaNavalController {

    @FXML
    private AnchorPane tableroVista;

    private Tablero tableroLogica = new Tablero();


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
        double margen = barcoFX.getMargenSobresaliente();


        barcoFX.setRotate(0);
        barcoFX.setTranslateX(0);
        barcoFX.setTranslateY(0);


        double desfase = TAMANO_CELDA;


        double x = (barcoLogica.getColumnaInicial() * TAMANO_CELDA) - desfase;


        double y = (barcoLogica.getFilaInicial() * TAMANO_CELDA) - margen - desfase;

        barcoFX.setLayoutX(x);
        barcoFX.setLayoutY(y);



        if (barcoLogica.getOrientacion() == EnumOrientacion.VERTICAL) {
            barcoFX.setRotate(90);


            double tamanoTotal = barcoLogica.getTamano() * TAMANO_CELDA;


            double ajuste = (tamanoTotal - TAMANO_CELDA) / 2.0;

            barcoFX.setTranslateX(ajuste + margen);

            barcoFX.setTranslateY(-ajuste - margen);
        }


        tableroVista.getChildren().add(barcoFX);
    }
}
