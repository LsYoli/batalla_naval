package com.example.funcion;

import com.example.batalla_naval.logica.*;
import com.example.batalla_naval.persistencia.*;
import com.example.batalla_naval.visualizacion.BarcoFX;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.text.Text;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.*;

/**
 * Clase ControladorJuego de la aplicación Batalla Naval.
 */
public class ControladorJuego {

    @FXML private GridPane tableroJugador;
    @FXML private GridPane tableroMaquina;
    @FXML private Button btnFragata;
    @FXML private Button btnDestructor;
    @FXML private Button btnSubmarino;
    @FXML private Button btnPortaaviones;
    @FXML private Button btnOrientacion;
    @FXML private Button btnIniciar;
    @FXML private Button btnGuardar;
    @FXML private Button btnCargar;
    @FXML private Button btnReiniciar;
    @FXML private Button btnVerTableroOculto;
    @FXML private Label lblEstadoFlota;
    @FXML private Label lblEstadoPartida;

    @FXML private TableView<Disparo> tablaDisparos;
    @FXML private TableColumn<Disparo, Integer> colNumero;
    @FXML private TableColumn<Disparo, String> colJugador;
    @FXML private TableColumn<Disparo, String> colCoordenada;
    @FXML private TableColumn<Disparo, String> colResultado;


    private FachadaJuego fachada = new FachadaJuego();
    private TipoBarco tipoSeleccionado = TipoBarco.FRAGATA;
    private final ArrayList<ArrayList<StackPane>> celdasJugadorUI = new ArrayList<>();
    private final ArrayList<ArrayList<StackPane>> celdasMaquinaUI = new ArrayList<>();
    private final ObservableList<Disparo> datosTabla = FXCollections.observableArrayList();
    private long tiempoInicio;
    private String nicknamejugador = "Jugador";
    private final double TAMANO_CELDA = 30.0;

