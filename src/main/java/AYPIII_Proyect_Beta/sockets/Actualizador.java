/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AYPIII_Proyect_Beta.sockets;


import java.util.Observable;
import java.util.Observer;

/**
 *
 * @author santi
 */
public class Actualizador implements Observer{
    int port;
    
    
    public Actualizador(){

    }
    
    /**
     * Esta clase se encarga de actualizar el juego, cuando el servidor2 recibe un mensaje,
     * este le notifica al actualizador y actualiza los datos del juego online
     * @param port el puerto con el que va a ser creado el servidor2
     */
    public void setPort(int port){
        this.port= port;
        Servidor2 sv= new Servidor2(this.port);
        sv.addObserver(this);
        Thread th= new Thread(sv);
        th.start();
    }

    @Override
    public void update(Observable o, Object o1) {
        Paquete paquete = (Paquete)o1;
        
    }
    
}
