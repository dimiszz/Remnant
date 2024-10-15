package servidor;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

public class Usuario implements Runnable {
    private static ArrayList<Usuario> usuarios = new ArrayList<>();
    private static int livre = 0;
    private int idUsuario;
    private int idSessao;
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String username = "";
    private Boolean flagPartida;

    public Usuario(Socket socket){
        try{
            setId();
            this.socket = socket;
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.idSessao = -1;
            this.flagPartida = false;
            usuarios.add(this);
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
        return usuarios.size();
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
            Usuario.usuarios.remove(this);
        }
    }

    private void decodifica(String mensagem){
        String comando, conteudo;
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
                    write("301");
                    break;
                case "113":
                    Sessao.escolheClasse(this, conteudo);
                    break;
                case "114":
                    Sessao.escolheCombate(this, conteudo);
                    break;
                case "115":
                    Sessao.sairPartida(this);
                    break;
                default:
                    write("COMANDO INVÁLIDO!");
                    break;
            }
        }
        else{
            switch(comando){
                case "100":
                    setUsername(conteudo);
                    System.out.println("Usuário " + this.username + " conectado!\n");
                    write("200");
                    break;
                case "101":
                    write("201");
                    break;
                case "103":
                    Sessao.listarSessoes(this);
                    break;
                case "104":
                    Sessao.criaSessao(this);
                    break;
                case "105":
                    Sessao.entrarSessao(this, conteudo);
                    break;
                case "106":
                    Sessao.sairSessao(this);
                    break;
                case "999":
                    this.closeEverything();
                    break;
                default:
                    write("COMANDO INVÁLIDO!");
                    break;
            }
        }
    }

    @Override
    public void run(){
        try {
            while(socket.isConnected() && !socket.isClosed()){
                String message = this.bufferedReader.readLine();
                this.decodifica(message);
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
            closeEverything();
            System.out.println("Usuários conectados: " + this.users() + "\n");
        }
    }
}