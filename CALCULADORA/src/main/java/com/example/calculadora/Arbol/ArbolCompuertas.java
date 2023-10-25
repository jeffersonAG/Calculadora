package com.example.calculadora.Arbol;

class NodoCompuertas {
    String valor;
    NodoCompuertas izquierda;
	NodoCompuertas derecha;

    public NodoCompuertas(String valor) {
        this.valor = valor;
        izquierda = derecha = null;
    }
}

public class ArbolCompuertas {
	NodoCompuertas raiz;
	
	public ArbolCompuertas() {
		raiz=null;
	}
	
	public boolean evaluar() {
		if (raiz==null) {
			throw new IllegalStateException("El árbol está vacío.");
        }
        return evaluarRecursivamente(raiz);
	}
	
	private boolean evaluarRecursivamente(NodoCompuertas nodo) {
		if(nodo.izquierda==null && nodo.derecha==null) {
			if(nodo.valor.equals("0")) {
				return false;
			} else if(nodo.valor.equals("1")) {
				return true;
			}else {
				throw new IllegalArgumentException("Operando no válido: "+ nodo.valor);
			}
		}
		

		
		boolean izquierda=evaluarRecursivamente(nodo.izquierda);
		boolean derecha=evaluarRecursivamente(nodo.derecha);
		
		switch(nodo.valor) {
			case "AND":
				return evaluarAND(izquierda,derecha);
			case "XOR":
				return evaluarXOR(izquierda,derecha);
			case "OR":
				return evaluarOR(izquierda,derecha);
			case "NOT":
				return !derecha;
			default:
				throw new IllegalArgumentException("Operador no válido: "+ nodo.valor);
			
		}
	}
	
	private boolean evaluarAND(boolean a, boolean b) {
		return a && b;	
	}
	
	private boolean evaluarXOR(boolean a, boolean b) {
		return a ^ b;
	}
	
	private boolean evaluarOR(boolean a, boolean b) {
		return a || b;
	}
	
	public static void main(String[] args) {
		ArbolCompuertas tree=new ArbolCompuertas();
	}
	
	public void construirArbol(String expresion) {
		expresion=expresion.replaceAll(" ", "");
		raiz=construirArbolRecursivo(expresion);
	}
	
	private NodoCompuertas construirArbolRecursivo(String expresion) {
		int nivel=0;
		int index=-1;
		int prioridad=0;
		String[] operadores= {"AND","XOR","OR","NOT"};
		for(int i=expresion.length()-1;i>=0;i--) {
			String c = expresion.substring(i,i+1);
			if(c.equals(")")) {
				nivel++;
			} else if(c.equals("(")) {
				nivel--;
			} else if (nivel==0) {
				for(String operador:operadores) {
					if(expresion.startsWith(operador,i)) {
						index=i;
						break;
					}
				}
			}
		}
		if(index != -1) {
			String operador = expresion.substring(index, index+3);
			NodoCompuertas nodo=new NodoCompuertas(operador);
			nodo.izquierda=construirArbolRecursivo(expresion.substring(0, index).trim());
			nodo.derecha=construirArbolRecursivo(expresion.substring(index+3).trim());
			return nodo;
		}else {
			if(expresion.startsWith("(") && expresion.endsWith(")")) {
				return construirArbolRecursivo(expresion.substring(1, expresion.length()-1).trim());
			}else {
				return new NodoCompuertas(expresion);
			}
		}
		
	}
}