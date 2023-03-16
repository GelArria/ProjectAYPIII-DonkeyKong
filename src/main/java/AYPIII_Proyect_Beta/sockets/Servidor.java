/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AYPIII_Proyect_Beta.sockets;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Servidor extends Observable implements Runnable {

    private int puerto;

    /**
     *
     * @param puerto
     */
    public Servidor(int puerto) {
        this.puerto = puerto;
    }

    @Override
    public void run() {

        ServerSocket servidor = null;
        Socket sc = null;
        DataInputStream in;

        try {

            servidor = new ServerSocket(puerto);
            System.out.println("Servidor iniciado");
            ObjectInputStream inStream;


            while (true) {

                sc = servidor.accept();

                inStream = new ObjectInputStream(sc.getInputStream());
            
                Paquete paquete = (Paquete) inStream.readObject();
                
                this.setChanged();
                this.notifyObservers(paquete);
                this.clearChanged();
                
                sc.close();
                
            }

        } catch (IOException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
