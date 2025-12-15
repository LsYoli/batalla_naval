package com.example.funcion;

import com.example.batalla_naval.HelloApplication;
import com.example.batalla_naval.logica.FachadaJuego;
import com.example.batalla_naval.persistencia.GestorPartidaAutomatico;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

/**
 * Controlador principal de la pantalla inicial: permite iniciar partida, acceder a ayuda o salir.
 * También muestra el estado de partidas guardadas para continuar el juego.
 */
public class ControladorPrincipal {
    @FXML private Button btnJugar;
    @FXML private Button btnAyuda;
    @FXML private Button btnSalir;
    @FXML private Label lblEstadoGuardado;

    /**
     * Inicializa la pantalla verificando si existe una partida previamente guardada.
     */
    @FXML
    public void initialize() {
        verificarPartidaGuardada();
    }

    /**
     * Comprueba si hay una partida activa y actualiza el indicador visual.
     */
    private void verificarPartidaGuardada() {
        if (GestorPartidaAutomatico.existePartidaActiva()) {
            String estado = GestorPartidaAutomatico.obtenerEstadoUltimoJugador();
            String nickname = GestorPartidaAutomatico.extraerNickname(estado);
            String horaGuardado = GestorPartidaAutomatico.obtenerHoraUltimoGuardado();

            if (lblEstadoGuardado != null) {
                lblEstadoGuardado.setText(
                        "📌 Última sesión guardada\n" +
                                "Jugador: " + nickname + "\n" +
                                "Guardado: " + horaGuardado
                );
                lblEstadoGuardado.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
                lblEstadoGuardado.setVisible(true);
            }
        } else {
            if (lblEstadoGuardado != null) {
                lblEstadoGuardado.setText("📭 Sin partidas guardadas");
                lblEstadoGuardado.setStyle("-fx-text-fill: gray;");
                lblEstadoGuardado.setVisible(true);
            }
        }
    }

    /**
     * Gestiona el clic del botón Jugar y decide si continuar una partida o iniciar una nueva.
     *
     * @param event evento de acción asociado al botón.
     */
    @FXML
    protected void manejarBotonJugar(ActionEvent event) throws IOException {
        if (GestorPartidaAutomatico.existePartidaActiva()) {
            mostrarDialogoCargarONuevo(event);
        } else {
            preguntarModoAccesoYIrAlJuego(null, event);
        }
    }

