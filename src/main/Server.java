package main;

import java.net.ServerSocket;
import java.net.Socket;
import server.*;

public class Server {
    public static void main(String[] args) {

        try {
            // Cria o servidor na porta 7777
            ServerSocket servidor = new ServerSocket(7777);
            System.out.println("Servidor ouvindo a porta 7777");

            // Aguarda conexões
            while(!servidor.isClosed()){
                Socket cliente = servidor.accept();
                System.out.println("Cliente conectado: " + cliente.getRemoteSocketAddress());

                // Cria uma nova thread para o cliente/jogador
                Player clienteHandler = new Player(cliente);
                new Thread(clienteHandler).start();
            }

            // Fecha o servidor
            if(servidor != null) servidor.close();
        }
        catch(Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }
}