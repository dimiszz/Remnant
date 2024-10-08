package client;

import java.io.*;
import java.net.Socket;

public class Client {
    private Socket socket;
    private BufferedWriter bufferedWriter;
    private BufferedReader bufferedReader;

    public Client(String host, int port) throws IOException {
        this.socket = new Socket(host, port);
        bufferedWriter = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream()));
        bufferedReader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
    }

    public void write(String message){
        try{
            this.bufferedWriter.write(message);
            this.bufferedWriter.newLine();
            this.bufferedWriter.flush();
        }
        catch(IOException e){
            System.out.println("Não foi possível escrever a mensagem." + e.getMessage());
            e.printStackTrace();
        }
    }

    public String read(){
        try {
            String message = this.bufferedReader.readLine();
            return message;
        }
        catch(IOException e){
            System.out.println("Não foi possível ler a mensagem: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public void close(){
        try {
            this.socket.close();
            this.bufferedWriter.close();
            this.bufferedReader.close();
        }
        catch(IOException e){
            System.out.println("Não foi possível encerrar o socket: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public boolean isConnected(){
        return socket.isConnected();
    }
}
