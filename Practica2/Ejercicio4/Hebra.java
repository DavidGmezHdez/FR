// Ejemplo de hebra para servidores concurrentes: Hebrita.java
//
// La clase creada debe extender "Thread".

import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.util.concurrent.*; 

public class Hebra extends Thread{
    DatagramSocket socketServicio = null;
    DatagramPacket paquete = null;
    Semaphore sem;
    // En el constructor podemos pasarle alguna variable que hayamos
    // creado en otra clase. Así podemos compartir algunos datos.
    Hebra(DatagramSocket s, DatagramPacket paquete){
        this.socketServicio = s;
        this.paquete = paquete;
    }
    // El contenido de este método se ejecutará tras llamar al
    // método "start()". Se trata del procesamiento de la hebra.
    public void run() {

        // Creamos un objeto de la clase ProcesadorYodafy, pasándole como 
        // argumento el nuevo socket, para que realice el procesamiento
        // Este esquema permite que se puedan usar hebras más fácilmente.
        ProcesadorYodafy procesador=new ProcesadorYodafy(socketServicio, paquete);
        procesador.procesa();
    }
}
    