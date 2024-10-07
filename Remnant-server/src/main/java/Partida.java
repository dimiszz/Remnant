import java.util.ArrayList;

public class Partida implements Runnable{
    private static ArrayList<Partida> partidas;
    private static int livre = 0;

    private int id;
    private Player player1;
    private Player player2;

    public Partida(){
        this.id = livre;
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

    public static synchronized Partida criaPartida(Player player1){
        if (getQuantidadePartidas() < 5){
            Partida partida = new Partida();
            partida.addPlayer(player1);
            return partida;
        }
        return null;
    }

    public int getId() {
        return id;
    }

    public static int getQuantidadePartidas() {
        return partidas.size();
    }

    public static void listarPartidas(){
        StringBuilder string_partidas = new StringBuilder();
        for(Partida partida : partidas){
            string_partidas.append("{\" ID \" : \" ").append(partida.id).append("\"}");
        }
    }

    public Tuple<String, String> getPlayers() {
        String p1 = null;
        String p2 = null;
        if (player1 != null) p1 = player1.getNome();
        if (player2 != null) p2 = player2.getNome();
        return new Tuple<>(p1, p2);
    }

}
