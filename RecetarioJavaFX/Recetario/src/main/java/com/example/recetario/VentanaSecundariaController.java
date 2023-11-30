package com.example.recetario;

import com.example.recetario.models.Receta;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controlador para la interfaz de la ventana secundaria de la aplicación.
 * Implementa la interfaz Initializable para realizar acciones de inicialización.
 */
public class VentanaSecundariaController implements Initializable
{
    @javafx.fxml.FXML
    private Button btnVolver;
    @javafx.fxml.FXML
    private TextField txtReceta;
    @javafx.fxml.FXML
    private ChoiceBox<String> comboDificultad;
    @javafx.fxml.FXML
    private ChoiceBox<String> comboTipo;
    @javafx.fxml.FXML
    private Spinner<Integer> spinnerDuracion;
    @javafx.fxml.FXML
    private Button btnActualizar;

    private Receta recetaActual;
    @javafx.fxml.FXML
    private Button btnBorrar;


    /**
     * Método llamado al hacer clic en el botón "Volver".
     * Carga la interfaz de la ventana principal.
     *
     * @param actionEvent El evento de acción asociado al botón "Volver".
     */
    @javafx.fxml.FXML
    public void volver(ActionEvent actionEvent) {
        HelloApplication.loadFXML("ventanaPrincipal.fxml");
    }


    /**
     * Método de inicialización llamado al cargar la interfaz.
     * Configura los elementos de la interfaz con los datos de la receta actual.
     *
     * @param url            La ubicación del archivo FXML. No se utiliza en este contexto.
     * @param resourceBundle El ResourceBundle que se puede usar para localizar elementos.
     *                       No se utiliza en este contexto.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Obtiene la receta actual de la sesión
        recetaActual = Session.getRecetaActual();

        // Configura el spinner de duración con valores predeterminados y el valor actual de la receta
        spinnerDuracion.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 180, recetaActual.getDuracion(), 10));

        // Configura el ChoiceBox de dificultad con opciones predeterminadas y el valor actual de la receta
        comboDificultad.getItems().addAll("Fácil", "Dificil", "Moderada");
        comboDificultad.setValue(recetaActual.getDificultad());

        // Configura el ChoiceBox de tipo con opciones predeterminadas y el valor actual de la receta
        comboTipo.getItems().addAll("Desayuno", "Segundo desayuno", "Almuerzo", "SobreAlmuerzo", "Merienda", "Cena", "Recena", "Postcena");
        comboTipo.setValue(recetaActual.getTipo());

        // Configura el campo de texto con el nombre actual de la receta
        txtReceta.setText(recetaActual.getNombre());

    }


    /**
     * Método llamado al hacer clic en el botón "Actualizar".
     * Actualiza la receta actual con los valores ingresados en la interfaz y carga la ventana principal.
     *
     * @param actionEvent El evento de acción asociado al botón "Actualizar".
     */
    @javafx.fxml.FXML
    public void actualizar(ActionEvent actionEvent) {
        // Actualiza la receta actual con los valores ingresados en la interfaz
        recetaActual.setNombre(txtReceta.getText());
        recetaActual.setTipo(comboTipo.getValue());
        recetaActual.setDificultad(comboDificultad.getValue());
        recetaActual.setDuracion(spinnerDuracion.getValue());

        // Actualiza la receta actual en la sesión y en la lista de recetas
        Session.setRecetaActual(recetaActual);
        Session.getRecetas().set(Session.getPos(), Session.getRecetaActual());

        // Muestra una alerta informando que el cambio se ha realizado
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setContentText("Cambio realizado!");
        alerta.showAndWait();

        // Reinicia la posición en la sesión y carga la ventana principal
        Session.setPos(null);
        HelloApplication.loadFXML("ventanaPrincipal.fxml");
    }


    /**
     * Método llamado al hacer clic en el botón "Borrar".
     * Imprime la posición y elimina la receta actual de la lista de recetas, luego carga la ventana principal.
     *
     * @param actionEvent El evento de acción asociado al botón "Borrar".
     */
    @javafx.fxml.FXML
    public void borrarTarea(ActionEvent actionEvent) {
        // Imprime la posición en la consola
        System.out.println(Session.getPos());

        // Elimina la receta actual de la lista de recetas usando la posición
        System.out.println(Session.getRecetas().remove((int) Session.getPos()));

        // Carga la ventana principal
        HelloApplication.loadFXML("ventanaPrincipal.fxml");

    }
}