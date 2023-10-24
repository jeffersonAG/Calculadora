package com.example.calculadora.Arbol;
import java.util.Stack;

/**
 * Clase que representa un nodo en un árbol binario utilizado para evaluar expresiones matemáticas.
 */
class Nodo {
    int valor; // Cambiar el tipo a int para admitir números enteros
    Nodo izquierda, derecha;

    /**
     * Constructor para un nodo con valor numérico.
     *
     * @param valor El valor del nodo.
     */
    public Nodo(int valor) {
        this.valor = valor;
        izquierda = derecha = null;
    }

    /**
     * Constructor para un nodo con valor en formato String.
     *
     * @param valor El valor del nodo en formato String.
     */
    public Nodo(String valor) {
        this.valor = Integer.parseInt(valor); // Convertir el valor a entero
        izquierda = derecha = null;
    }
}

/**
 * Clase que representa un árbol binario utilizado para evaluar expresiones matemáticas.
 */
public class ArbolBinario {
    Nodo raiz;

    /**
     * Constructor de la clase ArbolBinario.
     */
    public ArbolBinario() {
        raiz = null;
    }

    /**
     * Evalúa la expresión almacenada en el árbol y devuelve el resultado.
     *
     * @return El resultado de la evaluación de la expresión.
     * @throws IllegalStateException Si el árbol está vacío.
     */
    public int evaluar() {
        if (raiz == null) {
            throw new IllegalStateException("El árbol está vacío.");
        }
        return evaluarRecursivamente(raiz);
    }

    /**
     * Evalúa recursivamente un nodo del árbol.
     *
     * @param nodo El nodo a evaluar.
     * @return El resultado de la evaluación del nodo.
     */
    private int evaluarRecursivamente(Nodo nodo) {
        if (nodo.izquierda == null && nodo.derecha == null) {
            return nodo.valor;
        }

        int izquierda = evaluarRecursivamente(nodo.izquierda);
        int derecha = evaluarRecursivamente(nodo.derecha);

        switch (nodo.valor) {
            case '+':
                return izquierda + derecha;
            case '-':
                return izquierda - derecha;
            case '*':
                return izquierda * derecha;
            case '/':
                if (derecha != 0) {
                    return izquierda / derecha;
                } else {
                    throw new ArithmeticException("División por cero.");
                }
            case '%':
                return izquierda % derecha;
            case '^':
                double resultadoPotencia = Math.pow(izquierda, derecha);
                return (int) resultadoPotencia;
            default:
                throw new IllegalArgumentException("Operador no válido: " + nodo.valor);
        }
    }

    /**
     * Construye el árbol a partir de una expresión matemática en formato String.
     *
     * @param expresion La expresión matemática en formato String.
     */
    public void construirArbol(String expresion) {
        expresion = expresion.replaceAll(" ", "");
        Stack<Nodo> pilaNodos = new Stack<>();
        Stack<Character> pilaOperadores = new Stack();

        for (int i = 0; i < expresion.length(); i++) {
            char c = expresion.charAt(i);

            if (Character.isDigit(c) || (c == '-' && (i == 0 || expresion.charAt(i - 1) == '('))) {
                int j = i;
                while (j < expresion.length() && (Character.isDigit(expresion.charAt(j)) || (expresion.charAt(j) == '-' && j == i))) {
                    j++;
                }
                String numero = expresion.substring(i, j);
                i = j - 1;
                pilaNodos.push(new Nodo(numero));
            } else if (c == '(') {
                pilaOperadores.push(c);
            } else if (c == ')') {
                while (!pilaOperadores.isEmpty() && pilaOperadores.peek() != '(') {
                    procesarOperador(pilaNodos, pilaOperadores);
                }
                pilaOperadores.pop(); // Quitar el paréntesis abierto
            } else if (esOperador(c)) {
                while (!pilaOperadores.isEmpty() && prioridad(pilaOperadores.peek()) >= prioridad(c)) {
                    procesarOperador(pilaNodos, pilaOperadores);
                }
                pilaOperadores.push(c);
            }
        }

        while (!pilaOperadores.isEmpty()) {
            procesarOperador(pilaNodos, pilaOperadores);
        }

        raiz = pilaNodos.pop();
    }

    /**
     * Procesa un operador y construye un nuevo nodo en el árbol.
     *
     * @param pilaNodos       La pila de nodos.
     * @param pilaOperadores  La pila de operadores.
     */
    private void procesarOperador(Stack<Nodo> pilaNodos, Stack<Character> pilaOperadores) {
        char operador = pilaOperadores.pop();
        Nodo derecho = pilaNodos.pop();
        Nodo izquierdo = pilaNodos.pop();
        Nodo nuevoNodo = new Nodo(operador);
        nuevoNodo.izquierda = izquierdo;
        nuevoNodo.derecha = derecho;
        pilaNodos.push(nuevoNodo);
    }

    /**
     * Verifica si un carácter es un operador válido.
     *
     * @param c El carácter a verificar.
     * @return true si es un operador válido, de lo contrario false.
     */
    private boolean esOperador(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/' || c == '%' || c == '^';
    }

    /**
     * Obtiene la prioridad de un operador.
     *
     * @param operador El operador.
     * @return La prioridad del operador (1 para +, -, y 2 para *, /, %, ^).
     */
    private int prioridad(char operador) {
        switch (operador) {
            case '+':
            case '-':
                return 1;
            case '*':
            case '/':
            case '%':
            case '^':
                return 2;
            default:
                return 0;
        }
    }
}
