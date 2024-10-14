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

    public static String codifica(String mensagem){
        StringBuilder str = new StringBuilder("");
        String comando = separaComando(mensagem);
        String conteudo = separaConteudo(mensagem);

        switch(comando){
            case "/ajuda":
                str.append("101");
                break;
            case "/listar_sessoes":
                str.append("103");
                break;
            case "/criar_sessao":
                str.append("104");
                break;
            case "/entrar_sessao":
                str.append("105 ").append(conteudo);
                break;
            case "/sair_sessao":
                str.append("106");
                break;
            case "/escolher":
                str.append("113 ").append(conteudo);
                break;
            case "/combate":
                str.append("114 ").append(conteudo);
                break;
            case "/sair_partida":
                str.append("115 ");
                break;
            case "/fechar":
                str.append("999");
                break;
            default:
                str.append("COMANDO INVÁLIDO!");
                break;
        };
        return str.toString();
    }

    public static String decodifica(String mensagem){
        System.out.println("Servidor: " + mensagem + "\n");
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
                - Use "/listar_sessoes" para ver a lista de sessões disponíveis.
                - Use "/criar_sessao" para criar uma sessão.
                - Use "/entrar_sessao {id}" para se juntar a uma sessão.
                - Use "/sair_sessao" para sair da sessão atual.
                - Use "/fechar" para fechar o jogo.
                ----------------------------------------------------------------------------------------------------
                """);
                break;
            case "203":
                String[] sessoes = conteudo.split(";");
                str.append("----------------------------------------------------------------------------------------------------\n");
                str.append("Sessões disponíveis:\n");
                for(int i = 1; i < sessoes.length; i+=3){
                    str.append("id: ").append(sessoes[i]);
                    str.append("\tplayer1: ").append(sessoes[i+1]);
                    str.append("\tplayer2: ").append(sessoes[i+2]);
                    str.append("\n");
                }
                str.append("----------------------------------------------------------------------------------------------------\n");
                break;
            case "204":
                str.append("----------------------------------------------------------------------------------------------------\n");
                str.append("Criando sessão:\n");
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
                str.append("Entrando na sessão:\n");
                if(conteudo.contains(";")){
                    String[] sessao = conteudo.split(";");
                    str.append("id: ").append(sessao[0]);
                    str.append("\tplayer1: ").append(sessao[1]);
                    str.append("\tplayer2: ").append(sessao[2]);
                }
                else{
                    str.append(conteudo);
                }
                str.append("\n----------------------------------------------------------------------------------------------------\n");
                break;
            case "206":
                str.append("----------------------------------------------------------------------------------------------------\n");
                str.append("Saindo da sessão:\n").append(conteudo);
                str.append("\n----------------------------------------------------------------------------------------------------\n");
                break;
            case "300":
                str.append("""
                ----------------------------------------------------------------------------------------------------
                Partida iniciada!
                Escolha sua classe usando o comando "/escolher {classe}".
                Use "/ajuda" para ver os comandos disponíveis na partida.

                Classes:
                - Guerreiro
                - Mago
                - Arqueiro
                - Assassino (copilot gerou as classes, nem vamo usar essas mas é muito legal)
                ----------------------------------------------------------------------------------------------------
                """);
                break;
            case "301":
                str.append("""
                ----------------------------------------------------------------------------------------------------
                - Use "/escolher {classe}" para escolher a sua classe.
                - Use "/combate {ataque/defesa}" para escolher o tipo de ataque ou defesa.
                - Use "/sair_partida" para sair da partida.
                ----------------------------------------------------------------------------------------------------
                """);
                break;
            case "305":
                str.append("----------------------------------------------------------------------------------------------------\n");
                str.append(conteudo);
                str.append("\n----------------------------------------------------------------------------------------------------\n");
                break;
            case "306":
                str.append("----------------------------------------------------------------------------------------------------\n");
                str.append(conteudo);
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

        return str.toString();
    }
}