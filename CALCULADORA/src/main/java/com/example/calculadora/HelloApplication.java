package com.example.calculadora;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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

public class HelloApplication extends Application {
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

        Button[] botones = new Button[16];
        String[] nombresBotones = {
                "7", "8", "9", "/",
                "4", "5", "6", "*",
                "1", "2", "3", "-",
                "0", "C", "=", "+"
        };

        for (int i = 0; i < 16; i++) {
            botones[i] = new Button(nombresBotones[i]);
            botones[i].setPrefSize(50, 50);
            BackgroundFill buttonBackgroundFill = new BackgroundFill(Color.BLACK, new CornerRadii(5), Insets.EMPTY);
            Background buttonBackground = new Background(buttonBackgroundFill);
            botones[i].setBackground(buttonBackground);
            botones[i].setStyle(
                    "-fx-background-color: black; -fx-text-fill: #800080; " +
                            "-fx-border-color: #800080; -fx-border-width: 2px;"
            );
        }

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                grid.add(botones[i * 4 + j], j, i + 1);
            }
        }

        for (int i = 0; i < 16; i++) {
            final int j = i;
            botones[i].setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    botonPresionado(nombresBotones[j]);
                }
            });
        }

        Scene scene = new Scene(grid, 210, 250, Color.BLACK);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void botonPresionado(String digito) {
        if (nuevaOperacion) {
            pantalla.setText("");
            nuevaOperacion = false;
        }
        pantalla.appendText(digito);
    }
}
