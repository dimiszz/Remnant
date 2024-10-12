package main;

import servidor.*;
import java.net.Socket;
import java.net.ServerSocket;

// Fonte para o servidor e cliente: https://www.youtube.com/watch?v=gLfuZrrfKes
public class Servidor {
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
                Jogador novojogador = new Jogador(cliente);
                new Thread(novojogador).start();
            }

            // Fecha o servidor
            if(!servidor.isClosed()) servidor.close();
        }
        catch(Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }
}