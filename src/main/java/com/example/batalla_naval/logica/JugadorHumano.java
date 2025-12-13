package com.example.batalla_naval.logica; // paquete de la clase JugadorHumano

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList; // importa ArrayList para la flota
import java.util.List; // importa List como interfaz
import java.util.Stack; // importa Stack para historial de disparos

public class JugadorHumano implements Serializable { // clase que modela al jugador humano
    private final Tablero tableroPosicion; // tablero donde coloca sus barcos
    private final Tablero tableroRegistroDisparos; // tablero donde se registran impactos sobre la máquina
    private final List<Barco> flota; // lista de barcos del jugador
    private final Stack<Disparo> historialDisparos; // pila con los disparos realizados
    private EnumOrientacion orientacionActual; // orientación elegida para colocar
    private int contadorDisparos; // contador para numerar disparos
    @Serial
    private static final long serialVersionUID = 1L;

    public JugadorHumano() { // constructor sin parámetros
        tableroPosicion = new Tablero(); // crea el tablero de posición usando ArrayList
        tableroRegistroDisparos = new Tablero(); // crea tablero vacío para registrar disparos
        flota = new ArrayList<>(); // inicializa la flota
        historialDisparos = new Stack<>(); // crea la pila de disparos
        orientacionActual = EnumOrientacion.HORIZONTAL; // orientación por defecto
        contadorDisparos = 0; // inicia contador de disparos
    } // cierra el constructor

    public Tablero getTableroPosicion() { // getter del tablero de posición
        return tableroPosicion; // retorna el tablero
    } // cierra getTableroPosicion

    public Tablero getTableroRegistroDisparos() { // getter del tablero de disparos
        return tableroRegistroDisparos; // retorna el tablero de registro
    } // cierra getTableroRegistroDisparos

    public List<Barco> getFlota() { // getter de la flota
        return flota; // devuelve la lista de barcos
    } // cierra getFlota

    public Stack<Disparo> getHistorialDisparos() { // getter del historial
        return historialDisparos; // retorna la pila de disparos
    } // cierra getHistorialDisparos

    public EnumOrientacion getOrientacionActual() { // getter de la orientación actual
        return orientacionActual; // retorna la orientación guardada
    } // cierra getOrientacionActual

    public void alternarOrientacion() { // cambia la orientación para colocar barcos
        orientacionActual = orientacionActual == EnumOrientacion.HORIZONTAL ? EnumOrientacion.VERTICAL : EnumOrientacion.HORIZONTAL; // alterna entre horizontal y vertical
    } // cierra alternarOrientacion

    public void colocarBarco(Barco barco, Coordenada inicio) throws PosicionInvalidaException { // coloca un barco en el tablero
        tableroPosicion.colocarBarco(barco, inicio, orientacionActual); // delega en el tablero la colocación
        flota.add(barco); // agrega el barco a la flota del jugador
    } // cierra colocarBarco

    public EnumResultadoDisparo disparar(Tablero tableroMaquina, Coordenada objetivo) { // dispara al tablero de la máquina
        contadorDisparos++; // incrementa el contador de disparos
        EnumResultadoDisparo resultado = tableroMaquina.registrarDisparo(objetivo); // registra el disparo en el tablero enemigo
        tableroRegistroDisparos.obtenerCelda(objetivo).aplicarResultado(resultado); // marca la celda en el tablero de registro con el resultado real
        historialDisparos.push(new Disparo(contadorDisparos, "Humano", objetivo, resultado)); // guarda el disparo en la pila
        return resultado; // retorna el resultado obtenido
    } // cierra disparar
} // cierra la clase JugadorHumano
