package com.example.batalla_naval.logica;

import java.io.Serial;
import java.io.Serializable;

/**
 * Fábrica concreta responsable de crear portaaviones.
 */
public class FabricaPortaaviones extends FabricaBarcos implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Crea un portaaviones.
     *
     * @return nueva instancia de Portaaviones.
     */
    @Override
    public Barco crearBarco() {
        return new Portaaviones();
    }

    /**
     * Configura los elementos básicos y añade ajustes propios del portaaviones.
     */
    @Override
    public void configurarBarcoBase() {
        super.configurarBarcoBase();
        configurarPortaavionesEspecial();
    }

    /**
     * Realiza ajustes adicionales exclusivos del portaaviones.
     */
    public void configurarPortaavionesEspecial() {
        System.out.println("🛩️ Configurando pista de despegue y hangares...");
    }
}
