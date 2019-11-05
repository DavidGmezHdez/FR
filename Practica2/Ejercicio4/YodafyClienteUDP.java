//
// YodafyServidorIterativo
// (CC) jjramos, 2012
//
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class YodafyClienteUDP {

	public static void main(String[] args) {

		try {
			// Nombre del host donde se ejecuta el servidor:
			// String host="localhost";
			// Puerto en el que espera el servidor:
			DatagramSocket socketServicio=null;

			byte[] buferEnvio = new byte[256];
			byte[] buferRecibo = new byte[256];

			int port=8989;
			InetAddress direccion = InetAddress.getByName("localhost");

			socketServicio = new DatagramSocket();

			buferEnvio="Al monte del volcán debes ir sin demora".getBytes();

			// Envío
			DatagramPacket paquete = new DatagramPacket(buferEnvio, buferEnvio.length, direccion, port);

			socketServicio.send(paquete);

			// Recibo
			paquete = new DatagramPacket(buferRecibo, buferRecibo.length);
			socketServicio.receive(paquete);
			buferRecibo = paquete.getData();

			// Mostremos la cadena de caracteres recibidos:
			System.out.println("Recibido: ");
				for(int i=0;i<buferRecibo.length;i++){
					System.out.print((char)buferRecibo[i]);
				}
			
			socketServicio.close();
		} catch (UnknownHostException e) {
			System.err.println("Error: Nombre de host no encontrado.");
		} catch (IOException e) {
			System.err.println("Error de entrada/salida al abrir el socket.");
		}
	}
}
