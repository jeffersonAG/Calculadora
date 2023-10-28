package com.example.calculadora.interfaz;

/**
 * Clase que representa la interfaz de una calculadora de expresiones matemáticas
 * utilizando un árbol binario para evaluar las expresiones.
 */

import com.example.calculadora.Arbol.ArbolBinario;
import com.example.calculadora.Arbol.ArbolCompuertas;
import com.example.calculadora.CSV.Registro;
import com.example.calculadora.Servidor.servidor;
import com.example.calculadora.reconocimiento_de_patrones.ReconocimientoFacial;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.UUID;

public class Interfaz extends Application {

    private TextField textField;
    private ArbolBinario arbol;
    private Stage primaryStage;
    private String clienteID;
    private Registro archivo;
    
    /**
     * Método principal que inicia la aplicación.
     * @param primaryStage El escenario principal de la aplicación.
     */

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        archivo= new Registro(null);
        arbol = new ArbolBinario();
        
        //Genera idenficador unico para el cliente
        clienteID = UUID.randomUUID().toString();
        


        textField = new TextField();
        textField.setPromptText("0");
        textField.setFont(Font.font(18));
        textField.setEditable(false);

        GridPane botones = new GridPane();
        botones.setAlignment(Pos.CENTER);
        botones.setHgap(10);
        botones.setVgap(10);

        String[] teclas = {
                "C", "(", ")", "/", "%",
                "7", "8", "9", "*",
                "4", "5", "6", "-",
                "1", "2", "3", "+",
                "0", "←", "=", "^", "CAM", "Server", "BIN",
        };

        int row = 0;
        int col = 0;

        for (String tecla : teclas) {
            Button boton = new Button(tecla);
            if (tecla.equals("BIN")) {
                boton.setMinSize(50, 50);
                boton.setFont(Font.font(18));
                boton.setOnAction(e -> irACompuertas());
                botones.add(boton, col, row);
            } else if (tecla.equals("CAM")) {
                boton.setMinSize(50, 50);
                boton.setFont(Font.font(18));
                boton.setOnAction(e -> abrirReconocimientoFacial());
                botones.add(boton, col, row);
            } else if (tecla.equals("Server")) {
                boton.setMinSize(50, 50);
                boton.setFont(Font.font(18));
                boton.setOnAction(e -> iniciarServidor());
                botones.add(boton, col, row);
            }else {
                boton.setMinSize(50, 50);
                boton.setFont(Font.font(18));
                boton.setOnAction(e -> procesarTecla(tecla));
                botones.add(boton, col, row);
            }
            col++;
            if (col == 4) {
                col = 0;
                row++;
            }
        }

        VBox root = new VBox(10);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: black;");
        root.getChildren().addAll(textField, botones);

        Scene scene = new Scene(root, 350, 400);
        primaryStage.setTitle("Calculadora de Árbol de Expresión");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Método para abrir la ventana de reconocimiento facial.
     */
    
    private void abrirReconocimientoFacial() {
        // Código para abrir la ventana de reconocimiento facial
        ReconocimientoFacial reconocimientoFacialApp = new ReconocimientoFacial();
        Stage reconocimientoStage = new Stage();
        reconocimientoFacialApp.start(reconocimientoStage);
    }

    /**
     * Método principal que inicia la aplicación.
     * @param args Los argumentos de la línea de comandos.
     */
    
    public static void main(String[] args) {
        launch(args);
    }

    private void procesarTecla(String tecla) {
        if (tecla.equals("C")) {
            textField.setText("");
        } else if (tecla.equals("=")) {
        	 String expresion = clienteID + "," + "MATH: " + textField.getText();

            // Establece una conexión con el servidor
            try (Socket socket = new Socket("localhost", 12345)) {
                ObjectOutputStream salida = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream entrada = new ObjectInputStream(socket.getInputStream());

                // Envía la operación matemática al servidor
                salida.writeObject(expresion);

                // Recibe el resultado del servidor
                int resultado = (int) entrada.readObject();
                textField.setText(String.valueOf(resultado));
            } catch (Exception e) {
                textField.setText("Error al conectar con el servidor");
                e.printStackTrace();
            }
        } else if (tecla.equals("←")) {
            String textoActual = textField.getText();
            if (!textoActual.isEmpty()) {
                textField.setText(textoActual.substring(0, textoActual.length() - 1));
            }
        
        }else {
            textField.appendText(tecla);
        }
    }

    private void irACompuertas() {
        Stage stage = (Stage) textField.getScene().getWindow();
        stage.close();

        Compuertas segundaVentana = new Compuertas(primaryStage);
        segundaVentana.show();
    }

    private void iniciarServidor() {
        // Código para iniciar el servidor
        Thread serverThread = new Thread(() -> {
            servidor.main(null);
        });
        serverThread.setDaemon(true);
        serverThread.start();
    }
    

