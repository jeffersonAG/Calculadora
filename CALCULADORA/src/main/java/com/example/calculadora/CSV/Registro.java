package com.example.calculadora.CSV;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import com.opencsv.CSVWriter;

public class Registro {
	//private String nombreArchivo;
	private SimpleDateFormat dateFormat;
	private String filePath;
	
	public Registro(String filePath) {
		this.filePath=filePath;
		this.dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	}
	
	public void agregarRegistro(String operación, String resultado) {
		String hora=dateFormat.format(new Date());
		
		try(CSVWriter writer=new CSVWriter(new FileWriter(filePath))){
			String[] data= {hora,operación,resultado};
			writer.writeNext(data);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
