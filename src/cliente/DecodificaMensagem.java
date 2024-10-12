package cliente;

public class DecodificaMensagem {
    public static String decodifica(String mensagem){
        String comando, conteudo, str = "MENSAGEM NÃO DECODIFICADA";

        System.out.println("Lendo mensagem: " + mensagem);
        if(mensagem.contains(" ")){
            comando = mensagem.substring(0, mensagem.indexOf(' '));
            conteudo = mensagem.substring(mensagem.indexOf(' ') + 1);
        }
        else{
            comando = mensagem;
            conteudo = "";
        }

        switch(comando){
            case "200":
                str = """
                Seja bem vindo a Remnant! Nesse jogo, você deve escolher entre Atacar
                ou Defender e Magia ou Físico, além do golpe especial contra ataque.
                Os jogadores possuem 3 vidas. Defesas físicas defendem física, o mesmo para mágicas.
                Ou seja, se 1 jogador usar defesa física e o outro ataque mágico, toma dano.
                Se ambos atacarem, ambos levam dano. Se um atacar e o outro usar o contra-ataque, 
                apenas o que atacou leva dano, independente do tipo do ataque. Ganha quem ficar vivo!
                
                Para ver os possíveis comandos digite "/ajuda".
                ----------------------------------------------------------------------------------------------------
                """;
                break;
            case "201":
                str = """
                1. Use "/listar_partidas" para ver a lista de partidas disponíveis.
                2. Use "/criar_partida" para criar uma partida.
                3. Use "/entrar {id}" para se juntar a uma partida.
                4. Use "/sair" para sair de uma partida.
                5. Use "/fechar" para fechar o jogo.
                ----------------------------------------------------------------------------------------------------
                """;
                break;
            case "202":
                str = conteudo;
                break;
            case "203":
                str = conteudo;
                break;
            case "999":
                str = "";
                break;
        }

        return str;
    }
}