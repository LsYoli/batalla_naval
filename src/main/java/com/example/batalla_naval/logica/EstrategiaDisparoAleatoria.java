package com.example.batalla_naval.logica;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Clase EstrategiaDisparoAleatoria de la aplicación Batalla Naval.
 */
public class EstrategiaDisparoAleatoria implements EstrategiaDisparo, Serializable {
    private final Queue<Coordenada> colaDisparos;
    @Serial
    private static final long serialVersionUID = 1L;

/**
 * Descripción para EstrategiaDisparoAleatoria.
 */
    public EstrategiaDisparoAleatoria() {
        colaDisparos = new LinkedList<>();
        List<Coordenada> temporales = new LinkedList<>();
        for (int fila = 0; fila < 10; fila++) {
            for (int col = 0; col < 10; col++) {
                temporales.add(new Coordenada(fila, col));
            }
        }
        Collections.shuffle(temporales);
        colaDisparos.addAll(temporales);
    }

    @Override
/**
 * Descripción para obtenerSiguienteDisparo.
 * @param maquina parámetro de entrada.
 * @return valor resultante.
 */
    public Coordenada obtenerSiguienteDisparo(Maquina maquina) {
        while (!colaDisparos.isEmpty()) {
            Coordenada siguiente = colaDisparos.poll();
            if (!maquina.getCeldasYaDisparadas().contains(siguiente)) {
                maquina.getCeldasYaDisparadas().add(siguiente);
                return siguiente;
            }
        }
        return null;
    }
}
