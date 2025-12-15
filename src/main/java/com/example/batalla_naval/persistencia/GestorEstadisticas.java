package com.example.batalla_naval.persistencia;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class GestorEstadisticas {
    private static final String ARCHIVO_STATS = "estadisticas.txt";
    private static final DateTimeFormatter FORMATO_FECHA =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // Clase interna para modelar una estadística
    public static class Estadistica {
        public String nickname;
        public int barcosHundidos;
        public LocalDateTime fecha;
        public long tiempoPartida; // en segundos

        public Estadistica(String nickname, int barcosHundidos,
                           LocalDateTime fecha, long tiempoPartida) {
            this.nickname = nickname;
            this.barcosHundidos = barcosHundidos;
            this.fecha = fecha;
            this.tiempoPartida = tiempoPartida;
        }

        @Override
        public String toString() {
            return String.format("%s | Barcos: %d | Fecha: %s | Tiempo: %dmin",
                    nickname, barcosHundidos,
                    fecha.format(FORMATO_FECHA), tiempoPartida / 60);
        }
    }

    // Guardar una nueva estadística
    public static synchronized void guardarEstadistica(String nickname,
                                                       int barcosHundidos,
                                                       long tiempoPartida) {
        try (FileWriter fw = new FileWriter(ARCHIVO_STATS, true);
             BufferedWriter bw = new BufferedWriter(fw)) {

            String linea = String.format("%s|%d|%s|%d",
                    nickname,
                    barcosHundidos,
                    LocalDateTime.now().format(FORMATO_FECHA),
                    tiempoPartida);

            bw.write(linea);
            bw.newLine();
            System.out.println("✓ Estadística guardada: " + nickname);

        } catch (IOException e) {
            System.err.println("✗ Error al guardar estadística: " + e.getMessage());
        }
    }

    // Cargar todas las estadísticas
    public static List<Estadistica> cargarEstadisticas() {
        List<Estadistica> estadisticas = new ArrayList<>();
        File archivo = new File(ARCHIVO_STATS);

        if (!archivo.exists()) {
            System.out.println("ℹ No hay archivo de estadísticas previo");
            return estadisticas;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(ARCHIVO_STATS))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                try {
                    String[] partes = linea.split("\\|");
                    if (partes.length == 4) {
                        Estadistica est = new Estadistica(
                                partes[0],
                                Integer.parseInt(partes[1]),
                                LocalDateTime.parse(partes[2], FORMATO_FECHA),
                                Long.parseLong(partes[3])
                        );
                        estadisticas.add(est);
                    }
                } catch (NumberFormatException | java.time.format.DateTimeParseException e) {
                    System.err.println("⚠ Línea mal formada: " + linea);
                }
            }
            System.out.println("✓ Se cargaron " + estadisticas.size() + " estadísticas");
        } catch (IOException e) {
            System.err.println("✗ Error al cargar estadísticas: " + e.getMessage());
        }

        return estadisticas;
    }

    // Obtener top 5 mejores jugadores (más barcos hundidos)
    public static List<Estadistica> obtenerTop5() {
        List<Estadistica> todas = cargarEstadisticas();
        return todas.stream()
                .sorted((a, b) -> Integer.compare(b.barcosHundidos, a.barcosHundidos))
                .limit(5)
                .toList();
    }

    // Obtener promedio de barcos hundidos
    public static double obtenerPromedioBarcosHundidos() {
        List<Estadistica> todas = cargarEstadisticas();
        if (todas.isEmpty()) return 0;
        return todas.stream()
                .mapToInt(e -> e.barcosHundidos)
                .average()
                .orElse(0);
    }

    // Limpiar archivo de estadísticas (para testing)
    public static void limpiarEstadisticas() {
        File archivo = new File(ARCHIVO_STATS);
        if (archivo.delete()) {
            System.out.println("✓ Estadísticas borradas");
        }
    }

    // Imprimir todas las estadísticas (para debugging)
    public static void imprimirTodasLasEstadisticas() {
        System.out.println("\n=== ESTADÍSTICAS GUARDADAS ===");
        List<Estadistica> todas = cargarEstadisticas();
        if (todas.isEmpty()) {
            System.out.println("No hay estadísticas registradas");
        } else {
            todas.forEach(e -> System.out.println("  " + e));
        }
        System.out.println("==============================\n");
    }
}