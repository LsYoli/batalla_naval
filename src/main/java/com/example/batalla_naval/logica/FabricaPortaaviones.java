package com.example.batalla_naval.logica; // paquete de la fábrica de portaaviones

import java.io.Serial;
import java.io.Serializable;

public class FabricaPortaaviones extends FabricaBarcos implements Serializable { // ⭐⭐ NUEVA: fábrica concreta específica para portaaviones
    @Serial
    private static final long serialVersionUID = 1L;

    @Override // implementación del método factory abstracto
    public Barco crearBarco() { // crea EXCLUSIVAMENTE un portaaviones
        return new Portaaviones(); // retorna una instancia concreta de Portaaviones
    } // cierra crearBarco

    @Override // sobrescribe el método hook para añadir funcionalidad específica
    public void configurarBarcoBase() {
        super.configurarBarcoBase(); // llama a la implementación de la clase padre
        System.out.println("🛫 Añadiendo pista de aterrizaje para aviones...");
        System.out.println("🚁 Instalando hangares y sistemas de catapulta...");
    } // cierra configurarBarcoBase

    // ⭐⭐ MÉTODO ESPECÍFICO para esta fábrica
    public void configurarPortaavionesEspecial() {
        System.out.println("🎯 Configurando sistemas de defensa antiaérea...");
    } // cierra configurarPortaavionesEspecial
} // cierra la clase FabricaPortaaviones