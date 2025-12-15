package com.example.batalla_naval.logica;

import java.io.Serializable;

/**
 * Clase EstrategiaDisparo de la aplicación Batalla Naval.
 */
public interface EstrategiaDisparo extends Serializable {
    Coordenada obtenerSiguienteDisparo(Maquina maquina);
}
