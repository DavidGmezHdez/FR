import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

//
// YodafyServidorIterativo
// (CC) jjramos, 2012
//

public class YodafyServidorIterativo {

	public static void main(String[] args) {
	
		// Puerto de escucha
		int port=8989;
		// array de bytes auxiliar para recibir o enviar datos.
		byte []buffer=new byte[256];
		// Número de bytes leídos
		int bytesLeidos=0;
		DatagramPacket paquete;
		DatagramSocket serverSocket = null;

		try {
			// Abrimos el socket en modo pasivo, escuchando el en puerto indicado por "port"
			//////////////////////////////////////////////////
			// ...serverSocket=... (completar)
			//////////////////////////////////////////////////
			serverSocket = new DatagramSocket(port);
			
			// Mientras ... siempre!
			do {
				paquete = new DatagramPacket(buffer, buffer.length);
				// Aceptamos una nueva conexión con accept()
				/////////////////////////////////////////////////
				// socketServicio=... (completar)
				//////////////////////////////////////////////////
				serverSocket.receive(paquete);
				Hebra h = new Hebra(serverSocket, paquete);
				h.start();
				
			} while (true);
			
		} catch (IOException e) {
			System.err.println("Error al escuchar en el puerto "+port);
		}
		buffer = new byte[256];
		serverSocket.close();

	}

}
