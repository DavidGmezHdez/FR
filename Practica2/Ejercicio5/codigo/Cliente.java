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
			// System.out.println("0. Salir\n1. Ver los archivos listados\n2. Descargar un archivo\n-----------");
			// System.out.println("¿Qué acción quiere realizar?: ");
			// accion = reader.nextInt();
			//do{
				// LISTADO DE ARCHIVOS
				socketServicio = new Socket(host,port);

				
				OutputStream outputStream = socketServicio.getOutputStream();
				InputStream inputStream = socketServicio.getInputStream();

				ByteArrayOutputStream bout = new ByteArrayOutputStream();
				DataOutputStream dataOut = new DataOutputStream(bout);
				dataOut.writeInt(accion);
				buferEnvio = bout.toByteArray();
				// Envío peticion primera
				outputStream.write(buferEnvio, 0, buferEnvio.length);
				outputStream.flush();

				int bytesLeidos = inputStream.read(buferRecibo);

				// Mostremos la cadena de caracteres recibidos:
				System.out.println("Recibido: \n");
				for(int i=0;i<bytesLeidos;i++){
					System.out.print((char)buferRecibo[i]);
				}

				socketServicio.close();
				socketServicio = new Socket(host,port);

				outputStream = socketServicio.getOutputStream();
				inputStream = socketServicio.getInputStream();

				//DESCARGAR ARCHIVOS
				int accionArchivo = reader.nextInt();
				bout = new ByteArrayOutputStream();
				dataOut = new DataOutputStream(bout);
				dataOut.writeInt(accionArchivo);
				buferEnvio = bout.toByteArray();
				outputStream.write(buferEnvio, 0, buferEnvio.length);
				outputStream.flush();
				try{
					recibir(inputStream);
				}catch(Exception e){
					System.err.println("Error al recibir.");
				}

				socketServicio.close();
				
				// Envío
				// socketServicio = new DatagramSocket();
				// bout = new ByteArrayOutputStream();
				// dataOut = new DataOutputStream(bout);
				// dataOut.writeInt(accion);
				// buferEnvio = bout.toByteArray();
				// paquete = new DatagramPacket(buferEnvio, buferEnvio.length, direccion, port);
				// socketServicio.send(paquete);

				// System.out.println("Recibiendo archivo...");
				// // Recibimiento
				// paquete = new DatagramPacket(buferRecibo, buferRecibo.length);
				// socketServicio.receive(paquete);
				// buferRecibo = paquete.getData();
				// ByteArrayInputStream bin = new ByteArrayInputStream(paquete.getData());
				// FileInputStream dataIn = new FileInputStream(bin.toString());
				// socketServicio.close();

				// System.out.println("0. Salir\n1. Ver los archivos listados\n2. Descargar un archivo\n-----------");
				// System.out.println("¿Qué acción quiere realizar?: ");
				// 

			//}while(!salida);
		} catch (UnknownHostException e) {
			System.err.println("Error: Nombre de host no encontrado.");
		} catch (IOException e) {
			System.err.println("Error de entrada/salida al abrir el socket.");
		}
	
	}

	public static void recibir(InputStream is) throws Exception{
		byte[] datosRecibidos = new byte[6022386];
		Random random = new Random();
		FileOutputStream archivoRecibir = new FileOutputStream("ejemplo" + random.nextInt(50));
		BufferedOutputStream bos = new BufferedOutputStream(archivoRecibir);
		int bytesLeidos = is.read(datosRecibidos,0,datosRecibidos.length);
		int actual = bytesLeidos;
		System.out.println("Recibiendo: ejemplo" + random.nextInt(50));
		do{
			bytesLeidos = is.read(datosRecibidos,actual,datosRecibidos.length-actual);
			if(bytesLeidos >= 0)
				actual += bytesLeidos;
			System.out.println("a");
		} while(bytesLeidos>-1);
		bos.write(datosRecibidos,0,actual);
		bos.flush();
		// bos.close();
	}
}
