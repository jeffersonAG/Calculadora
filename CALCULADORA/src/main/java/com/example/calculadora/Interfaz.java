package com.example.calculadora;

import com.example.calculadora.Arbol.ArbolBinario;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Interfaz extends Application {

    private TextField textField;
    private ArbolBinario arbol;

    @Override
    public void start(Stage primaryStage) {
        arbol = new ArbolBinario();

        textField = new TextField();
        textField.setPromptText("0");
        textField.setFont(Font.font(18));
        textField.setEditable(false);

        GridPane botones = new GridPane();
        botones.setAlignment(Pos.CENTER);
        botones.setHgap(10);
        botones.setVgap(10);

        // Crear botones numéricos, de operadores, paréntesis, Borrar y "="
        String[] teclas = {
                "C", "(", ")", "/",
                "7", "8", "9", "*",
                "4", "5", "6", "-",
                "1", "2", "3", "+",
                "0", "←", "="
        };

        int row = 0;
        int col = 0;

        for (String tecla : teclas) {
            Button boton = new Button(tecla);
            boton.setMinSize(50, 50);
            boton.setFont(Font.font(18));
            boton.setOnAction(e -> procesarTecla(tecla));
            botones.add(boton, col, row);

            col++;
            if (col == 4) {
                col = 0;
                row++;
            }
        }

        VBox root = new VBox(10);
        root.setAlignment(Pos.CENTER);
        root.getChildren().addAll(textField, botones);

        Scene scene = new Scene(root, 300, 350);
        primaryStage.setTitle("Calculadora de Árbol de Expresión");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void procesarTecla(String tecla) {
        if (tecla.equals("C")) {
            textField.setText("");
        } else if (tecla.equals("=")) {
            String expresion = textField.getText();
            arbol.construirArbol(expresion);

            try {
                int resultado = arbol.evaluar();
                textField.setText(String.valueOf(resultado));
            } catch (Exception e) {
                textField.setText("Error");
            }
        } else if (tecla.equals("←")) { // Botón de Borrar
            String textoActual = textField.getText();
            if (!textoActual.isEmpty()) {
                textField.setText(textoActual.substring(0, textoActual.length() - 1));
            }
        } else {
            textField.appendText(tecla);
        }
    }
}
