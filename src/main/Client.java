package main;

import client.*;

public class Client {
    public static void main(String[] args) {
        try {
            ClientMessage cliente = new ClientMessage("127.0.0.1",7777);

            MessageHandler messageHandler = new MessageHandler();

            while(cliente.isConnected()){
                String mensagem = cliente.read();
                if (mensagem == null) break;
                System.out.println(messageHandler.handle(mensagem));
                Thread.sleep(3000);
                cliente.write(MessageHandler.communicateMessage("102", ""));
            }

            System.out.println("Conexão encerrada!");
        }
        catch(Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

}