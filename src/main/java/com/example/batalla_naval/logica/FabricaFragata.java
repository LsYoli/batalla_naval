package com.example.batalla_naval.logica; // paquete de la fábrica de fragatas

import java.io.Serial;
import java.io.Serializable;

public class FabricaFragata extends FabricaBarcos implements Serializable { // ⭐⭐ NUEVA: fábrica concreta específica para fragatas
    @Serial
    private static final long serialVersionUID = 1L;

    @Override // implementación del método factory abstracto
    public Barco crearBarco() { // crea EXCLUSIVAMENTE una fragata
        return new Fragata(); // retorna una instancia concreta de Fragata
    } // cierra crearBarco

    @Override // sobrescribe el método hook para añadir funcionalidad específica
    public void configurarBarcoBase() {
        super.configurarBarcoBase(); // llama a la implementación de la clase padre
        System.out.println("🎣 Instalando sistemas de patrulla costera...");
        System.out.println("📡 Configurando radar de corto alcance...");
    } // cierra configurarBarcoBase

    // ⭐⭐ MÉTODO ESPECÍFICO para esta fábrica
    public void configurarVelocidadMaxima(int nudos) {
        System.out.println("💨 Configurando velocidad máxima a " + nudos + " nudos...");
    } // cierra configurarVelocidadMaxima
} // cierra la clase FabricaFragata