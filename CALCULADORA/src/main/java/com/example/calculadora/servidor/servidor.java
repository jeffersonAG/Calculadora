package com.example.calculadora.Servidor;

import com.example.calculadora.Arbol.ArbolBinario;
import com.example.calculadora.Arbol.ArbolCompuertas;
import com.example.calculadora.CSV.Registro;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Esta clase implementa un servidor de calculadora que escucha las solicitudes de los clientes,
 * procesa las operaciones matemáticas y lógicas, y almacena registros en un archivo CSV.
 */

public class servidor {
	private Registro archivo;

    /**
     * Método principal que inicia el servidor de calculadora.
     * Escucha las conexiones entrantes de los clientes y procesa las solicitudes.
     *
     * @param args Los argumentos de la línea de comandos (no se utilizan en este caso).
     */
	
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
                    // Recibir el fujo de datos desde el cliente
                    String mensaje = (String) entrada.readObject();
                    
                    //Se divide el mensaje para optener el ID y la expresión
                    String[] partes=mensaje.split(",");
                    if(partes.length==2) {
                    	String clienteID=partes[0];
                    	String operacion=partes[1];
                    	
                    	if(operacion.startsWith("MATH: ")) {
                        	operacion=operacion.substring("MATH: ".length());
                            // Crear un objeto ArbolBinario y evaluar la operación
                            ArbolBinario arbol = new ArbolBinario();
                            arbol.construirArbol(operacion);
                            int resultado = arbol.evaluar();

                            // Enviar el resultado de vuelta al cliente
                            salida.writeObject(resultado);
                            System.out.println("Resultado enviado al cliente: " + resultado);
                            String filePath= "./history.csv";
                            Registro csv=new Registro(filePath);
                            csv.agregarRegistro(clienteID,operacion, String.valueOf(resultado));
                        } else if(operacion.startsWith("LOGIC: ")) {
                        	operacion=operacion.substring("LOGIC: ".length());
                        	System.out.println(operacion);
                        	// Crear un objeto ArbolCompuertas y evaluar la operación
                            ArbolCompuertas arbol = new ArbolCompuertas();
                            arbol.construirArbol(operacion);
                            boolean resultado = arbol.evaluar();

                            // Enviar el resultado de vuelta al cliente
                            salida.writeObject(resultado);
                            System.out.println("Resultado enviado al cliente: " + resultado);
                            String filePath= "./history.csv";
                            Registro csv=new Registro(filePath);
                            csv.agregarRegistro(clienteID,operacion, String.valueOf(resultado));
                        }else if (operacion.startsWith("CONSULTAR")) {
                        	String filePath= "./history.csv";
                            Registro csv=new Registro(filePath);
                            salida.writeObject(csv.obtenerRegistrosPorCliente(clienteID));
                        }

                    }
                    
                    
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