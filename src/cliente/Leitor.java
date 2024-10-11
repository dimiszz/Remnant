package cliente;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;

public class Leitor implements Runnable{
    private final Socket socket;
    private final BufferedReader bufferedReader;
    public Leitor(Socket s, BufferedReader br){
        this.socket = s;
        this.bufferedReader = br;
    }

    @Override
    public void run() {
        String mensagem;

        // BUG: quando servidor fecha inesperadamente esse cara fica tentando fazer
        // a conexão sem parar.
        while(!socket.isClosed()){
            try {
                mensagem = bufferedReader.readLine();
                System.out.println(mensagem);
                if (Thread.interrupted()) throw new InterruptedException();
            }
            catch(IOException e){
                System.out.println("Não foi possível ler a mensagem: " + e.getMessage());
                //e.printStackTrace();
            } catch (InterruptedException e) {
                System.out.println("Thread foi interrompida antes da conexão com o socket fechar.");
            }
        }
    }
}
