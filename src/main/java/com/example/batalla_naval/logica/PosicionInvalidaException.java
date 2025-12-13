package com.example.batalla_naval.logica; // paquete de la excepción personalizada

public class PosicionInvalidaException extends Exception { // excepción para posiciones inválidas
    public PosicionInvalidaException(String mensaje) { // constructor que recibe un mensaje
        super(mensaje); // pasa el mensaje a la clase base Exception
    } // cierra el constructor
} // cierra la clase PosicionInvalidaException
