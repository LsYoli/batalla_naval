package com.example.batalla_naval.logica;

import java.io.Serial;
import java.io.Serializable;

/**
 * Representa una coordenada en el tablero de Batalla Naval.
 */
public class Coordenada implements Serializable {
    private final int fila;
    private final int columna;
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Crea una coordenada con fila y columna específicas.
     *
     * @param fila índice de fila.
     * @param columna índice de columna.
     */
    public Coordenada(int fila, int columna) {
        this.fila = fila;
        this.columna = columna;
    }

    /**
     * Devuelve la fila asociada a la coordenada.
     *
     * @return número de fila.
     */
    public int getFila() {
        return fila;
    }

    /**
     * Devuelve la columna asociada a la coordenada.
     *
     * @return número de columna.
     */
    public int getColumna() {
        return columna;
    }

    /**
     * Compara la coordenada con otro objeto.
     *
     * @param obj objeto a comparar.
     * @return true si representan la misma posición.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Coordenada that = (Coordenada) obj;
        return fila == that.fila && columna == that.columna;
    }

    /**
     * Calcula el código hash basado en fila y columna.
     *
     * @return valor hash.
     */
    @Override
    public int hashCode() {
        return 31 * fila + columna;
    }

    /**
     * Representación textual de la coordenada.
     *
     * @return texto en formato (fila,columna).
     */
    @Override
    public String toString() {
        return "(" + fila + "," + columna + ")";
    }
}
