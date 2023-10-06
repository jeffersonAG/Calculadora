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

            if (nombresBotones[i].equals("CAM")) {
                botones[i].setText("CAM");
                // Agregar el controlador de eventos para el botón "CAM"
                botones[i].setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        // Aquí puedes iniciar la clase ReconocimientoFacial
                        ReconocimientoFacial.main(new String[] {});
                    }
                });
            }
        }

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                grid.add(botones[i * 4 + j], j, i + 1);
            }
        }

        grid.add(botones[16], 0, 5, 2, 1);
        grid.add(botones[17], 2, 5, 2, 1);

        for (int i = 0; i < 18; i++) {
            final int j = i;
            botones[i].setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    botonPresionado(nombresBotones[j]);
                }
            });
        }

        Scene scene = new Scene(grid, 210, 300, Color.BLACK);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void botonPresionado(String digito) {
        if (nuevaOperacion) {
            pantalla.setText("");
            nuevaOperacion = false;
        }

        if (digito.equals("AC")) {
            pantalla.clear();
        } else if (digito.equals("DEL")) {
            String contenido = pantalla.getText();
            if (!contenido.isEmpty()) {
                pantalla.setText(contenido.substring(0, contenido.length() - 1));
            }
        } else {
            pantalla.appendText(digito);
        }
    }
}
