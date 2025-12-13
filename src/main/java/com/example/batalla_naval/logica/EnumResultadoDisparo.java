package com.example.batalla_naval.logica; // paquete de la enumeración de resultados

public enum EnumResultadoDisparo { // enum para describir el resultado de un disparo
    AGUA, // cuando no se golpea un barco
    TOCADO, // cuando se impacta un barco pero no se hunde
    HUNDIDO // cuando se destruye completamente un barco
} // cierra el enum
