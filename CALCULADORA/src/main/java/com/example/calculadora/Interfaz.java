package com.example.calculadora;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Interfaz extends Application {
    private TextField pantalla;
    private String operador;
    private double numero1;
    private boolean nuevaOperacion = true;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Calculadora JavaFX");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(5);
        grid.setHgap(5);
        grid.setStyle("-fx-background-color: black;");

        pantalla = new TextField();
        pantalla.setPrefHeight(50);
        pantalla.setPrefWidth(200);
        pantalla.setEditable(false);
        BackgroundFill backgroundFill = new BackgroundFill(Color.BLACK, new CornerRadii(5), Insets.EMPTY);
        Background background = new Background(backgroundFill);
        pantalla.setBackground(background);
        pantalla.setStyle(
                "-fx-background-color: black; -fx-text-fill: #800080; " +
                        "-fx-border-color: #800080; -fx-border-width: 2px;"
        );
        grid.add(pantalla, 0, 0, 4, 1);

        Button[] botones = new Button[18];
        String[] nombresBotones = {
                "7", "8", "9", "/",
                "4", "5", "6", "*",
                "1", "2", "3", "-",
                "0", "AC", "=", "+",
                "CAM", "DEL"
        };

        for (int i = 0; i < 18; i++) {
            botones[i] = new Button(nombresBotones[i]);
            botones[i].setPrefSize(50, 50);
            BackgroundFill buttonBackgroundFill = new BackgroundFill(Color.BLACK, new CornerRadii(5), Insets.EMPTY);
            Background buttonBackground = new Background(buttonBackgroundFill);
            botones[i].setBackground(buttonBackground);
            botones[i].setStyle(
                    "-fx-background-color: black; -fx-text-fill: #800080; " +
                            "-fx-border-color: #800080; -fx-border-width: 2px;"
            );

            // Asigna el método correspondiente a cada botón
            int finalI = i;
            botones[i].setOnAction(e -> botonPresionado(nombresBotones[finalI]));
        }

        // Asigna los métodos específicos a cada botón
        botones[0].setOnAction(e -> botonNumero("7"));
        botones[1].setOnAction(e -> botonNumero("8"));
        botones[2].setOnAction(e -> botonNumero("9"));
        botones[3].setOnAction(e -> botonOperador("/"));
        botones[4].setOnAction(e -> botonNumero("4"));
        botones[5].setOnAction(e -> botonNumero("5"));
        botones[6].setOnAction(e -> botonNumero("6"));
        botones[7].setOnAction(e -> botonOperador("*"));
        botones[8].setOnAction(e -> botonNumero("1"));
        botones[9].setOnAction(e -> botonNumero("2"));
        botones[10].setOnAction(e -> botonNumero("3"));
        botones[11].setOnAction(e -> botonOperador("-"));
        botones[12].setOnAction(e -> botonNumero("0"));
        botones[13].setOnAction(e -> pantalla.clear());
        botones[14].setOnAction(e -> calcularResultado());
        botones[15].setOnAction(e -> botonOperador("+"));

        // Asigna el método abrirReconocimientoFacial al botón "CAM"
        botones[16].setOnAction(e -> abrirReconocimientoFacial());

        botones[17].setOnAction(e -> borrarCaracter());

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                grid.add(botones[i * 4 + j], j, i + 1);
            }
        }

        grid.add(botones[16], 0, 5, 2, 1);
        grid.add(botones[17], 2, 5, 2, 1);

        Scene scene = new Scene(grid, 210, 300, Color.BLACK);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void botonNumero(String digito) {
        if (nuevaOperacion) {
            pantalla.clear();
            nuevaOperacion = false;
        }
        pantalla.appendText(digito);
    }

    private void botonOperador(String op) {
        if (!nuevaOperacion) {
            calcularResultado();
            numero1 = Double.parseDouble(pantalla.getText());
            operador = op;
            nuevaOperacion = true;
        }
    }

    private void calcularResultado() {
        if (!nuevaOperacion) {
            double numero2 = Double.parseDouble(pantalla.getText());
            double resultado = 0;
            switch (operador) {
                case "+":
                    resultado = numero1 + numero2;
                    break;
                case "-":
                    resultado = numero1 - numero2;
                    break;
                case "*":
                    resultado = numero1 * numero2;
                    break;
                case "/":
                    resultado = numero1 / numero2;
                    break;
            }
            pantalla.setText(String.valueOf(resultado));
            nuevaOperacion = true;
        }
    }

    private void abrirReconocimientoFacial() {
        try {
            // Crear la instancia de ReconocimientoFacial y llamar a su método start

            ReconocimientoFacial reconocimientoFacial = new ReconocimientoFacial();
            Stage stage = new Stage();
            reconocimientoFacial.start(stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void borrarCaracter() {
        String contenido = pantalla.getText();
        if (!contenido.isEmpty()) {
            pantalla.setText(contenido.substring(0, contenido.length() - 1));
        }
    }

    private void botonPresionado(String digito) {
        if (nuevaOperacion) {
            pantalla.clear();
            nuevaOperacion = false;
        }

        if (digito.equals("AC")) {
            pantalla.clear();
        } else if (digito.equals("DEL")) {
            borrarCaracter();
        } else {
            pantalla.appendText(digito);
        }
    }
}