    @FXML
/**
 * Descripción para initialize.
 */
    public void initialize() {
        solicitarNickname();
        fachada.iniciarNuevaPartida();
        construirTablero(tableroJugador, false);
        construirTablero(tableroMaquina, true);
        prepararTabla();
        configurarBotones();
        configurarBotonVerificacionProfesor();
        actualizarEstadoFlota();
        actualizarEstadoPartida();
    }

/**
 * Descripción para solicitarNickname.
 */
    private void solicitarNickname() {
        TextInputDialog dialog = new TextInputDialog("Jugador");
        dialog.setTitle("¡Bienvenido!");
        dialog.setHeaderText("¿Cuál es tu nickname?");
        dialog.setContentText("Nickname:");

        Optional<String> resultado = dialog.showAndWait();
        if (resultado.isPresent() && !resultado.get().trim().isEmpty()) {
            nicknamejugador = resultado.get().trim();
        }
        System.out.println("Jugador: " + nicknamejugador);
    }

/**
 * Descripción para configurarBotonVerificacionProfesor.
 */
    private void configurarBotonVerificacionProfesor() {
        if (btnVerTableroOculto != null) {
            btnVerTableroOculto.setOnAction(e -> mostrarTableroOcultoParaProfesor());
        }
    }

/**
 * Descripción para mostrarTableroOcultoParaProfesor.
 */
    private void mostrarTableroOcultoParaProfesor() {
        try {

            VBoxExtra vistaVerificacion = new VBoxExtra(fachada);

            Stage stageVerificacion = new Stage();
            stageVerificacion.setTitle("🔍 VISTA DEL PROFESOR - Tablero de la Máquina");
            Scene escena = new Scene(vistaVerificacion, 600, 700);
            stageVerificacion.setScene(escena);
            stageVerificacion.show();
        } catch (Exception e) {
            mostrarMensaje("Error", "No se pudo abrir la vista de verificación: " + e.getMessage());
        }
    }

/**
 * Descripción para habilitarModoProfesor.
 * @param activar parámetro de entrada.
 */
    public void habilitarModoProfesor(boolean activar) {
        if (btnVerTableroOculto != null) {
            btnVerTableroOculto.setVisible(activar);
            btnVerTableroOculto.setManaged(activar);
        }
    }

/**
 * Descripción para cargarPartidaContinuacion.
 * @param fachadaCargada parámetro de entrada.
 */
    public void cargarPartidaContinuacion(FachadaJuego fachadaCargada) {
        this.fachada = fachadaCargada;

        String estado = GestorPartidaAutomatico.obtenerEstadoUltimoJugador();
        nicknamejugador = GestorPartidaAutomatico.extraerNickname(estado);

        actualizarVista();

        if (fachada.isPartidaIniciada()) {
            btnIniciar.setDisable(true);
            btnFragata.setDisable(true);
            btnDestructor.setDisable(true);
            btnSubmarino.setDisable(true);
            btnPortaaviones.setDisable(true);
            btnOrientacion.setDisable(true);
        }

        System.out.println("✓ Partida cargada para " + nicknamejugador);
    }

/**
 * Descripción para construirTablero.
 * @param grid parámetro de entrada.
 * @param esMaquina parámetro de entrada.
 */
    private void construirTablero(GridPane grid, boolean esMaquina) {
        ArrayList<ArrayList<StackPane>> referencia = esMaquina ? celdasMaquinaUI : celdasJugadorUI;
        referencia.clear();
        for (int fila = 0; fila < 10; fila++) {
            ArrayList<StackPane> filaLista = new ArrayList<>();
            for (int col = 0; col < 10; col++) {
                StackPane celda = new StackPane();

                celda.setPrefSize(TAMANO_CELDA, TAMANO_CELDA);
                celda.setStyle("-fx-border-color: grey; -fx-background-color: aliceblue;");
                final int f = fila;
                final int c = col;
                if (esMaquina) {
                    celda.setOnMouseClicked(e -> manejarDisparoJugador(f, c));
                } else {
                    celda.setOnMouseClicked(e -> manejarColocacionJugador(f, c));
                }
                grid.add(celda, col, fila);
                filaLista.add(celda);
            }
            referencia.add(filaLista);
        }
    }

/**
 * Descripción para prepararTabla.
 */
    private void prepararTabla() {
        colNumero.setCellValueFactory(new PropertyValueFactory<>("numero"));
        colJugador.setCellValueFactory(new PropertyValueFactory<>("jugador"));
        colCoordenada.setCellValueFactory(new PropertyValueFactory<>("coordenada"));
        colResultado.setCellValueFactory(new PropertyValueFactory<>("resultado"));
        tablaDisparos.setItems(datosTabla);
    }

/**
 * Descripción para configurarBotones.
 */
    private void configurarBotones() {
        btnFragata.setOnAction(e -> tipoSeleccionado = TipoBarco.FRAGATA);
        btnDestructor.setOnAction(e -> tipoSeleccionado = TipoBarco.DESTRUCTOR);
        btnSubmarino.setOnAction(e -> tipoSeleccionado = TipoBarco.SUBMARINO);
        btnPortaaviones.setOnAction(e -> tipoSeleccionado = TipoBarco.PORTAAVIONES);
        btnOrientacion.setOnAction(e -> alternarOrientacion());
        btnIniciar.setOnAction(e -> iniciarPartida());
        btnGuardar.setOnAction(e -> guardarPartida());
        btnCargar.setOnAction(e -> cargarPartida());
        btnReiniciar.setOnAction(e -> reiniciarPartida());
    }

/**
 * Descripción para alternarOrientacion.
 */
    private void alternarOrientacion() {
        fachada.getJugador().alternarOrientacion();
        String nuevo = fachada.getJugador().getOrientacionActual() == EnumOrientacion.HORIZONTAL ? "Horizontal" : "Vertical";
        btnOrientacion.setText(nuevo);
    }

/**
 * Descripción para estaFlotaCompleta.
 * @return valor resultante.
 */
    private boolean estaFlotaCompleta() {
        return fachada.getJugador().getFlota().size() == 10;
    }

/**
 * Descripción para actualizarEstadoFlota.
 */
    private void actualizarEstadoFlota() {
        int barcosColocados = fachada.getJugador().getFlota().size();
        lblEstadoFlota.setText(barcosColocados + "/10 barcos");

        if (barcosColocados == 10) {
            lblEstadoFlota.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
        } else {
            lblEstadoFlota.setStyle("-fx-text-fill: orange; -fx-font-weight: bold;");
        }
    }

/**
 * Descripción para actualizarEstadoPartida.
 */
    private void actualizarEstadoPartida() {
        if (!fachada.isPartidaIniciada()) {
            lblEstadoPartida.setText("📍 Colocando barcos...");
            lblEstadoPartida.setStyle("-fx-text-fill: blue;");
        } else if (fachada.isTurnoJugador()) {
            lblEstadoPartida.setText("🎯 Tu turno - ¡Dispara!");
            lblEstadoPartida.setStyle("-fx-text-fill: green;");
        } else {
            lblEstadoPartida.setText("⏳ Turno de la máquina...");
            lblEstadoPartida.setStyle("-fx-text-fill: red;");
        }
    }

/**
 * Descripción para manejarColocacionJugador.
 * @param fila parámetro de entrada.
 * @param col parámetro de entrada.
 */
    private void manejarColocacionJugador(int fila, int col) {
        if (fachada.isPartidaIniciada()) {
            mostrarMensaje("Partida iniciada", "Ya no puedes mover los barcos.");
            return;
        }


        int barcosAntes = fachada.getJugador().getFlota().size();

        try {


            fachada.colocarBarcoJugador(tipoSeleccionado, new Coordenada(fila, col));


            List<Barco> flota = fachada.getJugador().getFlota();
            if (flota.size() > barcosAntes) {

                dibujarBarcosJugador();
            }

            actualizarEstadoFlota();
        } catch (PosicionInvalidaException e) {
            mostrarMensaje("Posición inválida", e.getMessage());
        }
    }

