package com.example.batalla_naval.logica;

import java.io.Serial;
import java.io.Serializable;

/**
 * Fábrica concreta responsable de crear submarinos.
 */
public class FabricaSubmarino extends FabricaBarcos implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Crea un nuevo submarino.
     *
     * @return instancia lista para configuración adicional.
     */
    @Override
    public Barco crearBarco() {
        return new Submarino();
    }

    /**
     * Aplica la configuración base del barco y añade pasos específicos del submarino.
     */
    @Override
    public void configurarBarcoBase() {
        super.configurarBarcoBase();
        System.out.println("🌊 Instalando sistemas de inmersión...");
        System.out.println("🔍 Calibrando periscopio y sonar...");
    }

    /**
     * Ajusta la profundidad máxima operativa del submarino.
     *
     * @param metros profundidad máxima en metros.
     */
    public void configurarProfundidadMaxima(int metros) {
        System.out.println("📏 Configurando profundidad máxima a " + metros + " metros...");
    }
}
