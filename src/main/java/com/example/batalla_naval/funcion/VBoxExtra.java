package com.example.funcion; // paquete del componente adicional

import com.example.batalla_naval.logica.*; // importa la lógica para leer tableros
import com.example.batalla_naval.visualizacion.GestorVisualizacion; //Importado originalmente, se mantiene
import com.example.batalla_naval.vista.BarcoFX; // Importar la clase BarcoFX
import javafx.collections.FXCollections; // para listas observables
import javafx.collections.ObservableList; // lista observable
import javafx.scene.control.TableColumn; // columnas de la tabla
import javafx.scene.control.TableView; // tabla para mostrar disparos
import javafx.scene.control.cell.PropertyValueFactory; // factoría para columnas
import javafx.scene.layout.GridPane; // grilla para tableros
import javafx.scene.layout.StackPane; // celdas de la grilla
import javafx.scene.layout.VBox; // contenedor vertical
import javafx.scene.paint.Color; // colores
import javafx.scene.shape.Circle; // marca de agua
import javafx.scene.shape.Ellipse; // se mantiene para la marca de agua, ya no para barcos
import javafx.scene.text.Text; // texto para impactos
import javafx.scene.Group; // se mantiene para las figuras 2D (aunque ya no se usan para barcos)

import java.util.List;

public class VBoxExtra extends VBox { // clase sencilla que arma la vista de verificación

    // ⭐⭐ CONSTANTE: Tamaño de celda para la vista auxiliar (debe ser coherente con el CSS/diseño original)
    private final double TAMANO_CELDA_AUX = 25.0;

    public VBoxExtra(FachadaJuego fachada) { // constructor que recibe la fachada
        super(10); // llama al constructor de VBox con espacio de 10 px

        // El tablero de la máquina se dibuja con BarcoFX (mostrarBarcos=true)
        GridPane gridMaquina = construirTablero(fachada.getMaquina().getTableroPosicion(), true);

        // El tablero del jugador se dibuja sin BarcoFX (solo marcas de disparo)
        GridPane gridJugador = construirTablero(fachada.getJugador().getTableroPosicion(), false);

        getChildren().addAll(gridMaquina, gridJugador, construirTabla(fachada)); // agrega los componentes al VBox
    } // cierra el constructor

    private GridPane construirTablero(Tablero tablero, boolean esTableroMaquina) { // arma un GridPane con el estado del tablero
        GridPane grid = new GridPane(); // crea la grilla

        // 1. CONSTRUIR CELDAS Y MARCAS DE DISPARO (Barcos no incluidos, solo la cuadrícula)
        for (int fila = 0; fila < 10; fila++) { // recorre filas
            for (int col = 0; col < 10; col++) { // recorre columnas
                StackPane celdaUI = new StackPane(); // crea una celda visual
                celdaUI.setPrefSize(TAMANO_CELDA_AUX, TAMANO_CELDA_AUX); // define tamaño (usando la constante)
                celdaUI.setStyle("-fx-border-color: grey; -fx-background-color: aliceblue;"); // estilo
                Celda celda = tablero.obtenerCelda(new Coordenada(fila, col)); // obtiene la celda lógica

                // Marcas de disparos
                if (celda.getEstado() == EnumEstadoCelda.AGUA) { // si la celda fue agua
                    Ellipse marcaAgua = new Ellipse(5, 3);
                    marcaAgua.setFill(Color.BLUE);
                    celdaUI.getChildren().add(marcaAgua);
                } else if (celda.getEstado() == EnumEstadoCelda.TOCADO) { // si fue tocado
                    Text x = new Text("X"); // crea texto X
                    x.setFill(Color.ORANGERED); // color
                    x.setStyle("-fx-font-size: 15; -fx-font-weight: bold;"); // Ajuste de tamaño para 25x25
                    celdaUI.getChildren().add(x); // agrega texto
                } else if (celda.getEstado() == EnumEstadoCelda.HUNDIDO) { // si fue hundido
                    Text skull = new Text("☠"); // icono
                    skull.setFill(Color.DARKRED); // color
                    skull.setStyle("-fx-font-size: 15; -fx-font-weight: bold;"); // Ajuste de tamaño para 25x25
                    celdaUI.getChildren().add(skull);
                }

                grid.add(celdaUI, col, fila); // agrega celda al grid
            } // cierra for columnas
        } // cierra for filas

        // Añadir barcos usando BARCOFX (Solo para el tablero de la Máquina)
        if (esTableroMaquina) {
            // Usar BarcoFX para el diseño
            dibujarBarcosVBoxExtra(grid, tablero.getBarcos());
        }

        return grid; // retorna el grid construido
    } // cierra construirTablero

