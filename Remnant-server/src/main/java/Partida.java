import messages.PartidaMessage;
import messages.PartidasMessage;
import messages.Response;

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

    public static Result<PartidasMessage> listarPartidas(){
        PartidasMessage message = new PartidasMessage();
        for(Partida partida : partidas){
            Tuple<String, String> players = partida.getPlayers();
            message.addPartida(String.valueOf(partida.getId()),
                    players.x, players.y);
        }
        return Result.OK("101", message);
    }

    public static synchronized Result<PartidaMessage> criaPartida(Player player1){
        if (getQuantidadePartidas() < 5){
            Partida partida = new Partida();
            partida.addPlayer(player1);
            return Result.OK("102", new PartidaMessage(String.valueOf(partida.getId()),
                    partida.player1.getNome(),null));
        }
        return Result.Error("Não foi possível criar a partida: número máximo atingido.");
    }

    public Tuple<String, String> getPlayers() {
        String p1 = null;
        String p2 = null;
        if (player1 != null) p1 = player1.getNome();
        if (player2 != null) p2 = player2.getNome();
        return new Tuple<>(p1, p2);
    }
}
