package servidor;

import java.util.HashMap;

public class Partida implements Runnable{
    private static final HashMap<Integer, Partida> partidas = new HashMap<>();
    private static int livre = 0;
    private final int id;
    private Jogador player1;
    private Jogador player2;

    public Partida(){
        this.id = livre;
        partidas.put(this.id, this);
        livre++;
    }

    private int getId() {
        return id;
    }

    private String getPlayer1() {
        String p1 = "[VAGO]";
        if (player1 != null) p1 = this.player1.getUsername();
        return p1;
    }

    private String getPlayer2() {
        String p2 = "[VAGO]";
        if (player2 != null) p2 = this.player2.getUsername();
        return p2;
    }

    private synchronized boolean addPlayer(Jogador player){
        if(this.player1 == null) this.player1 = player;
        else if(this.player2 == null) this.player2 = player;
        else return false;

        player.setPartida(this.id);
        return true;
    }

    public static StringBuilder listarPartidas(){
        StringBuilder resultado = new StringBuilder(Integer.toString(partidas.size()));

        for(Partida partida : partidas.values()){
            resultado.append(";").append(partida.getId())
                    .append(";").append(partida.getPlayer1())
                    .append(";").append(partida.getPlayer2());
        }

        return resultado;
    }

    public static String criaPartida(Jogador player){
        if (partidas.size() >= 5) return "Não foi possível criar a partida: número máximo atingido.";

        StringBuilder resultado = new StringBuilder();
        Partida partida = new Partida();
        partida.addPlayer(player);
        resultado.append(partida.getId()).append(";").append(player.getUsername());

        return resultado.toString();

    }

    public static String entrarPartida(Jogador player, String id){
        Partida partida  = partidas.get(Integer.parseInt(id));
        if(partida == null) return "Partida não foi encontrada.";
        if (!partida.addPlayer(player)) return "Partida está cheia.";

        return partida.getId() +
                ";" + partida.getPlayer1() +
                ";" + partida.getPlayer2();
    }

    @Override
    public void run() {
    
    }
}
