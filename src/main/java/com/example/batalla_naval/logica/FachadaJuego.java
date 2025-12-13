package com.example.batalla_naval.logica; // paquete de la fachada

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList; // importa ArrayList para listas rápidas
import java.util.List; // interfaz de lista
import java.util.Stack; // pila para unir historiales

public class FachadaJuego implements Serializable { // clase que implementa el patrón Facade
    private final JugadorHumano jugador; // referencia al jugador humano
    private final Maquina maquina; // referencia a la máquina
    private boolean partidaIniciada; // bandera para indicar si la partida empezó
    private boolean turnoJugador; // bandera del turno actual
    private final List<Disparo> historialGlobal; // lista secuencial con todos los disparos
    @Serial
    private static final long serialVersionUID = 1L;

    public FachadaJuego() { // constructor sin parámetros
        jugador = new JugadorHumano(); // crea al jugador humano
        maquina = new Maquina(); // crea a la máquina
        partidaIniciada = false; // marca que aún no inicia la partida
        turnoJugador = true; // por defecto el primer turno es del jugador
        historialGlobal = new ArrayList<>(); // inicializa el historial global en orden secuencial
    } // cierra el constructor

    public void iniciarNuevaPartida() { // método de alto nivel para arrancar la partida
        maquina.prepararFlotaAleatoria(); // coloca la flota de la máquina de forma aleatoria usando Factory Method
        partidaIniciada = false; // asegura que la partida comience en modo de colocación
        turnoJugador = true; // define que el turno inicial será del jugador
        historialGlobal.clear(); // limpia el historial global para la nueva partida
        // TODO HU-6: aquí se podría cargar desde archivo una partida previa antes de crear la nueva
    } // cierra iniciarNuevaPartida

    public boolean isPartidaIniciada() { // indica si la partida ya inició
        return partidaIniciada; // retorna la bandera
    } // cierra isPartidaIniciada

    public boolean isTurnoJugador() { // indica si es turno del jugador
        return turnoJugador; // retorna la bandera
    } // cierra isTurnoJugador

    public JugadorHumano getJugador() { // getter del jugador
        return jugador; // retorna la referencia
    } // cierra getJugador

    public Maquina getMaquina() { // getter de la máquina
        return maquina; // retorna la referencia
    } // cierra getMaquina

    public void colocarBarcoJugador(TipoBarco tipo, Coordenada inicio) throws PosicionInvalidaException { // coloca un barco del jugador
        if (partidaIniciada) { // si la partida ya inició
            throw new PosicionInvalidaException("No se pueden mover barcos después de iniciar"); // lanza excepción
        } // cierra if
        if (contarBarcosJugador(tipo) >= limitePorTipo(tipo)) { // verifica si ya se alcanzó el máximo del tipo
            throw new PosicionInvalidaException("Ya se colocaron todos los barcos de este tipo"); // avisa que no se pueden agregar más
        } // cierra if
        // ⭐⭐ USO DEL FACTORY METHOD: obtener fábrica concreta y crear barco
        FabricaBarcos fabrica = obtenerFabricaPorTipo(tipo); // obtiene la fábrica concreta para el tipo
        Barco barco = fabrica.crearBarcoCompleto(); // usa el método template del Factory Method para crear el barco
        jugador.colocarBarco(barco, inicio); // delega la colocación al jugador
    } // cierra colocarBarcoJugador

    public void iniciarPartida() { // cambia el estado a partida activa
        partidaIniciada = true; // habilita la fase de disparos
    } // cierra iniciarPartida

    public EnumResultadoDisparo dispararJugador(Coordenada coordenada) { // procesa un disparo del jugador contra la máquina
        EnumResultadoDisparo resultado = jugador.disparar(maquina.getTableroPosicion(), coordenada); // realiza el disparo
        historialGlobal.add(jugador.getHistorialDisparos().peek()); // agrega el disparo al historial secuencial
        if (resultado == EnumResultadoDisparo.AGUA) { // si fue agua
            turnoJugador = false; // cede el turno a la máquina
        } // cierra if
        return resultado; // devuelve el resultado al controlador
    } // cierra dispararJugador

    public List<EnumResultadoDisparo> dispararMaquinaTurnoCompleto() { // dispara hasta que la máquina falle
        turnoJugador = false; // marca que la máquina está ejecutando su turno completo
        List<EnumResultadoDisparo> resultados = new ArrayList<>(); // lista para acumular resultados en orden
        EnumResultadoDisparo resultado; // variable para guardar cada resultado individual
        do { // ciclo que repite mientras la máquina siga acertando
            resultado = maquina.disparar(jugador); // dispara contra el jugador
            historialGlobal.add(maquina.getHistorial().peek()); // agrega el disparo de la máquina al historial secuencial
            resultados.add(resultado); // guarda el resultado del disparo actual
        } while (resultado != EnumResultadoDisparo.AGUA && !verificarFinDePartida()); // si acierta y no termina la partida continúa
        turnoJugador = true; // devuelve el turno al jugador después del ciclo
        return resultados; // retorna la lista de resultados para ser procesados en la vista
    } // cierra dispararMaquinaTurnoCompleto

    public boolean verificarFinDePartida() { // verifica si alguien ganó
        return jugador.getTableroPosicion().estanTodosLosBarcosHundidos() || maquina.getTableroPosicion().estanTodosLosBarcosHundidos(); // retorna true si algún tablero quedó sin barcos
    } // cierra verificarFinDePartida

    public List<Disparo> obtenerHistorial() { // arma una lista con todos los disparos
        return new ArrayList<>(historialGlobal); // retorna una copia del historial en orden secuencial
    } // cierra obtenerHistorial

    public Stack<Disparo> historialJugador() { // retorna la pila del jugador
        return jugador.getHistorialDisparos(); // devuelve la referencia
    } // cierra historialJugador

    public Stack<Disparo> historialMaquina() { // retorna la pila de la máquina
        return maquina.getHistorial(); // devuelve la referencia
    } // cierra historialMaquina

    private int contarBarcosJugador(TipoBarco tipo) { // cuenta cuántos barcos de un tipo tiene el jugador
        int contador = 0; // inicia contador en cero
        for (Barco barco : jugador.getFlota()) { // recorre la flota actual
            if (tipo == TipoBarco.PORTAAVIONES && barco instanceof Portaaviones) { // verifica si es portaaviones
                contador++; // suma al contador
            } else if (tipo == TipoBarco.SUBMARINO && barco instanceof Submarino) { // verifica submarino
                contador++; // suma al contador
            } else if (tipo == TipoBarco.DESTRUCTOR && barco instanceof Destructor) { // verifica destructor
                contador++; // suma al contador
            } else if (tipo == TipoBarco.FRAGATA && barco instanceof Fragata) { // verifica fragata
                contador++; // suma al contador
            } // cierra if encadenado
        } // cierra for
        return contador; // retorna el total encontrado
    } // cierra contarBarcosJugador

    private int limitePorTipo(TipoBarco tipo) { // devuelve el máximo permitido por tipo
        switch (tipo) { // revisa el enum recibido
            case PORTAAVIONES: // para portaaviones
                return 1; // solo uno permitido
            case SUBMARINO: // para submarinos
                return 2; // dos permitidos
            case DESTRUCTOR: // para destructores
                return 3; // tres permitidos
            case FRAGATA: // para fragatas
                return 4; // cuatro permitidas
            default: // caso por defecto que no debería ocurrir
                return 0; // retorna cero
        } // cierra switch
    } // cierra limitePorTipo

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
} // cierra la clase FachadaJuego