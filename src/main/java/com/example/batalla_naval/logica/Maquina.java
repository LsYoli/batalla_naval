package com.example.batalla_naval.logica; // paquete de la clase Maquina

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayDeque; // importa ArrayDeque para la deque
import java.util.ArrayList; // importa ArrayList para la flota
import java.util.Deque; // interfaz de deque
import java.util.HashSet; // conjunto para coordenadas ya disparadas
import java.util.List; // interfaz de lista
import java.util.Queue; // interfaz de cola
import java.util.LinkedList; // implementación de cola
import java.util.Set; // interfaz de conjunto
import java.util.Stack; // pila para historial
import java.util.Random; // generador aleatorio

public class Maquina implements Serializable { // clase que representa a la máquina
    private final Tablero tableroPosicion; // tablero donde se ubican los barcos de la máquina
    private final Tablero tableroRegistroDisparos; // tablero donde se registran disparos contra el jugador
    private final List<Barco> flota; // flota de barcos de la máquina
    private final Set<Coordenada> celdasYaDisparadas; // conjunto de celdas usadas para no repetir
    private final Queue<Coordenada> colaDisparos; // cola que alimenta la estrategia simple
    private final Deque<Coordenada> celdasVecinas; // deque para almacenar vecinos tras un impacto
    private final ContextoDisparo contextoDisparo; // ⭐⭐ CAMBIO: Contexto para el patrón Strategy
    private final Stack<Disparo> historial; // historial de disparos en pila
    private int contadorDisparos; // contador para numerar disparos
    @Serial
    private static final long serialVersionUID = 1L;

    public Maquina() { // constructor sin parámetros
        tableroPosicion = new Tablero(); // crea el tablero de posición
        tableroRegistroDisparos = new Tablero(); // crea el tablero donde se registran disparos contra el jugador
        flota = new ArrayList<>(); // inicializa la flota con ArrayList
        celdasYaDisparadas = new HashSet<>(); // conjunto para recordar disparos
        colaDisparos = new LinkedList<>(); // cola simple para gestionar coordenadas
        celdasVecinas = new ArrayDeque<>(); // deque para guardar vecinos de impactos
        contextoDisparo = new ContextoDisparo(); // ⭐⭐ CAMBIO: crea el contexto en lugar de la estrategia directa
        historial = new Stack<>(); // crea el historial de disparos
        contadorDisparos = 0; // inicia el contador de disparos
    } // cierra el constructor

    public Tablero getTableroPosicion() { // getter del tablero de posición
        return tableroPosicion; // devuelve la referencia
    } // cierra getTableroPosicion

    public Tablero getTableroRegistroDisparos() { // getter del tablero de registro de disparos
        return tableroRegistroDisparos; // devuelve la referencia
    } // cierra getTableroRegistroDisparos

    public List<Barco> getFlota() { // getter de flota
        return flota; // devuelve la flota
    } // cierra getFlota

    public Set<Coordenada> getCeldasYaDisparadas() { // getter de celdas ya atacadas
        return celdasYaDisparadas; // retorna el conjunto
    } // cierra getCeldasYaDisparadas

    public Stack<Disparo> getHistorial() { // getter del historial
        return historial; // retorna la pila
    } // cierra getHistorial

    public ContextoDisparo getContextoDisparo() { // ⭐⭐ NUEVO: getter del contexto
        return contextoDisparo;
    } // cierra getContextoDisparo

    public void prepararFlotaAleatoria() { // coloca la flota de forma aleatoria usando Factory Method
        agregarBarcos(TipoBarco.PORTAAVIONES, 1); // agrega un portaaviones usando Factory Method
        agregarBarcos(TipoBarco.SUBMARINO, 2); // agrega dos submarinos usando Factory Method
        agregarBarcos(TipoBarco.DESTRUCTOR, 3); // agrega tres destructores usando Factory Method
        agregarBarcos(TipoBarco.FRAGATA, 4); // agrega cuatro fragatas usando Factory Method
    } // cierra prepararFlotaAleatoria

