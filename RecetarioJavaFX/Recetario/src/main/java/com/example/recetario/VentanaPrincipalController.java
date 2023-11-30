package com.example.recetario;

import com.example.recetario.models.Receta;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import javafx.util.StringConverter;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class VentanaPrincipalController implements Initializable {
    @FXML
    private Label welcomeText;
    @FXML
    private TextField txtNombre;
    @FXML
    private Slider sliderDuracion;
    @FXML
    private ComboBox<String> comboDificultad;
    @FXML
    private ListView<String> listTipo;
    @FXML
    private Button btnAñadir;
    @FXML
    private TableView<Receta> tabla;
    @FXML
    private TableColumn<Receta, String> cNombre;
    @FXML
    private TableColumn<Receta, String> cDuracion;
    @FXML
    private TableColumn<Receta, String> cDificultad;
    @FXML
    private TableColumn<Receta, String> cTipo;
    @FXML
    private Label info;
    @FXML
    private Label lblDuracion;
    @FXML
    private ImageView imagen;
    @FXML
    private MenuItem menuSalir;
    @FXML
    private MenuItem menuAcercaDe;
    @FXML
    private ComboBox<Receta> comboRecetas;
    @FXML
    private ToggleGroup dificultad;
    @FXML
    private ImageView carita;

    private MediaPlayer mediaPlayer;

    private ObservableList<Receta> recetas = FXCollections.observableArrayList();

    /**
     * Inicializa la interfaz de usuario y configura los elementos visuales y de control
     * para la aplicación de recetario. Carga recetas predeterminadas si la lista de recetas
     * en la sesión está vacía. Configura el manejo de eventos para interactuar con la interfaz
     * y visualiza detalles de las recetas seleccionadas.
     *
     * @param url            La ubicación del archivo FXML. No se utiliza en este contexto.
     * @param resourceBundle El ResourceBundle que se puede usar para localizar elementos.
     *                       No se utiliza en este contexto.
     */

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // Se comprueba si la lista de recetas en la sesión está vacía y se inicializa con recetas predeterminadas si es así
        if (Session.getRecetas().isEmpty()) {
            ArrayList<Receta> temp = new ArrayList<>();
            temp.add(new Receta("Tacos de carne asada", "Almuerzo", 45, "Fácil"));
            temp.add(new Receta("Huevos revueltos con tocino", "Desayuno", 15, "Moderada"));
            temp.add(new Receta("Sándwich de jamón y queso", "Merienda", 10, "Fácil"));
            temp.add(new Receta("Pollo a la parrilla con verduras", "Almuerzo", 60, "Moderada"));
            temp.add(new Receta("Avena con frutas", "Desayuno", 20, "Fácil"));
            temp.add(new Receta("Ensalada de atún", "Almuerzo", 30, "Moderada"));
            Session.setRecetas(temp);
        }

        // Se añaden todas las recetas de la sesión a la lista observable de recetas
        recetas.addAll(Session.getRecetas());

        // Se inicializa el reproductor de medios con un sonido de clic
        Media sonido = new Media(HelloApplication.class.getClassLoader().getResource("com/example/recetario/audio/clic.wav").toExternalForm());
        mediaPlayer = new MediaPlayer(sonido);

        // Se inicializa la lista observable de dificultades y se configura el combo box
        ObservableList<String> datos = FXCollections.observableArrayList();
        datos.addAll("Fácil", "Dificil", "Moderada");
        comboDificultad.setItems(datos);
        comboDificultad.getSelectionModel().selectFirst();

        // Se agrega un listener al combo box para cambiar la imagen y reproducir el sonido según la dificultad seleccionada
        comboDificultad.valueProperty().addListener(
                (observableValue, s, t1) -> {
                    String imagen = "neutral.png";
                    if (t1.equals("Fácil")) imagen = "feliz.png";
                    else if (t1.equals("Dificil")) imagen = "muerto.png";

                    carita.setImage(new Image("com/example/recetario/img/" + imagen));
                    mediaPlayer.seek(new Duration(0));
                    mediaPlayer.play();
                }
        );

        // Se configura el valor y el listener del slider de duración
        sliderDuracion.setValue(60);
        lblDuracion.setText(Math.round(sliderDuracion.getValue()) + " min");
        sliderDuracion.valueProperty().addListener((observableValue, number, t1) -> lblDuracion.setText(t1.intValue() + " min"));

        // Se configura el contenido y la selección inicial de la lista de tipos de recetas
        listTipo.getItems().addAll("Desayuno", "Segundo desayuno", "Almuerzo", "SobreAlmuerzo", "Merienda", "Cena", "Recena", "Postcena");
        listTipo.getSelectionModel().select(0);

        // Se añade un listener a la tabla para seleccionar y mostrar detalles de la receta seleccionada
        tabla.getSelectionModel().selectedItemProperty().addListener(
                (observable, vOld, vNew) -> seleccionarReceta(vNew)
        );

        // Se configuran las celdas de la tabla con sus respectivas fábricas de celdas
        cNombre.setCellValueFactory((fila) -> {
            String nombre = fila.getValue().getNombre().toUpperCase() + " " + fila.getValue().getTipo();
            return new SimpleStringProperty(nombre);
        });
        cDificultad.setCellValueFactory((fila) -> new SimpleStringProperty(fila.getValue().getDificultad()));
        cDuracion.setCellValueFactory((fila) -> new SimpleStringProperty(fila.getValue().getDuracion() + " min"));
        cTipo.setCellValueFactory((fila) -> new SimpleStringProperty(fila.getValue().getTipo()));

        // Se añade un segundo listener a la tabla para seleccionar y mostrar detalles de la receta seleccionada
        tabla.getSelectionModel().selectedItemProperty().addListener((observableValue, receta, t1) -> {
            seleccionarReceta(tabla.getSelectionModel().getSelectedItem());
        });

        // Se establece la lista observable de recetas como el conjunto de datos de la tabla
        tabla.setItems(recetas);

        comboRecetas.setConverter(new StringConverter<Receta>() {
            @Override
            public String toString(Receta receta) {
                // Convierte una Receta a una cadena para su visualización en el ComboBox
                if (receta != null) {
                    return receta.getNombre();
                } else {
                    return null;
                }
            }

            @Override
            public Receta fromString(String s) {
                // No se implementa la conversión inversa en este caso
                return null;
            }
        });

// Se establece la lista de recetas como el conjunto de datos del ComboBox
        comboRecetas.setItems(recetas);
    }


    /**
     * Método llamado al cambiar el valor del slider de duración.
     * Actualmente desactivado; no realiza ninguna acción.
     *
     * @param event El evento asociado al cambio del slider.
     */
    @FXML
    public void actualizarDuracion(Event event) {
        //lblDuracion.setText( Math.round(sliderDuracion.getValue()) + " min");
    }


    /**
     * Método llamado al hacer clic en algún lugar de la tabla.
     * Imprime la receta seleccionada en la consola.
     *
     * @param event El evento de clic.
     */
    // Método llamado al hacer clic en algún lugar de la tabla
    @FXML
    public void click(Event event) {
        // Imprime la receta seleccionada en la consola
        System.out.println(tabla.getSelectionModel().getSelectedItem());
    }



    /**
     * Método llamado al seleccionar la opción "Salir" en el menú.
     * Cierra la aplicación.
     *
     * @param actionEvent El evento de acción asociado al menú "Salir".
     */
    // Método llamado al seleccionar la opción "Salir" en el menú
    @FXML
    public void salir(ActionEvent actionEvent) {
        // Cierra la aplicación
        System.exit(0);
    }



    /**
     * Método llamado al seleccionar la opción "Acerca de" en el menú.
     * Muestra un cuadro de diálogo de información con detalles sobre el creador.
     *
     * @param actionEvent El evento de acción asociado al menú "Acerca de".
     */
    // Método llamado al seleccionar la opción "Acerca de" en el menú


    @FXML
    public void mostrarAcercaDe(ActionEvent actionEvent) {
        // Muestra un cuadro de diálogo de información con detalles sobre el creador
        var alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("El Creador");
        alert.setContentText("Francisco Romero");
        alert.showAndWait();
    }


    /**
     * Método llamado al seleccionar la opción "Mostrar Receta" en el menú.
     * Invoca el método para mostrar detalles de la receta seleccionada en otra ventana.
     *
     * @param actionEvent El evento de acción asociado al menú "Mostrar Receta".
     */
    // Método llamado al seleccionar la opción "Mostrar Receta" en el menú
    @FXML
    public void mostrarReceta(ActionEvent actionEvent) {
        // Invoca el método para mostrar detalles de la receta seleccionada en otra ventana
        seleccionarReceta(comboRecetas.getSelectionModel().getSelectedItem());
    }


    /**
     * Maneja la selección de una receta. Imprime la receta seleccionada en la consola,
     * actualiza la receta actual y la posición en la sesión, y carga una nueva ventana FXML.
     *
     * @param r La receta seleccionada.
     */
    // Método privado para manejar la selección de una receta

    private void seleccionarReceta(Receta r) {
        // Imprime la receta seleccionada en la consola
        System.out.println(r);

        // Actualiza la receta actual y la posición en la sesión
        Session.setRecetaActual(r);
        Session.setPos(tabla.getSelectionModel().getSelectedIndex());

        // Carga una nueva ventana FXML
        HelloApplication.loadFXML("ventanaSecundaria.fxml");
    }


    /**
     * Método llamado al seleccionar la opción "Insertar Receta" en el menú.
     * Verifica que el campo de nombre no esté vacío, crea una nueva receta con los datos ingresados,
     * agrega la receta a la sesión y a la lista observable de recetas, y muestra información sobre la receta añadida.
     *
     * @param actionEvent El evento de acción asociado al menú "Insertar Receta".
     */
    // Método llamado al seleccionar la opción "Insertar Receta" en el menú
    @FXML
    public void insertarReceta(ActionEvent actionEvent) {
        // Verifica que el campo de nombre no esté vacío
        if (!txtNombre.getText().isEmpty()) {
            // Crea una nueva receta con los datos ingresados
            Receta receta = new Receta();
            receta.setNombre(txtNombre.getText());
            receta.setTipo(listTipo.getSelectionModel().getSelectedItem());
            receta.setDuracion((int) sliderDuracion.getValue());
            receta.setDificultad(comboDificultad.getSelectionModel().getSelectedItem());

            // Agrega la receta a la sesión y a la lista observable de recetas
            Session.getRecetas().add(receta);
            recetas.add(receta);

            // Muestra información sobre la receta añadida
            info.setText(receta.toString());
        }
    }
}