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
        // Configuración de la ventana principal
        primaryStage.setTitle("Calculadora JavaFX");

        // Creación del diseño de la interfaz utilizando GridPane
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10)); // Margen interior del grid
        grid.setVgap(5); // Espacio vertical entre celdas
        grid.setHgap(5); // Espacio horizontal entre celdas
        grid.setStyle("-fx-background-color: black;"); // Establecer fondo negro

        // Creación del campo de texto para mostrar y editar la entrada/salida
        pantalla = new TextField();
        pantalla.setPrefHeight(50);
        pantalla.setPrefWidth(200);
        pantalla.setEditable(false); // No editable
        BackgroundFill backgroundFill = new BackgroundFill(Color.BLACK, new CornerRadii(5), Insets.EMPTY);
        Background background = new Background(backgroundFill);
        pantalla.setBackground(background);
        pantalla.setStyle(
                "-fx-background-color: black; -fx-text-fill: #800080; " +
                        "-fx-border-color: #800080; -fx-border-width: 2px;"
        );
        grid.add(pantalla, 0, 0, 4, 1); // Agregar el campo de texto al grid

        // Creación de botones
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
            botones[i].setPrefSize(50, 50); // Tamaño del botón
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

        // Agregar botones al grid
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                grid.add(botones[i * 4 + j], j, i + 1);
            }
        }

        // Colocar el botón "CAM" en las celdas adecuadas
        grid.add(botones[16], 0, 5, 2, 1);
        grid.add(botones[17], 2, 5, 2, 1);

        // Crear la escena principal
        Scene scene = new Scene(grid, 210, 300, Color.BLACK); // Tamaño de la ventana
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Métodos de operación de la calculadora

    private void botonNumero(String digito) {
        // Verificar si se ha iniciado una nueva operación
        if (nuevaOperacion) {
            // Limpiar el campo de texto y marcar que no es una nueva operación
            pantalla.clear();
            nuevaOperacion = false;
        }
        // Agregar el dígito al campo de texto
        pantalla.appendText(digito);
    }

    private void botonOperador(String op) {
        // Verificar si no es una nueva operación
        if (!nuevaOperacion) {
            // Calcular el resultado si ya se ingresó un operador y otro número
            calcularResultado();
            // Almacenar el número actual en 'numero1' y el operador en 'operador'
            numero1 = Double.parseDouble(pantalla.getText());
            operador = op;
            // Marcar que se ha iniciado una nueva operación
            nuevaOperacion = true;
        }
    }

    private void calcularResultado() {
        // Verificar si no es una nueva operación
        if (!nuevaOperacion) {
            // Obtener el segundo número del campo de texto
            double numero2 = Double.parseDouble(pantalla.getText());
            double resultado = 0;
            // Realizar la operación adecuada según el operador almacenado en 'operador'
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
                    // Verificar la división por cero antes de realizar la operación
                    if (numero2 != 0) {
                        resultado = numero1 / numero2;
                    } else {
                        // Manejar el caso de división por cero mostrando un mensaje de error
                        pantalla.setText("Error: División por cero");
                        nuevaOperacion = true; // Marcar que se ha iniciado una nueva operación
                        return; // Salir de la función sin calcular el resultado
                    }
                    break;
            }
            // Mostrar el resultado en el campo de texto y marcar que se ha iniciado una nueva operación
            pantalla.setText(String.valueOf(resultado));
            nuevaOperacion = true;
        }
    }


    // Método para abrir la ventana de ReconocimientoFacial
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
        // Obtener el contenido actual del campo de texto
        String contenido = pantalla.getText();

        // Verificar si el campo de texto no está vacío
        if (!contenido.isEmpty()) {
            // Eliminar el último carácter del contenido
            pantalla.setText(contenido.substring(0, contenido.length() - 1));
        }
    }

    private void botonPresionado(String digito) {
        // Verificar si se ha iniciado una nueva operación
        if (nuevaOperacion) {
            // Limpiar el campo de texto y marcar que no es una nueva operación
            pantalla.clear();
            nuevaOperacion = false;
        }

        // Comprobar qué botón se ha presionado
        if (digito.equals("AC")) {
            // Si es el botón "AC" (borrar todo), limpiar el campo de texto
            pantalla.clear();
        } else if (digito.equals("DEL")) {
            // Si es el botón "DEL" (borrar un carácter), llamar a la función borrarCaracter()
            borrarCaracter();
        } else {
            // Si es cualquier otro dígito o operador, agregarlo al campo de texto
            pantalla.appendText(digito);
        }
    }}