    /**
     * Pregunta al usuario si desea continuar la partida guardada o comenzar una nueva.
     */
    private void mostrarDialogoCargarONuevo(ActionEvent event) throws IOException {
        String nickname = GestorPartidaAutomatico.extraerNickname(
                GestorPartidaAutomatico.obtenerEstadoUltimoJugador());
        String horaGuardado = GestorPartidaAutomatico.obtenerHoraUltimoGuardado();

        Alert dialog = new Alert(Alert.AlertType.CONFIRMATION);
        dialog.setTitle("Continuar o Nuevo Juego");
        dialog.setHeaderText("Se encontró una partida guardada");
        dialog.setContentText(
                "Jugador: " + nickname + "\n" +
                        "Guardado: " + horaGuardado + "\n\n" +
                        "¿Deseas continuar desde donde quedaste o iniciar un juego nuevo?"
        );

        ButtonType btnContinuar = new ButtonType("▶ Continuar", ButtonBar.ButtonData.YES);
        ButtonType btnNuevo = new ButtonType("✨ Nuevo Juego", ButtonBar.ButtonData.NO);
        ButtonType btnCancelar = new ButtonType("❌ Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);

        dialog.getButtonTypes().setAll(btnContinuar, btnNuevo, btnCancelar);

        Optional<ButtonType> resultado = dialog.showAndWait();

        if (resultado.isPresent()) {
            if (resultado.get() == btnContinuar) {
                cargarPartidaExistente(event);
            } else if (resultado.get() == btnNuevo) {
                GestorPartidaAutomatico.eliminarPartidaActiva();
                preguntarModoAccesoYIrAlJuego(null, event);
            }
        }
    }

    /**
     * Solicita el modo de acceso (profesor o jugador) y navega a la escena de juego.
     *
     * @param fachadaCargada fachada existente cuando se continúa una partida; null si es nueva.
     * @param event evento de navegación desde la pantalla principal.
     */
    private void preguntarModoAccesoYIrAlJuego(FachadaJuego fachadaCargada, ActionEvent event) throws IOException {
        Alert profesorDialog = new Alert(Alert.AlertType.CONFIRMATION);
        profesorDialog.setTitle("Modo Profesor");
        profesorDialog.setHeaderText("¿Eres profesor y quieres ver el tablero del alumno?");
        profesorDialog.setContentText("Esta opción activa las funcionalidades especiales del HU-3.");

        ButtonType btnSi = new ButtonType("Sí", ButtonBar.ButtonData.YES);
        ButtonType btnNo = new ButtonType("No", ButtonBar.ButtonData.NO);
        profesorDialog.getButtonTypes().setAll(btnSi, btnNo);

        Optional<ButtonType> resultadoProfesor = profesorDialog.showAndWait();
        boolean esProfesor = resultadoProfesor.isPresent() && resultadoProfesor.get() == btnSi;

        irAlJuego(fachadaCargada, event, esProfesor);
    }

    /**
     * Carga la partida guardada y redirige al juego con esos datos.
     */
    private void cargarPartidaExistente(ActionEvent event) throws IOException {
        try {
            FachadaJuego fachadaCargada = GestorPartidaAutomatico.cargarPartidaActiva();

            if (fachadaCargada == null) {
                mostrarError("No se pudo cargar la partida.");
                return;
            }

            String estadoJugador = GestorPartidaAutomatico.obtenerEstadoUltimoJugador();
            String nickname = GestorPartidaAutomatico.extraerNickname(estadoJugador);

            if (GestorPartidaAutomatico.partidaYaTermino(fachadaCargada)) {
                mostrarMensaje("Partida Terminada",
                        "La partida guardada ya ha terminado.\n" +
                                "Se iniciará un juego nuevo.");
                GestorPartidaAutomatico.eliminarPartidaActiva();
                preguntarModoAccesoYIrAlJuego(null, event);
                return;
            }

            preguntarModoAccesoYIrAlJuego(fachadaCargada, event);

        } catch (IOException e) {
            mostrarError("Error al cargar la partida: " + e.getMessage());
        }
    }

    /**
     * Navega a la pantalla de juego, configurando el modo profesor si corresponde.
     *
     * @param fachadaCargada fachada existente al continuar una partida; null para iniciar nueva.
     * @param esProfesor indica si el usuario activó el modo profesor.
     */
    private void irAlJuego(FachadaJuego fachadaCargada, ActionEvent event, boolean esProfesor) throws IOException {
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("juego.fxml"));
        Parent root = loader.load();

        ControladorJuego controlador = loader.getController();

        if (esProfesor) {
            controlador.habilitarModoProfesor(true);
        }

        if (fachadaCargada != null) {
            controlador.cargarPartidaContinuacion(fachadaCargada);
        }

        Scene escena = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(escena);
        stage.show();
    }

    /**
     * Abre la vista de instrucciones de juego.
     *
     * @param event evento de navegación desde la pantalla principal.
     */
    @FXML
    protected void manejarBotonAyuda(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("instrucciones.fxml"));
        Parent root = loader.load();
        Scene escena = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(escena);
        stage.show();
    }

    /**
     * Cierra la aplicación desde la pantalla principal.
     *
     * @param event evento asociado al botón de salida.
     */
    @FXML
    protected void manejarBotonSalir(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    /**
     * Muestra un cuadro de diálogo informativo.
     *
     * @param titulo título de la ventana.
     * @param mensaje contenido a mostrar.
     */
    private void mostrarMensaje(String titulo, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    /**
     * Muestra un cuadro de diálogo de error.
     *
     * @param mensaje detalle del problema.
     */
    private void mostrarError(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle("Error");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
