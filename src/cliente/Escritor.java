package cliente;

import java.io.*;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class Escritor implements Runnable {
    private final Socket socket;
    private final BufferedWriter bufferedWriter;
    private AtomicBoolean active;
    private final BlockingQueue<String> messageQueue;

    public Escritor(Socket socket, BufferedWriter bufferedWriter, AtomicBoolean active, BlockingQueue<String> messageQueue){
        this.socket = socket;
        this.bufferedWriter = bufferedWriter;
        this.active = active;
        this.messageQueue = messageQueue;
    }

    @Override
    public void run(){
        try {
            while(active.get() && !socket.isClosed() && socket.isConnected()){
                String mensagem = messageQueue.take();

                System.err.println("Cliente: " + mensagem);

                mensagem = CodificaDecodifica.codifica(mensagem);

                System.err.println("Cliente decodificado: " + mensagem);

                this.bufferedWriter.write(mensagem);
                this.bufferedWriter.newLine();
                this.bufferedWriter.flush();
                if (mensagem.equals("999")) {
                    Thread.sleep(500);
                    System.out.println("Pressione qualquer tecla para continuar...");
                    active.set(false);
                }
            }
        }
        catch(IOException e) {
            System.out.println("Não foi possível escrever a mensagem." + e.getMessage());
            //e.printStackTrace();
        }
        catch(NoSuchElementException e) {
            System.out.println("Thread do Escritor interrompida.");
        } catch (InterruptedException e) {
            System.out.println("Thread do Escritor interrompida.");
            throw new RuntimeException(e);
        }
    }
}
