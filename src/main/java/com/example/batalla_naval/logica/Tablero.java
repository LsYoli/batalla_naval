package com.example.batalla_naval.logica; // paquete de la clase Tablero

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList; // importa ArrayList para la grilla
import java.util.List; // importa List para interfaz de colecciones

public class Tablero implements Serializable { // clase que modela un tablero 10x10 usando listas
    private final ArrayList<ArrayList<Celda>> grilla; // grilla representada como lista de listas
    private final ArrayList<Barco> barcos; // lista de barcos colocados en el tablero
    @Serial
    private static final long serialVersionUID = 1L;

    public Tablero() { // constructor sin parámetros
        grilla = new ArrayList<>(); // inicializa la grilla principal
        barcos = new ArrayList<>(); // inicializa la lista de barcos
        for (int fila = 0; fila < 10; fila++) { // recorre las 10 filas
            ArrayList<Celda> filaActual = new ArrayList<>(); // crea la lista de celdas de la fila
            for (int col = 0; col < 10; col++) { // recorre las 10 columnas
                filaActual.add(new Celda(new Coordenada(fila, col))); // agrega una celda nueva con coordenada
            } // cierra for columnas
            grilla.add(filaActual); // agrega la fila completa a la grilla
        } // cierra for filas
    } // cierra el constructor

    public ArrayList<ArrayList<Celda>> getGrilla() { // getter de la grilla completa
        return grilla; // devuelve la referencia de la grilla
    } // cierra getGrilla

    public List<Barco> getBarcos() { // devuelve la lista de barcos
        return barcos; // retorna la lista existente
    } // cierra getBarcos

    public Celda obtenerCelda(Coordenada coord) { // devuelve la celda para una coordenada
        return grilla.get(coord.getFila()).get(coord.getColumna()); // busca en la lista de listas por fila y columna
    } // cierra obtenerCelda

    public boolean esPosicionValida(Barco barco, Coordenada inicio, EnumOrientacion orientacion) { // verifica si se puede colocar un barco
        int fila = inicio.getFila(); // toma la fila inicial
        int col = inicio.getColumna(); // toma la columna inicial
        if (orientacion == EnumOrientacion.HORIZONTAL && col + barco.getTamano() > 10) { // valida que no se pase en horizontal
            return false; // retorna falso si se sale
        } // cierra if horizontal
        if (orientacion == EnumOrientacion.VERTICAL && fila + barco.getTamano() > 10) { // valida que no se pase en vertical
            return false; // retorna falso si se sale
        } // cierra if vertical
        for (int i = 0; i < barco.getTamano(); i++) { // recorre las casillas que ocuparía
            int filaObjetivo = orientacion == EnumOrientacion.HORIZONTAL ? fila : fila + i; // calcula fila según orientación
            int colObjetivo = orientacion == EnumOrientacion.HORIZONTAL ? col + i : col; // calcula columna según orientación
            Celda celda = grilla.get(filaObjetivo).get(colObjetivo); // obtiene la celda objetivo
            if (celda.getBarco() != null) { // si ya hay barco
                return false; // no es válido
            } // cierra if
        } // cierra for
        return true; // si pasó todas las validaciones retorna true
    } // cierra esPosicionValida

    public void colocarBarco(Barco barco, Coordenada inicio, EnumOrientacion orientacion) throws PosicionInvalidaException { // coloca un barco en el tablero
        if (!esPosicionValida(barco, inicio, orientacion)) { // verifica la validez de la posición
            throw new PosicionInvalidaException("No se puede colocar el barco en esa posición"); // lanza excepción si no es válida
        } // cierra if
        barco.asignarOrientacion(orientacion); // guarda la orientación en el barco
        int fila = inicio.getFila(); // guarda fila inicial
        int col = inicio.getColumna(); // guarda columna inicial
        for (int i = 0; i < barco.getTamano(); i++) { // recorre las celdas necesarias
            int filaObjetivo = orientacion == EnumOrientacion.HORIZONTAL ? fila : fila + i; // calcula la fila de la celda
            int colObjetivo = orientacion == EnumOrientacion.HORIZONTAL ? col + i : col; // calcula la columna de la celda
            Celda celda = grilla.get(filaObjetivo).get(colObjetivo); // obtiene la celda elegida
            barco.agregarCelda(celda); // agrega la celda al barco
        } // cierra for
        barcos.add(barco); // añade el barco a la colección del tablero
    } // cierra colocarBarco

    public EnumResultadoDisparo registrarDisparo(Coordenada coordenada) { // registra un disparo en el tablero
        Celda celda = obtenerCelda(coordenada); // obtiene la celda atacada
        EnumResultadoDisparo resultado = celda.recibirDisparo(); // dispara y captura el resultado
        if (resultado == EnumResultadoDisparo.HUNDIDO && celda.getBarco() != null) { // si el barco quedó hundido
            for (Celda c : celda.getBarco().getCeldasOcupadas()) { // recorre todas las celdas del barco
                c.marcarHundidoCompleto(); // marca cada celda como hundida
            } // cierra for
        } // cierra if
        return resultado; // retorna el resultado final
    } // cierra registrarDisparo

    public boolean estanTodosLosBarcosHundidos() { // verifica si todos los barcos están hundidos
        for (Barco b : barcos) { // recorre cada barco
            if (!b.estaHundido()) { // si alguno no está hundido
                return false; // retorna falso
            } // cierra if
        } // cierra for
        return true; // si todos están hundidos retorna true
    } // cierra estanTodosLosBarcosHundidos
} // cierra la clase Tablero
