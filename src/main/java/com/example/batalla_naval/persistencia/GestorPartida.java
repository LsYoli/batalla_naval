package com.example.batalla_naval.persistencia;

import com.example.batalla_naval.logica.*;
import java.io.*;

/**
 * Clase GestorPartida de la aplicación Batalla Naval.
 */
public class GestorPartida {
    private static final String DIRECTORIO_PARTIDAS = "partidas_guardadas/";

    static {

        File directorio = new File(DIRECTORIO_PARTIDAS);
        if (!directorio.exists()) {
            directorio.mkdirs();
            System.out.println("✓ Directorio de partidas creado");
        }
    }

    /**
     * Guarda el estado completo de una partida en archivo binario
     * @param nombrePartida Nombre identificador de la partida
     * @param fachada La fachada del juego a guardar
     * @return true si se guardó exitosamente
     */
    public static boolean guardarPartida(String nombrePartida, FachadaJuego fachada) {
        String ruta = DIRECTORIO_PARTIDAS + nombrePartida + ".dat";

        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(ruta))) {
            oos.writeObject(fachada);
            oos.flush();
            System.out.println("✓ Partida guardada: " + ruta);
            return true;
        } catch (NotSerializableException e) {
            System.err.println("✗ Error de serialización - Clase no implementa Serializable: "
                    + e.getMessage());
            return false;
        } catch (IOException e) {
            System.err.println("✗ Error al guardar partida: " + e.getMessage());
            return false;
        }
    }

    /**
     * Carga el estado completo de una partida guardada
     * @param nombrePartida Nombre identificador de la partida
     * @return La FachadaJuego cargada, o null si hay error
     */
    public static FachadaJuego cargarPartida(String nombrePartida) {
        String ruta = DIRECTORIO_PARTIDAS + nombrePartida + ".dat";
        File archivo = new File(ruta);

        if (!archivo.exists()) {
            System.err.println("✗ La partida no existe: " + ruta);
            return null;
        }

        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(ruta))) {
            FachadaJuego fachada = (FachadaJuego) ois.readObject();
            System.out.println("✓ Partida cargada: " + ruta);
            return fachada;
        } catch (ClassNotFoundException e) {
            System.err.println("✗ Error al cargar - Clase no encontrada: " + e.getMessage());
            return null;
        } catch (IOException e) {
            System.err.println("✗ Error al cargar partida: " + e.getMessage());
            return null;
        }
    }

    /**
     * Lista todas las partidas guardadas
     * @return Array con nombres de partidas (sin extensión .dat)
     */
    public static String[] listarPartidasGuardadas() {
        File directorio = new File(DIRECTORIO_PARTIDAS);

        if (!directorio.exists() || !directorio.isDirectory()) {
            return new String[0];
        }

        File[] archivos = directorio.listFiles((dir, name) -> name.endsWith(".dat"));

        if (archivos == null || archivos.length == 0) {
            System.out.println("ℹ No hay partidas guardadas");
            return new String[0];
        }

        String[] nombresPartidas = new String[archivos.length];
        for (int i = 0; i < archivos.length; i++) {

            nombresPartidas[i] = archivos[i].getName()
                    .replace(".dat", "");
        }

        System.out.println("✓ Se encontraron " + nombresPartidas.length + " partidas");
        return nombresPartidas;
    }

    /**
     * Elimina una partida guardada
     * @param nombrePartida Nombre de la partida a eliminar
     * @return true si se eliminó exitosamente
     */
    public static boolean eliminarPartida(String nombrePartida) {
        String ruta = DIRECTORIO_PARTIDAS + nombrePartida + ".dat";
        File archivo = new File(ruta);

        if (archivo.exists()) {
            if (archivo.delete()) {
                System.out.println("✓ Partida eliminada: " + nombrePartida);
                return true;
            }
        }
        System.err.println("✗ No se pudo eliminar la partida: " + nombrePartida);
        return false;
    }

    /**
     * Verifica si una partida ya existe
     * @param nombrePartida Nombre de la partida
     * @return true si existe
     */
    public static boolean existePartida(String nombrePartida) {
        String ruta = DIRECTORIO_PARTIDAS + nombrePartida + ".dat";
        return new File(ruta).exists();
    }
}
