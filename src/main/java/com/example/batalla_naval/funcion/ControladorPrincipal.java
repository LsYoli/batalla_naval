package com.example.funcion; // paquete de los controladores

import com.example.batalla_naval.HelloApplication; // importa la clase principal para ubicar recursos
import com.example.batalla_naval.logica.*;
import com.example.batalla_naval.persistencia.*;
import javafx.event.ActionEvent; // importa ActionEvent para eventos de botones
import javafx.fxml.FXML; // import FXML para inyección
import javafx.fxml.FXMLLoader; // cargador de vistas
import javafx.scene.Node; // nodo base para obtener el escenario
import javafx.scene.Parent; // clase padre para escenas
import javafx.scene.Scene; // clase escena
import javafx.scene.control.*;
import javafx.stage.Stage; // ventana principal

import java.io.IOException; // para capturar errores de carga
import java.util.Optional;

/**
 * Controlador principal - Pantalla de bienvenida y selección
 * Implementa HU-6: Inicio del juego con opción de cargar o nuevo
 */
public class ControladorPrincipal { // controlador de la ventana principal
    @FXML private Button btnJugar;
    @FXML private Button btnAyuda;
    @FXML private Button btnSalir;
    @FXML private Label lblEstadoGuardado; // NUEVO - Mostrar si hay partida guardada

    @FXML
    public void initialize() {
        // Verificar si hay partida guardada y mostrar estado
        verificarPartidaGuardada();
    } // cierra initialize

    /**
     * HU-6: Verifica si existe una partida guardada y muestra información
     */
    private void verificarPartidaGuardada() { // verifica si hay una partida activa guardada
        if (GestorPartidaAutomatico.existePartidaActiva()) { // si existe partida activa
            String estado = GestorPartidaAutomatico.obtenerEstadoUltimoJugador(); // obtiene el estado del jugador
            String nickname = GestorPartidaAutomatico.extraerNickname(estado); // extrae el nickname
            String horaGuardado = GestorPartidaAutomatico.obtenerHoraUltimoGuardado(); // obtiene la hora del último guardado

            if (lblEstadoGuardado != null) { // si la etiqueta existe en la vista
                lblEstadoGuardado.setText(
                        "📌 Última sesión guardada\n" +
                                "Jugador: " + nickname + "\n" +
                                "Guardado: " + horaGuardado
                ); // muestra información de la partida guardada
                lblEstadoGuardado.setStyle("-fx-text-fill: green; -fx-font-weight: bold;"); // estilo verde para indicar disponibilidad
                lblEstadoGuardado.setVisible(true); // hace visible la etiqueta
            } // cierra if
        } else { // si no hay partida guardada
            if (lblEstadoGuardado != null) { // si la etiqueta existe
                lblEstadoGuardado.setText("📭 Sin partidas guardadas"); // mensaje informativo
                lblEstadoGuardado.setStyle("-fx-text-fill: gray;"); // estilo gris para indicar ausencia
                lblEstadoGuardado.setVisible(true); // hace visible la etiqueta
            } // cierra if
        } // cierra else
    } // cierra verificarPartidaGuardada

    @FXML // indica que se inyecta desde el FXML
    protected void manejarBotonJugar(ActionEvent event) throws IOException { // método llamado al presionar JUGAR
        // Verificar si existe partida guardada
        if (GestorPartidaAutomatico.existePartidaActiva()) { // si hay partida guardada
            mostrarDialogoCargarONuevo(event); // muestra diálogo para cargar o nuevo juego
        } else { // si no hay partida guardada
            preguntarModoAccesoYIrAlJuego(null, event); // pregunta el modo de acceso y va al juego
        } // cierra else
    } // cierra manejarBotonJugar

