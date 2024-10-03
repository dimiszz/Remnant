import java.io.*;
import java.net.Socket;
import java.util.*;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        try {
            Client cliente = new Client("127.0.0.1",12345);

            MessageHandler messageHandler = new MessageHandler();

            Scanner t = new Scanner(System.in);

            String code = "0";
            String menssage = "";

            while(cliente.isConnected()){
                String mensagem = cliente.read();
                System.out.println(messageHandler.handle(mensagem));
            }
            cliente.close();
            System.out.println("Conexão encerrada");
        }
        catch(Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

}