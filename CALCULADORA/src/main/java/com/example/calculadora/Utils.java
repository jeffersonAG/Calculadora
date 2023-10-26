package com.example.calculadora;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.paint.Color;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;

import java.io.ByteArrayInputStream;

public class Utils {
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
    public static Image mat2Image(Mat mat) {
        MatOfByte buffer = new MatOfByte();
        Imgcodecs.imencode(".png", mat, buffer);
        return new Image(new ByteArrayInputStream(buffer.toArray()));
    }

}
