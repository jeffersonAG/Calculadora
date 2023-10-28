package com.example.calculadora;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * Clase de utilidad para convertir entre tipos de imágenes y matrices OpenCV.
 */

public class Utils {
    /**
     * Convierte una imagen JavaFX en una matriz OpenCV `Mat`.
     * @param image La imagen JavaFX a convertir.
     * @return La matriz OpenCV `Mat` que representa la imagen.
     */
	
    public static Mat imageToMat(Image image) {
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();

        Mat mat = new Mat(height, width, CvType.CV_8UC3);
        PixelReader pixelReader = image.getPixelReader();

        byte[] buffer = new byte[3 * width];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color color = pixelReader.getColor(x, y);
                buffer[3 * x] = (byte) (color.getBlue() * 255);
                buffer[3 * x + 1] = (byte) (color.getGreen() * 255);
                buffer[3 * x + 2] = (byte) (color.getRed() * 255);
            }
            mat.put(y, 0, buffer);
        }

        return mat;
    }
    
    /**
     * Convierte una matriz OpenCV `Mat` en una imagen JavaFX.
     * @param mat La matriz OpenCV `Mat` a convertir en imagen.
     * @return La imagen JavaFX generada a partir de la matriz.
     */
    
    private Image matToJavaFXImage(Mat mat) {
        int width = mat.width();
        int height = mat.height();
        int channels = mat.channels();
        byte[] buffer = new byte[width * height * channels];
        mat.get(0, 0, buffer);

        WritableImage writableImage = new WritableImage(width, height);
        PixelWriter pixelWriter = writableImage.getPixelWriter();

        int pixelIndex = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int color = 0;
                color |= 0xFF << 24;  // Alpha
                for (int c = 0; c < channels; c++) {
                    color |= (buffer[pixelIndex] & 0xFF) << (8 * (2 - c)); // Blue, Green, Red
                    pixelIndex++;
                }
                pixelWriter.setArgb(x, y, color);
            }
        }

        return writableImage;
    }

    /**
     * Convierte una matriz OpenCV `Mat` en una imagen BufferedImage.
     * @param mat La matriz OpenCV `Mat` a convertir en imagen.
     * @return La imagen BufferedImage generada a partir de la matriz.
     */
    
    public static BufferedImage matToBufferedImage(Mat mat) {
        MatOfByte mob = new MatOfByte();
        Imgcodecs.imencode(".jpg", mat, mob);
        byte[] byteArray = mob.toArray();
        BufferedImage bufImage = null;
        try {
            bufImage = ImageIO.read(new ByteArrayInputStream(byteArray));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bufImage;
    }
    
    /**
     * Convierte una matriz OpenCV `Mat` en una imagen JavaFX.
     * @param mat La matriz OpenCV `Mat` a convertir en imagen.
     * @return La imagen JavaFX generada a partir de la matriz.
     */
    
    public static Image mat2Image(Mat mat) {
        MatOfByte buffer = new MatOfByte();
        Imgcodecs.imencode(".png", mat, buffer);
        return new Image(new ByteArrayInputStream(buffer.toArray()));
    }

}