    private void agregarBarcos(TipoBarco tipo, int cantidad) { // método auxiliar para agregar barcos usando Factory Method
        Random random = new Random(); // generador aleatorio simple
        int colocados = 0; // contador de barcos colocados
        while (colocados < cantidad) { // repite hasta colocar la cantidad requerida
            // ⭐⭐ USO DEL FACTORY METHOD: obtener fábrica concreta y crear barco
            FabricaBarcos fabrica = obtenerFabricaPorTipo(tipo); // obtiene la fábrica concreta para el tipo
            Barco barco = fabrica.crearBarcoCompleto(); // usa el método template del Factory Method
            EnumOrientacion orientacion = random.nextBoolean() ? EnumOrientacion.HORIZONTAL : EnumOrientacion.VERTICAL; // elige orientación aleatoria
            int fila = random.nextInt(10); // elige fila aleatoria
            int col = random.nextInt(10); // elige columna aleatoria
            try { // bloque de intento
                tableroPosicion.colocarBarco(barco, new Coordenada(fila, col), orientacion); // intenta colocar el barco
                flota.add(barco); // agrega el barco a la flota si se pudo colocar
                colocados++; // incrementa contador de colocados
            } catch (PosicionInvalidaException e) { // si la posición no sirve
                // no incrementa colocados y vuelve a intentar // comentario aclaratorio
            } // cierra catch
        } // cierra while
    } // cierra agregarBarcos

    // ⭐⭐ NUEVO MÉTODO: Obtener la fábrica concreta según el tipo de barco (Factory Method)
    private FabricaBarcos obtenerFabricaPorTipo(TipoBarco tipo) { // selecciona la implementación concreta del Factory Method
        switch (tipo) { // evalúa el tipo de barco
            case PORTAAVIONES: // si es portaaviones
                return new FabricaPortaaviones(); // retorna fábrica concreta para portaaviones
            case SUBMARINO: // si es submarino
                return new FabricaSubmarino(); // retorna fábrica concreta para submarinos
            case DESTRUCTOR: // si es destructor
                return new FabricaDestructor(); // retorna fábrica concreta para destructores
            case FRAGATA: // si es fragata
                return new FabricaFragata(); // retorna fábrica concreta para fragatas
            default: // por defecto
                throw new IllegalArgumentException("Tipo de barco no soportado: " + tipo); // lanza excepción
        } // cierra switch
    } // cierra obtenerFabricaPorTipo

    public EnumResultadoDisparo disparar(JugadorHumano jugador) { // realiza un disparo contra el jugador humano
        contadorDisparos++; // incrementa contador
        Coordenada objetivo = contextoDisparo.ejecutarEstrategia(this); // ⭐⭐ CAMBIO: usa el contexto para ejecutar la estrategia
        if (objetivo == null) { // si no hay coordenada
            return EnumResultadoDisparo.AGUA; // retorna agua por defecto
        } // cierra if
        EnumResultadoDisparo resultado = jugador.getTableroPosicion().registrarDisparo(objetivo); // dispara al tablero del jugador
        tableroRegistroDisparos.obtenerCelda(objetivo).aplicarResultado(resultado); // marca el disparo en el tablero de registro con el resultado real
        historial.push(new Disparo(contadorDisparos, "Maquina", objetivo, resultado)); // agrega el disparo al historial
        if (resultado == EnumResultadoDisparo.TOCADO) { // si toca un barco
            cargarVecinos(objetivo); // carga vecinos en la deque para futura estrategia
        } // cierra if
        return resultado; // devuelve el resultado
    } // cierra disparar

    private void cargarVecinos(Coordenada base) { // carga celdas vecinas alrededor de un impacto
        int fila = base.getFila(); // obtiene la fila base
        int col = base.getColumna(); // obtiene la columna base
        agregarVecino(fila + 1, col); // agrega vecino inferior
        agregarVecino(fila - 1, col); // agrega vecino superior
        agregarVecino(fila, col + 1); // agrega vecino derecha
        agregarVecino(fila, col - 1); // agrega vecino izquierda
    } // cierra cargarVecinos

    private void agregarVecino(int fila, int col) { // intenta agregar un vecino válido a la deque
        if (fila >= 0 && fila < 10 && col >= 0 && col < 10) { // verifica límites
            Coordenada posible = new Coordenada(fila, col); // crea la coordenada
            if (!celdasYaDisparadas.contains(posible)) { // evita repetidos
                celdasVecinas.offer(posible); // agrega al final de la deque
            } // cierra if
        } // cierra if de límites
    } // cierra agregarVecino

    public Deque<Coordenada> getCeldasVecinas() { // getter de deque de vecinos
        return celdasVecinas; // retorna la deque
    } // cierra getCeldasVecinas

    public Queue<Coordenada> getColaDisparos() { // getter de la cola base
        return colaDisparos; // retorna la cola
    } // cierra getColaDisparos

    public void setEstrategia(EstrategiaDisparo estrategia) { // permite cambiar la estrategia en tiempo de ejecución
        contextoDisparo.setEstrategia(estrategia); // ⭐⭐ CAMBIO: delega al contexto
    } // cierra setEstrategia
} // cierra la clase Maquina