    /**
     * MÉTODO CLAVE: Limpia el tablero y redibuja TODA la flota usando BarcoFX.
     * Usa GridPane.add() y Column/Row Span para evitar el apilamiento.
     */
    private void dibujarBarcosJugador() {

        tableroJugador.getChildren().removeIf(node -> node instanceof BarcoFX);


        for (Barco barco : fachada.getJugador().getFlota()) {
            if (barco.getFilaInicial() != -1) {

                BarcoFX barcoFX = new BarcoFX(barco);


                tableroJugador.add(barcoFX, barco.getColumnaInicial(), barco.getFilaInicial());


                int tamano = barco.getTamano();
                if (barco.getOrientacion() == EnumOrientacion.HORIZONTAL) {
                    GridPane.setColumnSpan(barcoFX, tamano);
                    GridPane.setRowSpan(barcoFX, 1);
                } else {
                    GridPane.setColumnSpan(barcoFX, 1);
                    GridPane.setRowSpan(barcoFX, tamano);
                }


                barcoFX.setRotate(0);
                barcoFX.setTranslateX(0);
                barcoFX.setTranslateY(0);

                if (barco.getOrientacion() == EnumOrientacion.VERTICAL) {
                    barcoFX.setRotate(90);


                    double tamanoTotal = barco.getTamano() * TAMANO_CELDA;
                    double ajuste = (tamanoTotal - TAMANO_CELDA) / 2.0;


                    barcoFX.setTranslateX(-ajuste);


                    barcoFX.setTranslateY(-ajuste);
                }
            }
        }
    }

