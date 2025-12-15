package com.example.batalla_naval.logica;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.Set;
import java.util.Stack;

/**
 * Representa al oponente controlado por la aplicación y gestiona su estrategia de disparos.
 */
public class Maquina implements Serializable {
    private final Tablero tableroPosicion;
    private final Tablero tableroRegistroDisparos;
    private final List<Barco> flota;
    private final Set<Coordenada> celdasYaDisparadas;
    private final Queue<Coordenada> colaDisparos;
    private final Deque<Coordenada> celdasVecinas;
    private final ContextoDisparo contextoDisparo;
    private final Stack<Disparo> historial;
    private int contadorDisparos;
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Inicializa la máquina con tableros vacíos y un contexto de disparo configurable.
     */
    public Maquina() {
        tableroPosicion = new Tablero();
        tableroRegistroDisparos = new Tablero();
        flota = new ArrayList<>();
        celdasYaDisparadas = new HashSet<>();
        colaDisparos = new LinkedList<>();
        celdasVecinas = new ArrayDeque<>();
        contextoDisparo = new ContextoDisparo();
        historial = new Stack<>();
        contadorDisparos = 0;
    }

    /**
     * Obtiene el tablero donde se posicionan los barcos de la máquina.
     *
     * @return tablero de posición de la máquina.
     */
    public Tablero getTableroPosicion() {
        return tableroPosicion;
    }

    /**
     * Obtiene el tablero donde se registran los disparos realizados contra el jugador.
     *
     * @return tablero de registro de disparos.
     */
    public Tablero getTableroRegistroDisparos() {
        return tableroRegistroDisparos;
    }

    /**
     * Devuelve la flota actual de la máquina.
     *
     * @return lista de barcos posicionados.
     */
    public List<Barco> getFlota() {
        return flota;
    }

    /**
     * Celdas en las que la máquina ya ha disparado.
     *
     * @return conjunto de coordenadas utilizadas.
     */
    public Set<Coordenada> getCeldasYaDisparadas() {
        return celdasYaDisparadas;
    }

    /**
     * Historial de disparos realizados por la máquina.
     *
     * @return pila de disparos.
     */
    public Stack<Disparo> getHistorial() {
        return historial;
    }

    /**
     * Devuelve el contexto que administra la estrategia de disparo actual.
     *
     * @return contexto de estrategia.
     */
    public ContextoDisparo getContextoDisparo() {
        return contextoDisparo;
    }

    /**
     * Coloca la flota de manera aleatoria utilizando las fábricas disponibles.
     */
    public void prepararFlotaAleatoria() {
        agregarBarcos(TipoBarco.PORTAAVIONES, 1);
        agregarBarcos(TipoBarco.SUBMARINO, 2);
        agregarBarcos(TipoBarco.DESTRUCTOR, 3);
        agregarBarcos(TipoBarco.FRAGATA, 4);
    }

    /**
     * Agrega barcos del tipo indicado intentando ubicarlos aleatoriamente.
     *
     * @param tipo tipo de barco a crear.
     * @param cantidad cantidad de barcos a colocar.
     */
    private void agregarBarcos(TipoBarco tipo, int cantidad) {
        Random random = new Random();
        int colocados = 0;
        while (colocados < cantidad) {
            FabricaBarcos fabrica = obtenerFabricaPorTipo(tipo);
            Barco barco = fabrica.crearBarcoCompleto();
            EnumOrientacion orientacion = random.nextBoolean() ? EnumOrientacion.HORIZONTAL : EnumOrientacion.VERTICAL;
            int fila = random.nextInt(10);
            int col = random.nextInt(10);
            try {
                tableroPosicion.colocarBarco(barco, new Coordenada(fila, col), orientacion);
                flota.add(barco);
                colocados++;
            } catch (PosicionInvalidaException e) {
            }
        }
    }

    /**
     * Obtiene la fábrica concreta correspondiente al tipo solicitado.
     *
     * @param tipo tipo de barco requerido.
     * @return implementación de fábrica adecuada.
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

    /**
     * Ejecuta un disparo contra el jugador humano siguiendo la estrategia configurada.
     *
     * @param jugador jugador objetivo.
     * @return resultado del disparo realizado.
     */
    public EnumResultadoDisparo disparar(JugadorHumano jugador) {
        contadorDisparos++;
        Coordenada objetivo = contextoDisparo.ejecutarEstrategia(this);
        if (objetivo == null) {
            return EnumResultadoDisparo.AGUA;
        }
        EnumResultadoDisparo resultado = jugador.getTableroPosicion().registrarDisparo(objetivo);
        tableroRegistroDisparos.obtenerCelda(objetivo).aplicarResultado(resultado);
        historial.push(new Disparo(contadorDisparos, "Maquina", objetivo, resultado));
        if (resultado == EnumResultadoDisparo.TOCADO) {
            cargarVecinos(objetivo);
        }
        return resultado;
    }

    /**
     * Carga las celdas adyacentes al último impacto para priorizar los siguientes disparos.
     *
     * @param base coordenada que resultó en impacto.
     */
    private void cargarVecinos(Coordenada base) {
        int fila = base.getFila();
        int col = base.getColumna();
        agregarVecino(fila + 1, col);
        agregarVecino(fila - 1, col);
        agregarVecino(fila, col + 1);
        agregarVecino(fila, col - 1);
    }

    /**
     * Intenta encolar una coordenada vecina si está dentro del tablero y no se ha usado.
     *
     * @param fila fila candidata.
     * @param col columna candidata.
     */
    private void agregarVecino(int fila, int col) {
        if (fila >= 0 && fila < 10 && col >= 0 && col < 10) {
            Coordenada posible = new Coordenada(fila, col);
            if (!celdasYaDisparadas.contains(posible)) {
                celdasVecinas.offer(posible);
            }
        }
    }

    /**
     * Devuelve las coordenadas vecinas pendientes de procesar.
     *
     * @return deque con celdas candidatas.
     */
    public Deque<Coordenada> getCeldasVecinas() {
        return celdasVecinas;
    }

    /**
     * Devuelve la cola utilizada por estrategias simples de disparo.
     *
     * @return cola de disparos pendientes.
     */
    public Queue<Coordenada> getColaDisparos() {
        return colaDisparos;
    }

    /**
     * Permite reemplazar la estrategia de disparo utilizada por la máquina.
     *
     * @param estrategia implementación concreta de estrategia.
     */
    public void setEstrategia(EstrategiaDisparo estrategia) {
        contextoDisparo.setEstrategia(estrategia);
    }
}
