package com.example.batalla_naval.logica;

import java.io.Serial;
import java.io.Serializable;

/**
 * Fábrica concreta encargada de construir fragatas.
 */
public class FabricaFragata extends FabricaBarcos implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Crea una fragata lista para su configuración.
     *
     * @return instancia específica de Fragata.
     */
    @Override
    public Barco crearBarco() {
        return new Fragata();
    }

    /**
     * Ejecuta la configuración base y establece los parámetros propios de la fragata.
     */
    @Override
    public void configurarBarcoBase() {
        super.configurarBarcoBase();
        configurarVelocidadMaxima();
    }

    /**
     * Ajusta la velocidad máxima que caracteriza a la fragata.
     */
    public void configurarVelocidadMaxima() {
        System.out.println("⚡ Configurando velocidad máxima de la fragata...");
    }
}
