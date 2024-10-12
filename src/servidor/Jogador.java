package servidor;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

public class Jogador implements Runnable {
    public static ArrayList<Jogador> jogadores = new ArrayList<>();
    private static int livre = 0;
    private int userId;
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String username = "";
    private int partida;

    public Jogador(Socket socket) {
        try{
            setId();
            this.socket = socket;
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            jogadores.add(this);
            System.out.println("Usuários conectados: " + this.users());
        }
        catch(IOException e){
            System.out.println("Erro no cliente " + socket.getRemoteSocketAddress() + ": " + e.getMessage());
        }
    }

    public void write(String mensagem){
        try{
            this.bufferedWriter.write(mensagem);
            this.bufferedWriter.newLine();
            this.bufferedWriter.flush();
        }
        catch(IOException e){
            System.out.println("Não foi possível escrever a mensagem: " + e.getMessage());
        }
    }

    public synchronized int users(){
        return jogadores.size();
    }

    public String getUsername(){
        return this.username;
    }

    public void setPartida(int partida) {
        this.partida = partida;
    }

    public synchronized void setId(){
        this.userId = ++livre;
        setUsername(null);
    }

    public void setUsername(String username){
        this.username = "Anônimo " + this.userId;
        if (username != null && !username.isEmpty()) this.username = username;
    }

    public void closeEverything() {
        try {
            if (this.bufferedReader != null) this.bufferedReader.close();
            if (this.bufferedWriter != null) this.bufferedWriter.close();
            if (this.socket != null) this.socket.close();
        } catch (IOException e) {
            System.out.println("Erro ao finalizar conexão com o cliente " + socket.getInetAddress() + ": " + e.getMessage());
        } finally {
            Jogador.jogadores.remove(this);
        }
    }

    public String decodifica(String mensagem){
        String comando, conteudo, str;

        if(mensagem.contains(" ")){
            comando = mensagem.substring(0, mensagem.indexOf(' '));
            conteudo = mensagem.substring(mensagem.indexOf(' ') + 1);
        }
        else{
            comando = mensagem;
            conteudo = "";
        }

        switch(comando){
            case "100":
                setUsername(conteudo);
                System.out.println("Usuário " + this.username + " conectado!");
                str = "200";
                break;
            case "101":
                str = "201";
                break;
            case "103":
                str = "203 " + Partida.listarPartidas();
                break;
            case "104":
                str = "204 " + Partida.criaPartida(this);
                break;
            case "999":
                str = "";
                this.closeEverything();
                break;
            default:
                str = "MENSAGEM NÃO DECODIFICADA";
                break;
        }

        return str;
    }

    @Override
    public void run() {
        try {
            while(socket.isConnected() && !socket.isClosed()){
                String message = this.bufferedReader.readLine();
                String response = decodifica(message);
                if (response.isEmpty()) continue;
                write(response);
            }
        } catch (SocketException e){
            System.out.println("Conexão interrompida pelo cliente " + socket.getRemoteSocketAddress());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } 
        finally {
            System.out.println("Fechando conexão com o cliente " + socket.getRemoteSocketAddress());
            closeEverything();
        }
    }
}