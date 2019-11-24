//
// (CC) Pablo Cordero Romero, David Gómez Hernández, 2019
//
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Random;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.*; 
import java.util.*;
import java.io.DataInputStream;
import java.io.DataOutputStream; 


//
// Nota: si esta clase extendiera la clase Thread, y el procesamiento lo hiciera el método "run()",
// ¡Podríamos realizar un procesado concurrente! 
//
public class Procesador {
	// Referencia a un socket para enviar/recibir las peticiones/respuestas
	private Socket socketServicio;
	// stream de lectura (por aquí se recibe lo que envía el cliente)
	private InputStream inputStream;
	// stream de escritura (por aquí se envía los datos al cliente)
	private OutputStream outputStream;


	// Como máximo leeremos un bloque de 1024 bytes. Esto se puede modificar.
	byte [] datosRecibidos=new byte[1024];
		
	// Array de bytes para enviar la respuesta. Podemos reservar memoria cuando vayamos a enviarla:
	byte [] datosEnviar;

	// Archivos
	File carpetaActual = new File("files/.");	//Cambiar ruta de archivos
	String[] archivos = carpetaActual.list();
	String archivosS = crearLista(archivos);
	
	// Constructor que tiene como parámetro una referencia al socket abierto en por otra clase
	public Procesador(Socket socketServicio) {
		this.socketServicio=socketServicio;
	}

	public Procesador() {}
	
	// Aquí es donde se realiza el procesamiento realmente:
	void procesa(){
		try {
			inputStream=socketServicio.getInputStream();
			outputStream=socketServicio.getOutputStream();

			inputStream.read(datosRecibidos);

			ByteArrayInputStream bin = new ByteArrayInputStream(datosRecibidos);
			DataInputStream dataIn = new DataInputStream(bin);
			int integ = dataIn.readInt();
			int accion = 0;
			// Creamos un String a partir de un array de bytes de tamaño "bytesRecibidos":
			String peticion=new String(datosRecibidos,0,datosRecibidos.length);
			String respuesta="";
			if(integ == -1){
				respuesta = archivosS;
				datosEnviar=respuesta.getBytes();
				outputStream.write(datosEnviar,0,datosEnviar.length);
			}
			else{
				// Enviar un archivo
				// System.err.println("Enviando...");
				// File myFile = new File("files/aaa");
				// datosEnviar = new byte[(int) myFile.length() + 1];
				// FileInputStream fis = new FileInputStream(myFile);
				// BufferedInputStream bis = new BufferedInputStream(fis);
				// // datosEnviar = bout.toByteArray();
				try{
					//System.out.println("Enviando...");
					enviar(outputStream,integ);
				}catch(Exception e){
					System.err.println("Error al enviar.");
				}
			}		
			
			
		} catch (IOException e) {
			System.err.println("Error al obtener los flujos de entrada/salida.");
		}

	}

	String crearLista( String[] arch){
		String salida = "----------------\n" +" ¿Que archivos quieres?"+ "\n----------------\n";
		int contador = 0;
		for(String i : arch){
			salida += contador + ".- " + i + "\n";
			contador++;
		}
		return salida;
	}

	public void enviar(OutputStream os,int i) throws Exception{
		File archivo = new File("./files/aaa");
		datosEnviar = new byte[(int)archivo.length()];
		FileInputStream archivoEnviar = new FileInputStream(archivo);
		BufferedInputStream bis = new BufferedInputStream(archivoEnviar);
		bis.read(datosEnviar,0,datosEnviar.length);
		System.out.println("Enviando...");
		os.write(datosEnviar,0,datosEnviar.length);
		os.flush();
	}
/*
	public void recibir(InputStream is)throws Exception{
		FileOutputStream archivoRecibir = new FileOutputStream("ejemplo");
		BufferedOutputStream bos = new BufferedOutputStream(archivoRecibir);
		int bytesLeidos = is.read(datosRecibidos,0,datosRecibidos.length());
		int actual = bytesLeidos;

		do{
			bytesLeidos = is.read(datosRecibidos,actual,datosRecibidos.length()-actual);
			if(bytesLeidos >= 0)
				actual += bytesLeidos;
		} while(bytesLeidos>-1);
		bos.write(datosRecibidos,0,actual);
		bos.flush();
		bos.close();
	}
*/
}
