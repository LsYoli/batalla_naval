package com.example.batalla_naval.logica; // paquete de la fábrica de barcos

import java.io.Serial;
import java.io.Serializable;

public abstract class FabricaBarcos implements Serializable { // ⭐⭐ MODIFICADO: ahora es abstracta (Factory Method)
    @Serial
    private static final long serialVersionUID = 1L;

    // ⭐⭐ MÉTODO FACTORY ABSTRACTO (Factory Method)
    public abstract Barco crearBarco(); // ⭐⭐ SIN PARÁMETROS: cada fábrica concreta crea UN tipo específico

    // ⭐⭐ MÉTODO HOOK para configuración común
    public void configurarBarcoBase() {
        System.out.println("⚙️ Configurando características base del barco...");
    } // cierra configurarBarcoBase

    // ⭐⭐ MÉTODO TEMPLATE para proceso completo de creación
    public final Barco crearBarcoCompleto() {
        configurarBarcoBase(); // paso común a todos los barcos
        Barco barco = crearBarco(); // paso variable (implementado por subclases)
        System.out.println("✅ Barco creado: " + barco.getNombre());
        return barco; // retorna el barco creado
    } // cierra crearBarcoCompleto
} // cierra la clase FabricaBarcos