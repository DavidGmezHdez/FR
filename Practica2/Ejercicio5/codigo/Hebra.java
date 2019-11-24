// Ejemplo de hebra para servidores concurrentes: Hebrita.java
//
// La clase creada debe extender "Thread".

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*; 

public class Hebra extends Thread{
    Socket socketServicio = null;
    Semaphore sem;
    // En el constructor podemos pasarle alguna variable que hayamos
    // creado en otra clase. Así podemos compartir algunos datos.
    Hebra(Socket s){
        this.socketServicio = s;
    }
    // El contenido de este método se ejecutará tras llamar al
    // método "start()". Se trata del procesamiento de la hebra.
    public void run() {

        // Creamos un objeto de la clase Procesador, pasándole como 
        // argumento el nuevo socket, para que realice el procesamiento
        // Este esquema permite que se puedan usar hebras más fácilmente.
        Procesador procesador=new Procesador(socketServicio);
        procesador.procesa();
    }
}
    