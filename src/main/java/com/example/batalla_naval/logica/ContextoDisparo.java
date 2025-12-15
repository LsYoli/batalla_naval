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

/**
 * Descripción para ContextoDisparo.
 */
    public ContextoDisparo() {

        this.estrategia = new EstrategiaDisparoAleatoria();
    }

/**
 * Descripción para setEstrategia.
 * @param estrategia parámetro de entrada.
 */
    public void setEstrategia(EstrategiaDisparo estrategia) {
        this.estrategia = estrategia;
    }

/**
 * Descripción para getEstrategia.
 * @return valor resultante.
 */
    public EstrategiaDisparo getEstrategia() {
        return estrategia;
    }

/**
 * Descripción para ejecutarEstrategia.
 * @param maquina parámetro de entrada.
 * @return valor resultante.
 */
    public Coordenada ejecutarEstrategia(Maquina maquina) {
        return estrategia.obtenerSiguienteDisparo(maquina);
    }
}
