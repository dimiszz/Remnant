package server;

import java.net.ServerSocket;
import java.net.Socket;


public class Main {

    public static String communicateMessage(String Message){
        return "{\"code\":\"1\",\"message\":\"" + Message +  "\"}";
    }

    public static void main(String[] args) {

        try {
            ServerSocket servidor = new ServerSocket(7777);
            System.out.println("Servidor ouvindo a porta 7777");
            while(true) {
                // só aceitamos 2 conexões no servidor.
                Socket cliente = servidor.accept();
                System.out.println("Cliente conectado: " + cliente.getInetAddress());
                Player clienteHandler = new Player(cliente);
                new Thread(clienteHandler).start();
            }
        }
        catch(Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }
}