package com.example.batalla_naval.logica; // paquete de la clase Barco

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList; // importa ArrayList para las celdas ocupadas
import java.util.List; // importa List para la interfaz de la colección

public abstract class Barco  implements Serializable { // clase abstracta base para cualquier barco
    protected int tamano; // tamaño del barco en casillas
    protected EnumOrientacion orientacion; // orientación del barco
    protected List<Celda> celdasOcupadas; // lista de celdas que ocupa
    protected int impactos; // contador de impactos recibidos
    protected String nombre; // nombre simple del barco
    @Serial
    private static final long serialVersionUID = 1L;

    public Barco(int tamano, String nombre) { // constructor que recibe tamaño y nombre
        this.tamano = tamano; // asigna el tamaño
        this.nombre = nombre; // asigna el nombre
        this.celdasOcupadas = new ArrayList<>(); // inicializa la lista con ArrayList
        this.impactos = 0; // inicia el contador de impactos
        this.orientacion = EnumOrientacion.HORIZONTAL; // orientación por defecto
    } // cierra el constructor

    public void asignarOrientacion(EnumOrientacion orientacion) { // método para setear la orientación
        this.orientacion = orientacion; // guarda la orientación elegida
    } // cierra asignarOrientacion

    public void agregarCelda(Celda celda) { // agrega una celda a la lista ocupada
        celdasOcupadas.add(celda); // añade la celda a la colección
        celda.setBarco(this); // asocia la celda al barco
    } // cierra agregarCelda

    public int getTamano() { // getter de tamaño
        return tamano; // devuelve el tamaño
    } // cierra getTamano

    public String getNombre() { // getter de nombre
        return nombre; // devuelve el nombre del barco
    } // cierra getNombre

    public List<Celda> getCeldasOcupadas() { // devuelve las celdas ocupadas
        return celdasOcupadas; // retorna la lista
    } // cierra getCeldasOcupadas

    public void registrarImpacto(Celda celda) { // registra que el barco fue golpeado
        impactos++; // incrementa el contador de impactos
    } // cierra registrarImpacto

    public boolean estaHundido() { // verifica si el barco está hundido
        return impactos >= tamano; // retorna true si los impactos cubren el tamaño
    } // cierra estaHundido
} // cierra la clase Barco
