/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AYPIII_Proyect_Beta.sockets;

import java.io.Serializable;

/**
 *
 * Esta clase es el paquete que se llenara para enviar a los otros jugadores online
 */
public class Paquete implements Serializable {
    public int vidas;
    public Paquete(int vidas){
        this.vidas=vidas;
    }

   
}
