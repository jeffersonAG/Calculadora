package com.example.calculadora.CSV;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;

/**
 * Clase que gestiona registros en un archivo CSV, incluyendo la adición de registros
 * y la obtención de registros específicos por cliente.
 */

public class Registro {
	private SimpleDateFormat dateFormat; //variable para obtener la fecha y hora
	private String filePath; //variable para la ruta del archivo
	
    /**
     * Constructor de la clase Registro.
     * @param filePath La ruta del archivo CSV donde se almacenarán los registros.
     */
	
	public Registro(String filePath) {
		this.filePath=filePath;
		this.dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //configuracion del formato de fecha y hora
	}
	
    /**
     * Agrega un nuevo registro al archivo CSV.
     * @param clienteID El identificador único del cliente.
     * @param operación La operación realizada por el cliente.
     * @param resultado El resultado de la operación.
     */
	
	public void agregarRegistro(String clienteID,String operación, String resultado) {
		String hora=dateFormat.format(new Date());
		
		try(CSVWriter writer=new CSVWriter(new FileWriter(filePath))){
			String[] data= {hora,operación,resultado};
			writer.writeNext(data);
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	 /**
     * Obtiene todos los registros de un cliente específico.
     * @param clienteID El identificador único del cliente.
     * @return Una lista de registros (como arreglos de cadenas) asociados al cliente.
     */
	
	public List<String[]> obtenerRegistrosPorCliente(String clienteID) {
	    List<String[]> registrosCliente = new ArrayList<>();

	    try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
	        String[] nextRecord;
	        while ((nextRecord = reader.readNext()) != null) {
	            // Verificar si el primer elemento (clienteID) coincide con el ID del cliente
	            if (nextRecord.length >= 3 && nextRecord[0].equals(clienteID)) {
	                registrosCliente.add(nextRecord);
	            }
	        }
	    } catch (IOException | CsvValidationException e) {
	        e.printStackTrace(); // Manejar la excepción de acuerdo a tus necesidades
	    }

	    return registrosCliente;
	}

}