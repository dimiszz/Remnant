package client;

public class MessageHandler {
    public String handle(String mensagem){

        System.out.println("Lendo mensagem: " + mensagem);

        String code = mensagem.substring(0, mensagem.indexOf(' '));
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
                p = "Criação de partida feita com sucesso!";
                break;
        };
        return p;
    }

    public static String communicateMessage(String Code, String Message){
        return Code + " " + Message;
    }
}
