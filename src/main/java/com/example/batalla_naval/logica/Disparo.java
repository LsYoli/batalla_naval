package com.example.batalla_naval.logica; // paquete de la clase Disparo

import java.io.Serial;
import java.io.Serializable;

public class Disparo implements Serializable { // clase que modela un disparo realizado
    private final int numero; // número consecutivo del disparo
    private final String jugador; // nombre del jugador que disparó
    private final Coordenada coordenada; // coordenada del disparo
    private final EnumResultadoDisparo resultado; // resultado obtenido
    @Serial
    private static final long serialVersionUID = 1L;

    public Disparo(int numero, String jugador, Coordenada coordenada, EnumResultadoDisparo resultado) { // constructor que recibe datos del disparo
        this.numero = numero; // asigna el número
        this.jugador = jugador; // asigna el jugador
        this.coordenada = coordenada; // asigna la coordenada
        this.resultado = resultado; // asigna el resultado
    } // cierra el constructor

    public int getNumero() { // getter de número
        return numero; // devuelve el número
    } // cierra getNumero

    public String getJugador() { // getter de jugador
        return jugador; // devuelve el jugador
    } // cierra getJugador

    public Coordenada getCoordenada() { // getter de coordenada
        return coordenada; // devuelve la coordenada
    } // cierra getCoordenada

    public EnumResultadoDisparo getResultado() { // getter de resultado
        return resultado; // devuelve el resultado
    } // cierra getResultado
} // cierra la clase Disparo
