package com.example.batalla_naval.logica; // paquete de la fábrica de submarinos

import java.io.Serial;
import java.io.Serializable;

public class FabricaSubmarino extends FabricaBarcos implements Serializable { // ⭐⭐ NUEVA: fábrica concreta específica para submarinos
    @Serial
    private static final long serialVersionUID = 1L;

    @Override // implementación del método factory abstracto
    public Barco crearBarco() { // crea EXCLUSIVAMENTE un submarino
        return new Submarino(); // retorna una instancia concreta de Submarino
    } // cierra crearBarco

    @Override // sobrescribe el método hook para añadir funcionalidad específica
    public void configurarBarcoBase() {
        super.configurarBarcoBase(); // llama a la implementación de la clase padre
        System.out.println("🌊 Instalando sistemas de inmersión...");
        System.out.println("🔍 Calibrando periscopio y sonar...");
    } // cierra configurarBarcoBase

    // ⭐⭐ MÉTODO ESPECÍFICO para esta fábrica
    public void configurarProfundidadMaxima(int metros) {
        System.out.println("📏 Configurando profundidad máxima a " + metros + " metros...");
    } // cierra configurarProfundidadMaxima
} // cierra la clase FabricaSubmarino