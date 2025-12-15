package com.example.funcion; // paquete del controlador del juego

import com.example.batalla_naval.logica.*; // <-- Importar TODA la lógica, incluyendo TipoBarco
import com.example.batalla_naval.persistencia.*;
import com.example.batalla_naval.vista.BarcoFX; // Importar la clase BarcoFX

import javafx.collections.FXCollections; // utilidades para listas observables
import javafx.collections.ObservableList; // lista observable para la tabla
import javafx.fxml.FXML; // anotación FXML
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory; // factoría para enlazar propiedades
import javafx.scene.layout.GridPane; // contenedor de cuadrícula
import javafx.scene.layout.StackPane; // contenedor para cada celda
import javafx.scene.paint.Color; // colores para dibujos
import javafx.scene.shape.Ellipse; // forma ovalada para los barcos
import javafx.scene.text.Text; // texto para marcas
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.*;

public class ControladorJuego { // clase que maneja la escena de juego
    // --- Referencias FXML ---
    @FXML private GridPane tableroJugador; // referencia al grid del jugador
    @FXML private GridPane tableroMaquina; // referencia al grid de la máquina
    @FXML private Button btnFragata; // botón para seleccionar fragata
    @FXML private Button btnDestructor; // botón para seleccionar destructor
    @FXML private Button btnSubmarino; // botón para seleccionar submarino
    @FXML private Button btnPortaaviones; // botón para seleccionar portaaviones
    @FXML private Button btnOrientacion; // botón para alternar orientación
    @FXML private Button btnIniciar; // botón para iniciar partida
    @FXML private Button btnGuardar;
    @FXML private Button btnCargar;
    @FXML private Button btnReiniciar;
    @FXML private Button btnVerTableroOculto; // botón para ver tablero oculto (HU-3)
    @FXML private Label lblEstadoFlota;
    @FXML private Label lblEstadoPartida;
    // --- Referencias Tabla ---
    @FXML private TableView<Disparo> tablaDisparos; // tabla para mostrar disparos
    @FXML private TableColumn<Disparo, Integer> colNumero; // columna número
    @FXML private TableColumn<Disparo, String> colJugador; // columna jugador
    @FXML private TableColumn<Disparo, String> colCoordenada; // columna coordenada
    @FXML private TableColumn<Disparo, String> colResultado; // columna resultado

    // --- Variables de Estado ---
    private FachadaJuego fachada = new FachadaJuego(); // fachada que encapsula la lógica (patrón Facade)
    private TipoBarco tipoSeleccionado = TipoBarco.FRAGATA; // tipo seleccionado por defecto
    private final ArrayList<ArrayList<StackPane>> celdasJugadorUI = new ArrayList<>(); // estructura de celdas visuales del jugador
    private final ArrayList<ArrayList<StackPane>> celdasMaquinaUI = new ArrayList<>(); // estructura de celdas visuales de la máquina
    private final ObservableList<Disparo> datosTabla = FXCollections.observableArrayList(); // lista observable para la TableView
    private long tiempoInicio;
    private String nicknamejugador = "Jugador";
    private final double TAMANO_CELDA = 30.0; // TAMAÑO DE CELDA para BarcoFX (Asegurar consistencia)

