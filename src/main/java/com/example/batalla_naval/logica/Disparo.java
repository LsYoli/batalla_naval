package com.example.batalla_naval.logica;

import java.io.Serial;
import java.io.Serializable;

/**
 * Clase Disparo de la aplicación Batalla Naval.
 */
public class Disparo implements Serializable {
    private final int numero;
    private final String jugador;
    private final Coordenada coordenada;
    private final EnumResultadoDisparo resultado;
    @Serial
    private static final long serialVersionUID = 1L;

/**
 * Descripción para Disparo.
 * @param numero parámetro de entrada.
 * @param jugador parámetro de entrada.
 * @param coordenada parámetro de entrada.
 * @param resultado parámetro de entrada.
 */
    public Disparo(int numero, String jugador, Coordenada coordenada, EnumResultadoDisparo resultado) {
        this.numero = numero;
        this.jugador = jugador;
        this.coordenada = coordenada;
        this.resultado = resultado;
    }

/**
 * Descripción para getNumero.
 * @return valor resultante.
 */
    public int getNumero() {
        return numero;
    }

/**
 * Descripción para getJugador.
 * @return valor resultante.
 */
    public String getJugador() {
        return jugador;
    }

/**
 * Descripción para getCoordenada.
 * @return valor resultante.
 */
    public Coordenada getCoordenada() {
        return coordenada;
    }

/**
 * Descripción para getResultado.
 * @return valor resultante.
 */
    public EnumResultadoDisparo getResultado() {
        return resultado;
    }
}
