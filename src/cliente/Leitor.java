package cliente;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.atomic.AtomicBoolean;

public class Leitor implements Runnable{
    private final Socket socket;
    private final BufferedReader bufferedReader;
    private AtomicBoolean active;

    public Leitor(Socket s, BufferedReader br, AtomicBoolean active){
        this.socket = s;
        this.bufferedReader = br;
        this.active = active;
    }

    @Override
    public void run(){
        String mensagem;

        while(!socket.isClosed() && this.active.get()){
            try {
                mensagem = bufferedReader.readLine();
                mensagem = CodificaDecodifica.decodifica(mensagem);
                System.out.println(mensagem);
                if (Thread.interrupted()) throw new InterruptedException();
            }
            catch(SocketException e) {
                System.err.println("Conexão com o servidor foi finalizada. " + e.getMessage());
                this.active.set(false);
            }
            catch(IOException e) {
                System.out.println("Não foi possível ler a mensagem: " + e.getMessage());
                //e.printStackTrace();
            } catch (InterruptedException e) {
                System.out.println("Thread foi interrompida antes da conexão com o socket fechar.");
                this.active.set(false);
            }
        }
    }
}