    /**
     * Dibuja la flota de la máquina en el tableroMaquina (Vista del Profesor).
     * Utiliza la misma lógica de BarcoFX y traslación que el jugador.
     */
    private void dibujarBarcosProfesor() {

        tableroMaquina.getChildren().removeIf(node -> node instanceof BarcoFX);


        for (Barco barco : fachada.getMaquina().getFlota()) {
            if (barco.getFilaInicial() != -1) {

                BarcoFX barcoFX = new BarcoFX(barco);


                tableroMaquina.add(barcoFX, barco.getColumnaInicial(), barco.getFilaInicial());


                int tamano = barco.getTamano();
                if (barco.getOrientacion() == EnumOrientacion.HORIZONTAL) {
                    GridPane.setColumnSpan(barcoFX, tamano);
                    GridPane.setRowSpan(barcoFX, 1);
                } else {
                    GridPane.setColumnSpan(barcoFX, 1);
                    GridPane.setRowSpan(barcoFX, tamano);
                }


                barcoFX.setRotate(0);
                barcoFX.setTranslateX(0);
                barcoFX.setTranslateY(0);

                if (barco.getOrientacion() == EnumOrientacion.VERTICAL) {
                    barcoFX.setRotate(90);


                    double tamanoTotal = barco.getTamano() * TAMANO_CELDA;
                    double ajuste = (tamanoTotal - TAMANO_CELDA) / 2.0;


                    barcoFX.setTranslateX(-ajuste);


                    barcoFX.setTranslateY(-ajuste);
                }
            }
        }
    }

/**
 * Descripción para iniciarPartida.
 */
    private void iniciarPartida() {
        if (!estaFlotaCompleta()) {
            mostrarMensaje("Flota incompleta",
                    "Debes colocar todos los 10 barcos antes de iniciar.\n" +
                            "Barcos colocados: " + fachada.getJugador().getFlota().size() + "/10");
            return;
        }

        tiempoInicio = System.currentTimeMillis();
        fachada.iniciarPartida();
        btnIniciar.setDisable(true);
        btnFragata.setDisable(true);
        btnDestructor.setDisable(true);
        btnSubmarino.setDisable(true);
        btnPortaaviones.setDisable(true);
        btnOrientacion.setDisable(true);
        actualizarEstadoPartida();
        guardarAutomaticamente();
    }

/**
 * Descripción para guardarAutomaticamente.
 */
    private void guardarAutomaticamente() {
        boolean guardado = GestorPartidaAutomatico.guardarPartidaActiva(fachada, nicknamejugador);
        if (guardado) {
            System.out.println("💾 Partida guardada automáticamente");
        }
    }

/**
 * Descripción para manejarDisparoJugador.
 * @param fila parámetro de entrada.
 * @param col parámetro de entrada.
 */
    private void manejarDisparoJugador(int fila, int col) {
        if (!fachada.isPartidaIniciada()) {
            mostrarMensaje("Ubica tus barcos", "Debes presionar Jugar para iniciar la partida.");
            return;
        }
        if (!fachada.isTurnoJugador()) {
            mostrarMensaje("Espera", "Ahora está disparando la máquina.");
            return;
        }
        Coordenada objetivo = new Coordenada(fila, col);
        Celda celdaObjetivo = fachada.getMaquina().getTableroPosicion().obtenerCelda(objetivo);
        if (celdaObjetivo.isFueAtacada()) {
            mostrarMensaje("Repite", "Ya disparaste en esa celda.");
            return;
        }
        EnumResultadoDisparo resultado = fachada.dispararJugador(objetivo);
        marcarResultadoEnTablero(celdasMaquinaUI, objetivo, resultado, false);
        actualizarTabla();

        if (resultado == EnumResultadoDisparo.AGUA) {
            int disparosPrevios = fachada.historialMaquina().size();
            List<EnumResultadoDisparo> respuestasMaquina = fachada.dispararMaquinaTurnoCompleto();
            procesarDisparosMaquinaEnOrden(disparosPrevios, respuestasMaquina.size());
            guardarAutomaticamente();
        } else {
            guardarAutomaticamente();
        }

        actualizarEstadoPartida();

        if (verificarFin()) {
            guardarAutomaticamente();
        }
    }

/**
 * Descripción para procesarDisparosMaquinaEnOrden.
 * @param indiceInicio parámetro de entrada.
 * @param cantidadNuevos parámetro de entrada.
 */
    private void procesarDisparosMaquinaEnOrden(int indiceInicio, int cantidadNuevos) {
        Stack<Disparo> pila = fachada.historialMaquina();
        int limite = indiceInicio + cantidadNuevos;
        for (int i = indiceInicio; i < limite && i < pila.size(); i++) {
            Disparo disparo = pila.get(i);
            marcarResultadoEnTablero(celdasJugadorUI, disparo.getCoordenada(), disparo.getResultado(), true);
        }
        actualizarTabla();
    }

/**
 * Descripción para marcarResultadoEnTablero.
 * @param referencia parámetro de entrada.
 * @param coord parámetro de entrada.
 * @param resultado parámetro de entrada.
 * @param esTableroJugador parámetro de entrada.
 */
    private void marcarResultadoEnTablero(ArrayList<ArrayList<StackPane>> referencia, Coordenada coord, EnumResultadoDisparo resultado, boolean esTableroJugador) {
        StackPane celdaUI = referencia.get(coord.getFila()).get(coord.getColumna());


        celdaUI.getChildren().removeIf(node -> node instanceof Ellipse || node instanceof Text);

        if (resultado == EnumResultadoDisparo.AGUA) {
            Ellipse marca = new Ellipse(8, 5);
            marca.setFill(Color.web("#4da6ff"));
            celdaUI.getChildren().add(marca);
        } else if (resultado == EnumResultadoDisparo.TOCADO || resultado == EnumResultadoDisparo.HUNDIDO) {
            Text texto = new Text("X");
            texto.setFill(resultado == EnumResultadoDisparo.HUNDIDO ? Color.DARKRED : Color.RED);
            texto.setStyle("-fx-font-size: 20; -fx-font-weight: bold;");
            celdaUI.getChildren().add(texto);
            texto.toFront();

            if (esTableroJugador) {
                celdaUI.toFront();
            }
        }
    }

/**
 * Descripción para actualizarTabla.
 */
    private void actualizarTabla() {
        datosTabla.clear();
        List<Disparo> todos = fachada.obtenerHistorial();
        datosTabla.addAll(todos);
        tablaDisparos.refresh();
    }

/**
 * Descripción para verificarFin.
 * @return valor resultante.
 */
    private boolean verificarFin() {
        if (fachada.verificarFinDePartida()) {
            boolean gano = fachada.getMaquina().getTableroPosicion()
                    .estanTodosLosBarcosHundidos();

            long tiempoPartida = (System.currentTimeMillis() - tiempoInicio) / 1000;
            int barcosHundidos = (int) fachada.historialJugador().stream()
                    .filter(d -> d.getResultado() == EnumResultadoDisparo.HUNDIDO)
                    .count();

            if (gano) {
                mostrarMensaje("¡GANASTE!",
                        "¡Felicidades! Hundiste toda la flota enemiga.\n" +
                                "Tiempo: " + (tiempoPartida / 60) + " minutos");

                GestorEstadisticas.guardarEstadistica(nicknamejugador,
                        barcosHundidos, tiempoPartida);
                mostrarMensaje("Estadística guardada",
                        "Victoria registrada para: " + nicknamejugador);

                GestorPartidaAutomatico.eliminarPartidaActiva();
            } else {
                mostrarMensaje("Fin de partida",
                        "La máquina hundió todos tus barcos.\n" +
                                "Tiempo jugado: " + (tiempoPartida / 60) + " minutos");

                GestorPartidaAutomatico.eliminarPartidaActiva();
            }

            return true;
        }
        return false;
    }

/**
 * Descripción para guardarPartida.
 */
    private void guardarPartida() {
        if (!fachada.isPartidaIniciada()) {
            mostrarMensaje("Aviso", "Debes iniciar una partida primero.");
            return;
        }

        TextInputDialog dialog = new TextInputDialog("mi_partida");
        dialog.setTitle("Guardar Partida");
        dialog.setHeaderText("¿Con qué nombre deseas guardar la partida?");
        dialog.setContentText("Nombre:");

        Optional<String> resultado = dialog.showAndWait();
        resultado.ifPresent(nombre -> {
            if (GestorPartida.guardarPartida(nombre, fachada)) {
                mostrarMensaje("Éxito", "Partida guardada como: " + nombre);
            } else {
                mostrarMensaje("Error", "No se pudo guardar la partida.");
            }
        });
    }

/**
 * Descripción para cargarPartida.
 */
    private void cargarPartida() {
        String[] partidas = GestorPartida.listarPartidasGuardadas();

        if (partidas.length == 0) {
            mostrarMensaje("Aviso", "No hay partidas guardadas.");
            return;
        }

        ChoiceDialog<String> dialog = new ChoiceDialog<>(partidas[0],
                Arrays.asList(partidas));
        dialog.setTitle("Cargar Partida");
        dialog.setHeaderText("Selecciona una partida para cargar:");
        dialog.setContentText("Partida:");

        Optional<String> resultado = dialog.showAndWait();
        resultado.ifPresent(nombre -> {
            FachadaJuego cargada = GestorPartida.cargarPartida(nombre);
            if (cargada != null) {
                fachada = cargada;
                actualizarVista();
                mostrarMensaje("Éxito", "Partida cargada: " + nombre);
            } else {
                mostrarMensaje("Error", "No se pudo cargar la partida.");
            }
        });
    }

/**
 * Descripción para reiniciarPartida.
 */
    private void reiniciarPartida() {
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Reiniciar Juego");
        confirmacion.setHeaderText("¿Estás seguro?");
        confirmacion.setContentText("Se perderá el progreso actual. ¿Deseas continuar?");

        Optional<ButtonType> resultado = confirmacion.showAndWait();
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {

            GestorPartidaAutomatico.eliminarPartidaActiva();


            fachada = new FachadaJuego();
            fachada.iniciarNuevaPartida();


            limpiarTablerosVisualmente();


            dibujarBarcosJugador();


            datosTabla.clear();
            tablaDisparos.refresh();


            btnIniciar.setDisable(false);
            btnFragata.setDisable(false);
            btnDestructor.setDisable(false);
            btnSubmarino.setDisable(false);
            btnPortaaviones.setDisable(false);
            btnOrientacion.setDisable(false);


            actualizarEstadoFlota();
            actualizarEstadoPartida();

            mostrarMensaje("Reinicio", "Partida reiniciada. ¡A jugar!");
        }
    }

/**
 * Descripción para actualizarVista.
 */
    private void actualizarVista() {

        dibujarBarcosJugador();

        dibujarBarcosProfesor();


        for (ArrayList<StackPane> fila : celdasMaquinaUI) {
            for (StackPane celda : fila) {

                celda.getChildren().clear();
            }
        }


        for (Disparo disparo : fachada.obtenerHistorial()) {
            if ("Humano".equals(disparo.getJugador())) {
                marcarResultadoEnTablero(celdasMaquinaUI, disparo.getCoordenada(),
                        disparo.getResultado(), false);
            } else {
                marcarResultadoEnTablero(celdasJugadorUI, disparo.getCoordenada(),
                        disparo.getResultado(), true);
            }
        }

        actualizarTabla();
        actualizarEstadoFlota();
        actualizarEstadoPartida();
    }

/**
 * Descripción para mostrarMensaje.
 * @param titulo parámetro de entrada.
 * @param mensaje parámetro de entrada.
 */
    private void mostrarMensaje(String titulo, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

/**
 * Descripción para limpiarTablerosVisualmente.
 */
    private void limpiarTablerosVisualmente() {

        for (ArrayList<StackPane> fila : celdasJugadorUI) {
            for (StackPane celda : fila) {
                celda.getChildren().clear();

                celda.setStyle("-fx-border-color: grey; -fx-background-color: aliceblue;");
            }
        }


        for (ArrayList<StackPane> fila : celdasMaquinaUI) {
            for (StackPane celda : fila) {
                celda.getChildren().clear();

                celda.setStyle("-fx-border-color: grey; -fx-background-color: aliceblue;");
            }
        }


        tableroJugador.getChildren().removeIf(node -> node instanceof BarcoFX);
        tableroMaquina.getChildren().removeIf(node -> node instanceof BarcoFX);
    }
}
