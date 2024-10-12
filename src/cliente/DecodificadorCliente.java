package cliente;

public class DecodificadorCliente {
    public static String decodifica(String mensagem){
        String comando, conteudo, str;

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
                ----------------------------------------------------------------------------------------------------
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
                ----------------------------------------------------------------------------------------------------
                1. Use "/listar_partidas" para ver a lista de partidas disponíveis.
                2. Use "/criar_partida" para criar uma partida.
                3. Use "/entrar {id}" para se juntar a uma partida.
                4. Use "/sair" para sair de uma partida.
                5. Use "/fechar" para fechar o jogo.
                ----------------------------------------------------------------------------------------------------
                """;
                break;
            case "203":
                String partidas[] = conteudo.split(";");
                str = "----------------------------------------------------------------------------------------------------\n";
                str += "Partidas disponíveis:\n";
                for(int i = 1; i < partidas.length; i+=3){
                    str += "id: " + partidas[i] + "\tplayer1: " + partidas[i+1] + "\tplayer2: " + partidas[i+2] + "\n";
                }
                str += "----------------------------------------------------------------------------------------------------\n";
                break;
            case "204":
                str = "----------------------------------------------------------------------------------------------------\n";
                str += "Partida criada:\n";
                str += "id: " + conteudo.substring(0, conteudo.indexOf(';'));
                str += "\tusername: " + conteudo.substring(conteudo.indexOf(';')+1) + "\n";
                str += "----------------------------------------------------------------------------------------------------\n";
                break;
            case "999":
                str = "";
                break;
            default:
                str = "----------------------------------------------------------------------------------------------------\n";
                str += "MENSAGEM NÃO DECODIFICADA\n";
                str += "----------------------------------------------------------------------------------------------------\n";
                break;
        }

        return str;
    }
}