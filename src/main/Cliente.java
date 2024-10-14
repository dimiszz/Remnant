package main;

import cliente.*;
import logs.LogFrame;
import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

// Fonte para o servidor e cliente: https://www.youtube.com/watch?v=gLfuZrrfKes
public class Cliente {
    private final Socket socket;
    private final BufferedWriter bufferedWriter;
    private final BufferedReader bufferedReader;
    private AtomicBoolean active = new AtomicBoolean(true);

    public Cliente(String host, int port) throws IOException {
        this.socket = new Socket(host, port);
        this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream()));
        this.bufferedReader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
    }

    public void iniciaLeitor(){
        Thread.ofPlatform().daemon().start(new Leitor(this.socket, this.bufferedReader, this.active));
    }

    public void iniciaEscritor(){
        Thread.ofPlatform().daemon().start(new Escritor(this.socket, this.bufferedWriter, this.active));
    }

    public void mainLoop(){
        while(active.get());
        close();
    }

    //https://stackoverflow.com/questions/10961714/how-to-properly-stop-the-thread-in-java
    public void close(){
        try {
            if(this.socket != null && bufferedReader != null && bufferedWriter != null){
                this.socket.close();
                this.bufferedReader.close();
                this.bufferedWriter.close();
            }
        }
        catch(IOException e) {
            System.out.println("Não foi possível encerrar: " + e.getMessage());
            throw new RuntimeException();
            //e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            LogFrame logs = new LogFrame(false);

            System.err.println("Iniciando console de logs.");


            Cliente cliente = new Cliente("127.0.0.1",7777);

            // Le o nome de usuario e envia ao servidor
            Scanner scanner = new Scanner(System.in);
            System.out.println("Digite o seu nome de usuário: ");
            String username = scanner.nextLine();
            cliente.bufferedWriter.write("100 " + username);
            cliente.bufferedWriter.newLine();
            cliente.bufferedWriter.flush();

            // Inicia a thread de leitura, escrita e main
            cliente.iniciaLeitor();
            cliente.iniciaEscritor();
            cliente.mainLoop();

            System.out.println("Tentando finalizar o scanner...");
            scanner.close();
            logs.dispose();
            System.out.println("acabou a thread principal.");
        }
        catch(Exception e) {
            System.out.println("Erro: " + e.getMessage());
            //e.printStackTrace();
        }
    }
}