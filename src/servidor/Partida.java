package servidor;

import java.util.HashMap;

public class Partida implements Runnable{
    private static HashMap<Integer, Partida> partidas = new HashMap<>();
    private static int livre = 0;

    private int id;
    private Jogador player1;
    private Jogador player2;

    public Partida(){
        this.id = livre;
        partidas.put(this.id, this);
        livre++;
    }

    public void addPlayer(Jogador player){
        player.setPartida(this.id);
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
        StringBuilder resultado = new StringBuilder("101");

        for(Partida partida : partidas.values()){
            resultado.append(" ")
                    .append(partida.getId())
                    .append(" ").append(partida.getPlayers().x)
                    .append(" ").append(partida.getPlayers().y)
                    .append("\n");
        }

        return resultado.toString();
    }

    public static synchronized String criaPartida(Jogador player1){
        String result;
        if (getQuantidadePartidas() < 5){
            Partida partida = new Partida();
            partida.addPlayer(player1);
            result = partida.getId() + " " + player1.getUsername() + " " + null;
            return result;
        }
        result = "Não foi possível criar a partida: número máximo atingido.";
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
