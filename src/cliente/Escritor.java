package cliente;

import java.io.*;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

public class Escritor implements Runnable {
    private final Socket socket;
    private final BufferedWriter bufferedWriter;
    private final Scanner scanner;
    private AtomicBoolean active;

    public Escritor(Socket socket, BufferedWriter bufferedWriter, AtomicBoolean active){
        this.socket = socket;
        this.bufferedWriter = bufferedWriter;
        this.active = active;
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void run() {
        try{
            while(active.get() && !socket.isClosed() && socket.isConnected()){
                String mensagem = scanner.nextLine();
                mensagem = CodificaDecodifica.codifica(mensagem);
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
        catch(NoSuchElementException e){
            System.out.println("Thread do Escritor interrompida.");
        }
    }
}
