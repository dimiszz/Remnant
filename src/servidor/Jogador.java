package servidor;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

public class Jogador implements Runnable {
    private static ArrayList<Jogador> jogadores = new ArrayList<>();
    private static int livre = 0;
    private int idUsuario;
    private int idSessao;
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String username = "";
    private Boolean flagPartida;

    public Jogador(Socket socket){
        try{
            setId();
            this.socket = socket;
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.idSessao = -1;
            this.flagPartida = false;
            jogadores.add(this);
            System.out.println("Usuários conectados: " + this.users() + "\n");
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

    public int getSessao(){
        return this.idSessao;
    }

    public boolean estaEmSessao(){
        return this.idSessao != -1;
    }

    public void setSessao(int idSessao){
        this.idSessao = idSessao;
    }

    public void removeSessao(){
        setSessao(-1);
    }

    public synchronized void setId(){
        this.idUsuario = ++livre;
        setUsername(null);
    }

    public synchronized void setPartida(Boolean flag){
        this.flagPartida = flag;
    }

    public void setUsername(String username){
        this.username = "Anônimo " + this.idUsuario;
        if (username != null && !username.isEmpty()) this.username = username;
    }

    public void closeEverything(){
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

        if(this.flagPartida){
            switch(comando){
                case "101":
                    str = "301";
                    break;
                case "113":
                    str = "303 " + Sessao.escolheClasse(this, conteudo);
                    break;
                case "114":
                    str = "304";
                    break;
                case "115":
                    str = "305 " + Sessao.sairPartida(this);
                    break;
                default:
                    str = "COMANDO INVÁLIDO!";
                    break;
            }
        }
        else{
            switch(comando){
                case "100":
                    setUsername(conteudo);
                    System.out.println("Usuário " + this.username + " conectado!\n");
                    str = "200";
                    break;
                case "101":
                    str = "201";
                    break;
                case "103":
                    str = "203 " + Sessao.listarSessoes();
                    break;
                case "104":
                    str = "204 " + Sessao.criaSessao(this);
                    break;
                case "105":
                    str = "205 " + Sessao.entrarSessao(this, conteudo);
                    break;
                case "106":
                    str = "206 " + Sessao.sairSessao(this);
                    break;
                case "999":
                    str = "";
                    this.closeEverything();
                    break;
                default:
                    str = "COMANDO INVÁLIDO!";
                    break;
            }
        }
        return str;
    }

    @Override
    public void run(){
        try {
            while(socket.isConnected() && !socket.isClosed()){
                String message = this.bufferedReader.readLine();
                String response = decodifica(message);
                if (response.isEmpty()) continue;
                write(response);
            }
        }
        catch(SocketException e) {
            System.out.println("Conexão interrompida pelo cliente " + socket.getRemoteSocketAddress());
        }
        catch(IOException e) {
            throw new RuntimeException(e);
        }
        finally {
            System.out.println("Cliente desconectado: " + socket.getRemoteSocketAddress());
            System.out.println("Usuários conectados: " + this.users() + "\n");
            closeEverything();
        }
    }
}