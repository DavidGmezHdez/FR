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
	File carpetaActual = new File("files/.");	// Ruta de archivos
	String[] archivos = carpetaActual.list();	// Lista de nombres de archivos
	String archivosS = crearLista(archivos);	// Lisa creada como String con números para enviar al cliente
	
	// Constructor que tiene como parámetro una referencia al socket abierto en por otra clase
	public Procesador(Socket socketServicio) {
		this.socketServicio=socketServicio;
	}
	
	// Aquí es donde se realiza el procesamiento realmente:
	void procesa(){
		try {
			inputStream=socketServicio.getInputStream();
			outputStream=socketServicio.getOutputStream();

			// Leemos el entero que nos envían
			// Si es -1 -> listaremos los archivos
			// Si es otro entero -> Enviamos ese archivo
			inputStream.read(datosRecibidos);
			ByteArrayInputStream bin = new ByteArrayInputStream(datosRecibidos);
			DataInputStream dataIn = new DataInputStream(bin);
			int integ = dataIn.readInt();

			String respuesta="";
			if(integ == -1){
				// Enviamos la lista de archivos
				respuesta = archivosS;
				datosEnviar=respuesta.getBytes();
				outputStream.write(datosEnviar,0,datosEnviar.length);
				outputStream.flush();
			}
			else if(integ <= archivos.length){	
				// Envío del nombre de archivo elegido por el usuario	
				PrintWriter outPrinter = new PrintWriter(outputStream,true);
				outPrinter.println(archivos[integ]);

				//Enviamos el archivo
				try{
					enviar(outputStream,integ);
				}catch(Exception e){
					System.err.println("Error al enviar");
				}
			}		
			else{
				// Cierra la conexión por que no se han indicado un archivo válido
				socketServicio.close();
				System.err.println("Archivo no válido");
			}
			
			
		} catch (IOException e) {
			System.err.println("Error al obtener los flujos de entrada/salida.");
		}

	}

	String crearLista( String[] arch){
		String salida = "----------------"+"\n¿Qué archivos quieres?\n"+"----------------\n";
		int contador = 0;
		for(String i : arch){
			salida += contador + ".- " + i + "\n";
			contador++;
		}
		salida+="----------------";

		return salida;
	}

	public void enviar(OutputStream os,int i) throws Exception{
		File archivo = new File("files/" + archivos[i]);
		datosEnviar = new byte[(int)archivo.length()];
		FileInputStream archivoEnviar = new FileInputStream(archivo);
		BufferedInputStream bis = new BufferedInputStream(archivoEnviar);
		bis.read(datosEnviar,0,datosEnviar.length);
		System.out.println("Enviando " + archivos[i]);
		os.write(datosEnviar,0,datosEnviar.length);
		os.flush();
	}
/*
	public void recibir(InputStream is)throws Exception{
		FileOutputStream archivoRecibir = new FileOutputStream("ejemplo");
		BufferedOutputStream bos = new BufferedOutputStream(archivoRecibir);
		int bytesLeidos = is.read(datosRecibidos,0,datosRecibidos.length());
		int actual = bytesLeidos;
		bos.write(datosRecibidos,0,actual);
		bos.flush();
		bos.close();
	}
*/
}
