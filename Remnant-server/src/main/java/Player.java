import com.google.gson.Gson;
import messages.Response;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

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
            write("{\"code\":\"0\"}");
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

                String response = HandleReceiveMessage(message);

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

    public static String sendCode(String code){
        return "{\"code\":\""+ code + "\"}";
    }

    public static String communicateMessage(String Code, String Message){
        return "{\"code\":\""+ Code +"\",\"message\":\"" + Message +  "\"}";
    }

    public String getNome(){
        return this.nome;
    }

    public String HandleReceiveMessage(String message){
        String code = getCodeFromJson(message);

        Gson gson = new Gson();
        Response<?> jsonResult;

        // PARTIDA
        switch(code){
            case "101": // Listar partidas ativas;
                jsonResult = HandleSendMessage(Partida.listarPartidas());
                break;
            case "102":
                jsonResult = HandleSendMessage(Partida.criaPartida(this));
                break;
            default:
                jsonResult = new Response<>("100", "Mensagem não tratada.");
                break;
        }

        String jsonResponse = gson.toJson(jsonResult);

        System.out.println("escrevendo mensagem: " + jsonResponse);
        return jsonResponse;
    }

    public <T> Response<?> HandleSendMessage(Result<T> result){
        if (!result.isSuccess()) return new Response<>(result.getCode(), result.getMessage());

        return new Response<>(result.getCode(), result.getValue());
    }

    public static String getCodeFromJson(String json) {
        // Verifica se a string JSON contém "Code"
        int codeStartIndex = json.indexOf("\"code\":\"") + 8; // 11 é o comprimento de "\"Code\" : \""
        int codeEndIndex = json.indexOf("\"", codeStartIndex); // Encontra o próximo " após o valor do código
        // Extrai o código entre as aspas
        return json.substring(codeStartIndex, codeEndIndex);
    }

}
