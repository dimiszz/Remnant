package main;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import cliente.*;

// Fonte para o servidor e cliente: https://www.youtube.com/watch?v=gLfuZrrfKes
public class Cliente {
    private Socket socket;
    private BufferedWriter bufferedWriter;
    private BufferedReader bufferedReader;
    private CodificaMensagem encodeMensagem;

    public Cliente(String host, int port) throws IOException {
        this.socket = new Socket(host, port);
        bufferedWriter = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream()));
        bufferedReader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        encodeMensagem = new CodificaMensagem();
    }

    public void escritor(){
        try{
            Scanner scanner = new Scanner(System.in);

            while(socket.isConnected()){
                String mensagem = scanner.nextLine();
                mensagem = encodeMensagem.codifica(mensagem);
                this.bufferedWriter.write(mensagem);
                this.bufferedWriter.newLine();
                this.bufferedWriter.flush();
            }
        }
        catch(IOException e){
            System.out.println("Não foi possível escrever a mensagem." + e.getMessage());
            e.printStackTrace();
        }
    }

    public void leitor(){
        new Thread(new Runnable() {
            @Override
            public void run(){
                String mensagem;

                while(socket.isConnected()){
                    try {
                        mensagem = bufferedReader.readLine();
                        System.out.println(mensagem);
                    }
                    catch(IOException e){
                        System.out.println("Não foi possível ler a mensagem: " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public void close(){
        try {
            if(this.socket != null && bufferedReader != null && bufferedWriter != null){
                this.socket.close();
                this.bufferedReader.close();
                this.bufferedWriter.close();
            }
        }
        catch(IOException e){
            System.out.println("Não foi possível encerrar: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public boolean isConnected(){
        return socket.isConnected();
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
            cliente.leitor();
            // Inicia a o loop da escrita
            cliente.escritor();
        }
        catch(Exception e) {
            System.out.println("Erro: " + e.getMessage());
            e.printStackTrace();
        }
    }
}