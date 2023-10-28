 package com.example.calculadora.reconocimiento_de_patrones;

import com.example.calculadora.Utils;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;

import java.io.File;

/**
 * Esta clase proporciona una interfaz gráfica para capturar y mostrar un flujo de cámara en vivo,
 * tomar capturas de pantalla de la cámara y analizar el texto en las capturas utilizando Tesseract OCR.
 */

public class ReconocimientoFacial extends Application {

    private ImageView imageView;
    private VideoCapture videoCapture;
    private Text detectedText; // Texto detectado
    private Mat frame; // Variable de instancia para el fotograma
    private int captureCount = 1; // Inicializar un contador

    /**
     * Constructor de la clase ReconocimientoFacial.
     * Carga la biblioteca OpenCV al instanciar la clase.
     */
    
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

        // Crear un VBox para organizar los elementos en la interfaz gráfica.
        VBox vbox = new VBox();
        detectedText = new Text("Texto detectado: ");

        // Botón para capturar pantalla
        Button captureButton = new Button("Capturar");
        captureButton.setOnAction(event -> capturarPantalla());

        // Botón para abrir la ventana de selección de captura
        Button openSelectorButton = new Button("escanear");
        openSelectorButton.setOnAction(event -> abrirescaneo(primaryStage));

        vbox.getChildren().addAll(detectedText, captureButton, openSelectorButton);

        // Mostrar la ventana principal de la aplicación.
        StackPane mainPane = new StackPane(root);
        mainPane.getChildren().add(vbox);

        Scene scene = new Scene(mainPane, frameWidth, frameHeight);
        primaryStage.setScene(scene);

        // Capturar y mostrar el flujo de la cámara en un hilo separado para no bloquear la interfaz de usuario.
        Thread captureThread = new Thread(this::captureAndShowVideo);
        captureThread.setDaemon(true); // Hacer que el hilo sea un hilo de fondo que se cierre cuando se cierre la aplicación.
        captureThread.start();

        primaryStage.show();
    }

    // Método para capturar y mostrar el flujo de la cámara.
    private void captureAndShowVideo() {
        frame = new Mat(); // Inicializar el fotograma

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

    // 
    
    /**
     * Método para capturar pantalla completa y analizar el texto en la captura utilizando Tesseract OCR.
     */
    private void capturarPantalla() {
        if (frame != null) {
            String userDir = System.getProperty("user.dir"); // Obtener la ruta del directorio de usuario
            String outputPath = userDir + "/src/main/java/com/example/calculadora/capturas/captura" + captureCount + ".png";

            // Guardar toda la captura de la cámara como un archivo de imagen
            File outputFile = new File(outputPath);
            Imgcodecs.imwrite(outputFile.getAbsolutePath(), frame);

            // Analizar el texto en la captura utilizando Tesseract OCR
            Tesseract tesseract = new Tesseract();
            tesseract.setDatapath("\"C:\\Program Files\\Tesseract-OCR\\tessdata\""); // Reemplaza con la ruta correcta a la carpeta "tessdata" de Tesseract

            try {
                String resultado = tesseract.doOCR(outputFile);

                // Mostrar el texto detectado
                Platform.runLater(() -> detectedText.setText("Texto detectado en captura " + captureCount + ": " + resultado));

                // Incrementar el contador para el próximo archivo
                captureCount++;
            } catch (TesseractException e) {
                e.printStackTrace();
            }
        }

    }
    /**
     * Método para abrir la ventana de selección de captura escaneo.
     * @param primaryStage El escenario principal de la aplicación.
     */

    private void abrirescaneo(Stage primaryStage) {
        escaneo escaneo = new escaneo();
        escaneo.start(new Stage());
    }

    public static void main(String[] args) {
        launch(args);
    }
}
