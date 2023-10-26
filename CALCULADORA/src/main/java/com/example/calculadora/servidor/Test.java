package com.example.calculadora.Servidor;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import java.io.File;

/**
 * A Hello World of Tess4j Example
 * @author whuang022ai
 *
 */

public class Test {
    public static void main(String[] args) {
        File imageFile = new File("C:\\Users\\gutie\\OneDrive\\Documentos\\GitHub\\Calculadora\\CALCULADORA\\src\\main\\resources\\imgs\\b.png");
        ITesseract instance = new Tesseract();  // JNA Interface Mapping
        // ITesseract instance = new Tesseract1(); // JNA Direct Mapping
        instance.setDatapath("C:\\Tess4J\\tessdata"); // path to tessdata directory

        try {
            String result = instance.doOCR(imageFile);
            System.out.println(result);
        } catch (TesseractException e) {
            System.err.println(e.getMessage());
        }
    }
}