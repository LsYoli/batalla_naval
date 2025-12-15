package com.example.batalla_naval.logica;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Clase JugadorHumano de la aplicación Batalla Naval.
 */
public class JugadorHumano implements Serializable {
    private final Tablero tableroPosicion;
    private final Tablero tableroRegistroDisparos;
    private final List<Barco> flota;
    private final Stack<Disparo> historialDisparos;
    private EnumOrientacion orientacionActual;
    private int contadorDisparos;
    @Serial
    private static final long serialVersionUID = 1L;

/**
 * Descripción para JugadorHumano.
 */
    public JugadorHumano() {
        tableroPosicion = new Tablero();
        tableroRegistroDisparos = new Tablero();
        flota = new ArrayList<>();
        historialDisparos = new Stack<>();
        orientacionActual = EnumOrientacion.HORIZONTAL;
        contadorDisparos = 0;
    }

/**
 * Descripción para getTableroPosicion.
 * @return valor resultante.
 */
    public Tablero getTableroPosicion() {
        return tableroPosicion;
    }

/**
 * Descripción para getTableroRegistroDisparos.
 * @return valor resultante.
 */
    public Tablero getTableroRegistroDisparos() {
        return tableroRegistroDisparos;
    }

/**
 * Descripción para getFlota.
 * @return valor resultante.
 */
    public List<Barco> getFlota() {
        return flota;
    }

/**
 * Descripción para getHistorialDisparos.
 * @return valor resultante.
 */
    public Stack<Disparo> getHistorialDisparos() {
        return historialDisparos;
    }

/**
 * Descripción para getOrientacionActual.
 * @return valor resultante.
 */
    public EnumOrientacion getOrientacionActual() {
        return orientacionActual;
    }

/**
 * Descripción para alternarOrientacion.
 */
    public void alternarOrientacion() {
        orientacionActual = orientacionActual == EnumOrientacion.HORIZONTAL ? EnumOrientacion.VERTICAL : EnumOrientacion.HORIZONTAL;
    }

/**
 * Descripción para colocarBarco.
 * @param barco parámetro de entrada.
 * @param inicio parámetro de entrada.
 */
    public void colocarBarco(Barco barco, Coordenada inicio) throws PosicionInvalidaException {
        tableroPosicion.colocarBarco(barco, inicio, orientacionActual);
        flota.add(barco);
    }

/**
 * Descripción para disparar.
 * @param tableroMaquina parámetro de entrada.
 * @param objetivo parámetro de entrada.
 * @return valor resultante.
 */
    public EnumResultadoDisparo disparar(Tablero tableroMaquina, Coordenada objetivo) {
        contadorDisparos++;
        EnumResultadoDisparo resultado = tableroMaquina.registrarDisparo(objetivo);
        tableroRegistroDisparos.obtenerCelda(objetivo).aplicarResultado(resultado);
        historialDisparos.push(new Disparo(contadorDisparos, "Humano", objetivo, resultado));
        return resultado;
    }
}
