package com.example.batalla_naval.logica;

import java.io.Serial;
import java.io.Serializable;

/**
 * Fábrica concreta responsable de crear destructores.
 */
public class FabricaDestructor extends FabricaBarcos implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Crea un nuevo destructor.
     *
     * @return instancia específica del tipo Destructor.
     */
    @Override
    public Barco crearBarco() {
        return new Destructor();
    }

    /**
     * Configura los parámetros comunes y añade el armamento propio del destructor.
     */
    @Override
    public void configurarBarcoBase() {
        super.configurarBarcoBase();
        configurarArmamento();
    }

    /**
     * Ajusta el armamento característico del destructor.
     */
    public void configurarArmamento() {
        System.out.println("💥 Instalando armamento pesado para el destructor...");
    }
}
