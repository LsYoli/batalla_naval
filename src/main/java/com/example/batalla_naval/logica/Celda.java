package com.example.batalla_naval.logica;

import java.io.Serial;
import java.io.Serializable;

/**
 * Clase Celda de la aplicación Batalla Naval.
 */
public class Celda implements Serializable {
    private final Coordenada coordenada;
    private Barco barco;
    private boolean fueAtacada;
    private EnumEstadoCelda estado;
    @Serial
    private static final long serialVersionUID = 1L;

/**
 * Descripción para Celda.
 * @param coordenada parámetro de entrada.
 */
    public Celda(Coordenada coordenada) {
        this.coordenada = coordenada;
        this.fueAtacada = false;
        this.estado = EnumEstadoCelda.SIN_DISPARO;
    }

/**
 * Descripción para getCoordenada.
 * @return valor resultante.
 */
    public Coordenada getCoordenada() {
        return coordenada;
    }

/**
 * Descripción para getBarco.
 * @return valor resultante.
 */
    public Barco getBarco() {
        return barco;
    }

/**
 * Descripción para setBarco.
 * @param barco parámetro de entrada.
 */
    public void setBarco(Barco barco) {
        this.barco = barco;
    }

/**
 * Descripción para isFueAtacada.
 * @return valor resultante.
 */
    public boolean isFueAtacada() {
        return fueAtacada;
    }

/**
 * Descripción para getEstado.
 * @return valor resultante.
 */
    public EnumEstadoCelda getEstado() {
        return estado;
    }

/**
 * Descripción para recibirDisparo.
 * @return valor resultante.
 */
    public EnumResultadoDisparo recibirDisparo() {
        fueAtacada = true;
        if (barco == null) {
            estado = EnumEstadoCelda.AGUA;
            return EnumResultadoDisparo.AGUA;
        }
        barco.registrarImpacto(this);
        if (barco.estaHundido()) {
            estado = EnumEstadoCelda.HUNDIDO;
            return EnumResultadoDisparo.HUNDIDO;
        } else {
            estado = EnumEstadoCelda.TOCADO;
            return EnumResultadoDisparo.TOCADO;
        }
    }

/**
 * Descripción para marcarHundidoCompleto.
 */
    public void marcarHundidoCompleto() {
        estado = EnumEstadoCelda.HUNDIDO;
    }

/**
 * Descripción para aplicarResultado.
 * @param resultado parámetro de entrada.
 */
    public void aplicarResultado(EnumResultadoDisparo resultado) {
        fueAtacada = true;
        if (resultado == EnumResultadoDisparo.AGUA) {
            estado = EnumEstadoCelda.AGUA;
        } else if (resultado == EnumResultadoDisparo.TOCADO) {
            estado = EnumEstadoCelda.TOCADO;
        } else {
            estado = EnumEstadoCelda.HUNDIDO;
        }
    }
}
