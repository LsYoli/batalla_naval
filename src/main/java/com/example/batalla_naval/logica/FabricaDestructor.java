package com.example.batalla_naval.logica; // paquete de la fábrica de destructores

import java.io.Serial;
import java.io.Serializable;

public class FabricaDestructor extends FabricaBarcos implements Serializable { // ⭐⭐ NUEVA: fábrica concreta específica para destructores
    @Serial
    private static final long serialVersionUID = 1L;

    @Override // implementación del método factory abstracto
    public Barco crearBarco() { // crea EXCLUSIVAMENTE un destructor
        return new Destructor(); // retorna una instancia concreta de Destructor
    } // cierra crearBarco

    @Override // sobrescribe el método hook para añadir funcionalidad específica
    public void configurarBarcoBase() {
        super.configurarBarcoBase(); // llama a la implementación de la clase padre
        System.out.println("💥 Instalando sistemas de artillería naval...");
        System.out.println("🚀 Calibrando lanzatorpedos...");
    } // cierra configurarBarcoBase

    // ⭐⭐ MÉTODO ESPECÍFICO para esta fábrica
    public void configurarArmamento(int cantidadTorpedos) {
        System.out.println("⚔️ Configurando " + cantidadTorpedos + " torpedos disponibles...");
    } // cierra configurarArmamento
} // cierra la clase FabricaDestructor