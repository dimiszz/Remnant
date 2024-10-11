package main;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import cliente.*;

// Fonte para o servidor e cliente: https://www.youtube.com/watch?v=gLfuZrrfKes
public class Cliente {
    private final Socket socket;
    private final BufferedWriter bufferedWriter;
    private final BufferedReader bufferedReader;
    private final CodificaMensagem encodeMensagem;
    private Thread leitor;

    public Cliente(String host, int port) throws IOException {
        this.socket = new Socket(host, port);
        this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream()));
        this.bufferedReader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        this.encodeMensagem = new CodificaMensagem();
    }

    public void iniciaLeitor(){
        Leitor leit = new Leitor(this.socket, this.bufferedReader);
        this.leitor = new Thread(leit);
        this.leitor.start();
    }

    public void escritor(){
        try{
            Scanner scanner = new Scanner(System.in);

            while(!socket.isClosed() && socket.isConnected()){
                String mensagem = scanner.nextLine();
                mensagem = encodeMensagem.codifica(mensagem);
                this.bufferedWriter.write(mensagem);
                this.bufferedWriter.newLine();
                this.bufferedWriter.flush();
                if (mensagem.equals("999")) this.close();
            }
        }
        catch(IOException e){
            System.out.println("Não foi possível escrever a mensagem." + e.getMessage());
            //e.printStackTrace();
        }
    }

    //https://stackoverflow.com/questions/10961714/how-to-properly-stop-the-thread-in-java
    public void close(){
        try {
            if(this.socket != null && bufferedReader != null && bufferedWriter != null){
                this.socket.close();
                this.bufferedReader.close();
                this.bufferedWriter.close();
                this.leitor.interrupt();
            }
        }
        catch(IOException e){
            System.out.println("Não foi possível encerrar: " + e.getMessage());
            throw new RuntimeException();
            //e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            // Cria o cliente e conecta ao servidor
            Cliente cliente = new Cliente("127.0.0.1",7777);

            // Le o nome de usuario e envia ao servidor
            Scanner scanner = new Scanner(System.in);
            System.out.println("Digite o seu nome de usuário: ");
            String username = scanner.nextLine();
            cliente.bufferedWriter.write("100 " + username);
            cliente.bufferedWriter.newLine();
            cliente.bufferedWriter.flush();
            System.out.println("----------------------------------------------------------------------------------------------------");
            // Inicia a thread de leitura
            cliente.iniciaLeitor();
            // Inicia a o loop da escrita
            cliente.escritor();

            scanner.close();
        }
        catch(Exception e) {
            System.out.println("Erro: " + e.getMessage());
            //e.printStackTrace();
        }
    }
}