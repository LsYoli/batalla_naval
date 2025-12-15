package com.example.batalla_naval.persistencia;

import com.example.batalla_naval.logica.*;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Gestor especializado en guardado automático y recuperación de partidas activas.
 * Mantiene un archivo de estado actual que se sobrescribe con cada jugada.
 *
 * HU-5: Guardado automático tras cada jugada
 * HU-6: Cargar/Nuevo juego al iniciar
 */
public class GestorPartidaAutomatico {
    private static final String ARCHIVO_PARTIDA_ACTIVA = "partida_activa.dat";
    private static final String ARCHIVO_ESTADO_JUGADOR = "estado_jugador.txt";
    private static final String DIRECTORIO_BASE = "datos_juego/";
    private static final DateTimeFormatter FORMATO_FECHA =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    static {

        File directorio = new File(DIRECTORIO_BASE);
        if (!directorio.exists()) {
            directorio.mkdirs();
            System.out.println("✓ Directorio de datos creado");
        }
    }

    /**
     * Guarda automáticamente el estado actual de la partida
     * Se ejecuta después de cada jugada (del jugador o máquina)
     *
     * @param fachada El estado completo del juego
     * @param nicknamejugador Nickname del jugador actual
     * @return true si se guardó exitosamente
     */
    public static synchronized boolean guardarPartidaActiva(FachadaJuego fachada,
                                                            String nicknamejugador) {
        try {

            String rutaPartida = DIRECTORIO_BASE + ARCHIVO_PARTIDA_ACTIVA;
            try (ObjectOutputStream oos = new ObjectOutputStream(
                    new FileOutputStream(rutaPartida))) {
                oos.writeObject(fachada);
                oos.flush();
            }


            guardarEstadoJugador(nicknamejugador, fachada);

            System.out.println("✓ Partida automáticamente guardada: " +
                    LocalDateTime.now().format(FORMATO_FECHA));
            return true;

        } catch (NotSerializableException e) {
            System.err.println("✗ Error de serialización: " + e.getMessage());
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            System.err.println("✗ Error al guardar partida automáticamente: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Guarda la información del jugador en archivo plano CON ENCABEZADOS
     * Incluye: nickname, barcos hundidos del jugador, estado de la partida
     */
    private static void guardarEstadoJugador(String nickname, FachadaJuego fachada) {
        try (FileWriter fw = new FileWriter(DIRECTORIO_BASE + ARCHIVO_ESTADO_JUGADOR);
             BufferedWriter bw = new BufferedWriter(fw)) {

            bw.write("nickname|barcosJugadorHundidos|barcosMaquinaHundidos|totalDisparos|partidaIniciada|estado|fechaGuardado");
            bw.newLine();


            long barcosHundidosJugador = fachada.historialJugador().stream()
                    .filter(d -> d.getResultado() == EnumResultadoDisparo.HUNDIDO)
                    .count();


            long barcosHundidosMaquina = fachada.historialMaquina().stream()
                    .filter(d -> d.getResultado() == EnumResultadoDisparo.HUNDIDO)
                    .count();


            String estadoPartida = "EN_JUEGO";
            if (fachada.verificarFinDePartida()) {
                boolean ganoJugador = fachada.getMaquina().getTableroPosicion()
                        .estanTodosLosBarcosHundidos();
                estadoPartida = ganoJugador ? "GANADA" : "PERDIDA";
            }


            String linea = String.format("%s|%d|%d|%d|%b|%s|%s",
                    nickname,
                    barcosHundidosJugador,
                    barcosHundidosMaquina,
                    fachada.historialJugador().size(),
                    fachada.isPartidaIniciada(),
                    estadoPartida,
                    LocalDateTime.now().format(FORMATO_FECHA));

            bw.write(linea);
            System.out.println("✓ Estado del jugador guardado: " + nickname);

        } catch (IOException e) {
            System.err.println("✗ Error al guardar estado del jugador: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Verifica si existe una partida activa guardada
     * @return true si existe partida_activa.dat
     */
    public static boolean existePartidaActiva() {
        File archivo = new File(DIRECTORIO_BASE + ARCHIVO_PARTIDA_ACTIVA);
        return archivo.exists();
    }

    /**
     * Carga la partida activa si existe
     * @return La FachadaJuego cargada, o null si no existe o hay error
     */
    public static FachadaJuego cargarPartidaActiva() {
        String ruta = DIRECTORIO_BASE + ARCHIVO_PARTIDA_ACTIVA;
        File archivo = new File(ruta);

        if (!archivo.exists()) {
            System.out.println("ℹ No hay partida activa para cargar");
            return null;
        }

        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(ruta))) {
            FachadaJuego fachada = (FachadaJuego) ois.readObject();
            System.out.println("✓ Partida activa cargada exitosamente");
            return fachada;

        } catch (ClassNotFoundException e) {
            System.err.println("✗ Clase no encontrada al cargar: " + e.getMessage());
            return null;
        } catch (IOException e) {
            System.err.println("✗ Error al cargar partida activa: " + e.getMessage());
            return null;
        }
    }

    /**
     * Obtiene el estado del último jugador que estaba activo
     * @return Información del jugador: nickname|barcosHundidos|...
     *         O null si no hay datos o es el encabezado
     */
    public static String obtenerEstadoUltimoJugador() {
        try (BufferedReader br = new BufferedReader(
                new FileReader(DIRECTORIO_BASE + ARCHIVO_ESTADO_JUGADOR))) {

            String linea;
            String ultimaLineaValida = null;


            while ((linea = br.readLine()) != null) {

                if (linea.startsWith("nickname|")) {
                    continue;
                }

                if (!linea.trim().isEmpty()) {
                    ultimaLineaValida = linea;
                }
            }

            if (ultimaLineaValida != null) {
                System.out.println("✓ Estado del último jugador recuperado");
                return ultimaLineaValida;
            } else {
                System.out.println("ℹ No hay datos de jugador (solo encabezados)");
                return null;
            }

        } catch (FileNotFoundException e) {
            System.out.println("ℹ No hay archivo de estado de jugador");
            return null;
        } catch (IOException e) {
            System.out.println("ℹ Error al leer estado del jugador: " + e.getMessage());
            return null;
        }
    }

    /**
     * Analiza si la partida guardada ya terminó
     * @param fachada La partida cargada
     * @return true si la partida ya tiene un ganador
     */
    public static boolean partidaYaTermino(FachadaJuego fachada) {
        if (fachada == null) return false;
        return fachada.verificarFinDePartida();
    }

    /**
     * Extrae el nickname de la línea de estado del jugador
     * Maneja encabezados y líneas vacías
     */
    public static String extraerNickname(String lineaEstado) {
        if (lineaEstado == null || lineaEstado.trim().isEmpty()) {
            return null;
        }


        if (lineaEstado.startsWith("nickname|")) {
            return null;
        }

        String[] partes = lineaEstado.split("\\|");
        return partes.length > 0 ? partes[0] : null;
    }

    /**
     * Extrae si la partida ya estaba iniciada
     */
    public static boolean extraerPartidaIniciada(String lineaEstado) {
        if (lineaEstado == null || lineaEstado.startsWith("nickname|")) {
            return false;
        }

        String[] partes = lineaEstado.split("\\|");
        return partes.length > 4 && Boolean.parseBoolean(partes[4]);
    }

    /**
     * Extrae el estado de la partida (EN_JUEGO, GANADA, PERDIDA)
     */
    public static String extraerEstadoPartida(String lineaEstado) {
        if (lineaEstado == null || lineaEstado.startsWith("nickname|")) {
            return "DESCONOCIDO";
        }

        String[] partes = lineaEstado.split("\\|");
        return partes.length > 5 ? partes[5] : "DESCONOCIDO";
    }

    /**
     * Extrae los barcos hundidos del jugador
     */
    public static int extraerBarcosHundidosJugador(String lineaEstado) {
        if (lineaEstado == null || lineaEstado.startsWith("nickname|")) {
            return 0;
        }

        String[] partes = lineaEstado.split("\\|");
        try {
            return partes.length > 1 ? Integer.parseInt(partes[1]) : 0;
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /**
     * Extrae los barcos hundidos de la máquina
     */
    public static int extraerBarcosHundidosMaquina(String lineaEstado) {
        if (lineaEstado == null || lineaEstado.startsWith("nickname|")) {
            return 0;
        }

        String[] partes = lineaEstado.split("\\|");
        try {
            return partes.length > 2 ? Integer.parseInt(partes[2]) : 0;
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /**
     * Extrae el total de disparos del jugador
     */
    public static int extraerTotalDisparos(String lineaEstado) {
        if (lineaEstado == null || lineaEstado.startsWith("nickname|")) {
            return 0;
        }

        String[] partes = lineaEstado.split("\\|");
        try {
            return partes.length > 3 ? Integer.parseInt(partes[3]) : 0;
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /**
     * Obtiene la hora del último guardado
     */
    public static String obtenerHoraUltimoGuardado() {
        String estado = obtenerEstadoUltimoJugador();
        if (estado == null || estado.startsWith("nickname|")) {
            return "Nunca";
        }

        String[] partes = estado.split("\\|");
        return partes.length > 6 ? partes[6] : "Desconocida";
    }

    /**
     * Elimina la partida activa (al iniciar una nueva)
     */
    public static void eliminarPartidaActiva() {
        File archivo = new File(DIRECTORIO_BASE + ARCHIVO_PARTIDA_ACTIVA);
        File estado = new File(DIRECTORIO_BASE + ARCHIVO_ESTADO_JUGADOR);

        if (archivo.delete()) {
            System.out.println("✓ Partida activa eliminada");
        }

        if (estado.delete()) {
            System.out.println("✓ Estado de jugador eliminado");
        }
    }

    /**
     * Elimina todos los datos de guardado (para testing)
     */
    public static void limpiarTodosDatos() {
        try {
            File partida = new File(DIRECTORIO_BASE + ARCHIVO_PARTIDA_ACTIVA);
            File estado = new File(DIRECTORIO_BASE + ARCHIVO_ESTADO_JUGADOR);

            if (partida.delete()) System.out.println("✓ Partida eliminada");
            if (estado.delete()) System.out.println("✓ Estado eliminado");
        } catch (Exception e) {
            System.err.println("✗ Error al limpiar datos: " + e.getMessage());
        }
    }

    /**
     * Imprime información de depuración
     */
    public static void imprimirInfoDebug() {
        System.out.println("\n=== INFO DE GUARDADO AUTOMÁTICO ===");
        System.out.println("Partida activa existe: " + existePartidaActiva());

        String estado = obtenerEstadoUltimoJugador();
        if (estado != null && !estado.startsWith("nickname|")) {
            System.out.println("Estado del jugador: " + estado);
            System.out.println("  - Nickname: " + extraerNickname(estado));
            System.out.println("  - Barcos jugador hundidos: " + extraerBarcosHundidosJugador(estado));
            System.out.println("  - Barcos máquina hundidos: " + extraerBarcosHundidosMaquina(estado));
            System.out.println("  - Total disparos: " + extraerTotalDisparos(estado));
            System.out.println("  - Partida iniciada: " + extraerPartidaIniciada(estado));
            System.out.println("  - Estado partida: " + extraerEstadoPartida(estado));
            System.out.println("  - Último guardado: " + obtenerHoraUltimoGuardado());
        } else {
            System.out.println("No hay estado previo (o solo encabezados)");
        }
        System.out.println("====================================\n");
    }
}