    @FXML // método llamado al cargar la vista
    public void initialize() { // inicializa controles y tableros
        solicitarNickname();
        fachada.iniciarNuevaPartida(); // reinicia la fachada y la flota de la máquina
        construirTablero(tableroJugador, false); // construye las celdas del tablero del jugador
        construirTablero(tableroMaquina, true); // construye las celdas del tablero de la máquina
        prepararTabla(); // configura las columnas de la tabla
        configurarBotones(); // conecta los botones de selección
        configurarBotonVerificacionProfesor(); // configura el botón de verificación para el profesor (HU-3)
        actualizarEstadoFlota();
        actualizarEstadoPartida();
    } // cierra initialize

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
    } // cierra solicitarNickname

    private void configurarBotonVerificacionProfesor() { // configura el botón oculto para ver el tablero de la máquina
        if (btnVerTableroOculto != null) {
            btnVerTableroOculto.setOnAction(e -> mostrarTableroOcultoParaProfesor());
        }
    } // cierra configurarBotonVerificacionProfesor

    private void mostrarTableroOcultoParaProfesor() { // crea una ventana con la vista completa del tablero de la máquina
        try {
            //Se asume que VBoxExtra está correctamente definido para mostrar el tablero de la máquina.
            VBoxExtra vistaVerificacion = new VBoxExtra(fachada);
            //dibujarBarcosProfesor();
            Stage stageVerificacion = new Stage();
            stageVerificacion.setTitle("🔍 VISTA DEL PROFESOR - Tablero de la Máquina");
            Scene escena = new Scene(vistaVerificacion, 600, 700);
            stageVerificacion.setScene(escena);
            stageVerificacion.show();
        } catch (Exception e) {
            mostrarMensaje("Error", "No se pudo abrir la vista de verificación: " + e.getMessage());
        }
    } // cierra mostrarTableroOcultoParaProfesor

    public void habilitarModoProfesor(boolean activar) { // activa o desactiva el botón de verificación
        if (btnVerTableroOculto != null) {
            btnVerTableroOculto.setVisible(activar);
            btnVerTableroOculto.setManaged(activar);
        }
    } // cierra habilitarModoProfesor

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
    } // cierra cargarPartidaContinuacion

    private void construirTablero(GridPane grid, boolean esMaquina) { // crea la grilla visual de 10x10
        ArrayList<ArrayList<StackPane>> referencia = esMaquina ? celdasMaquinaUI : celdasJugadorUI;
        referencia.clear();
        for (int fila = 0; fila < 10; fila++) {
            ArrayList<StackPane> filaLista = new ArrayList<>();
            for (int col = 0; col < 10; col++) {
                StackPane celda = new StackPane();
                // Ajuste de tamaño de celda UI para que coincida con TAMANO_CELDA
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
    } // cierra construirTablero

    private void prepararTabla() { // configura columnas de la TableView
        colNumero.setCellValueFactory(new PropertyValueFactory<>("numero"));
        colJugador.setCellValueFactory(new PropertyValueFactory<>("jugador"));
        colCoordenada.setCellValueFactory(new PropertyValueFactory<>("coordenada"));
        colResultado.setCellValueFactory(new PropertyValueFactory<>("resultado"));
        tablaDisparos.setItems(datosTabla);
    } // cierra prepararTabla

    private void configurarBotones() { // conecta acciones de los botones
        btnFragata.setOnAction(e -> tipoSeleccionado = TipoBarco.FRAGATA);
        btnDestructor.setOnAction(e -> tipoSeleccionado = TipoBarco.DESTRUCTOR);
        btnSubmarino.setOnAction(e -> tipoSeleccionado = TipoBarco.SUBMARINO);
        btnPortaaviones.setOnAction(e -> tipoSeleccionado = TipoBarco.PORTAAVIONES);
        btnOrientacion.setOnAction(e -> alternarOrientacion());
        btnIniciar.setOnAction(e -> iniciarPartida());
        btnGuardar.setOnAction(e -> guardarPartida());
        btnCargar.setOnAction(e -> cargarPartida());
        btnReiniciar.setOnAction(e -> reiniciarPartida());
    } // cierra configurarBotones

    private void alternarOrientacion() { // cambia el texto y la orientación almacenada
        fachada.getJugador().alternarOrientacion();
        String nuevo = fachada.getJugador().getOrientacionActual() == EnumOrientacion.HORIZONTAL ? "Horizontal" : "Vertical";
        btnOrientacion.setText(nuevo);
    } // cierra alternarOrientacion

    private boolean estaFlotaCompleta() {
        return fachada.getJugador().getFlota().size() == 10;
    } // cierra estaFlotaCompleta

    private void actualizarEstadoFlota() {
        int barcosColocados = fachada.getJugador().getFlota().size();
        lblEstadoFlota.setText(barcosColocados + "/10 barcos");

        if (barcosColocados == 10) {
            lblEstadoFlota.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
        } else {
            lblEstadoFlota.setStyle("-fx-text-fill: orange; -fx-font-weight: bold;");
        }
    } // cierra actualizarEstadoFlota

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
    } // cierra actualizarEstadoPartida

    private void manejarColocacionJugador(int fila, int col) { // procesa el clic en el tablero del jugador
        if (fachada.isPartidaIniciada()) {
            mostrarMensaje("Partida iniciada", "Ya no puedes mover los barcos.");
            return;
        }

        // Contar la flota antes de la colocación
        int barcosAntes = fachada.getJugador().getFlota().size();

        try { // intenta colocar el barco
            // Lógica: Colocar el barco en el modelo
            // NO INTENTAR ASIGNAR EL RESULTADO A UN BARCO SI EL MÉTODO ES VOID/NO DEVUELVE NADA
            fachada.colocarBarcoJugador(tipoSeleccionado, new Coordenada(fila, col));

            // Si la colocación fue exitosa y se añadió un barco nuevo:
            List<Barco> flota = fachada.getJugador().getFlota();
            if (flota.size() > barcosAntes) { // Verifica si el tamaño de la flota aumentó
                // 3. Llama a dibujarBarcosJugador() que limpiará y repintará toda la flota
                dibujarBarcosJugador();
            }

            actualizarEstadoFlota();
        } catch (PosicionInvalidaException e) {
            mostrarMensaje("Posición inválida", e.getMessage());
        }
    } // cierra manejarColocacionJugador

    /**
     * MÉTODO CLAVE: Limpia el tablero y redibuja TODA la flota usando BarcoFX.
     * Usa GridPane.add() y Column/Row Span para evitar el apilamiento.
     */
    private void dibujarBarcosJugador() {
        // 1. Limpiar todas las figuras de BarcoFX anteriores
        tableroJugador.getChildren().removeIf(node -> node instanceof BarcoFX);

        // 2. Dibujar cada barco con su representación BarcoFX
        for (Barco barco : fachada.getJugador().getFlota()) {
            if (barco.getFilaInicial() != -1) {

                BarcoFX barcoFX = new BarcoFX(barco);

                // Colocación por índice de GridPane (Correcto: col, fila)
                tableroJugador.add(barcoFX, barco.getColumnaInicial(), barco.getFilaInicial());

                // Establecer el tamaño que ocupa en celdas (Span)
                int tamano = barco.getTamano();
                if (barco.getOrientacion() == EnumOrientacion.HORIZONTAL) {
                    GridPane.setColumnSpan(barcoFX, tamano);
                    GridPane.setRowSpan(barcoFX, 1);
                } else { // VERTICAL
                    GridPane.setColumnSpan(barcoFX, 1);
                    GridPane.setRowSpan(barcoFX, tamano);
                }

                // Manejo de la Orientación y Rotación (con ajustes de traslación)
                barcoFX.setRotate(0);
                barcoFX.setTranslateX(0);
                barcoFX.setTranslateY(0);

                if (barco.getOrientacion() == EnumOrientacion.VERTICAL) {
                    barcoFX.setRotate(90); // Rota 90 grados

                    // Cálculo del ajuste de largo (compensación de giro)
                    double tamanoTotal = barco.getTamano() * TAMANO_CELDA;
                    double ajuste = (tamanoTotal - TAMANO_CELDA) / 2.0;

                    // (Mueve a la derecha para centrar y compensar el margen)
                    barcoFX.setTranslateX(-ajuste);

                    // (Mueve hacia arriba para centrar y compensar el margen)
                    barcoFX.setTranslateY(-ajuste); // Traslada Y negativamente: Ajuste de largo (-ajuste)
                }
            }
        }
    } // cierra dibujarBarcosJugador

    /**
     * Dibuja la flota de la máquina en el tableroMaquina (Vista del Profesor).
     * Utiliza la misma lógica de BarcoFX y traslación que el jugador.
     */
    private void dibujarBarcosProfesor() {
        // Limpiar todas las figuras de BarcoFX anteriores
        tableroMaquina.getChildren().removeIf(node -> node instanceof BarcoFX);

        // Dibujar cada barco con su representación BarcoFX
        for (Barco barco : fachada.getMaquina().getFlota()) {
            if (barco.getFilaInicial() != -1) {

                BarcoFX barcoFX = new BarcoFX(barco);

                // Colocación por índice de GridPane (Correcto: col, fila)
                tableroMaquina.add(barcoFX, barco.getColumnaInicial(), barco.getFilaInicial());

                // Establecer el tamaño que ocupa en celdas
                int tamano = barco.getTamano();
                if (barco.getOrientacion() == EnumOrientacion.HORIZONTAL) {
                    GridPane.setColumnSpan(barcoFX, tamano);
                    GridPane.setRowSpan(barcoFX, 1);
                } else { // VERTICAL
                    GridPane.setColumnSpan(barcoFX, 1);
                    GridPane.setRowSpan(barcoFX, tamano);
                }

                // Manejo de la Orientación y Rotación (Igual al del jugador)
                barcoFX.setRotate(0);
                barcoFX.setTranslateX(0);
                barcoFX.setTranslateY(0);

                if (barco.getOrientacion() == EnumOrientacion.VERTICAL) {
                    barcoFX.setRotate(90); // Rota 90 grados

                    // Cálculo del ajuste de largo (compensación de giro)
                    double tamanoTotal = barco.getTamano() * TAMANO_CELDA;
                    double ajuste = (tamanoTotal - TAMANO_CELDA) / 2.0;

                    // (Mueve a la derecha para centrar)
                    barcoFX.setTranslateX(-ajuste);

                    // (Mueve hacia arriba para centrar)
                    barcoFX.setTranslateY(-ajuste);
                }
            }
        }
    }

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
    } // cierra iniciarPartida

    private void guardarAutomaticamente() {
        boolean guardado = GestorPartidaAutomatico.guardarPartidaActiva(fachada, nicknamejugador);
        if (guardado) {
            System.out.println("💾 Partida guardada automáticamente");
        }
    } // cierra guardarAutomaticamente

    private void manejarDisparoJugador(int fila, int col) { // procesa el disparo del jugador sobre la máquina
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
    } // cierra manejarDisparoJugador

    private void procesarDisparosMaquinaEnOrden(int indiceInicio, int cantidadNuevos) { // procesa todos los disparos nuevos de la máquina
        Stack<Disparo> pila = fachada.historialMaquina();
        int limite = indiceInicio + cantidadNuevos;
        for (int i = indiceInicio; i < limite && i < pila.size(); i++) {
            Disparo disparo = pila.get(i);
            marcarResultadoEnTablero(celdasJugadorUI, disparo.getCoordenada(), disparo.getResultado(), true);
        }
        actualizarTabla();
    } // cierra procesarDisparosMaquinaEnOrden

    private void marcarResultadoEnTablero(ArrayList<ArrayList<StackPane>> referencia, Coordenada coord, EnumResultadoDisparo resultado, boolean esTableroJugador) { // agrega marcas visuales según resultado
        StackPane celdaUI = referencia.get(coord.getFila()).get(coord.getColumna());

        // Limpiar solo marcas de disparo anteriores (no la figura del barcoFX)
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
    } // cierra marcarResultadoEnTablero

    private void actualizarTabla() { // actualiza la TableView con los disparos de ambos jugadores
        datosTabla.clear();
        List<Disparo> todos = fachada.obtenerHistorial();
        datosTabla.addAll(todos);
        tablaDisparos.refresh();
    } // cierra actualizarTabla

    private boolean verificarFin() { // revisa si alguien ganó
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
    } // cierra verificarFin

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
    } // cierra guardarPartida

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
    } // cierra cargarPartida

    private void reiniciarPartida() {
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Reiniciar Juego");
        confirmacion.setHeaderText("¿Estás seguro?");
        confirmacion.setContentText("Se perderá el progreso actual. ¿Deseas continuar?");

        Optional<ButtonType> resultado = confirmacion.showAndWait();
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            // 1. Eliminar partida activa
            GestorPartidaAutomatico.eliminarPartidaActiva();

            // 2. Reiniciar fachada
            fachada = new FachadaJuego();
            fachada.iniciarNuevaPartida();

            // 3. LIMPIAR VISUALMENTE AMBOS TABLEROS
            limpiarTablerosVisualmente();

            // 4. Redibujar barcos del jugador (la flota estará vacía)
            dibujarBarcosJugador();

            // 5. Limpiar tabla de disparos
            datosTabla.clear();
            tablaDisparos.refresh();

            // 6. Habilitar botones
            btnIniciar.setDisable(false);
            btnFragata.setDisable(false);
            btnDestructor.setDisable(false);
            btnSubmarino.setDisable(false);
            btnPortaaviones.setDisable(false);
            btnOrientacion.setDisable(false);

            // 7. Actualizar estados
            actualizarEstadoFlota();
            actualizarEstadoPartida();

            mostrarMensaje("Reinicio", "Partida reiniciada. ¡A jugar!");
        }
    } // cierra reiniciarPartida

    private void actualizarVista() {
        // Redibujar todos los barcos del jugador
        dibujarBarcosJugador();
        // Redibujar todos los barcos del profesor
        dibujarBarcosProfesor();

        // Limpiar marcas de la máquina y luego repintar el historial
        for (ArrayList<StackPane> fila : celdasMaquinaUI) {
            for (StackPane celda : fila) {
                // Solo limpiar marcas de disparo, no la cuadrícula
                celda.getChildren().clear();
            }
        }

        // Repintar marcas de disparo en ambos tableros
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
    } // cierra actualizarVista

    private void mostrarMensaje(String titulo, String mensaje) { // muestra un cuadro de diálogo sencillo
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    } // cierra mostrarMensaje

    private void limpiarTablerosVisualmente() {
        // Limpiar tablero del JUGADOR (celdasJugadorUI)
        for (ArrayList<StackPane> fila : celdasJugadorUI) {
            for (StackPane celda : fila) {
                celda.getChildren().clear();  // LIMPIAR TODAS LAS MARCAS
                // Restaurar estilo básico
                celda.setStyle("-fx-border-color: grey; -fx-background-color: aliceblue;");
            }
        }

        // Limpiar tablero de la MÁQUINA (celdasMaquinaUI)
        for (ArrayList<StackPane> fila : celdasMaquinaUI) {
            for (StackPane celda : fila) {
                celda.getChildren().clear();  // LIMPIAR TODAS LAS MARCAS
                // Restaurar estilo básico
                celda.setStyle("-fx-border-color: grey; -fx-background-color: aliceblue;");
            }
        }

        // Remover objetos BarcoFX del GridPane del jugador
        tableroJugador.getChildren().removeIf(node -> node instanceof BarcoFX);
        tableroMaquina.getChildren().removeIf(node -> node instanceof BarcoFX);
    } // cierra limpiarTablerosVisualmente
} // cierra la clase ControladorJuego