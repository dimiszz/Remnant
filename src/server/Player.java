package server;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class Player implements Runnable {
    public static ArrayList<Player> players = new ArrayList<>();
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String username = "";

    public Player(Socket socket) {
        try{
            this.socket = socket;
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            players.add(this);
            System.out.println("Usuários conectados: " + this.users());
            write("0 Conexão estabelecida");
        }
        catch(IOException e){
            System.out.println("Erro no cliente " + socket.getRemoteSocketAddress() + ": " + e.getMessage());
        }
    }

    @Override
    public void run() {
        try {
            if(socket.isClosed()) return;

            while(socket.isConnected() && !socket.isClosed()){
                String message = this.bufferedReader.readLine();
                String response = handleMensagemRecebida(message);
                write(response);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            System.out.println("Fechando conexão com o cliente " + socket.getRemoteSocketAddress());
            closeEverything();
        }
    }

    public void closeEverything() {
        try {
            write(enviaMensagem("100", "Finalizando conexão."));
            if (this.bufferedReader != null) this.bufferedReader.close();
            if (this.bufferedWriter != null) this.bufferedWriter.close();
            if (this.socket != null) this.socket.close();
        } catch (IOException e) {
            System.out.println("Erro ao finalizar conexão com o cliente " + socket.getInetAddress() + ": " + e.getMessage());
        } finally {
            Player.players.remove(this);
        }
    }

    public void write(String message){
        try{
            this.bufferedWriter.write(message);
            this.bufferedWriter.newLine();
            this.bufferedWriter.flush();
        }
        catch(IOException e){
            System.out.println("Não foi possível escrever a mensagem: " + e.getMessage());
        }
    }

    public synchronized int users(){
        return players.size();
    }

    public static String enviaCode(String code){
        return code;
    }

    public static String enviaMensagem(String code, String message){
        return code + " " + message;
    }

    public String getUsername(){
        return this.username;
    }

    public String handleMensagemRecebida(String message){
        String code = message.substring(0, message.indexOf(' '));
        String result;

        // PARTIDA
        switch(code){
            case "101": // Listar partidas ativas;
                result = Partida.listarPartidas();
                break;
            case "102":
                result = Partida.criaPartida(this);
                break;
            default:
                result = "100 Mensagem não tratada";
                break;
        }

        System.out.println("Escrevendo mensagem: " + result);
        return result;
    }
}