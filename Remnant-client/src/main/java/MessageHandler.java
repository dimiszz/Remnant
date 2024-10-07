import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import messages.PartidaMessage;
import messages.PartidasMessage;
import messages.Response;

import java.util.HashMap;

public class MessageHandler {
    private Client client;

    public String handle(String mensagem){

        System.out.println("lendo mensagem: " + mensagem);

        String code = getCodeFromJson(mensagem);
        String p = "MENSAGEM NÃO TRATADA";

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        switch (code) {
            case "-1":
                p = "Servidor está cheio!";
                break;
            case "0":
                p = """
                Seja bem vindo a Remnant! Nesse jogo, você deve escolher entre Atacar
                ou Defender e Magia ou Físico, além do golpe especial contra ataque.
                Os jogadores possuem 3 vidas. Defesas físicas defendem física, o mesmo para mágicas.
                Ou seja, se 1 jogador usar defesa física e o outro ataque mágico, toma dano.
                Se ambos atacarem, ambos levam dano. Se um atacar e o outro usar o contra-ataque, 
                apenas o que atacou leva dano, independente do tipo do ataque. Ganha quem ficar vivo!
                
                1. Para ver a lista de partidas disponíveis, escreva /partidas
                2. Para criar uma partida, use /criar_partida
                3. Para se juntar a uma partida, use /entrar {codigo}
                """;
                break;
            case "1":
                p = "Esperando jogador...";
                break;
            case "2":
                p = "Escreva seu nome: ";
                break;
            case "101":
                gson.fromJson(mensagem, new TypeToken<Response<PartidasMessage>>(){}.getType());
                p = gson.toString();
                break;
            case "102":
                gson.fromJson(mensagem, new TypeToken<Response<PartidaMessage>>(){}.getType());
                p = gson.toString();
                break;
        };
        return p;
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

    public static String getCodeFromJson(String json) {
        // Verifica se a string JSON contém "Code"
        int codeStartIndex = json.indexOf("\"Code\" : \"") + 11; // 11 é o comprimento de "\"Code\" : \""
        int codeEndIndex = json.indexOf("\"", codeStartIndex); // Encontra o próximo " após o valor do código

        // Extrai o código entre as aspas
        return json.substring(codeStartIndex, codeEndIndex);
    }


    public static String communicateMessage(String Code, String Message){
        return "{\"Code\": \""+ Code + "\", \"Message\": \"" + Message +  "\"}";
    }
}
