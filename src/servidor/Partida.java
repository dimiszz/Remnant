package servidor;

import java.util.ArrayList;

public class Partida implements Runnable{
    private static final ArrayList<Partida> partidas = new ArrayList<>();
    private static int livre = 0;
    private final int id;
    private Jogador player1;
    private Jogador player2;

    public Partida(){
        this.id = livre;
        partidas.add(this);
        livre++;
    }

    private int getId() {
        return id;
    }

    private String getPlayer1() {
        String p1 = "null";
        if (player1 != null) p1 = this.player1.getUsername();
        return p1;
    }

    private String getPlayer2() {
        String p2 = "null";
        if (player2 != null) p2 = this.player2.getUsername();
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
        String resultado = Integer.toString(partidas.size());
        System.out.println(resultado);

        for(Partida partida : partidas){
            resultado += ";" + partida.getId() + ";" + partida.getPlayer1() + ";" + partida.getPlayer2();
        }
        System.out.println(resultado);

        return resultado;
    }

    public static String criaPartida(Jogador player){
        String resultado;
        if (partidas.size() < 5){
            Partida partida = new Partida();
            partida.addPlayer(player);
            resultado = partida.getId() + ";" + player.getUsername();
            return resultado;
        }
        resultado = "Não foi possível criar a partida: número máximo atingido.";
        return resultado;
    }

    @Override
    public void run() {
    
    }
}
