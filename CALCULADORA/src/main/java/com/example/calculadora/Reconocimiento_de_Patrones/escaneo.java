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
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.awt.*;
import java.io.File;
import java.io.IOException;

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

    private void seleccionarCaptura(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg"));
        File archivo = fileChooser.showOpenDialog(stage);

        if (archivo != null) {
            // Cargar la imagen seleccionada en un objeto de tipo Image
            Image image = new Image(archivo.toURI().toString());

            // Convertir el Image a una imagen OpenCV (Mat)
            Mat matImage = Utils.imageToMat(image);

            // Aquí puedes usar matImage para realizar el escaneo de la imagen
            // Puedes llamar a tus métodos de procesamiento de imágenes OpenCV aquí
        }
    }


    private void escanearImagen() {
        // Obtener la imagen actual
        Image image = imageView.getImage();
        if (image == null) {
            return;
        }

        // Convertir la imagen de JavaFX a una imagen OpenCV (Mat)
        Mat matImage = Utils.imageToMat(image);


        // Aplicar preprocesamiento de imágenes (por ejemplo, escala de grises y umbralización)
        Imgproc.cvtColor(matImage, matImage, Imgproc.COLOR_BGR2GRAY);
        Imgproc.threshold(matImage, matImage, 0, 255, Imgproc.THRESH_BINARY | Imgproc.THRESH_OTSU);

        // Guardar la imagen preprocesada temporalmente
        File tempFile = new File("temp.png");
        Imgcodecs.imwrite(tempFile.getAbsolutePath(), matImage);

        // Realizar OCR en la imagen preprocesada
        Tesseract tesseract = new Tesseract();
        try {
            String resultado = tesseract.doOCR(tempFile);
            System.out.println("Texto detectado: " + resultado);

            // Aquí puedes agregar la lógica para detectar si el texto contiene una operación matemática
            // Por ejemplo, puedes usar expresiones regulares o procesamiento de texto.

        } catch (TesseractException e) {
            e.printStackTrace();
        } finally {
            // Eliminar el archivo temporal
            tempFile.delete();
        }
    }


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
