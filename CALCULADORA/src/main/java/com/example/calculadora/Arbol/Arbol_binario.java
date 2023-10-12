package com.example.calculadora.Arbol;
/**
 * Clase que representa un árbol de expresión binaria y evalúa expresiones matemáticas simples.
 */
public class Arbol_binario {
    /**
     * Clase para representar un nodo en el árbol de expresión.
     */
    static class Nodo {
        String valor;  // Valor del nodo (puede ser un operador o número)
        Nodo izquierdo; // Nodo hijo izquierdo
        Nodo derecho;   // Nodo hijo derecho

        /**
         * Constructor de la clase Nodo.
         *
         * @param valor El valor del nodo (operador o número).
         */
        Nodo(String valor) {
            this.valor = valor;
            this.izquierdo = null;
            this.derecho = null;
        }
    }

    /**
     * Evalúa el árbol de expresión de manera recursiva y retorna el resultado.
     *
     * @param nodo El nodo raíz del árbol de expresión.
     * @return El resultado de la evaluación de la expresión.
     * @throws ArithmeticException Si se intenta realizar una división por cero.
     * @throws IllegalArgumentException Si se encuentra un operador no válido.
     */
    static double evaluar(Nodo nodo) {
        if (nodo == null) {
            return 0; // Nodo nulo, retornamos un valor por defecto (esto no debería ocurrir en un árbol válido)
        }

        if (esNumero(nodo.valor)) {
            return Double.parseDouble(nodo.valor); // Si el nodo contiene un número, lo convertimos y lo retornamos
        }

        double izquierdo = evaluar(nodo.izquierdo); // Evaluamos el subárbol izquierdo
        double derecho = evaluar(nodo.derecho);     // Evaluamos el subárbol derecho

        // Realizamos la operación correspondiente según el operador almacenado en el nodo
        switch (nodo.valor) {
            case "+":
                return izquierdo + derecho;
            case "-":
                return izquierdo - derecho;
            case "*":
                return izquierdo * derecho;
            case "/":
                if (derecho != 0) {
                    return izquierdo / derecho;
                } else {
                    throw new ArithmeticException("División por cero"); // Manejo de la división por cero
                }
            default:
                throw new IllegalArgumentException("Operador no válido: " + nodo.valor); // Operador desconocido
        }
    }

    /**
     * Verifica si una cadena es un número.
     *
     * @param str La cadena a verificar.
     * @return true si la cadena es un número, de lo contrario false.
     */
    static boolean esNumero(String str) {
        try {
            Double.parseDouble(str); // Intentamos convertir la cadena a un número
            return true; // Si no se lanza una excepción, es un número válido
        } catch (NumberFormatException e) {
            return false; // Si se lanza una excepción, no es un número válido
        }
    }

    public static void main(String[] args) {
        // Creamos un ejemplo de árbol de expresión para una calculadora
        Nodo raiz = new Nodo("+"); // Nodo raíz con el operador de suma
        raiz.izquierdo = new Nodo("3"); // Hijo izquierdo con el número 3
        Nodo resta = new Nodo("-"); // Hijo derecho con el operador de resta
        raiz.derecho = resta;
        resta.izquierdo = new Nodo("7"); // Hijo izquierdo de la operación de resta con el número 7
        resta.derecho = new Nodo("*"); // Hijo derecho de la operación de resta con el operador de multiplicación
        resta.derecho.izquierdo = new Nodo("2"); // Hijo izquierdo de la operación de multiplicación con el número 2
        resta.derecho.derecho = new Nodo("5"); // Hijo derecho de la operación de multiplicación con el número 5

        // Evaluamos el árbol
        double resultado = evaluar(raiz); // Llamamos a la función evaluar para obtener el resultado
        System.out.println("Resultado de la expresión: " + resultado); // Mostramos el resultado en la consola
    }
}
