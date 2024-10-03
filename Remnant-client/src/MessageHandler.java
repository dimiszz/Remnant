import java.util.HashMap;

public class MessageHandler {
    private Client client;

    public String handle(String mensagem){

        HashMap<String, String> map = decodificarMensagem(mensagem);

        String p = "MENSAGEM NÃO TRATADA";

        switch (map.get("Code")) {
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
                apenas o que atacou leva dano, independente do tipo do ataque. Ganha quem ficar vivo!""";
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


    public static String communicateMessage(String Code, String Message){
        return "{\"Code\": \""+ Code + "\", \"Message\": \"" + Message +  "\"}";
    }
}
