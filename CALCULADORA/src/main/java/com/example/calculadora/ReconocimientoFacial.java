package com.example.calculadora;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ReconocimientoFacial extends Application {

    private ImageView imageView;
    private VideoCapture videoCapture;

    public static void main(String[] args) {
        // Cargar la biblioteca OpenCV (Open Source Computer Vision Library).
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        // Iniciar la aplicación JavaFX.
        launch(args);
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

        // Configurar la ventana de JavaFX.
        primaryStage.setTitle("Aplicación de Cámara");
        StackPane root = new StackPane();
        imageView = new ImageView();
        root.getChildren().add(imageView);
        Scene scene = new Scene(root, 800, 600); // Crear una escena con dimensiones predefinidas.
        primaryStage.setScene(scene);

        // Crear un VBox para organizar el botón en la esquina inferior derecha.
        VBox buttonContainer = new VBox();
        buttonContainer.setAlignment(Pos.BOTTOM_RIGHT); // Alinear el VBox en la esquina inferior derecha.

        // Agregar un botón de detención para cerrar la aplicación.
        Button stopButton = new Button("Detener");
        stopButton.setOnAction(event -> stopApplication(primaryStage));
        buttonContainer.getChildren().add(stopButton);

        // Agregar el VBox al StackPane principal.
        root.getChildren().add(buttonContainer);

        // Mostrar la ventana principal de la aplicación.
        primaryStage.show();

        // Capturar y mostrar el flujo de la cámara en un hilo separado para no bloquear la interfaz de usuario.
        Thread captureThread = new Thread(this::captureAndShowVideo);
        captureThread.setDaemon(true); // Hacer que el hilo sea un hilo de fondo que se cierre cuando se cierre la aplicación.
        captureThread.start();
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
        }
        // Liberar los recursos de la cámara cuando se sale del bucle.
        videoCapture.release();
    }

    // Método para detener la aplicación.
    private void stopApplication(Stage primaryStage) {
        // Liberar los recursos de la cámara y cerrar la ventana principal de JavaFX.
        videoCapture.release();
        primaryStage.close();
    }
}