    /**
     * HU-6: Diálogo que pregunta al usuario si desea continuar o nuevo juego
     */
    private void mostrarDialogoCargarONuevo(ActionEvent event) throws IOException { // muestra opciones para partida guardada
        String nickname = GestorPartidaAutomatico.extraerNickname(
                GestorPartidaAutomatico.obtenerEstadoUltimoJugador()); // obtiene el nickname del jugador guardado
        String horaGuardado = GestorPartidaAutomatico.obtenerHoraUltimoGuardado(); // obtiene la hora del guardado

        Alert dialog = new Alert(Alert.AlertType.CONFIRMATION); // crea diálogo de confirmación
        dialog.setTitle("Continuar o Nuevo Juego"); // título del diálogo
        dialog.setHeaderText("Se encontró una partida guardada"); // encabezado informativo
        dialog.setContentText(
                "Jugador: " + nickname + "\n" + // muestra información del jugador
                        "Guardado: " + horaGuardado + "\n\n" + // muestra hora del guardado
                        "¿Deseas continuar desde donde quedaste o iniciar un juego nuevo?" // pregunta al usuario
        ); // contenido del diálogo

        ButtonType btnContinuar = new ButtonType("▶ Continuar", ButtonBar.ButtonData.YES); // botón para continuar
        ButtonType btnNuevo = new ButtonType("✨ Nuevo Juego", ButtonBar.ButtonData.NO); // botón para nuevo juego
        ButtonType btnCancelar = new ButtonType("❌ Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE); // botón para cancelar

        dialog.getButtonTypes().setAll(btnContinuar, btnNuevo, btnCancelar); // establece los botones en el diálogo

        Optional<ButtonType> resultado = dialog.showAndWait(); // muestra el diálogo y espera respuesta

        if (resultado.isPresent()) { // si el usuario seleccionó una opción
            if (resultado.get() == btnContinuar) { // si eligió continuar
                // Cargar partida existente
                cargarPartidaExistente(event); // carga la partida guardada
            } else if (resultado.get() == btnNuevo) { // si eligió nuevo juego
                // Nuevo juego - eliminar partida anterior
                GestorPartidaAutomatico.eliminarPartidaActiva(); // elimina la partida guardada
                preguntarModoAccesoYIrAlJuego(null, event); // pregunta modo de acceso y va al juego
            } // cierra else if
            // Si elige cancelar, no hace nada
        } // cierra if
    } // cierra mostrarDialogoCargarONuevo

    // ⭐⭐ NUEVO MÉTODO: Preguntar si es profesor para activar HU-3
    private void preguntarModoAccesoYIrAlJuego(FachadaJuego fachadaCargada, ActionEvent event) throws IOException { // pregunta el modo de acceso al usuario
        Alert profesorDialog = new Alert(Alert.AlertType.CONFIRMATION); // crea diálogo de confirmación
        profesorDialog.setTitle("Modo de acceso"); // título del diálogo
        profesorDialog.setHeaderText("¿Eres el profesor?"); // encabezado con la pregunta
        profesorDialog.setContentText("Selecciona 'Sí' para activar la visualización del tablero oculto (verificación).\n" + // explica la opción de profesor
                "Selecciona 'No' para jugar en modo normal."); // explica la opción de jugador normal

        ButtonType btnSi = new ButtonType("👨‍🏫 Sí, soy profesor", ButtonBar.ButtonData.YES); // botón para modo profesor
        ButtonType btnNo = new ButtonType("🎮 No, soy jugador", ButtonBar.ButtonData.NO); // botón para modo jugador normal
        profesorDialog.getButtonTypes().setAll(btnSi, btnNo); // establece los botones

        Optional<ButtonType> resultadoProfesor = profesorDialog.showAndWait(); // muestra el diálogo y espera respuesta
        boolean esProfesor = resultadoProfesor.isPresent() && resultadoProfesor.get() == btnSi; // determina si es profesor

        // Ir al juego con la información del modo de acceso
        irAlJuego(fachadaCargada, event, esProfesor); // navega a la pantalla de juego con el modo correspondiente
    } // cierra preguntarModoAccesoYIrAlJuego

    /**
     * Carga la partida guardada y pasa la información al controlador del juego
     */
    private void cargarPartidaExistente(ActionEvent event) throws IOException { // carga una partida previamente guardada
        try { // bloque de intento para manejar posibles errores
            // Cargar fachada
            FachadaJuego fachadaCargada = GestorPartidaAutomatico.cargarPartidaActiva(); // carga la partida activa desde archivo

            if (fachadaCargada == null) { // si no se pudo cargar
                mostrarError("No se pudo cargar la partida."); // muestra mensaje de error
                return; // sale del método
            } // cierra if

            // Obtener nickname
            String estadoJugador = GestorPartidaAutomatico.obtenerEstadoUltimoJugador(); // obtiene el estado del jugador
            String nickname = GestorPartidaAutomatico.extraerNickname(estadoJugador); // extrae el nickname

            // Verificar si la partida ya terminó
            if (GestorPartidaAutomatico.partidaYaTermino(fachadaCargada)) { // si la partida ya terminó
                mostrarMensaje("Partida Terminada",
                        "La partida guardada ya ha terminado.\n" +
                                "Se iniciará un juego nuevo."); // informa al usuario
                GestorPartidaAutomatico.eliminarPartidaActiva(); // elimina la partida terminada
                preguntarModoAccesoYIrAlJuego(null, event); // pregunta modo de acceso y va a nuevo juego
                return; // sale del método
            } // cierra if

            // Preguntar modo de acceso y luego ir al juego con la partida cargada
            preguntarModoAccesoYIrAlJuego(fachadaCargada, event); // pregunta modo de acceso y navega al juego

        } catch (IOException e) { // si ocurre un error de entrada/salida
            mostrarError("Error al cargar la partida: " + e.getMessage()); // muestra mensaje de error
        } // cierra catch
    } // cierra cargarPartidaExistente

    /**
     * Navega a la pantalla de juego
     * @param fachadaCargada Si es null, inicia juego nuevo; si no, continúa
     * @param esProfesor true si el usuario es profesor (activa HU-3)
     */
    private void irAlJuego(FachadaJuego fachadaCargada, ActionEvent event, boolean esProfesor) throws IOException { // navega a la pantalla principal del juego
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("juego.fxml")); // prepara el cargador de la vista de juego
        Parent root = loader.load(); // carga el contenido del FXML

        // Pasar la fachada al controlador del juego si existe
        com.example.funcion.ControladorJuego controlador = loader.getController(); // obtiene el controlador de la vista de juego

        // ⭐⭐ ACTIVAR MODO PROFESOR SI ES NECESARIO (HU-3)
        if (esProfesor) { // si el usuario es profesor
            controlador.habilitarModoProfesor(true); // activa el modo profesor en el controlador
        } // cierra if

        if (fachadaCargada != null) { // si hay una fachada cargada
            controlador.cargarPartidaContinuacion(fachadaCargada); // pasa la fachada al controlador
        } // cierra if

        Scene escena = new Scene(root); // crea la escena con el contenido
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow(); // obtiene la ventana actual a partir del botón
        stage.setScene(escena); // cambia la escena visible a la de juego
        stage.show(); // muestra la nueva escena
    } // cierra irAlJuego

