/**
 * Módulo principal de la aplicación Batalla Naval.
 */
module com.example.batalla_naval {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.batalla_naval to javafx.fxml;
    exports com.example.batalla_naval;
    opens com.example.funcion to javafx.fxml;
    exports com.example.funcion;
    exports com.example.batalla_naval.logica;
    opens com.example.batalla_naval.logica to javafx.fxml;
}
