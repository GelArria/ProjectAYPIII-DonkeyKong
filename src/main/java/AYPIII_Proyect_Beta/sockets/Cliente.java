/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AYPIII_Proyect_Beta.sockets;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Cliente implements Runnable {

    private int puerto;
    private Paquete paquete;
    private String ip;
    /**
     *  
     * @param puerto el puerto al cual va a ser enviado el paquete
     * @param paquete el paquete con los datos que se van a enviar
     */
    public Cliente(int puerto, Paquete paquete) {
        this.puerto = puerto;
        this.paquete = paquete;
    }
    
    /**
     *  
     * @param puerto el puerto al cual va a ser enviado el paquete
     * @param paquete el paquete con los datos que se van a enviar
     * @param ip es el ip al cual se va a enviar el paquete
     */
    public Cliente(int puerto, Paquete paquete,String ip) {
        this.puerto = puerto;
        this.paquete = paquete;
        this.ip = ip;
    }
    
    @Override
    public void run() {
        ObjectOutputStream outStream;
        
        try {
            //se  envia el paquete al ip localhost, de quererse enviar a otra pc se cambia
            Socket socket = new Socket("localHost", puerto);
            //Socket socket = new Socket(ip, puerto);
            outStream = new ObjectOutputStream(socket.getOutputStream());
            outStream.writeObject(paquete);
            socket.close();  //cerrar el socket es super importante
            

            
            

        } catch (SocketException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnknownHostException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
}
