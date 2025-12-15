package com.example.batalla_naval.logica;

import java.io.Serial;
import java.io.Serializable;

/**
 * Define la interfaz común para las fábricas de barcos utilizando el patrón Factory Method.
 */
public abstract class FabricaBarcos implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Crea la instancia concreta del barco definido por cada subclase.
     *
     * @return barco específico producido por la fábrica.
     */
    public abstract Barco crearBarco();

    /**
     * Configura aspectos básicos compartidos por todas las embarcaciones.
     */
    public void configurarBarcoBase() {
        System.out.println("⚙️ Configurando características base del barco...");
    }

    /**
     * Ejecuta el proceso completo de creación del barco aplicando la configuración común.
     *
     * @return barco inicializado y listo para usarse.
     */
    public final Barco crearBarcoCompleto() {
        configurarBarcoBase();
        Barco barco = crearBarco();
        System.out.println("✅ Barco creado: " + barco.getNombre());
        return barco;
    }
}
