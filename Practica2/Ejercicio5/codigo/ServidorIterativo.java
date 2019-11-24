import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.FileOutputStream;

//
// (CC) Pablo Cordero Romero, David Gómez Hernández, 2019
//

public class ServidorIterativo {
	public static void main(String[] args) {
	
		// Puerto de escucha
		int port=8989;
		// array de bytes auxiliar para recibir o enviar datos.
		byte []buffer=new byte[256];
		// Número de bytes leídos
		int bytesLeidos=0;
		ServerSocket serverSocket = null;
		Socket socketServicio = null;

		try {
			serverSocket = new ServerSocket(port);
			do {

				socketServicio = serverSocket.accept();
				Hebra h = new Hebra(socketServicio);
				h.start();
				
			} while (true);
			
		} catch (IOException e) {
			System.err.println("Error al escuchar en el puerto "+port);
		}
		buffer = new byte[256];
	}

}
