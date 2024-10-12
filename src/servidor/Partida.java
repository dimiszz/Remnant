package servidor;

import java.util.HashMap;

public class Partida{
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
        String p1 = null;
        if (player1 != null) p1 = this.player1.getUsername();
        return p1;
    }

    private String getPlayer2() {
        String p2 = null;
        if (player1 != null) p2 = this.player2.getUsername();
        return p2;
    }

    private void addPlayer(Jogador player){
        player.setPartida(this.id);
        if(this.player1 == null){
            this.player1 = player;
        }
        else if(this.player2 == null){
            this.player2 = player;
        }
    }

    public static String listarPartidas(){
        String resultado = "101 " + partidas.size();

        for(Partida partida : partidas.values()){
            resultado += ";" + partida.getId() + ";" + partida.getPlayer1() + ";" + partida.getPlayer2();
        }

        return resultado.toString();
    }

    public static String criaPartida(Jogador player){
        String result;
        if (partidas.size() < 5){
            Partida partida = new Partida();
            partida.addPlayer(player);
            result = partida.getId() + " " + player.getUsername() + " " + null;
            return result;
        }
        result = "Não foi possível criar a partida: número máximo atingido.";
        return result;
    }
}
