package main;

import chat.Chat;
import cliente.*;
import logs.LogFrame;
import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

// Fonte para o servidor e cliente: https://www.youtube.com/watch?v=gLfuZrrfKes
public class Cliente {
    private final Socket socket;
    private final BufferedWriter bufferedWriter;
    private final BufferedReader bufferedReader;
    private final AtomicBoolean active = new AtomicBoolean(true);
    private final BlockingQueue<String> messageQueue;

    public Cliente(String host, int port) throws IOException {
        this.socket = new Socket(host, port);
        this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream()));
        this.bufferedReader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        this.messageQueue = new ArrayBlockingQueue<>(10);
    }

    public void iniciaLeitor(){
        Thread.ofPlatform().daemon().start(new Leitor(this.socket, this.bufferedReader, this.active));
    }

    public void iniciaEscritor(){
        Thread.ofPlatform().daemon().start(new Escritor(this.socket, this.bufferedWriter, this.active, this.messageQueue));
    }

    public void mainLoop() throws InterruptedException {
        Scanner scanner = new Scanner(System.in);
        while(active.get()){
            String mensagem = scanner.nextLine();
            boolean result = messageQueue.offer(mensagem, 1, TimeUnit.SECONDS);
            if (!result) System.err.println("Erro ao enviar mensagem: fila cheia. " + mensagem);
        };
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

            if (args.length < 2) {
                System.out.println("Uso: java -jar cliente.jar <endereço_ip> <porta> [DEBUG]");
                return;
            }

            String enderecoIp = args[0];
            int porta;
            try {
                porta = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                System.out.println("A porta deve ser um número válido.");
                return;
            }

            boolean modoDebug = args.length > 2 && args[2].equalsIgnoreCase("DEBUG");


            Scanner scanner = new Scanner(System.in);
//            System.out.println("Insira o IP:");
//            String ip = scanner.nextLine();

            Cliente cliente = new Cliente(enderecoIp,porta);
            LogFrame logs = new LogFrame(modoDebug);
            System.err.println("Iniciando console de logs.");
            Chat.addMessageQueue(cliente.messageQueue);

            // Le o nome de usuario e envia ao servidor
            System.out.println("Digite o seu nome de usuário: ");
            String username = scanner.nextLine();
            cliente.bufferedWriter.write("100 " + username);
            cliente.bufferedWriter.newLine();
            cliente.bufferedWriter.flush();

            // Inicia a thread de leitura, escrita e main
            cliente.iniciaLeitor();
            cliente.iniciaEscritor();
            cliente.mainLoop();

            scanner.close();
            logs.dispose();
            Chat.finalizaChat();
            System.out.println("acabou a thread principal.");
        }
        catch(Exception e) {
            System.out.println("Erro: " + e.getMessage());
            //e.printStackTrace();
        }
    }
}