package com.example.funcion;

import com.example.batalla_naval.logica.Barco;
import com.example.batalla_naval.logica.Celda;
import com.example.batalla_naval.logica.Coordenada;
import com.example.batalla_naval.logica.Disparo;
import com.example.batalla_naval.logica.EnumEstadoCelda;
import com.example.batalla_naval.logica.EnumOrientacion;
import com.example.batalla_naval.logica.FachadaJuego;
import com.example.batalla_naval.logica.Tablero;
import com.example.batalla_naval.visualizacion.BarcoFX;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.text.Text;

import java.util.List;

/**
 * Panel auxiliar que muestra el tablero de la máquina, el tablero del jugador y el historial de disparos.
 */
public class VBoxExtra extends VBox {
    private static final double TAMANO_CELDA_AUX = 25.0;

    /**
     * Construye el panel auxiliar con los tableros y la tabla de disparos.
     *
     * @param fachada fachada de juego desde la que se obtienen los datos a visualizar.
     */
    public VBoxExtra(FachadaJuego fachada) {
        super(10);
        GridPane gridMaquina = construirTablero(fachada.getMaquina().getTableroPosicion(), true);
        GridPane gridJugador = construirTablero(fachada.getJugador().getTableroPosicion(), false);
        getChildren().addAll(gridMaquina, gridJugador, construirTabla(fachada));
    }

    /**
     * Construye la representación visual de un tablero.
     *
     * @param tablero tablero a mostrar.
     * @param esTableroMaquina indica si debe dibujarse la flota visible.
     * @return grilla configurada para el tablero.
     */
    private GridPane construirTablero(Tablero tablero, boolean esTableroMaquina) {
        GridPane grid = new GridPane();

        for (int fila = 0; fila < 10; fila++) {
            for (int col = 0; col < 10; col++) {
                StackPane celdaUI = new StackPane();
                celdaUI.setPrefSize(TAMANO_CELDA_AUX, TAMANO_CELDA_AUX);
                celdaUI.setStyle("-fx-border-color: grey; -fx-background-color: aliceblue;");
                Celda celda = tablero.obtenerCelda(new Coordenada(fila, col));

                if (celda.getEstado() == EnumEstadoCelda.AGUA) {
                    Ellipse marcaAgua = new Ellipse(5, 3);
                    marcaAgua.setFill(Color.BLUE);
                    celdaUI.getChildren().add(marcaAgua);
                } else if (celda.getEstado() == EnumEstadoCelda.TOCADO) {
                    Text x = new Text("X");
                    x.setFill(Color.ORANGERED);
                    x.setStyle("-fx-font-size: 15; -fx-font-weight: bold;");
                    celdaUI.getChildren().add(x);
                } else if (celda.getEstado() == EnumEstadoCelda.HUNDIDO) {
                    Text skull = new Text("☠");
                    skull.setFill(Color.DARKRED);
                    skull.setStyle("-fx-font-size: 15; -fx-font-weight: bold;");
                    celdaUI.getChildren().add(skull);
                }

                grid.add(celdaUI, col, fila);
            }
        }

        if (esTableroMaquina) {
            dibujarBarcosVBoxExtra(grid, tablero.getBarcos());
        }

        return grid;
    }

    /**
     * Dibuja la flota usando componentes BarcoFX dentro del grid auxiliar.
     */
    private void dibujarBarcosVBoxExtra(GridPane grid, List<Barco> flota) {
        for (Barco barco : flota) {
            if (barco.getFilaInicial() != -1) {
                BarcoFX barcoFX = new BarcoFX(barco);
                grid.add(barcoFX, barco.getColumnaInicial(), barco.getFilaInicial());

                int tamano = barco.getTamano();
                if (barco.getOrientacion() == EnumOrientacion.HORIZONTAL) {
                    GridPane.setColumnSpan(barcoFX, tamano);
                    GridPane.setRowSpan(barcoFX, 1);
                } else {
                    GridPane.setColumnSpan(barcoFX, 1);
                    GridPane.setRowSpan(barcoFX, tamano);
                }

                barcoFX.setRotate(0);
                barcoFX.setTranslateX(0);
                barcoFX.setTranslateY(0);

                if (barco.getOrientacion() == EnumOrientacion.VERTICAL) {
                    barcoFX.setRotate(90);
                    double tamanoTotal = barco.getTamano() * TAMANO_CELDA_AUX;
                    double ajuste = (tamanoTotal - TAMANO_CELDA_AUX) / 2.0;
                    barcoFX.setTranslateX(-ajuste);
                    barcoFX.setTranslateY(-ajuste);
                }
            }
        }
    }

    /**
     * Construye la tabla con el historial de disparos registrados.
     *
     * @param fachada fachada de juego para obtener los disparos.
     * @return tabla configurada.
     */
    private TableView<Disparo> construirTabla(FachadaJuego fachada) {
        TableView<Disparo> tabla = new TableView<>();
        TableColumn<Disparo, Integer> num = new TableColumn<>("#");
        num.setCellValueFactory(new PropertyValueFactory<>("numero"));
        TableColumn<Disparo, String> jug = new TableColumn<>("Jugador");
        jug.setCellValueFactory(new PropertyValueFactory<>("jugador"));
        TableColumn<Disparo, String> coord = new TableColumn<>("Coordenada");
        coord.setCellValueFactory(new PropertyValueFactory<>("coordenada"));
        TableColumn<Disparo, String> res = new TableColumn<>("Resultado");
        res.setCellValueFactory(new PropertyValueFactory<>("resultado"));
        tabla.getColumns().addAll(num, jug, coord, res);
        ObservableList<Disparo> datos = FXCollections.observableArrayList();
        datos.addAll(fachada.obtenerHistorial());
        tabla.setItems(datos);
        tabla.setPrefHeight(200);
        return tabla;
    }
}
