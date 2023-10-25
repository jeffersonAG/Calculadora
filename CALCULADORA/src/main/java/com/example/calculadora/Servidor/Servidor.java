package com.example.calculadora.Servidor;

import com.example.calculadora.Arbol.ArbolBinario;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class servidor {

    public static void main(String[] args) {
        final int puerto = 12345; // Puerto en el que escuchará el servidor

        try {
            ServerSocket serverSocket = new ServerSocket(puerto);
            System.out.println("Servidor de Calculadora iniciado. Esperando conexiones...");

            while (true) {
                // Aceptar una conexión entrante
                Socket socket = serverSocket.accept();
                System.out.println("Cliente conectado desde " + socket.getInetAddress());

                // Crear flujos de entrada y salida
                ObjectInputStream entrada = new ObjectInputStream(socket.getInputStream());
                ObjectOutputStream salida = new ObjectOutputStream(socket.getOutputStream());

                try {
                    // Recibir la operación matemática desde el cliente
                    String operacion = (String) entrada.readObject();

                    // Crear un objeto ArbolBinario y evaluar la operación
                    ArbolBinario arbol = new ArbolBinario();
                    arbol.construirArbol(operacion);
                    int resultado = arbol.evaluar();

                    // Enviar el resultado de vuelta al cliente
                    salida.writeObject(resultado);
                    System.out.println("Resultado enviado al cliente: " + resultado);
                } catch (Exception e) {
                    System.err.println("Error al procesar la operación: " + e.getMessage());
                    salida.writeObject("Error");
                }

                // Cerrar la conexión con el cliente
                socket.close();
            }
        } catch (IOException e) {
            System.err.println("Error en el servidor: " + e.getMessage());
        }
    }
}
