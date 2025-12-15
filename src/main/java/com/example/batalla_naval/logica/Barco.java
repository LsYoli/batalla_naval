package com.example.batalla_naval.logica;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa un barco dentro del juego, gestionando su tamaño, orientación y
 * celdas ocupadas.
 */
public abstract class Barco implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /** Número de celdas que ocupa el barco. */
    protected int tamano;
    /** Orientación actual del barco. */
    protected EnumOrientacion orientacion;
    /** Fila inicial asignada al barco. */
    protected int filaInicial;
    /** Columna inicial asignada al barco. */
    protected int columnaInicial;
    /** Celdas ocupadas por el barco en el tablero. */
    protected List<Celda> celdasOcupadas;
    /** Cantidad de impactos recibidos. */
    protected int impactos;
    /** Nombre descriptivo del barco. */
    protected String nombre;

    /**
     * Crea un barco indicando su tamaño y nombre.
     *
     * @param tamano número de celdas que ocupa
     * @param nombre etiqueta descriptiva del barco
     */
    public Barco(int tamano, String nombre) {
        this.tamano = tamano;
        this.nombre = nombre;
        this.celdasOcupadas = new ArrayList<>();
        this.impactos = 0;
        this.orientacion = EnumOrientacion.HORIZONTAL;
    }

    /**
     * Registra la posición inicial del barco en el tablero.
     *
     * @param fila    fila inicial
     * @param columna columna inicial
     */
    public void setPosicionInicial(int fila, int columna) {
        this.filaInicial = fila;
        this.columnaInicial = columna;
    }

    /**
     * Obtiene la fila inicial definida para el barco.
     *
     * @return fila inicial
     */
    public int getFilaInicial() {
        return filaInicial;
    }

    /**
     * Obtiene la columna inicial definida para el barco.
     *
     * @return columna inicial
     */
    public int getColumnaInicial() {
        return columnaInicial;
    }

    /**
     * Devuelve la orientación actual del barco.
     *
     * @return orientación del barco
     */
    public EnumOrientacion getOrientacion() {
        return orientacion;
    }

    /**
     * Asigna la orientación del barco.
     *
     * @param orientacion orientación deseada
     */
    public void asignarOrientacion(EnumOrientacion orientacion) {
        this.orientacion = orientacion;
    }

    /**
     * Agrega una celda al barco y registra la relación en ambas direcciones.
     *
     * @param celda celda ocupada por el barco
     */
    public void agregarCelda(Celda celda) {
        celdasOcupadas.add(celda);
        celda.setBarco(this);
    }

    /**
     * Obtiene el tamaño del barco.
     *
     * @return número de celdas ocupadas
     */
    public int getTamano() {
        return tamano;
    }

    /**
     * Obtiene el nombre del barco.
     *
     * @return nombre descriptivo
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Devuelve las celdas ocupadas por el barco.
     *
     * @return lista de celdas
     */
    public List<Celda> getCeldasOcupadas() {
        return celdasOcupadas;
    }

    /**
     * Registra un impacto recibido en el barco.
     *
     * @param celda celda alcanzada
     */
    public void registrarImpacto(Celda celda) {
        impactos++;
    }

    /**
     * Determina si el barco está completamente hundido.
     *
     * @return {@code true} si los impactos alcanzan su tamaño
     */
    public boolean estaHundido() {
        return impactos >= tamano;
    }
}
