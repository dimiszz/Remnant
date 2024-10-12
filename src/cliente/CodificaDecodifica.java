package cliente;

public class CodificaDecodifica {
    public static String separaComando(String mensagem){
        if(mensagem.contains(" ")){
            return mensagem.substring(0, mensagem.indexOf(' '));
        }
        return mensagem;
    }

    public static String separaConteudo(String mensagem){
        if(mensagem.contains(" ")){
            return mensagem.substring(mensagem.indexOf(' ') + 1);
        }
        return "";
    }

    public static StringBuilder codifica(String mensagem){
        StringBuilder str = new StringBuilder("");
        String comando = separaComando(mensagem);
        String conteudo = separaConteudo(mensagem);

        switch(comando){
            case "/ajuda":
                str.append("101");
                break;
            case "/listar_partidas":
                str.append("103");
                break;
            case "/criar_partida":
                str.append("104");
                break;
            case "/entrar_partida":
                str.append("105 ").append(conteudo);
                break;
            case "/sair_partida":
                str.append("106");
                break;
            case "/fechar":
                str.append("999");
                break;
            default:
                str.append("COMANDO INVÁLIDO!");
                break;
        };
        return str;
    }

    public static StringBuilder decodifica(String mensagem){
        StringBuilder str = new StringBuilder("");
        String comando = separaComando(mensagem);
        String conteudo = separaConteudo(mensagem);

        switch(comando){
            case "200":
                str.append("""
                ----------------------------------------------------------------------------------------------------
                Seja bem vindo a Remnant! Nesse jogo, você deve escolher entre Atacar
                ou Defender e Magia ou Físico, além do golpe especial contra ataque.
                Os jogadores possuem 3 vidas. Defesas físicas defendem física, o mesmo para mágicas.
                Ou seja, se 1 jogador usar defesa física e o outro ataque mágico, toma dano.
                Se ambos atacarem, ambos levam dano. Se um atacar e o outro usar o contra-ataque, 
                apenas o que atacou leva dano, independente do tipo do ataque. Ganha quem ficar vivo!
                
                Para ver os possíveis comandos digite "/ajuda".
                ----------------------------------------------------------------------------------------------------
                """);
                break;
            case "201":
                str.append("""
                ----------------------------------------------------------------------------------------------------
                - Use "/listar_partidas" para ver a lista de partidas disponíveis.
                - Use "/criar_partida" para criar uma partida.
                - Use "/entrar_partida {id}" para se juntar a uma partida.
                - Use "/sair_partida" para sair da partida atual.
                - Use "/fechar" para fechar o jogo.
                ----------------------------------------------------------------------------------------------------
                """);
                break;
            case "203":
                String[] partidas = conteudo.split(";");
                str.append("----------------------------------------------------------------------------------------------------\n");
                str.append("Partidas disponíveis:\n");
                for(int i = 1; i < partidas.length; i+=3){
                    str.append("id: ").append(partidas[i]);
                    str.append("\tplayer1: ").append(partidas[i+1]);
                    str.append("\tplayer2: ").append(partidas[i+2]);
                    str.append("\n");
                }
                str.append("----------------------------------------------------------------------------------------------------\n");
                break;
            case "204":
                str.append("----------------------------------------------------------------------------------------------------\n");
                str.append("Criando partida:\n");
                if(conteudo.contains(";")){
                    str.append("id: ").append(conteudo.substring(0, conteudo.indexOf(';')));
                    str.append("\tusername: ").append(conteudo.substring(conteudo.indexOf(';')+1));
                }
                else{
                    str.append(conteudo);
                }
                str.append("\n----------------------------------------------------------------------------------------------------\n");
                break;
            case "205":
                str.append("----------------------------------------------------------------------------------------------------\n");
                str.append("Entrando na partida:\n");
                if(conteudo.contains(";")){
                    String[] partida = conteudo.split(";");
                    str.append("id: ").append(partida[0]);
                    str.append("\tplayer1: ").append(partida[1]);
                    str.append("\tplayer2: ").append(partida[2]);
                }
                else{
                    str.append(conteudo);
                }
                str.append("\n----------------------------------------------------------------------------------------------------\n");
                break;
            case "206":
                str.append("----------------------------------------------------------------------------------------------------\n");
                str.append("Saindo da partida:\n").append(conteudo);
                str.append("\n----------------------------------------------------------------------------------------------------\n");
                break;
            case "999":
                str.append("");
                break;
            default:
                str.append("""
                ----------------------------------------------------------------------------------------------------
                COMANDO INVÁLIDO!
                ----------------------------------------------------------------------------------------------------
                """);
                break;
        }

        return str;
    }
}