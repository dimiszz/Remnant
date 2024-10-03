import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Random;

/// FONTE PARA O CLIENTHANDLER: https://www.youtube.com/watch?v=gLfuZrrfKes

public class ClientHandler implements Runnable {

    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;

    public ClientHandler(Socket socket) {
        try{
            this.socket = socket;
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            clientHandlers.add(this);
            System.out.println("Usuários conectados: " + clientHandlers.size());
            if(clientHandlers.size() > 2) {
                write("{\"Code\": \"-1\"}");
                System.out.println("Número limite de clientes atingido.");
                closeEverything();
            }
            else{
                write("{\"Code\": \"0\"}");
            }
        }
        catch(IOException e){
            System.out.println("Erro no cliente " + socket.getRemoteSocketAddress() + ": " + e.getMessage());
        }
    }

    @Override
    public void run() {

        if (socket.isClosed()) return;

        String mensagem;
        while(socket.isConnected() && !socket.isClosed()){
            try{
                mensagem = bufferedReader.readLine();
                if (mensagem == null) break;
                System.out.println(mensagem + "from " + socket.getRemoteSocketAddress());
                HashMap<String, String> map = decodificarMensagem(mensagem);

                String jogada = map.get("Message");

                if (Objects.equals(map.get("Code"), "10")) break;
            } catch (IOException e) {
                closeEverything();
                System.out.println("Erro lidando com o cliente " + socket.getRemoteSocketAddress() + ": " + e.getMessage());
                throw new RuntimeException();
            }
        }
        System.out.println("Fechando conexão com o cliente " + socket.getRemoteSocketAddress());
        if(socket.isConnected()){
            closeEverything();
        }
    }

    public void closeEverything() {
        try {
            if (this.bufferedReader != null) this.bufferedReader.close();
            if (this.bufferedWriter != null) this.bufferedWriter.close();
            if (this.socket != null) this.socket.close();
        } catch (IOException e) {
            System.out.println("Erro ao finalizar conexão com o cliente " + socket.getInetAddress() +
                    socket.getRemoteSocketAddress() + ": " + e.getMessage());
        }
        ClientHandler.clientHandlers.remove(this);  // Remover o cliente da lista ao desconectar
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
    public static String communicateMessage(String Code, String Message){
        return "{\"Code\": \""+ Code +"\", \"Message\": \"" + Message +  "\"}";
    }
}
