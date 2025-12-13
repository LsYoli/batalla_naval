package com.example.batalla_naval.logica; // paquete donde se ubican las clases de lógica

import java.io.Serial;
import java.io.Serializable;

public class Coordenada implements Serializable { // clase simple para guardar fila y columna
    private final int fila; // almacena la fila de la celda
    private final int columna; // almacena la columna de la celda
    @Serial
    private static final long serialVersionUID = 1L;

    public Coordenada(int fila, int columna) { // constructor que recibe fila y columna
        this.fila = fila; // asigna la fila recibida
        this.columna = columna; // asigna la columna recibida
    } // cierra el constructor

    public int getFila() { // getter para la fila
        return fila; // devuelve la fila almacenada
    } // cierra el método getFila

    public int getColumna() { // getter para la columna
        return columna; // devuelve la columna almacenada
    } // cierra el método getColumna

    @Override // indica que sobrescribimos equals
    public boolean equals(Object obj) { // compara coordenadas
        if (this == obj) { // si es la misma referencia
            return true; // devuelve verdadero
        } // cierra if
        if (obj == null || getClass() != obj.getClass()) { // valida tipo y nulidad
            return false; // devuelve falso si no coincide
        } // cierra if
        Coordenada that = (Coordenada) obj; // convierte el objeto a Coordenada
        return fila == that.fila && columna == that.columna; // compara fila y columna
    } // cierra equals

    @Override // indica que sobrescribimos hashCode
    public int hashCode() { // genera código hash
        return 31 * fila + columna; // calcula un hash sencillo
    } // cierra hashCode

    @Override // sobrescribe toString para depuración
    public String toString() { // construye texto de coordenada
        return "(" + fila + "," + columna + ")"; // arma la representación
    } // cierra toString
} // cierra la clase Coordenada
