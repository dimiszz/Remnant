package servidor;

import java.util.HashMap;

public class Sessao {
    private static final HashMap<Integer, Sessao> sessoes = new HashMap<>();
    private static final int maximoSessoes = 5;
    private static int livre = 0;
    private final int id;
    private Jogador player1;
    private Jogador player2;
    private Partida partida;

    public Sessao(){
        this.id = livre;
        sessoes.put(livre, this);
        livre++;
        this.player1 = null;
        this.player2 = null;
        this.partida = null;
    }

    private int getId(){
        return id;
    }

    private String getPlayer1(){
        String p1 = "[VAGO]";
        if (player1 != null) p1 = this.player1.getUsername();
        return p1;
    }

    private String getPlayer2(){
        String p2 = "[VAGO]";
        if (player2 != null) p2 = this.player2.getUsername();
        return p2;
    }

    private synchronized boolean addPlayer(Jogador player){
        if(this.player1 == null) this.player1 = player;
        else if(this.player2 == null) this.player2 = player;
        else return false;

        player.setSessao(this.id);
        return true;
    }

    private synchronized boolean removePlayer(Jogador player){
        if(this.player1 == player){
            this.player1 = null;
            player.removeSessao();
        }
        else if(this.player2 == player){
            this.player2 = null;
            player.removeSessao();
        }
        else return false;
        return true;
    }

    protected static String listarSessoes(){
        StringBuilder resultado = new StringBuilder(Integer.toString(sessoes.size()));

        for(Sessao sessao : sessoes.values()){
            resultado.append(";").append(sessao.getId())
                    .append(";").append(sessao.getPlayer1())
                    .append(";").append(sessao.getPlayer2());
        }

        return resultado.toString();
    }

    protected static String criaSessao(Jogador player){
        if(sessoes.get(player.getSessao()) != null) return "Você já está em uma sessão.";
        if(sessoes.size() >= Sessao.maximoSessoes)
            return "Não foi possível criar a sessão: número máximo atingido.";

        Sessao sessao = new Sessao();
        sessao.addPlayer(player);

        return sessao.getId() + ";" + player.getUsername();
    }

    protected static String entrarSessao(Jogador player, String id){
        if(player.estaEmSessao()) return "Você já está em uma sessão.";
        //https://stackoverflow.com/questions/18711896/how-can-i-prevent-java-lang-numberformatexception-for-input-string-n-a
        if(!id.matches("\\d+")) return "ID da sessao inválido.";

        Sessao sessao  = sessoes.get(Integer.parseInt(id));
        if(sessao == null) return "Sessão não foi encontrada.";
        if(!sessao.addPlayer(player)) return "Sessão está cheia.";

        if(sessao.player1 != null && sessao.player2 != null){
            sessao.player1.setPartida(true);
            sessao.player2.setPartida(true);
            sessao.partida = new Partida(sessao.player1, sessao.player2);
            if(sessao.player1 == player) sessao.player2.write("300");
            
            if(sessao.player2 == player) sessao.player1.write("300");
            return sessao.getId() + ";" + sessao.getPlayer1() + ";" + sessao.getPlayer2() + "\n300";
        }

        return sessao.getId() + ";" + sessao.getPlayer1() + ";" + sessao.getPlayer2();
    }

    protected static String sairSessao(Jogador player){
        if(player.getSessao() == -1) return "Você não está em nenhuma sessão.";

        Sessao sessao = sessoes.get(player.getSessao());

        if(!sessao.removePlayer(player)) return "Você não está na sessão " + sessao.getId();

        return "Você saiu da sessão " + sessao.getId() + ".";
    }

    protected static String escolheClasse(Jogador player, String classe){
        return sessoes.get(player.getSessao()).partida.setClasse(player, classe);
    }

    protected static String sairPartida(Jogador playerAtual){
        Sessao sessao = sessoes.get(playerAtual.getSessao());
        sessao.partida = null;
        playerAtual.setPartida(false);

        if(sessao.player1 == playerAtual){
            Jogador playerOutro = sessao.player2;
            playerOutro.setPartida(false);
            playerOutro.write("306 Seu oponente saiu da partida.\n206 " + sairSessao(playerAtual));
        }
        if(sessao.player2 == playerAtual){
            Jogador playerOutro = sessao.player1;
            playerOutro.setPartida(false);
            playerOutro.write("306 Seu oponente saiu da partida.\n206 " + sairSessao(playerAtual));
        }
        
        return "Você saiu da partida.\n206 " + sairSessao(playerAtual);
    }
}
