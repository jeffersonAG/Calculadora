package com.example.calculadora.Arbol;
class Nodo {
    char valor;
    Nodo izquierda, derecha;

    public Nodo(char valor) {
        this.valor = valor;
        izquierda = derecha = null;
    }
}

public class ArbolBinario {
    Nodo raiz;

    public ArbolBinario() {
        raiz = null;
    }

    // Método para evaluar la expresión en el árbol de manera recursiva
    public int evaluar() {
        if (raiz == null) {
            throw new IllegalStateException("El árbol está vacío.");
        }
        return evaluarRecursivamente(raiz);
    }

    private int evaluarRecursivamente(Nodo nodo) {
        if (nodo.izquierda == null && nodo.derecha == null) {
            if (nodo.valor == '-') {
                return -Character.getNumericValue(nodo.izquierda.valor);
            } else {
                return Character.getNumericValue(nodo.valor);
            }
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
            default:
                throw new IllegalArgumentException("Operador no válido: " + nodo.valor);
        }
    }


    public static void main(String[] args) {
        ArbolBinario arbol = new ArbolBinario();
        arbol.construirArbol("(5*(10-15))+7");

        System.out.println("Resultado: " + arbol.evaluar());
    }

    public void construirArbol(String expresion) {
        expresion = expresion.replaceAll(" ", ""); // Elimina espacios en blanco
        raiz = construirArbolRecursivo(expresion);
    }

    private Nodo construirArbolRecursivo(String expresion) {
        int nivel = 0;
        int index = -1;
        int prioridad = 0;
        for (int i = expresion.length() - 1; i >= 0; i--) {
            char c = expresion.charAt(i);
            if (c == ')') {
                nivel++;
            } else if (c == '(') {
                nivel--;
            } else if (nivel == 0 && (c == '*' || c == '/' || c == '%') && prioridad < 2) {
                index = i;
                prioridad = 2;
            } else if (nivel == 0 && (c == '+' || c == '-') && prioridad == 0) {
                index = i;
                prioridad = 1;
            }
        }

        if (index != -1) {
            Nodo nodo = new Nodo(expresion.charAt(index));
            nodo.izquierda = construirArbolRecursivo(expresion.substring(0, index));
            nodo.derecha = construirArbolRecursivo(expresion.substring(index + 1));
            return nodo;
        } else {
            if (expresion.charAt(0) == '(' && expresion.charAt(expresion.length() - 1) == ')') {
                return construirArbolRecursivo(expresion.substring(1, expresion.length() - 1));
            } else {
                return new Nodo(expresion.charAt(0));
            }
        }
    }
}
