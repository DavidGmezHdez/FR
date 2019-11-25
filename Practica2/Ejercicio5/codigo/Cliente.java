// 
// (CC) Pablo Cordero Romero, David Gómez Hernández, 2019
//
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.UnknownHostException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.InetAddress;
import java.io.*;
import java.util.Random;

public class Cliente {

	public static String nombreArchivo = "newFile";
	public static void main(String[] args) {

		try {
			int accion = -1;
			// Nombre del host donde se ejecuta el servidor:
			// String host="localhost";
			// Puerto en el que espera el servidor:
			Socket socketServicio=null;

			byte[] buferEnvio = new byte[256];
			byte[] buferRecibo = new byte[256];

			int port=8989;
			String host="localhost";
			// InetAddress direccion = InetAddress.getByName("localhost");
			boolean salida = false;
			Scanner reader = new Scanner(System.in);

			// LISTADO DE ARCHIVOS
			socketServicio = new Socket(host,port);
			OutputStream outputStream = socketServicio.getOutputStream();
			InputStream inputStream = socketServicio.getInputStream();

			// Enviamos -1 para que el servidor sepa que nos tienen que 
			// enviar la lista de archivos
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			DataOutputStream dataOut = new DataOutputStream(bout);
			dataOut.writeInt(accion);
			buferEnvio = bout.toByteArray();

			// Envío peticion para listado de archivos
			outputStream.write(buferEnvio, 0, buferEnvio.length);
			outputStream.flush();

			// Recibimos la lista de archivos
			int bytesLeidos = inputStream.read(buferRecibo);
			String lista = new String(buferRecibo, 0, buferRecibo.length);
			// Mostremos la cadena de caracteres recibidos:
			// Lista de archivos
			System.out.println(lista);
			
			socketServicio.close();
			// Cerramos el socket para que la espera de selección
			// de archivo sea en el cliente y no en el servidor

			// Recibir archivo
			// Introduce el número que quiere
			int accionArchivo = reader.nextInt();
			
			// Se inicia una nueva conexion
			socketServicio = new Socket(host,port);
			outputStream = socketServicio.getOutputStream();
			inputStream = socketServicio.getInputStream();

			// Se envía el número del archivo que quiere el cliente
			bout = new ByteArrayOutputStream();
			dataOut = new DataOutputStream(bout);
			dataOut.writeInt(accionArchivo);
			buferEnvio = bout.toByteArray();
			outputStream.write(buferEnvio, 0, buferEnvio.length);
			outputStream.flush();

			// Se recibe el nombre del archivo
			BufferedReader inReader = new BufferedReader(new InputStreamReader(inputStream));
			nombreArchivo = inReader.readLine();

			// Recibimos el archivo
			try{
				recibir(inputStream);
			}catch(Exception e){
				System.err.println("Error al recibir. Puede que el archivo indicado no sea válido");
			}
			socketServicio.close();

		} catch (UnknownHostException e) {
			System.err.println("Error: Nombre de host no encontrado.");
		} catch (IOException e) {
			System.err.println("Error de entrada/salida al abrir el socket.");
		}
	
	}

	public static void recibir(InputStream is) throws Exception{
		byte[] datosRecibidos = new byte[6022386];
		FileOutputStream archivoRecibir = new FileOutputStream(nombreArchivo);
		BufferedOutputStream bos = new BufferedOutputStream(archivoRecibir);
		int bytesLeidos = is.read(datosRecibidos,0,datosRecibidos.length);
		int actual = bytesLeidos;
		System.out.println("Recibiendo archivo: " + nombreArchivo);
		bos.write(datosRecibidos,0,actual);
		bos.flush();
		bos.close();
	}
}
