package com.example.batalla_naval.logica;

import java.io.Serial;
import java.io.Serializable;

/**
 * Clase Context para el patrón Strategy
 * Encapsula el comportamiento de disparo y permite cambiar estrategias en tiempo de ejecución
 */
public class ContextoDisparo implements Serializable {
    private EstrategiaDisparo estrategia;
    @Serial
    private static final long serialVersionUID = 1L;

    public ContextoDisparo() {
        // Estrategia por defecto
        this.estrategia = new EstrategiaDisparoAleatoria();
    }

    public void setEstrategia(EstrategiaDisparo estrategia) {
        this.estrategia = estrategia;
    }

    public EstrategiaDisparo getEstrategia() {
        return estrategia;
    }

    public Coordenada ejecutarEstrategia(Maquina maquina) {
        return estrategia.obtenerSiguienteDisparo(maquina);
    }
}