package cliente;

import chat.Chat;

// Classe para codificar e decodificar as mensagens tando da escrita quanto da leitura
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
        StringBuilder str = new StringBuilder();
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
                str.append("113 ").append(conteudo.toLowerCase());
                break;
            case "/combate":
                str.append("114 ").append(conteudo.toLowerCase());
                break;
            case "/sair_partida":
                str.append("115");
                break;
            case "/fechar":
                str.append("999");
                break;
            case "/escrever":
                str.append("1102 ").append(conteudo);
                break;
            default:
                str.append("COMANDO INVÁLIDO!");
                break;
        };
        return str.toString();
    }

    public static String decodifica(String mensagem){
        //System.out.println("Servidor: " + mensagem + "\n");
        StringBuilder str = new StringBuilder();
        String comando = separaComando(mensagem);
        String conteudo = separaConteudo(mensagem);
        String[] conteudos = null;
        if(conteudo.contains(";")){
            conteudos = conteudo.split(";");
        }

        switch(comando){
            case "200":
                str.append("""
                ----------------------------------------------------------------------------------------------------
                 _____  ______ __  __ _   _          _   _ _______ 
                |  __ \\|  ____|  \\/  | \\ | |   /\\   | \\ | |__   __|
                | |__) | |__  | \\  / |  \\| |  /  \\  |  \\| |  | |   
                |  _  /|  __| | |\\/| | . ` | / /\\ \\ | . ` |  | |   
                | | \\ \\| |____| |  | | |\\  |/ ____ \\| |\\  |  | |   
                |_|  \\_\\______|_|  |_|_| \\_/_/    \\_\\_| \\_|  |_|   
                                                    
                Bem vindo ao Remnant! Crie uma sessão e disputa com outros jogadores!
                Nesse jogo, você luta com um outro player em um combate por turnos.
                No início escolhe uma classe entre Guerreiro, Feiticeiro e Paladino.
                Quando o combate começa, ocorre as rodadas, que é composto por dois turnos.
                O primeiro é definido aleatoriamente quem ataca e quem defende.
                O segundo é o contrário do primeiro. Assim, toda roda cada um ataca e defende uma vez.
                
                Detalhes do combate:
                O atacante tem três opções, Ataque Físico, Ataque Mágico e Ataque Especial.
                - Ataque Físico escala com a Força.
                - Ataque Mágico escala com a Inteligência.
                - Ataque Especial escala com a soma da Força e Inteligência.
                E o defensor tem três opções, Escudo, Barreira e Counter.
                - Escudo reduz o dano de todos os tipos de ataques, mas pouco do Ataque Mágico.
                - Barreira reduz apenas o dano do Ataque Mágico.
                - Counter devolve inteiramente contra Ataque Especial e não faz nada contra o resto.
                
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
                str.append("----------------------------------------------------------------------------------------------------\n");
                str.append("Sessões disponíveis:\n");
                if(conteudos != null) {
                    for (int i = 1; i < conteudos.length; i += 3) {
                        str.append("id: ").append(conteudos[i]);
                        str.append("\tuser1: ").append(conteudos[i + 1]);
                        str.append("\tuser2: ").append(conteudos[i + 2]);
                        str.append("\n");
                    }
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
                str.append("\n");
                str.append("----------------------------------------------------------------------------------------------------\n");
                break;
            case "205":
                str.append("----------------------------------------------------------------------------------------------------\n");
                str.append("Entrando na sessão:\n");
                if(conteudo.contains(";")){
                    str.append("id: ").append(conteudos[0]);
                    str.append("\tuser1: ").append(conteudos[1]);
                    str.append("\tuser2: ").append(conteudos[2]);
                }
                else{
                    str.append(conteudo);
                }
                str.append("\n");
                str.append("----------------------------------------------------------------------------------------------------\n");
                break;
            case "206":
                str.append("----------------------------------------------------------------------------------------------------\n");
                str.append("Saindo da sessão:\n").append(conteudo).append("\n");
                str.append("----------------------------------------------------------------------------------------------------\n");
                Chat.finalizaChat();
                break;
            case "207":
                str.append("----------------------------------------------------------------------------------------------------\n");
                str.append("Usuário ").append(conteudos[0]).append(" criou a sessão ").append(conteudos[1]).append("!").append("\n");
                str.append("----------------------------------------------------------------------------------------------------\n");
                break;
            case "300":
                str.append("""
                ----------------------------------------------------------------------------------------------------
                Partida iniciada!
                Escolha sua classe usando o comando "/escolher {classe}".
                Ex: "/escolher guerreiro"
                Use "/ajuda" para ver os comandos disponíveis na partida.

                Classes:
                - Guerreiro   vida: 72  defesa: 7  força: 22  inteligência: 10
                - Feiticeiro  vida: 65  defesa: 5  força: 6   inteligência: 26
                - Paladino   vida: 68  defesa: 4  força: 20  inteligência: 16
                ----------------------------------------------------------------------------------------------------
                """);
                break;
            case "301":
                str.append("""
                ----------------------------------------------------------------------------------------------------
                - Use "/escolher {classe}" para escolher a sua classe.
                - Use "/combate {ataque/defesa}" para escolher o tipo de ataque ou defesa.
                - Use "/sair_partida" para sair da partida.
                
                Detalhes do combando:
                A escolha da classe pode ser entre "guerreiro", "feiticeiro" e "paladino".
                O atacante tem três opções, "fisico", "magico" e "especial".
                E o defensor tem três opções, "escudo", "barreira" e "counter".
                ----------------------------------------------------------------------------------------------------
                """);
                break;
            case "303":
                str.append("----------------------------------------------------------------------------------------------------\n");
                str.append("Escolhendo classe:\n").append(conteudo).append("\n");
                str.append("----------------------------------------------------------------------------------------------------\n");
                break;
            case "304":
                str.append("----------------------------------------------------------------------------------------------------\n");
                str.append(conteudo).append("\n");
                str.append("----------------------------------------------------------------------------------------------------\n");
                break;
            case "305":
                str.append("----------------------------------------------------------------------------------------------------\n");
                str.append("Saindo da partida:\n").append(conteudo).append("\n");
                str.append("----------------------------------------------------------------------------------------------------\n");
                break;
            case "306":
                str.append("----------------------------------------------------------------------------------------------------\n");
                str.append(conteudo).append("\n");
                str.append("----------------------------------------------------------------------------------------------------\n");
                break;
            case "400":
                str.append("----------------------------------------------------------------------------------------------------\n");
                str.append("Combate iniciado!\n");
                str.append("Jogador 1: ").append(conteudos[0]).append("\tClasse: ").append(conteudos[1]);
                str.append("\nJogador 2: ").append(conteudos[2]).append("\tClasse: ").append(conteudos[3]);
                str.append("\n");
                str.append("----------------------------------------------------------------------------------------------------\n");
                break;
            case "401":
                str.append("----------------------------------------------------------------------------------------------------\n");
                str.append("Começando Rodada ").append(conteudos[13]).append(":\n\n");
                str.append("Jogador 1: ").append(conteudos[1]).append("\tClasse: ").append(conteudos[2]);
                str.append("\nVida: ").append(conteudos[3]).append("\tDefesa: ").append(conteudos[4]);
                str.append("\tForça: ").append(conteudos[5]).append("\tInteligência: ").append(conteudos[6]);
                str.append("\n\nJogador 2: ").append(conteudos[7]).append("\tClasse: ").append(conteudos[8]);
                str.append("\nVida: ").append(conteudos[9]).append("\tDefesa: ").append(conteudos[10]);
                str.append("\tForça: ").append(conteudos[11]).append("\tInteligência: ").append(conteudos[12]);
                str.append("\n\nVocê começa com: ").append(conteudos[0]).append("\n\n");
                str.append("Se você estiver atacando, use \"/combate {tipo}\" e escolha entre \"fisico\", \"magico\" e \"especial\".\n");
                str.append("Se você estiver defendendo, use \"/combate {tipo}\" e escolha entre \"escudo\", \"barreira\" e \"counter\".\n");
                str.append("----------------------------------------------------------------------------------------------------\n");
                break;
            case "402":
                str.append("----------------------------------------------------------------------------------------------------\n");
                str.append("Você escolheu ").append(conteudos[1]).append(" e o ").append(conteudos[2]);
                str.append(" escolheu ").append(conteudos[3]).append(".\n");
                if(conteudos[0].equals("0")){
                    str.append("Você recebeu ").append(conteudos[4]).append(" de dano e ficou com ");
                    str.append(conteudos[5]).append(" de vida restante.\n");
                }
                else{
                    str.append("Você causou ").append(conteudos[4]).append(" de dano e o oponente ficou com ");
                    str.append(conteudos[5]).append(" de vida restante.\n");
                }
                str.append("----------------------------------------------------------------------------------------------------\n");
                break;
            case "403":
                str.append("----------------------------------------------------------------------------------------------------\n");
                str.append("Agora é a sua vez de ").append(conteudo).append("!\n");
                str.append("----------------------------------------------------------------------------------------------------\n");
                break;
            case "404":
                str.append("----------------------------------------------------------------------------------------------------\n");
                if(conteudo.equals("1")){
                    str.append("""
                     __     ______  _    _    __          _______ _   _ _ 
                     \\ \\   / / __ \\| |  | |   \\ \\        / /_   _| \\ | | |
                      \\ \\_/ / |  | | |  | |    \\ \\  /\\  / /  | | |  \\| | |
                       \\   /| |  | | |  | |     \\ \\/  \\/ /   | | | . ` | |
                        | | | |__| | |__| |      \\  /\\  /   _| |_| |\\  |_|
                        |_|  \\____/ \\____/        \\/  \\/   |_____|_| \\_(_)
                    
                    """);
                }
                else{
                    str.append("""
                     __     ______  _    _     _      ____   _____ ______ 
                     \\ \\   / / __ \\| |  | |   | |    / __ \\ / ____|  ____|
                      \\ \\_/ / |  | | |  | |   | |   | |  | | (___ | |__   
                       \\   /| |  | | |  | |   | |   | |  | |\\___ \\|  __|  
                        | | | |__| | |__| |   | |___| |__| |____) | |____ 
                        |_|  \\____/ \\____/    |______\\____/|_____/|______|  
                    
                    """);
                }
                str.append("----------------------------------------------------------------------------------------------------\n");
                break;
            case "999":
                str.append("");
                break;
            case "1201":
                Chat.iniciaChat(conteudo);
                str.append("CHAT INICIADO COM O USUÁRIO ").append(conteudo);
                break;
            case "1202":
                Chat.write(conteudos[0], conteudos[1]);
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