package com.example.calculadora.reconocimiento_de_patrones;

import com.example.calculadora.Utils;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.opencv.core.Mat;

import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * Esta clase proporciona una interfaz gráfica para seleccionar una imagen, escanearla utilizando
 * el motor de reconocimiento óptico de caracteres (OCR) Tesseract y mostrar el texto detectado.
 */

public class escaneo extends Application {
    private ImageView imageView;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Selector de Captura");
        VBox vbox = new VBox();

        // Crear un visor de imágenes para mostrar la captura seleccionada
        imageView = new ImageView();
        imageView.setFitWidth(400);
        imageView.setFitHeight(300);

        // Crear un botón para seleccionar una captura
        Button seleccionarButton = new Button("Seleccionar Captura");
        seleccionarButton.setOnAction(event -> seleccionarCaptura(primaryStage));

        // Crear un botón para escanear la imagen
        Button escanearButton = new Button("Escanear");
        escanearButton.setOnAction(event -> escanearImagen());

        vbox.getChildren().addAll(imageView, seleccionarButton, escanearButton);
        Scene scene = new Scene(vbox, 400, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Abre un cuadro de diálogo para permitir al usuario seleccionar una imagen.
     * La imagen seleccionada se muestra en el visor de imágenes.
     * @param stage El escenario actual.
     */
    
    private void seleccionarCaptura(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg"));
        File archivo = fileChooser.showOpenDialog(stage);

        if (archivo != null) {
            // Cargar la imagen seleccionada en un objeto de tipo Image
            Image image = new Image(archivo.toURI().toString());

            // Mostrar la imagen en el ImageView
            imageView.setImage(image);
        }
    }

    /**
     * Escanea la imagen actualmente mostrada en el visor utilizando Tesseract OCR
     * y muestra el texto detectado en la consola.
     */
    
    private void escanearImagen() {
        // Obtener la imagen actual
        Image image = imageView.getImage();
        if (image == null) {
            System.out.println("No se ha seleccionado una imagen.");
            return;
        }

        // Convertir la imagen de JavaFX a una imagen OpenCV (Mat)
        Mat matImage = Utils.imageToMat(image);

        // Inicializar el motor Tesseract para OCR
        Tesseract tesseract = new Tesseract();
        tesseract.setLanguage("eng"); // Establecer el idioma (puedes cambiarlo según tus necesidades)

        try {
            // Realizar OCR en la imagen
            String resultado = tesseract.doOCR(Utils.matToBufferedImage(matImage));

            // Mostrar el texto detectado en la consola
            System.out.println("Texto detectado: " + resultado);
        } catch (TesseractException e) {
            e.printStackTrace();
            System.err.println("Error al realizar OCR: " + e.getMessage());
        }
    }

    /**
     * Abre la carpeta contenedora del archivo especificado.
     * @param carpeta La carpeta que se va a abrir.
     */

    private void abrirCarpetaContenedora(File carpeta) {
        if (Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().open(carpeta);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
