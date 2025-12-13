package com.example.batalla_naval.logica; // paquete de la estrategia

import java.io.Serializable;

public interface EstrategiaDisparo extends Serializable { // interfaz del patrón Strategy
    Coordenada obtenerSiguienteDisparo(Maquina maquina); // método que entrega la siguiente coordenada
} // cierra la interfaz
