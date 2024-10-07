import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

/// FONTE PARA O CLIENTHANDLER: https://www.youtube.com/watch?v=gLfuZrrfKes

public class Player implements Runnable {

    public static ArrayList<Player> players = new ArrayList<>();
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String nome = "";

    public Player(Socket socket) {
        try{
            this.socket = socket;
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            players.add(this);
            System.out.println("Usuários conectados: " + this.users());
            write("{\"Code\": \"0\"}");
        }
        catch(IOException e){
            System.out.println("Erro no cliente " + socket.getRemoteSocketAddress() + ": " + e.getMessage());
        }
    }

    @Override
    public void run() {
        try {
        if (socket.isClosed()) return;

        while(socket.isConnected() && !socket.isClosed()){
                String message = this.bufferedReader.readLine();

                String response = HandleSendMessage(message);

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
            write(communicateMessage("100", "Finalizando conexão."));
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

    public static HashMap<String, String> decodificarMensagem(String mensagem){
        int i, y, bin = 0;
        String key = "";
        String value = "";
        HashMap<String, String> map = new HashMap<String, String>();
        for(i = 0, y = 0; i < mensagem.length()-1; i++){
            if(mensagem.charAt(i) == '\"'){
                y = i+1;
                StringBuilder m1 = new StringBuilder();
                while(mensagem.charAt(y) != '\"'){
                    m1.append(mensagem.charAt(y));
                    y++;
                }
                i = y+1;
                if(bin % 2 == 0) key = m1.toString();
                else {
                    value = m1.toString();
                    map.put(key, value);
                }
                bin++;
            }
        }

        return map;
    }

    public static String sendCode(String code){
        return "{\"Code\": \""+ code + "\"}";
    }
    public static String communicateMessage(String Code, String Message){
        return "{\"Code\": \""+ Code +"\", \"Message\": \"" + Message +  "\"}";
    }

    public String getNome(){
        return this.nome;
    }

    public String HandleSendMessage(String message){
        String code = getCodeFromJson(message);
        Gson gson = new Gson();

        String jsonResponse = "";

        // PARTIDA
        switch(code){
            case "101": // Listar partidas ativas;
                jsonResponse = gson.toJson(Partida.listarPartidas());
                break;
            case "102":
                jsonResponse = gson.toJson(Partida.criaPartida(this));
        }
        System.out.println("escrevendo mensagem: " + jsonResponse);
        return jsonResponse;
    }

    public static String getCodeFromJson(String json) {

        int codeStartIndex = json.indexOf("\"Code\" : \"") + 11;
        int codeEndIndex = json.indexOf("\"", codeStartIndex);

        return json.substring(codeStartIndex, codeEndIndex);
    }

}
