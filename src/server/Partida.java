package server;

import java.util.ArrayList;

public class Partida implements Runnable{
    private static ArrayList<Partida> partidas = new ArrayList<>();
    private static int livre = 0;

    private int id;
    private Player player1;
    private Player player2;

    public Partida(){
        this.id = livre;
        partidas.add(this);
        livre++;
    }

    public void addPlayer(Player player){
        if (this.player1 == null){
            this.player1 = player;
        }
        else if (this.player2 == null) this.player2 = player;
    }

    @Override
    public void run() {

    }

    public int getId() {
        return id;
    }

    public static int getQuantidadePartidas() {
        return partidas.size();
    }

    public static String listarPartidas(){
        String resultado = "101";

        for(Partida partida : partidas){
            resultado += " " + String.valueOf(partida.getId()) + " " + partida.getPlayers().x + " " + partida.getPlayers().y + "\n";
        }

        return resultado;
    }

    public static synchronized String criaPartida(Player player1){
        String result;
        if (getQuantidadePartidas() < 5){
            Partida partida = new Partida();
            partida.addPlayer(player1);
            result = "102" + " " + String.valueOf(partida.getId()) + " " + player1.getUsername() + " " + null;
            return result;
        }
        result = "100 Não foi possível criar a partida: número máximo atingido.";
        return result;
    }

    public Tuple<String, String> getPlayers() {
        String p1 = null;
        String p2 = null;
        if (player1 != null) p1 = player1.getUsername();
        if (player2 != null) p2 = player2.getUsername();
        return new Tuple<>(p1, p2);
    }
}
