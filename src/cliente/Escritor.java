package cliente;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

public class Escritor implements Runnable {
    private final Socket socket;
    private final CodificaMensagem encodeMensagem;
    private final BufferedWriter bufferedWriter;
    private final AtomicBoolean active;
    private final Scanner scanner = new Scanner(System.in);

    public Escritor(Socket socket, CodificaMensagem encodeMensagem,
                    BufferedWriter bufferedWriter, AtomicBoolean active){
        this.socket = socket;
        this.encodeMensagem = encodeMensagem;
        this.bufferedWriter = bufferedWriter;
        this.active = active;
    }

    @Override
    public void run() {
        try{
            while(!socket.isClosed() && socket.isConnected()){
                String mensagem = scanner.nextLine();
                mensagem = encodeMensagem.codifica(mensagem);

                this.bufferedWriter.write(mensagem);
                this.bufferedWriter.newLine();
                this.bufferedWriter.flush();
                if (mensagem.equals("999")) active.set(false);
            }
        }
        catch(IOException e){
            System.out.println("Não foi possível escrever a mensagem." + e.getMessage());
            //e.printStackTrace();
        }
    }
}
