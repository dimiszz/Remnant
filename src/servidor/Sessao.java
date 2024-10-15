package servidor;

import java.util.HashMap;

public class Sessao {
    private static final HashMap<Integer, Sessao> sessoes = new HashMap<>();
    private static final int maximoSessoes = 5;
    private static int livre = 0;
    private final int id;
    private Usuario user1;
    private Usuario user2;
    private Partida partida;

    public Sessao(){
        this.id = livre;
        sessoes.put(livre, this);
        livre++;
        this.user1 = null;
        this.user2 = null;
        this.partida = null;
    }

    private int getId(){
        return id;
    }

    private String getuser1(){
        String p1 = "[VAGO]";
        if (user1 != null) p1 = this.user1.getUsername();
        return p1;
    }

    private String getuser2(){
        String p2 = "[VAGO]";
        if (user2 != null) p2 = this.user2.getUsername();
        return p2;
    }

    private synchronized boolean addPlayer(Usuario player){
        if(this.user1 == null) this.user1 = player;
        else if(this.user2 == null) this.user2 = player;
        else return false;

        player.setSessao(this.id);
        return true;
    }

    private synchronized boolean removePlayer(Usuario player){
        if(this.user1 == player){
            this.user1 = null;
            player.removeSessao();
        }
        else if(this.user2 == player){
            this.user2 = null;
            player.removeSessao();
        }
        else return false;
        return true;
    }

    protected static void listarSessoes(Usuario player){
        StringBuilder resultado = new StringBuilder(Integer.toString(sessoes.size()));

        for(Sessao sessao : sessoes.values()){
            resultado.append(";").append(sessao.getId())
                    .append(";").append(sessao.getuser1())
                    .append(";").append(sessao.getuser2());
        }

        player.write("203 " + resultado.toString());
    }

    protected static void criaSessao(Usuario player){
        if(sessoes.get(player.getSessao()) != null){
            player.write("204 Você já está em uma sessão.");
            return;
        }
        if(sessoes.size() >= Sessao.maximoSessoes){
            player.write("204 Não foi possível criar a sessão: número máximo atingido.");
            return;
        }

        Sessao sessao = new Sessao();
        sessao.addPlayer(player);
        player.write("204 " + sessao.getId() + ";" + player.getUsername());
    }

    protected static void entrarSessao(Usuario player, String id){
        if(player.estaEmSessao()){
            player.write("205 Você já está em uma sessão.");
            return;
        }
        //https://stackoverflow.com/questions/18711896/how-can-i-prevent-java-lang-numberformatexception-for-input-string-n-a
        if(!id.matches("\\d+")){
            player.write("205 ID da sessao inválido.");
            return;
        }

        Sessao sessao  = sessoes.get(Integer.parseInt(id));
        if(sessao == null){
            player.write("205 Sessão não foi encontrada.");
            return;
        }
        if(!sessao.addPlayer(player)){
            player.write("205 Sessão está cheia.");
            return;
        }

        if(sessao.user1 != null && sessao.user2 != null){
            sessao.user1.setPartida(true);
            sessao.user2.setPartida(true);
            sessao.partida = new Partida(sessao.user1, sessao.user2);

            if(sessao.user1 == player){
                sessao.user2.write("306 " + player.getUsername() + " entrou na partida!");
                sessao.user2.write("300");
            }
            else if(sessao.user2 == player){
                sessao.user1.write("306 " + player.getUsername() + " entrou na partida!");
                sessao.user1.write("300");
            }
            player.write("205 " + sessao.getId() + ";" + sessao.getuser1() + ";" + sessao.getuser2());
            player.write("300");
            return;
        }

        player.write("205 " + sessao.getId() + ";" + sessao.getuser1() + ";" + sessao.getuser2());
    }

    protected static void sairSessao(Usuario player){
        if(player.getSessao() == -1){
            player.write("206 Você não está em nenhuma sessão.");
            return;
        }

        Sessao sessao = sessoes.get(player.getSessao());
        if(!sessao.removePlayer(player)){
            player.write("206 Você não está na sessão " + sessao.getId() + ".");
            return;
        }

        if(sessao.user1 == null && sessao.user2 == null){
            sessoes.remove(sessao.getId());
        }
        player.write("206 Você saiu da sessão " + sessao.getId() + ".");
        return;
    }

    protected static void escolheClasse(Usuario player, String classe){
        sessoes.get(player.getSessao()).partida.setClasse(player, classe);
    }

    protected static void escolheCombate(Usuario player, String jogada){
        sessoes.get(player.getSessao()).partida.combate(player, jogada);
    }

    protected static void sairPartida(Usuario playerAtual){
        Sessao sessao = sessoes.get(playerAtual.getSessao());
        sessao.partida = null;
        playerAtual.setPartida(false);

        if(sessao.user1 == playerAtual){
            Usuario playerOutro = sessao.user2;
            playerOutro.setPartida(false);
            playerOutro.write("305 Seu oponente saiu da partida.");
            sairSessao(playerOutro);
        }
        else if(sessao.user2 == playerAtual){
            Usuario playerOutro = sessao.user1;
            playerOutro.setPartida(false);
            playerOutro.write("305 Seu oponente saiu da partida.");
            sairSessao(playerOutro);
        }
        
        playerAtual.write("305 Você saiu da partida.");
        sairSessao(playerAtual);
        return;
    }
}
