import java.net.ServerSocket;
import java.net.Socket;


public class Main {

    public static String communicateMessage(String Message){
        return "{\"Code\": \"1\", \"Message\": \"" + Message +  "\"}";
    }

    public static void main(String[] args) {

        try {
            ServerSocket servidor = new ServerSocket(12345);
            System.out.println("Servidor ouvindo a porta 12345");
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