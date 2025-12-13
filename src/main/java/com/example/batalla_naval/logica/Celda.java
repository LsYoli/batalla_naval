package com.example.batalla_naval.logica; // paquete de la clase Celda

import java.io.Serial;
import java.io.Serializable;

public class Celda implements Serializable { // clase que representa una casilla del tablero
    private final Coordenada coordenada; // guarda la ubicación de la celda
    private Barco barco; // referencia al barco que ocupa la celda o null
    private boolean fueAtacada; // indica si ya se disparó a la celda
    private EnumEstadoCelda estado; // estado actual de la celda
    @Serial
    private static final long serialVersionUID = 1L;

    public Celda(Coordenada coordenada) { // constructor que recibe la coordenada
        this.coordenada = coordenada; // asigna la coordenada
        this.fueAtacada = false; // marca que inicialmente no ha sido atacada
        this.estado = EnumEstadoCelda.SIN_DISPARO; // estado inicial sin disparo
    } // cierra el constructor

    public Coordenada getCoordenada() { // getter de coordenada
        return coordenada; // devuelve la coordenada almacenada
    } // cierra getCoordenada

    public Barco getBarco() { // getter del barco
        return barco; // devuelve el barco o null
    } // cierra getBarco

    public void setBarco(Barco barco) { // setter para asignar barco
        this.barco = barco; // guarda la referencia del barco
    } // cierra setBarco

    public boolean isFueAtacada() { // indica si la celda ya fue atacada
        return fueAtacada; // devuelve el valor del indicador
    } // cierra isFueAtacada

    public EnumEstadoCelda getEstado() { // getter del estado de la celda
        return estado; // devuelve el estado actual
    } // cierra getEstado

    public EnumResultadoDisparo recibirDisparo() { // procesa un disparo sobre la celda
        fueAtacada = true; // marca que ya fue atacada
        if (barco == null) { // si no hay barco
            estado = EnumEstadoCelda.AGUA; // el estado queda en agua
            return EnumResultadoDisparo.AGUA; // retorna agua como resultado
        } // cierra if
        barco.registrarImpacto(this); // notifica al barco el impacto
        if (barco.estaHundido()) { // si el barco queda hundido
            estado = EnumEstadoCelda.HUNDIDO; // marca la celda como hundida
            return EnumResultadoDisparo.HUNDIDO; // retorna hundido
        } else { // si solo fue tocado
            estado = EnumEstadoCelda.TOCADO; // actualiza estado a tocado
            return EnumResultadoDisparo.TOCADO; // retorna tocado
        } // cierra else
    } // cierra recibirDisparo

    public void marcarHundidoCompleto() { // método para marcar la celda como hundida cuando el barco completo cayó
        estado = EnumEstadoCelda.HUNDIDO; // fuerza el estado a hundido
    } // cierra marcarHundidoCompleto

    public void aplicarResultado(EnumResultadoDisparo resultado) { // método auxiliar para tableros sin barcos
        fueAtacada = true; // marca la celda como atacada
        if (resultado == EnumResultadoDisparo.AGUA) { // si fue agua
            estado = EnumEstadoCelda.AGUA; // estado agua
        } else if (resultado == EnumResultadoDisparo.TOCADO) { // si fue tocado
            estado = EnumEstadoCelda.TOCADO; // estado tocado
        } else { // en otro caso
            estado = EnumEstadoCelda.HUNDIDO; // estado hundido
        } // cierra if
    } // cierra aplicarResultado
} // cierra la clase Celda