    /**
     * Dibuja la flota usando BarcoFX en el GridPane de la ventana auxiliar,
     * utilizando la lógica de rotación y traslación.
     */
    private void dibujarBarcosVBoxExtra(GridPane grid, List<Barco> flota) {
        for (Barco barco : flota) {
            if (barco.getFilaInicial() != -1) {

                BarcoFX barcoFX = new BarcoFX(barco);

                // Colocación por índice de GridPane y Span
                grid.add(barcoFX, barco.getColumnaInicial(), barco.getFilaInicial());

                int tamano = barco.getTamano();
                if (barco.getOrientacion() == EnumOrientacion.HORIZONTAL) {
                    GridPane.setColumnSpan(barcoFX, tamano);
                    GridPane.setRowSpan(barcoFX, 1);
                } else { // VERTICAL
                    GridPane.setColumnSpan(barcoFX, 1);
                    GridPane.setRowSpan(barcoFX, tamano);
                }

                // Manejo de la Orientación y Rotación (con ajustes de traslación)
                barcoFX.setRotate(0);
                barcoFX.setTranslateX(0);
                barcoFX.setTranslateY(0);

                if (barco.getOrientacion() == EnumOrientacion.VERTICAL) {
                    barcoFX.setRotate(90); // Rota 90 grados

                    // Cálculo del ajuste de largo (compensación de giro)
                    // Usando la constante local TAMANO_CELDA_AUX (25.0)
                    double tamanoTotal = barco.getTamano() * TAMANO_CELDA_AUX;
                    double ajuste = (tamanoTotal - TAMANO_CELDA_AUX) / 2.0;

                    // Aplicar la fórmula de traslación
                    barcoFX.setTranslateX(-ajuste);
                    barcoFX.setTranslateY(-ajuste);
                }
            }
        }
    } // cierra dibujarBarcosVBoxExtra

    private TableView<Disparo> construirTabla(FachadaJuego fachada) { // arma la tabla de disparos
        TableView<Disparo> tabla = new TableView<>(); // crea la tabla
        TableColumn<Disparo, Integer> num = new TableColumn<>("#"); // crea columna número
        num.setCellValueFactory(new PropertyValueFactory<>("numero")); // enlaza propiedad numero
        TableColumn<Disparo, String> jug = new TableColumn<>("Jugador"); // columna jugador
        jug.setCellValueFactory(new PropertyValueFactory<>("jugador")); // enlaza propiedad jugador
        TableColumn<Disparo, String> coord = new TableColumn<>("Coordenada"); // columna coordenada
        coord.setCellValueFactory(new PropertyValueFactory<>("coordenada")); // enlaza propiedad coordenada
        TableColumn<Disparo, String> res = new TableColumn<>("Resultado"); // columna resultado
        res.setCellValueFactory(new PropertyValueFactory<>("resultado")); // enlaza propiedad resultado
        tabla.getColumns().addAll(num, jug, coord, res); // agrega columnas a la tabla
        ObservableList<Disparo> datos = FXCollections.observableArrayList(); // crea lista observable
        datos.addAll(fachada.obtenerHistorial()); // carga los disparos desde la fachada
        tabla.setItems(datos); // asigna los datos
        tabla.setPrefHeight(200); // tamaño preferido
        return tabla; // retorna la tabla
    } // cierra construirTabla
} // cierra la clase VBoxExtra