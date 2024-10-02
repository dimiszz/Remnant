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
    private int points = 0;
    private int wins = 3;
    private int loses = 0;

    public ClientHandler(Socket socket) {
        try{
            this.socket = socket;
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            clientHandlers.add(this);
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        String message;
        while(socket.isConnected()){
            try{
                message = bufferedReader.readLine();
                if (message == null) break;
                System.out.println(message);
                HashMap<String, String> map = decodificarMensagem(message);

                Random random = new Random();

                int choice = random.nextInt(3);

                int value = switch (map.get("Message")) {
                    case "Pedra" -> 0;
                    case "Papel" -> 1;
                    case "Tesoura" -> 2;
                    default -> -750;
                };

                String code = "640";

                if (value != -750) {
                    switch (verificarVencedor(choice, value)) {
                        case "Empate":
                            code = "0";
                            break;
                        case "Jogador 1":
                            code = "2";
                            this.loses++;
                            break;
                        case "Jogador 2":
                            code = "1";
                            this.points++;
                            break;
                        default:
                    }
                }

                bufferedWriter.write(communicateMessage(code, "pq vc me ignora?"));
                bufferedWriter.newLine();
                bufferedWriter.flush();

                if (points == wins){
                    bufferedWriter.write(communicateMessage("10", "pq vc me ignora? Terminou " + this.points + " a " + this.loses));
                    bufferedWriter.newLine();
                    bufferedWriter.flush();
                }

                if (Objects.equals(map.get("Code"), "10")){
                    closeEverything();
                    System.out.println("Fechando conexão com o cliente " + socket.getRemoteSocketAddress());
                    break;
                }
            } catch (IOException e) {
                closeEverything();
                throw new RuntimeException(e);
            }
        }
    }

    public static String verificarVencedor(int jogador1, int jogador2) {
        if (jogador1 == jogador2) {
            return "Empate";
        }

        // Cálculo otimizado para decidir o vencedor
        // (jogador1 - jogador2 + 3) % 3 garante resultado positivo
        return ((jogador1 - jogador2 + 3) % 3 == 1) ? "Jogador 1" : "Jogador 2";
    }

    public void closeEverything() {
        try {
            if (this.bufferedReader != null) this.bufferedReader.close();
            if (this.bufferedWriter != null) this.bufferedWriter.close();
            if (this.socket != null) this.socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ClientHandler.clientHandlers.remove(this);  // Remover o cliente da lista ao desconectar
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
