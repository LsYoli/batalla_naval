package com.example.batalla_naval.visualizacion;

import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

/**
 * Gestor de visualizaciones 2D para el juego Batalla Naval
 * Proporciona figuras profesionales para barcos y resultados de disparos
 */
public class GestorVisualizacion {



    /**
     * Crea un barco visual basado en su tamaño
     * @param tamano Número de casillas que ocupa el barco (1-4)
     * @return Group con la representación gráfica del barco
     */
    public static Group crearBarco(int tamano) {
        return switch (tamano) {
            case 1 -> crearFragata();
            case 2 -> crearDestructor();
            case 3 -> crearSubmarino();
            case 4 -> crearPortaaviones();
            default -> crearFragata();
        };
    }

    /**
     * Fragata: 1 casilla - Pequeña nave militar
     */
    private static Group crearFragata() {
        Group grupo = new Group();


        Rectangle casco = new Rectangle(10, 22);
        casco.setFill(Color.web("#1a472a"));
        casco.setStroke(Color.web("#0d2415"));
        casco.setStrokeWidth(1.5);
        casco.setArcWidth(4);
        casco.setArcHeight(4);


        Polygon vela = new Polygon(10, 2, 14, 10, 6, 10);
        vela.setFill(Color.web("#d4a574"));
        vela.setStroke(Color.web("#8b6f47"));
        vela.setStrokeWidth(1);


        vela.setTranslateY(3);
        vela.setTranslateX(2);

        grupo.getChildren().addAll(casco, vela);
        return grupo;
    }

    /**
     * Destructor: 2 casillas - Nave media
     */
    private static Group crearDestructor() {
        Group grupo = new Group();


        Rectangle casco = new Rectangle(26, 22);
        casco.setFill(Color.web("#2d5a3d"));
        casco.setStroke(Color.web("#1a2f22"));
        casco.setStrokeWidth(1.5);
        casco.setArcWidth(3);
        casco.setArcHeight(3);


        Polygon vela1 = new Polygon(5, 2, 9, 10, 2, 10);
        vela1.setFill(Color.web("#e8b896"));
        vela1.setStroke(Color.web("#8b6f47"));
        vela1.setStrokeWidth(1);
        vela1.setTranslateX(3);
        vela1.setTranslateY(2);


        Polygon vela2 = new Polygon(18, 3, 21, 9, 15, 9);
        vela2.setFill(Color.web("#d4a574"));
        vela2.setStroke(Color.web("#8b6f47"));
        vela2.setStrokeWidth(1);
        vela2.setTranslateX(2);
        vela2.setTranslateY(3);

        grupo.getChildren().addAll(casco, vela1, vela2);
        return grupo;
    }

    /**
     * Submarino: 3 casillas - Nave grande
     */
    private static Group crearSubmarino() {
        Group grupo = new Group();


        Ellipse casco = new Ellipse(14, 10);
        casco.setFill(Color.web("#1a472a"));
        casco.setStroke(Color.web("#0d2415"));
        casco.setStrokeWidth(1.5);


        Line periscopio = new Line(10, 0, 10, 6);
        periscopio.setStroke(Color.web("#3d5a47"));
        periscopio.setStrokeWidth(2);


        Circle cabezaPeriscopio = new Circle(10, 0, 2);
        cabezaPeriscopio.setFill(Color.web("#555555"));
        cabezaPeriscopio.setStroke(Color.web("#333333"));
        cabezaPeriscopio.setStrokeWidth(0.5);


        Polygon ala1 = new Polygon(5, 9, 8, 12, 2, 12);
        ala1.setFill(Color.web("#2d5a3d"));
        ala1.setStroke(Color.web("#1a2f22"));
        ala1.setStrokeWidth(1);

        Polygon ala2 = new Polygon(15, 9, 18, 12, 12, 12);
        ala2.setFill(Color.web("#2d5a3d"));
        ala2.setStroke(Color.web("#1a2f22"));
        ala2.setStrokeWidth(1);

        grupo.getChildren().addAll(casco, periscopio, cabezaPeriscopio, ala1, ala2);
        return grupo;
    }

    /**
     * Portaaviones: 4 casillas - Nave grande de guerra
     */
    private static Group crearPortaaviones() {
        Group grupo = new Group();


        Rectangle casco = new Rectangle(28, 22);
        casco.setFill(Color.web("#3d5a47"));
        casco.setStroke(Color.web("#1a2f22"));
        casco.setStrokeWidth(1.5);
        casco.setArcWidth(2);
        casco.setArcHeight(2);


        Rectangle puente = new Rectangle(8, 8);
        puente.setFill(Color.web("#5a7a6a"));
        puente.setStroke(Color.web("#1a2f22"));
        puente.setStrokeWidth(1);
        puente.setTranslateX(18);
        puente.setTranslateY(2);


        Line antena = new Line(22, 2, 22, -2);
        antena.setStroke(Color.web("#999999"));
        antena.setStrokeWidth(1.5);
        antena.setTranslateX(18);
        antena.setTranslateY(3);


        Polygon vela1 = new Polygon(3, 2, 7, 10, 0, 10);
        vela1.setFill(Color.web("#e8b896"));
        vela1.setStroke(Color.web("#8b6f47"));
        vela1.setStrokeWidth(1);
        vela1.setTranslateY(2);

        Polygon vela2 = new Polygon(12, 1, 15, 9, 9, 9);
        vela2.setFill(Color.web("#d4a574"));
        vela2.setStroke(Color.web("#8b6f47"));
        vela2.setStrokeWidth(1);
        vela2.setTranslateY(2);

        Polygon vela3 = new Polygon(20, 3, 24, 10, 16, 10);
        vela3.setFill(Color.web("#c89968"));
        vela3.setStroke(Color.web("#8b6f47"));
        vela3.setStrokeWidth(1);
        vela3.setTranslateY(2);

        grupo.getChildren().addAll(casco, vela1, vela2, vela3, puente, antena);
        return grupo;
    }



