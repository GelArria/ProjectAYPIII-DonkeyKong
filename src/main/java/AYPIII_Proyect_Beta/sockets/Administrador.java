/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AYPIII_Proyect_Beta.sockets;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author santi
 */
public class Administrador implements Runnable {
    
    final int port = 5000;
    byte[] buffer = new byte[1024];
    int [] ports ={4992,4993,4994}; //3 puertos porque se conectan maximo 3 personas 
    boolean [] availablePorts= {true,true,true};
    DatagramSocket [] sockets = new DatagramSocket[2]; //ah?
    InetAddress puntero;
    int punteroPuerto;
    public Administrador(){
        
    }

    /**
     * esta clase se crea para hostear la app
     * administra las solicitudes de ingreso que se reciben y le otorga un puerto dispotible
     * para que reciban datos
     * @param address este seria el address del administrador
     * @param port este es el puerto en el que va a estar ubicado
     */
    public Administrador(InetAddress address, int port){
        puntero=address;
        punteroPuerto=port;
    }
    
    /**
     * aqui se inicializa el servidor que se queda a la espera de peticiones 
     */
    @Override
    public void run() {
        try {
            
            DatagramSocket socket = new DatagramSocket(port);
            DatagramPacket peticion = new DatagramPacket(buffer, buffer.length);
            System.out.println("administrador abierto");
            while(true){
                socket.receive(peticion);
                String mensaje = new String(peticion.getData());
                System.out.println(mensaje);
                /*
                punteroPuerto= peticion.getPort();
                puntero= peticion.getAddress();
                */
                int puertoCliente = peticion.getPort();
                InetAddress direccion = peticion.getAddress();
                
                
                for(int i= 0;i<3;i++ ){
                    if (availablePorts[i]==true ){
                        buffer =  ByteBuffer.allocate(4).putInt(ports[i]).array();
                        availablePorts[i]= false;
                        break;
                    }
                }
                
                DatagramPacket respuesta = new DatagramPacket(buffer, buffer.length, direccion, puertoCliente);
                socket.send(respuesta);
            }
            
            
            
            
            
            
        } catch (SocketException ex) {
            Logger.getLogger(Administrador.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Administrador.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }



}
