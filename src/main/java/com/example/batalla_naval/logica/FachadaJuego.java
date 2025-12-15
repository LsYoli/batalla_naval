package com.example.batalla_naval.logica;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Fachada del juego que coordina las acciones entre el jugador humano, la
 * máquina y los historiales de disparos.
 */
public class FachadaJuego implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /** Jugador controlado por la persona. */
    private final JugadorHumano jugador;
    /** Oponente controlado por la aplicación. */
    private final Maquina maquina;
    /** Indicador de que la partida ya comenzó. */
    private boolean partidaIniciada;
    /** Indica si actualmente juega el usuario. */
    private boolean turnoJugador;
    /** Historial secuencial de todos los disparos realizados. */
    private final List<Disparo> historialGlobal;

    /**
     * Crea la fachada inicializando el jugador humano, la máquina y los
     * historiales necesarios.
     */
    public FachadaJuego() {
        jugador = new JugadorHumano();
        maquina = new Maquina();
        partidaIniciada = false;
        turnoJugador = true;
        historialGlobal = new ArrayList<>();
    }

    /**
     * Prepara una partida nueva colocando la flota de la máquina y limpiando
     * los historiales.
     */
    public void iniciarNuevaPartida() {
        maquina.prepararFlotaAleatoria();
        partidaIniciada = false;
        turnoJugador = true;
        historialGlobal.clear();
    }

    /**
     * Indica si la partida ya se encuentra activa.
     *
     * @return {@code true} si la partida está en curso
     */
    public boolean isPartidaIniciada() {
        return partidaIniciada;
    }

    /**
     * Indica si el siguiente turno corresponde al jugador humano.
     *
     * @return {@code true} cuando debe disparar el usuario
     */
    public boolean isTurnoJugador() {
        return turnoJugador;
    }

    /**
     * Obtiene la instancia del jugador humano.
     *
     * @return el jugador humano
     */
    public JugadorHumano getJugador() {
        return jugador;
    }

    /**
     * Obtiene la instancia de la máquina rival.
     *
     * @return la máquina que actúa como oponente
     */
    public Maquina getMaquina() {
        return maquina;
    }

    /**
     * Coloca un barco del jugador humano en la posición indicada.
     *
     * @param tipo    tipo de barco solicitado
     * @param inicio  coordenada inicial donde debe ubicarse
     * @throws PosicionInvalidaException si la partida ya comenzó o se supera el
     *                                   límite de barcos por tipo
     */
    public void colocarBarcoJugador(TipoBarco tipo, Coordenada inicio) throws PosicionInvalidaException {
        if (partidaIniciada) {
            throw new PosicionInvalidaException("No se pueden mover barcos después de iniciar");
        }
        if (contarBarcosJugador(tipo) >= limitePorTipo(tipo)) {
            throw new PosicionInvalidaException("Ya se colocaron todos los barcos de este tipo");
        }
        FabricaBarcos fabrica = obtenerFabricaPorTipo(tipo);
        Barco barco = fabrica.crearBarcoCompleto();
        jugador.colocarBarco(barco, inicio);
    }

    /**
     * Activa la fase de disparos indicando que la partida se encuentra en
     * progreso.
     */
    public void iniciarPartida() {
        partidaIniciada = true;
    }

    /**
     * Procesa el disparo del jugador humano contra la flota de la máquina.
     *
     * @param coordenada ubicación objetivo del disparo
     * @return resultado obtenido tras el disparo
     */
    public EnumResultadoDisparo dispararJugador(Coordenada coordenada) {
        EnumResultadoDisparo resultado = jugador.disparar(maquina.getTableroPosicion(), coordenada);
        historialGlobal.add(jugador.getHistorialDisparos().peek());
        if (resultado == EnumResultadoDisparo.AGUA) {
            turnoJugador = false;
        }
        return resultado;
    }

    /**
     * Ejecuta el turno completo de la máquina disparando hasta fallar o hasta
     * que termine la partida.
     *
     * @return lista de resultados generados por cada disparo de la máquina
     */
    public List<EnumResultadoDisparo> dispararMaquinaTurnoCompleto() {
        turnoJugador = false;
        List<EnumResultadoDisparo> resultados = new ArrayList<>();
        EnumResultadoDisparo resultado;
        do {
            resultado = maquina.disparar(jugador);
            historialGlobal.add(maquina.getHistorial().peek());
            resultados.add(resultado);
        } while (resultado != EnumResultadoDisparo.AGUA && !verificarFinDePartida());
        turnoJugador = true;
        return resultados;
    }

    /**
     * Verifica si alguna de las flotas ya fue destruida por completo.
     *
     * @return {@code true} cuando el juego termina
     */
    public boolean verificarFinDePartida() {
        return jugador.getTableroPosicion().estanTodosLosBarcosHundidos()
                || maquina.getTableroPosicion().estanTodosLosBarcosHundidos();
    }

    /**
     * Obtiene el historial combinado de disparos realizados por ambos
     * participantes.
     *
     * @return copia del historial global en orden secuencial
     */
    public List<Disparo> obtenerHistorial() {
        return new ArrayList<>(historialGlobal);
    }

    /**
     * Devuelve el historial individual del jugador humano.
     *
     * @return pila de disparos del jugador
     */
    public Stack<Disparo> historialJugador() {
        return jugador.getHistorialDisparos();
    }

    /**
     * Devuelve el historial individual de la máquina.
     *
     * @return pila de disparos de la máquina
     */
    public Stack<Disparo> historialMaquina() {
        return maquina.getHistorial();
    }

/**
 * Descripción para contarBarcosJugador.
 * @param tipo parámetro de entrada.
 * @return valor resultante.
 */
    private int contarBarcosJugador(TipoBarco tipo) {
        int contador = 0;
        for (Barco barco : jugador.getFlota()) {
            if (tipo == TipoBarco.PORTAAVIONES && barco instanceof Portaaviones) {
                contador++;
            } else if (tipo == TipoBarco.SUBMARINO && barco instanceof Submarino) {
                contador++;
            } else if (tipo == TipoBarco.DESTRUCTOR && barco instanceof Destructor) {
                contador++;
            } else if (tipo == TipoBarco.FRAGATA && barco instanceof Fragata) {
                contador++;
            }
        }
        return contador;
    }

/**
 * Descripción para limitePorTipo.
 * @param tipo parámetro de entrada.
 * @return valor resultante.
 */
    private int limitePorTipo(TipoBarco tipo) {
        switch (tipo) {
            case PORTAAVIONES:
                return 1;
            case SUBMARINO:
                return 2;
            case DESTRUCTOR:
                return 3;
            case FRAGATA:
                return 4;
            default:
                return 0;
        }
    }

/**
 * Descripción para obtenerFabricaPorTipo.
 * @param tipo parámetro de entrada.
 * @return valor resultante.
 */
    private FabricaBarcos obtenerFabricaPorTipo(TipoBarco tipo) {
        switch (tipo) {
            case PORTAAVIONES:
                return new FabricaPortaaviones();
            case SUBMARINO:
                return new FabricaSubmarino();
            case DESTRUCTOR:
                return new FabricaDestructor();
            case FRAGATA:
                return new FabricaFragata();
            default:
                throw new IllegalArgumentException("Tipo de barco no soportado: " + tipo);
        }
    }
}
