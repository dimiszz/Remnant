package servidor;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Phaser;

public class Jogador implements Runnable {
    public static ArrayList<Jogador> jogadores = new ArrayList<>();
    private static int livre = 0;
    private int userId;
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String username = "";
    private int partidaId;
    private boolean emJogo = false;
    private Phaser phaser;
    BlockingQueue<String> filaRespostas;

    public Jogador(Socket socket){
        try{
            setId();
            this.socket = socket;
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.partidaId = -1;
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

    public void setEmJogo(boolean estado){
        this.emJogo = estado;
    }

    public void setFilaRespostas(BlockingQueue<String> filaRespostas){
        this.filaRespostas = filaRespostas;
    }

    public synchronized int users(){
        return jogadores.size();
    }

    public String getUsername(){
        return this.username;
    }

    public void setPhaser(Phaser phaser){
        this.phaser = phaser;
        this.phaser.register();
    }

    public int getPartidaId(){
        return this.partidaId;
    }

    public boolean estaEmPartida(){
        return this.partidaId != -1;
    }

    public void setPartidaId(int partida){
        this.partidaId = partida;
    }

    public void removePartida(){
        setPartidaId(-1);
    }

    public synchronized void setId(){
        this.userId = ++livre;
        setUsername(null);
    }

    public void setUsername(String username){
        this.username = "Anônimo " + this.userId;
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

    public String decodifica(String mensagem) {
        String comando, conteudo, str;

        if(mensagem.contains(" ")){
            comando = mensagem.substring(0, mensagem.indexOf(' '));
            conteudo = mensagem.substring(mensagem.indexOf(' ') + 1);
        }
        else{
            comando = mensagem;
            conteudo = "";
        }


        if (this.emJogo){
            try {
                // Colocamos o input do usuário na fila de respostas para a partida tratar.
                this.filaRespostas.put(this.userId + ";" + comando);

                System.out.println("Esperando mensagem ser colocada na fila para avançar Jogador " + this.userId);
                this.phaser.arriveAndAwaitAdvance();

                System.out.println("Esperando mensagem ser processada pela Partida " + this.userId);
                this.phaser.arriveAndAwaitAdvance();


                // Essa parte do código é para procurar a resposta desse jogador na fila de respostas
                String resposta = this.filaRespostas.take();
                String[] respostaQuebrada = resposta.split(";");

                if (Integer.parseInt(respostaQuebrada[0]) != this.userId) {
                    filaRespostas.put(resposta);
                    resposta = this.filaRespostas.take();
                }

                System.out.println("O cliente " + this.userId + " de nome " + this.username + " está pronto! " + resposta);

                // aqui esperamos para enviar a mensagem simultaneamente  para ambos os clientes.
                //this.phaser.arriveAndAwaitAdvance();
                this.phaser.arriveAndDeregister();

                this.phaser.register();
                return "997 Obrigado por receber a mensagem " + this.userId + mensagem;
            }
            catch(InterruptedException e){
                System.out.println(e.getMessage());
                throw new RuntimeException();
            }
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
            case "105":
                str = "205 " + Partida.entrarPartida(this, conteudo);
                break;
            case "106":
                str = "206 " + Partida.sairPartida(this);
                break;
            case "999":
                str = "";
                this.closeEverything();
                break;
            default:
                str = "COMANDO INVÁLIDO!";
                break;
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
            System.out.println("Fechando conexão com o cliente " + socket.getRemoteSocketAddress());
            closeEverything();
        }
    }
}