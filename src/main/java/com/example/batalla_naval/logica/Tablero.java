package com.example.batalla_naval.logica;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase Tablero de la aplicación Batalla Naval.
 */
public class Tablero implements Serializable {
    private final ArrayList<ArrayList<Celda>> grilla;
    private final ArrayList<Barco> barcos;
    @Serial
    private static final long serialVersionUID = 1L;

/**
 * Descripción para Tablero.
 */
    public Tablero() {
        grilla = new ArrayList<>();
        barcos = new ArrayList<>();
        for (int fila = 0; fila < 10; fila++) {
            ArrayList<Celda> filaActual = new ArrayList<>();
            for (int col = 0; col < 10; col++) {

                filaActual.add(new Celda(new Coordenada(fila, col)));
            }
            grilla.add(filaActual);
        }
    }



/**
 * Descripción para getGrilla.
 * @return valor resultante.
 */
    public ArrayList<ArrayList<Celda>> getGrilla() {
        return grilla;
    }

/**
 * Descripción para getBarcos.
 * @return valor resultante.
 */
    public List<Barco> getBarcos() {
        return barcos;
    }

/**
 * Descripción para obtenerCelda.
 * @param coord parámetro de entrada.
 * @return valor resultante.
 */
    public Celda obtenerCelda(Coordenada coord) {

        return grilla.get(coord.getFila()).get(coord.getColumna());
    }



/**
 * Descripción para esPosicionValida.
 * @param barco parámetro de entrada.
 * @param inicio parámetro de entrada.
 * @param orientacion parámetro de entrada.
 * @return valor resultante.
 */
    public boolean esPosicionValida(Barco barco, Coordenada inicio, EnumOrientacion orientacion) {
        int fila = inicio.getFila();
        int col = inicio.getColumna();


        if (orientacion == EnumOrientacion.HORIZONTAL && col + barco.getTamano() > 10) {
            return false;
        }
        if (orientacion == EnumOrientacion.VERTICAL && fila + barco.getTamano() > 10) {
            return false;
        }


        for (int i = 0; i < barco.getTamano(); i++) {

            int filaObjetivo = orientacion == EnumOrientacion.HORIZONTAL ? fila : fila + i;
            int colObjetivo = orientacion == EnumOrientacion.HORIZONTAL ? col + i : col;

            Celda celda = grilla.get(filaObjetivo).get(colObjetivo);
            if (celda.getBarco() != null) {
                return false;
            }
        }
        return true;
    }

/**
 * Descripción para colocarBarco.
 * @param barco parámetro de entrada.
 * @param inicio parámetro de entrada.
 * @param orientacion parámetro de entrada.
 */
    public void colocarBarco(Barco barco, Coordenada inicio, EnumOrientacion orientacion) throws PosicionInvalidaException {
        if (!esPosicionValida(barco, inicio, orientacion)) {
            throw new PosicionInvalidaException("No se puede colocar el barco en esa posición");
        }



        barco.setPosicionInicial(inicio.getFila(), inicio.getColumna());

        barco.asignarOrientacion(orientacion);
        int fila = inicio.getFila();
        int col = inicio.getColumna();

        for (int i = 0; i < barco.getTamano(); i++) {
            int filaObjetivo = orientacion == EnumOrientacion.HORIZONTAL ? fila : fila + i;
            int colObjetivo = orientacion == EnumOrientacion.HORIZONTAL ? col + i : col;

            Celda celda = grilla.get(filaObjetivo).get(colObjetivo);
            barco.agregarCelda(celda);
        }
        barcos.add(barco);
    }



/**
 * Descripción para registrarDisparo.
 * @param coordenada parámetro de entrada.
 * @return valor resultante.
 */
    public EnumResultadoDisparo registrarDisparo(Coordenada coordenada) {
        Celda celda = obtenerCelda(coordenada);

        EnumResultadoDisparo resultado = celda.recibirDisparo();

        if (resultado == EnumResultadoDisparo.HUNDIDO && celda.getBarco() != null) {
            for (Celda c : celda.getBarco().getCeldasOcupadas()) {
                c.marcarHundidoCompleto();
            }
        }
        return resultado;
    }

/**
 * Descripción para estanTodosLosBarcosHundidos.
 * @return valor resultante.
 */
    public boolean estanTodosLosBarcosHundidos() {
        for (Barco b : barcos) {
            if (!b.estaHundido()) {
                return false;
            }
        }
        return true;
    }
}
