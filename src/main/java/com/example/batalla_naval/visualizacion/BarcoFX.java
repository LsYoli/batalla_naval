package com.example.batalla_naval.visualizacion;

import com.example.batalla_naval.logica.Barco;
import com.example.batalla_naval.logica.EnumOrientacion;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Polygon;

/**
 * Clase BarcoFX de la aplicación Batalla Naval.
 */
public class BarcoFX extends Pane {
    private final Barco barcoLogica;
    private final double TAMANO_CELDA = 27.0;


    private final double MARGEN_SOBRESALIENTE = 0.0001 * TAMANO_CELDA;

/**
 * Descripción para BarcoFX.
 * @param barcoLogica parámetro de entrada.
 */
    public BarcoFX(Barco barcoLogica) {
        this.barcoLogica = barcoLogica;
        dibujarBarco();
        this.setStyle("-fx-border-color: transparent;");
    }


/**
 * Descripción para getMargenSobresaliente.
 * @return valor resultante.
 */
    public double getMargenSobresaliente() {
        return MARGEN_SOBRESALIENTE;
    }
/**
 * Descripción para getBarcoLogica.
 * @return valor resultante.
 */
    public Barco getBarcoLogica() {
        return barcoLogica;
    }

    /**
     * Configura el tamaño del Pane contenedor (BarcoFX) y dibuja la figura interna.
     * El tamaño del Pane incluye el margen, para evitar recortes al rotar.
     */
    private void dibujarBarco() {
        int tamano = barcoLogica.getTamano();


        if (barcoLogica.getOrientacion() == EnumOrientacion.HORIZONTAL) {

            setPrefWidth(tamano * TAMANO_CELDA);

            setPrefHeight(TAMANO_CELDA + MARGEN_SOBRESALIENTE);
        }
        else {

            setPrefWidth(TAMANO_CELDA + MARGEN_SOBRESALIENTE);

            setPrefHeight(tamano * TAMANO_CELDA);
        }

        Color colorBase = Color.web("#6a7c8c");


        switch (barcoLogica.getNombre()) {
            case "Fragata":
                dibujarFragata(colorBase);
                break;
            case "Destructor":
                dibujarDestructor(colorBase);
                break;
            case "Submarino":
                dibujarSubmarino(colorBase);
                break;
            case "Portaaviones":
                dibujarPortaaviones(colorBase);
                break;
        }
    }



/**
 * Descripción para dibujarFragata.
 * @param colorBase parámetro de entrada.
 */
    private void dibujarFragata(Color colorBase) {

        double anchoCuerpo = TAMANO_CELDA;
        double alto = TAMANO_CELDA;
        double desplazamientoY = MARGEN_SOBRESALIENTE;


        Polygon fragataShape = new Polygon(
                0.0, desplazamientoY + alto * 0.1,
                anchoCuerpo * 0.9, desplazamientoY + 0.0,
                anchoCuerpo, desplazamientoY + alto * 0.5,
                anchoCuerpo * 0.9, desplazamientoY + alto,
                0.0, desplazamientoY + alto * 0.9
        );

        fragataShape.setFill(colorBase.brighter());
        fragataShape.setStroke(Color.BLACK);
        getChildren().add(fragataShape);
    }

/**
 * Descripción para dibujarDestructor.
 * @param colorBase parámetro de entrada.
 */
    private void dibujarDestructor(Color colorBase) {
        double anchoCuerpo = barcoLogica.getTamano() * TAMANO_CELDA;
        double alto = TAMANO_CELDA;
        double desplazamientoY = MARGEN_SOBRESALIENTE;

        Polygon cuerpoProa = new Polygon(
                0.0, desplazamientoY + 0.0,
                anchoCuerpo * 0.9, desplazamientoY + 0.0,
                anchoCuerpo, desplazamientoY + alto / 2.0,
                anchoCuerpo * 0.9, desplazamientoY + alto,
                0.0, desplazamientoY + alto
        );
        cuerpoProa.setFill(colorBase);
        cuerpoProa.setStroke(Color.BLACK);

        Rectangle estructura = new Rectangle(anchoCuerpo * 0.2, alto * 0.5, Color.web("#4a5c6b"));
        estructura.setLayoutX(anchoCuerpo * 0.35);
        estructura.setLayoutY(desplazamientoY + alto * 0.25);

        getChildren().addAll(cuerpoProa, estructura);
    }

/**
 * Descripción para dibujarSubmarino.
 * @param colorBase parámetro de entrada.
 */
    private void dibujarSubmarino(Color colorBase) {
        double anchoCuerpo = barcoLogica.getTamano() * TAMANO_CELDA;
        double alto = TAMANO_CELDA;
        double desplazamientoY = MARGEN_SOBRESALIENTE;


        Rectangle cuerpo = new Rectangle(anchoCuerpo, alto, Color.web("#4a5c6b"));
        cuerpo.setArcWidth(alto);
        cuerpo.setArcHeight(alto);
        cuerpo.setStroke(Color.BLACK);

        cuerpo.setLayoutY(desplazamientoY);


        Rectangle torreta = new Rectangle(anchoCuerpo * 0.15, alto * 0.6, Color.web("#5d7e8b"));
        torreta.setLayoutX(anchoCuerpo * 0.6);

        torreta.setLayoutY(desplazamientoY - alto * 0.3);
        torreta.setArcWidth(5);
        torreta.setArcHeight(5);


        Rectangle periscopio = new Rectangle(anchoCuerpo * 0.02, alto * 0.4, Color.web("#333"));
        periscopio.setLayoutX(anchoCuerpo * 0.6 + torreta.getWidth() / 2 - periscopio.getWidth() / 2);

        periscopio.setLayoutY(desplazamientoY - alto * 0.7);

        getChildren().addAll(cuerpo, torreta, periscopio);
    }

/**
 * Descripción para dibujarPortaaviones.
 * @param colorBase parámetro de entrada.
 */
    private void dibujarPortaaviones(Color colorBase) {
        double anchoCuerpo = barcoLogica.getTamano() * TAMANO_CELDA;
        double alto = TAMANO_CELDA;
        double desplazamientoY = MARGEN_SOBRESALIENTE;


        Rectangle cuerpo = new Rectangle(anchoCuerpo, alto, Color.web("#6d7e8f"));
        cuerpo.setArcWidth(10);
        cuerpo.setArcHeight(10);
        cuerpo.setLayoutY(desplazamientoY);


        Rectangle isla = new Rectangle(anchoCuerpo * 0.1, alto * 0.8, Color.web("#8c9fae"));
        isla.setLayoutX(anchoCuerpo * 0.8);
        isla.setLayoutY(desplazamientoY + alto * 0.1);
        isla.setArcWidth(5);
        isla.setArcHeight(5);

        getChildren().addAll(cuerpo, isla);
    }
}
