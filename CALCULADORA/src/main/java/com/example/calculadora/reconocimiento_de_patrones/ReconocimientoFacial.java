package com.example.calculadora.reconocimiento_de_patrones;

import com.example.calculadora.Utils;
import com.example.calculadora.interfaz.Interfaz;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;
public class ReconocimientoFacial extends Application {

    private ImageView imageView;
    private VideoCapture videoCapture;

    public ReconocimientoFacial() {
        // Cargar la biblioteca OpenCV (Open Source Computer Vision Library).
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    @Override
    public void start(Stage primaryStage) {
        // Inicializar la cámara utilizando OpenCV.
        videoCapture = new VideoCapture(0);

        if (!videoCapture.isOpened()) {
            System.out.println("No se pudo abrir la cámara.");
            Platform.exit(); // Cerrar la aplicación de JavaFX si la cámara no se pudo abrir.
            return;
        }

        // Obtener la resolución de la cámara.
        int frameWidth = (int) videoCapture.get(Videoio.CAP_PROP_FRAME_WIDTH);
        int frameHeight = (int) videoCapture.get(Videoio.CAP_PROP_FRAME_HEIGHT);

        // Configurar la ventana de JavaFX para que coincida con la resolución de la cámara.
        primaryStage.setTitle("Aplicación de Cámara");
        StackPane root = new StackPane();
        imageView = new ImageView();
        imageView.setPreserveRatio(true); // Mantener la relación de aspecto de la imagen.
        imageView.setFitWidth(frameWidth); // Establecer el ancho del ImageView según la resolución de la cámara.
        imageView.setFitHeight(frameHeight); // Establecer la altura del ImageView según la resolución de la cámara.
        root.getChildren().add(imageView);

        // Crear un VBox para organizar el botón en la esquina inferior derecha.
        VBox buttonContainer = new VBox();
        buttonContainer.setStyle("-fx-alignment: bottom-right;"); // Alinear el VBox en la esquina inferior derecha.
        Button stopButton = new Button("VOLVER");
        stopButton.setOnAction(event -> {
            // Detener la captura de video si es necesario
            videoCapture.release();

            // Cerrar la ventana actual de ReconocimientoFacial
            primaryStage.close();

            // Crear una nueva instancia de la clase Interfaz
            Interfaz interfaz = new Interfaz();
            Stage newStage = new Stage();
            interfaz.start(newStage);
        });
        buttonContainer.getChildren().add(stopButton);

        // Agregar el VBox al StackPane principal.
        root.getChildren().addAll(buttonContainer);

        // Mostrar la ventana principal de la aplicación.
        Scene scene = new Scene(root, frameWidth, frameHeight); // Tamaño inicial de la ventana.
        primaryStage.setScene(scene);

        // Capturar y mostrar el flujo de la cámara en un hilo separado para no bloquear la interfaz de usuario.
        Thread captureThread = new Thread(this::captureAndShowVideo);
        captureThread.setDaemon(true); // Hacer que el hilo sea un hilo de fondo que se cierre cuando se cierre la aplicación.
        captureThread.start();

        primaryStage.show();
    }

    // Método para capturar y mostrar el flujo de la cámara.
    private void captureAndShowVideo() {
        Mat frame = new Mat();

        while (true) {
            // Leer un fotograma desde la cámara.
            if (!videoCapture.read(frame)) {
                break; // Salir del bucle si no se pudo leer un fotograma.
            }

            // Convertir el fotograma en una imagen de JavaFX y mostrarlo en el ImageView.
            Image image = Utils.mat2Image(frame);
            imageView.setImage(image);

            // Realizar el procesamiento de imagen para la detección de texto aquí
            detectarTexto(frame);

            // Mostrar la imagen con la detección de texto
            Image resultImage = Utils.mat2Image(frame);
            imageView.setImage(resultImage);
        }

        // Liberar los recursos de la cámara cuando se sale del bucle.
        videoCapture.release();
    }

    // Método para detectar texto en un fotograma
    private void detectarTexto(Mat frame) {
        // Implementa tu lógica de detección de texto aquí.
        // Puedes utilizar técnicas de procesamiento de imágenes para identificar regiones de texto.
        // Por ejemplo, puedes buscar contornos de texto o aplicar filtros para resaltar el texto.

        // En este ejemplo, simplemente dibujaremos un cuadro alrededor de una región de ejemplo.
        Imgproc.rectangle(frame, new Point(100, 100), new Point(300, 200), new Scalar(0, 255, 0), 2);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