    /**
     * Obtiene el ID único del cliente.
     * @return El ID único del cliente.
     */
    
    public String getClienteID() {
		return clienteID;
	}
    
    /**
     * Clase que representa la interfaz de la calculadora de compuertas lógicas.
     */

	public class Compuertas extends Stage {
        private ArbolCompuertas tree;
        private Interfaz interfaz;

        /**
         * Constructor de la clase Compuertas.
         * @param Interfaz La instancia de la clase Interfaz que contiene la calculadora principal.
         */
        
        public Compuertas(Stage Interfaz) {
        	this.interfaz=interfaz;
            tree = new ArbolCompuertas();
            VBox root1 = new VBox(10);
            root1.setAlignment(Pos.CENTER);
            root1.setStyle("-fx-background-color: black;");
            textField = new TextField();
            textField.setPromptText("0");
            textField.setFont(Font.font(18));
            textField.setEditable(false);

            GridPane botones = new GridPane();
            botones.setAlignment(Pos.CENTER);
            botones.setHgap(10);
            botones.setVgap(10);

            String[] teclas = {
                    "C", "(", ")", "AND",
                    "7", "8", "9", ".OR",
                    "4", "5", "6", "XOR",
                    "1", "2", "3", "NOT",
                    "0", "←", "=", "DEC", "CAM", "Server",
            };

            int row = 0;
            int col = 0;

            for (String tecla : teclas) {
                Button boton = new Button(tecla);
                if (tecla.equals("DEC")) {
                    boton.setMinSize(50, 50);
                    boton.setFont(Font.font(18));
                    boton.setOnAction(e -> {
                        this.close();

                        Interfaz interfaz = new Interfaz();
                        Stage newStage = new Stage();
                        interfaz.start(newStage);
                    });
                    botones.add(boton, col, row);
                } else if (tecla.equals("CAM")) {
                    boton.setMinSize(50, 50);
                    boton.setFont(Font.font(18));
                    boton.setOnAction(e -> abrirReconocimientoFacial());
                    botones.add(boton, col, row);
                } else if (tecla.equals("Server")) {
                    boton.setMinSize(50, 50);
                    boton.setFont(Font.font(18));
                    boton.setOnAction(e -> iniciarServidor());
                    botones.add(boton, col, row);
                } else {
                    boton.setMinSize(50, 50);
                    boton.setFont(Font.font(18));
                    boton.setOnAction(e -> procesar(tecla));
                    botones.add(boton, col, row);
                }
                col++;
                if (col == 4) {
                    col = 0;
                    row++;
                }
                if (tecla.equals("9") || tecla.equals("8") || tecla.equals("7") ||
                        tecla.equals("6") || tecla.equals("5") || tecla.equals("4") ||
                        tecla.equals("3") || tecla.equals("2")) {
                    boton.setDisable(true);
                }
            }
            root1.getChildren().addAll(textField, botones);
            StackPane root = new StackPane();
            root.getChildren().addAll(root1);

            Scene scene = new Scene(root, 350, 400);

            this.setTitle("Calculadora");
            this.setScene(scene);
        }

        /**
         * Procesa una tecla presionada en la calculadora de compuertas lógicas.
         * @param tecla La tecla presionada.
         */
        
        private void procesar(String tecla) {
            if (tecla.equals("C")) {
                textField.setText("");
            } else if (tecla.equals("=")) {
            	String clienteID=interfaz.getClienteID();
            	String expresion =clienteID + "," + "LOGIC: " + textField.getText();
            	 
                 try (Socket socket = new Socket("localhost", 12345)) {
                     // Crea flujos de entrada y salida
                     ObjectOutputStream salida = new ObjectOutputStream(socket.getOutputStream());
                     ObjectInputStream entrada = new ObjectInputStream(socket.getInputStream());

                     // Envía la operación matemática al servidor
                     salida.writeObject(expresion);

                     // Recibe el resultado del servidor
                     boolean resultado = (boolean) entrada.readObject();
                     textField.setText(String.valueOf(resultado));
                 } catch (Exception e) {
                     textField.setText("Error al conectar con el servidor");
                     e.printStackTrace();
                 }
            } else if (tecla.equals("←")) {
                String textoActual = textField.getText();
                if (!textoActual.isEmpty()) {
                    textField.setText(textoActual.substring(0, textoActual.length() - 1));
                }
            } else {
                textField.appendText(tecla);
            }
        }
        
        /**
         * Inicia el servidor para manejar las operaciones de compuertas lógicas.
         */
        
        private void iniciarServidor() {
            // Código para iniciar el servidor
            Thread serverThread = new Thread(() -> {
                servidor.main(null);
            });
            serverThread.setDaemon(true);
            serverThread.start();
        }
    }
}