    @FXML // anotación FXML
    protected void manejarBotonAyuda(ActionEvent event) throws IOException { // método para el botón de ayuda
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("instrucciones.fxml")); // carga la vista de instrucciones
        Parent root = loader.load(); // carga el contenido
        Scene escena = new Scene(root); // crea la escena
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow(); // obtiene el escenario
        stage.setScene(escena); // cambia la escena a instrucciones
        stage.show(); // muestra la escena
    } // cierra manejarBotonAyuda

    @FXML // anotación FXML
    protected void manejarBotonSalir(ActionEvent event) { // método para el botón de salir
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow(); // obtiene la ventana actual
        stage.close(); // cierra la ventana
    } // cierra manejarBotonSalir

    private void mostrarMensaje(String titulo, String mensaje) { // muestra un mensaje informativo
        Alert alerta = new Alert(Alert.AlertType.INFORMATION); // crea una alerta de información
        alerta.setTitle(titulo); // establece el título
        alerta.setHeaderText(null); // sin encabezado
        alerta.setContentText(mensaje); // establece el mensaje
        alerta.showAndWait(); // muestra y espera
    } // cierra mostrarMensaje

    private void mostrarError(String mensaje) { // muestra un mensaje de error
        Alert alerta = new Alert(Alert.AlertType.ERROR); // crea una alerta de error
        alerta.setTitle("Error"); // establece el título
        alerta.setHeaderText(null); // sin encabezado
        alerta.setContentText(mensaje); // establece el mensaje
        alerta.showAndWait(); // muestra y espera
    } // cierra mostrarError
} // cierra la clase ControladorPrincipal