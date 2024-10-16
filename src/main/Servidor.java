package main;

import servidor.*;
import java.net.Socket;
import java.net.ServerSocket;

// Fonte para o servidor e cliente: https://www.youtube.com/watch?v=gLfuZrrfKes
public class Servidor {
    public static void main(String[] args) {
        try {
            if (args.length < 1) {
                System.out.println("Uso: java -jar servidor.jar <porta>");
                return;
            }
            // Segundo argumento: porta
            int porta;
            try {
                porta = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.out.println("A porta deve ser um número válido.");
                return;
            }
            // Cria o servidor na porta 7777
            ServerSocket servidor = new ServerSocket(porta);
            System.out.println("Servidor ouvindo a porta " + porta);

            // Aguarda conexões
            while(!servidor.isClosed()){
                Socket cliente = servidor.accept();
                System.out.println("Cliente conectado: " + cliente.getRemoteSocketAddress());

                // Cria uma nova thread para o cliente/usuario
                Usuario novoUsuario = new Usuario(cliente);
                new Thread(novoUsuario).start();
            }

            // Fecha o servidor
            if(!servidor.isClosed()) servidor.close();
        }
        catch(Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }
}