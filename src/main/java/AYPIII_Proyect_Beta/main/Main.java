package AYPIII_Proyect_Beta.main;

import AYPIII_Proyect_Beta.menu.Login;
import AYPIII_Proyect_Beta.sockets.Actualizador;
import AYPIII_Proyect_Beta.sockets.Administrador;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;

public class Main implements Runnable {

    Window window;
    public static int modo = 0;

    /**
     *
     * @param i
     */
    public Main(int i) {
        this.modo = i;
        if (i == 1) {
            Thread t = new Thread(new Administrador());
            t.start();
        }
        if (i == 2) {
            unirse();
        }
        window = Window.get();
        window.run();

    }

    /**
     *
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        //si quieres que inicie el juego de una
              
        Login login = new Login();

    }

    public void unirse() {
        byte[] buffer = new byte[1024];

        try {
            InetAddress direccionServidor = InetAddress.getByName("localhost");

            DatagramSocket socketUDP = new DatagramSocket();

            String mensaje = "me puedo unir?";
            System.out.println(mensaje);
            buffer = mensaje.getBytes();
            DatagramPacket pregunta = new DatagramPacket(buffer, buffer.length, direccionServidor, 5000);
            socketUDP.send(pregunta);
            DatagramPacket peticion = new DatagramPacket(buffer, buffer.length);
            socketUDP.receive(peticion);

            int port = ByteBuffer.wrap(buffer).getInt();
            System.out.println(port);
            Actualizador ac = new Actualizador();
            ac.setPort(port);
            //r.port= this.port;
            socketUDP.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void run() {
        window = Window.get();
        window.run();
    }
}
