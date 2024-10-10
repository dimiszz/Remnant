package main;

//import java.util.Scanner;
import client.*;

// Fonte para o servidor e cliente: https://www.youtube.com/watch?v=gLfuZrrfKes
public class Client {
    public static void main(String[] args) {
        try {
            //Scanner scanner = new Scanner(System.in);
            //System.out.println("Digite o seu nome de usuário:");
            //String username = scanner.nextLine();


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