    /**
     * Marca para AGUA: círculo azul con onda
     */
    public static Group crearMarcaAgua() {
        Group grupo = new Group();


        Circle circulo = new Circle(15, 15, 6);
        circulo.setFill(Color.web("#4da6ff"));
        circulo.setStroke(Color.web("#0066cc"));
        circulo.setStrokeWidth(1.5);


        Circle onda1 = new Circle(15, 15, 9);
        onda1.setFill(Color.TRANSPARENT);
        onda1.setStroke(Color.web("#0066cc"));
        onda1.setStrokeWidth(0.8);
        onda1.setOpacity(0.6);

        Circle onda2 = new Circle(15, 15, 12);
        onda2.setFill(Color.TRANSPARENT);
        onda2.setStroke(Color.web("#0066cc"));
        onda2.setStrokeWidth(0.5);
        onda2.setOpacity(0.3);

        grupo.getChildren().addAll(onda2, onda1, circulo);
        return grupo;
    }

    /**
     * Marca para TOCADO: explosión roja con radiación
     */
    public static Group crearMarcaTocado() {
        Group grupo = new Group();


        Circle explosion = new Circle(15, 15, 5);
        explosion.setFill(Color.web("#ff4444"));
        explosion.setStroke(Color.web("#cc0000"));
        explosion.setStrokeWidth(1.5);


        for (int i = 0; i < 8; i++) {
            double angulo = (i * Math.PI) / 4;
            double x1 = 15 + 7 * Math.cos(angulo);
            double y1 = 15 + 7 * Math.sin(angulo);
            double x2 = 15 + 11 * Math.cos(angulo);
            double y2 = 15 + 11 * Math.sin(angulo);

            Line linea = new Line(x1, y1, x2, y2);
            linea.setStroke(Color.web("#ff6600"));
            linea.setStrokeWidth(1.5);
            grupo.getChildren().add(linea);
        }


        Circle anillo = new Circle(15, 15, 10);
        anillo.setFill(Color.TRANSPARENT);
        anillo.setStroke(Color.web("#ff4444"));
        anillo.setStrokeWidth(1);
        anillo.setOpacity(0.6);

        grupo.getChildren().addAll(anillo, explosion);
        return grupo;
    }

    /**
     * Marca para HUNDIDO: calavera con huesos
     */
    public static Group crearMarcaHundido() {
        Group grupo = new Group();


        Circle calavera = new Circle(15, 12, 6);
        calavera.setFill(Color.web("#cccccc"));
        calavera.setStroke(Color.web("#333333"));
        calavera.setStrokeWidth(1.5);


        Rectangle mandibula = new Rectangle(9, 18, 12, 4);
        mandibula.setFill(Color.web("#bbbbbb"));
        mandibula.setStroke(Color.web("#333333"));
        mandibula.setStrokeWidth(1);


        Line ojo1a = new Line(11, 9, 13, 11);
        ojo1a.setStroke(Color.web("#333333"));
        ojo1a.setStrokeWidth(1.5);
        Line ojo1b = new Line(13, 9, 11, 11);
        ojo1b.setStroke(Color.web("#333333"));
        ojo1b.setStrokeWidth(1.5);

        Line ojo2a = new Line(17, 9, 19, 11);
        ojo2a.setStroke(Color.web("#333333"));
        ojo2a.setStrokeWidth(1.5);
        Line ojo2b = new Line(19, 9, 17, 11);
        ojo2b.setStroke(Color.web("#333333"));
        ojo2b.setStrokeWidth(1.5);


        Line hueso1 = new Line(5, 15, 10, 18);
        hueso1.setStroke(Color.web("#666666"));
        hueso1.setStrokeWidth(2);
        Line hueso2 = new Line(25, 15, 20, 18);
        hueso2.setStroke(Color.web("#666666"));
        hueso2.setStrokeWidth(2);

        grupo.getChildren().addAll(hueso1, hueso2, mandibula, calavera,
                ojo1a, ojo1b, ojo2a, ojo2b);
        return grupo;
    }

    /**
     * Marca de turno - Indicador de "Dispara aquí"
     */
    public static Group crearMarcaTurno() {
        Group grupo = new Group();


        Circle punto = new Circle(15, 15, 4);
        punto.setFill(Color.web("#ff3333"));
        punto.setStroke(Color.web("#cc0000"));
        punto.setStrokeWidth(1);


        Line linea1 = new Line(15, 8, 15, 22);
        linea1.setStroke(Color.web("#ffaa00"));
        linea1.setStrokeWidth(1);
        linea1.setOpacity(0.7);

        Line linea2 = new Line(8, 15, 22, 15);
        linea2.setStroke(Color.web("#ffaa00"));
        linea2.setStrokeWidth(1);
        linea2.setOpacity(0.7);


        Circle anillo = new Circle(15, 15, 10);
        anillo.setFill(Color.TRANSPARENT);
        anillo.setStroke(Color.web("#ffaa00"));
        anillo.setStrokeWidth(1.5);
        anillo.setOpacity(0.5);

        grupo.getChildren().addAll(anillo, linea1, linea2, punto);
        return grupo;
    }
}
