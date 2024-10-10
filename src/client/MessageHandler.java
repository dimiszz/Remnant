package client;

import message.PartidaMessage;
import message.PartidasMessage;
import message.Response;

public class MessageHandler {
    private ClientMessage client;

    public String handle(String mensagem){

        System.out.println("lendo mensagem: " + mensagem);

        String code = getCodeFromJson(mensagem);
        String p = "MENSAGEM NÃO TRATADA";

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
            case "100":
                p = "Erro";
                break;
            case "101":
                p = "Teste";
                break;
            case "102":
                p = "Teste";
                break;
        };
        return p;
    }

    public static String getCodeFromJson(String json) {
        // Verifica se a string JSON contém "Code"
        int codeStartIndex = json.indexOf("\"code\":\"") + 8; // 11 é o comprimento de "\"Code\" : \""
        int codeEndIndex = json.indexOf("\"", codeStartIndex); // Encontra o próximo " após o valor do código
        // Extrai o código entre as aspas
        return json.substring(codeStartIndex, codeEndIndex);
    }

    public static String communicateMessage(String Code, String Message){
        return "{\"code\":\""+ Code + "\", \"message\":\"" + Message +  "\"}";
    }
}
