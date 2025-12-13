package com.example.batalla_naval.logica; // paquete de la estrategia concreta

import java.io.Serial;
import java.io.Serializable;
import java.util.Collections; // importa utilidades para mezclar
import java.util.LinkedList; // importa LinkedList como implementación de cola
import java.util.List; // importa List como interfaz
import java.util.Queue; // importa Queue para la estructura FIFO

public class EstrategiaDisparoAleatoria implements EstrategiaDisparo, Serializable { // estrategia que dispara en orden aleatorio
    private final Queue<Coordenada> colaDisparos; // cola con todas las coordenadas mezcladas
    @Serial
    private static final long serialVersionUID = 1L;

    public EstrategiaDisparoAleatoria() { // constructor sin parámetros
        colaDisparos = new LinkedList<>(); // inicializa la cola
        List<Coordenada> temporales = new LinkedList<>(); // crea una lista temporal para cargar todas las coordenadas
        for (int fila = 0; fila < 10; fila++) { // recorre filas
            for (int col = 0; col < 10; col++) { // recorre columnas
                temporales.add(new Coordenada(fila, col)); // agrega la coordenada a la lista
            } // cierra for columnas
        } // cierra for filas
        Collections.shuffle(temporales); // mezcla las coordenadas para que sean aleatorias
        colaDisparos.addAll(temporales); // pasa todas las coordenadas a la cola
    } // cierra el constructor

    @Override // implementación del método de la interfaz
    public Coordenada obtenerSiguienteDisparo(Maquina maquina) { // devuelve la siguiente coordenada
        while (!colaDisparos.isEmpty()) { // mientras haya coordenadas
            Coordenada siguiente = colaDisparos.poll(); // toma la primera de la cola
            if (!maquina.getCeldasYaDisparadas().contains(siguiente)) { // verifica que no se haya usado
                maquina.getCeldasYaDisparadas().add(siguiente); // marca la coordenada como usada
                return siguiente; // devuelve la coordenada válida
            } // cierra if
        } // cierra while
        return null; // si no hay más coordenadas retorna null
    } // cierra obtenerSiguienteDisparo
} // cierra la clase EstrategiaDisparoAleatoria
