package com.example.batalla_naval.logica; // paquete para la enumeración de estados

public enum EnumEstadoCelda { // enum que describe el estado de una celda
    SIN_DISPARO, // celda sin atacar aún
    AGUA, // se disparó y no había barco
    TOCADO, // se impactó una parte de un barco
    HUNDIDO // todas las partes del barco fueron impactadas
} // cierra el enum
