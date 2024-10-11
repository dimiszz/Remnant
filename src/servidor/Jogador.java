package servidor;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class Jogador implements Runnable {
    public static ArrayList<Jogador> jogadores = new ArrayList<>();
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String username = "";
    private int partida;

    public Jogador(Socket socket) {
        try{
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

    public void closeEverything() {
        try {
            write("Fechando conexão com o cliente " + socket.getRemoteSocketAddress());
            if (this.bufferedReader != null) this.bufferedReader.close();
            if (this.bufferedWriter != null) this.bufferedWriter.close();
            if (this.socket != null) this.socket.close();
        } catch (IOException e) {
            System.out.println("Erro ao finalizar conexão com o cliente " + socket.getInetAddress() + ": " + e.getMessage());
        } finally {
            Jogador.jogadores.remove(this);
        }
    }

    public String DecodificaMensagem(String mensagem){
        String comando, conteudo, str = "MENSAGEM NÃO DECODIFICADA";

        System.out.println("Lendo mensagem: " + mensagem);
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
                this.username = conteudo;
                str = """
                Seja bem vindo a Remnant! Nesse jogo, você deve escolher entre Atacar
                ou Defender e Magia ou Físico, além do golpe especial contra ataque.
                Os jogadores possuem 3 vidas. Defesas físicas defendem física, o mesmo para mágicas.
                Ou seja, se 1 jogador usar defesa física e o outro ataque mágico, toma dano.
                Se ambos atacarem, ambos levam dano. Se um atacar e o outro usar o contra-ataque, 
                apenas o que atacou leva dano, independente do tipo do ataque. Ganha quem ficar vivo!
                
                Para ver os possíveis comandos digite "/ajuda".
                ----------------------------------------------------------------------------------------------------
                """;
                break;
            case "101":
                str = """
                1. Use "/partidas" para ver a lista de partidas disponíveis.
                2. Use "/criar_partida" para criar uma partida.
                3. Use "/entrar {id}" para se juntar a uma partida.
                4. Use "/sair" para sair de uma partida.
                5. Use "/fechar" para fechar o jogo.
                ----------------------------------------------------------------------------------------------------
                """;
                break;
            case "105": // Listar partidas ativas;
                str = Partida.listarPartidas();
                break;
            case "106":
                str = Partida.criaPartida(this);
                break;
            case "999":
                str = "";
                this.closeEverything();
                break;
        }

        return str;
    }

    @Override
    public void run() {
        try {
            while(socket.isConnected() && !socket.isClosed()){
                String message = this.bufferedReader.readLine();
                String response = DecodificaMensagem(message);
                write(response);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            System.out.println("Fechando conexão com o cliente " + socket.getRemoteSocketAddress());
            closeEverything();
        }
    }
}