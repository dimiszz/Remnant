import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;


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
                if(ClientHandler.clientHandlers.size() < 2) {
                    Socket cliente = servidor.accept();
                    System.out.println("Cliente conectado: " + cliente.getInetAddress().getHostAddress());

                    ClientHandler clienteHandler = new ClientHandler(cliente);

                    Thread thread = new Thread(clienteHandler);
                    thread.start();
                }
            }
        }
        catch(Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }
}