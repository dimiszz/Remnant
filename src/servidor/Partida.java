package servidor;

import java.util.HashMap;

public class Partida implements Runnable {
    private static final HashMap<Integer, Partida> partidas = new HashMap<>();
    private static int livre = 0;
    private final int id;
    private Jogador player1;
    private Jogador player2;

    public Partida(){
        this.id = livre;
        partidas.put(livre, this);
        livre++;
        this.player1 = null;
        this.player2 = null;
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

        player.setPartida(this.id);
        return true;
    }

    public static String listarPartidas(){
        StringBuilder resultado = new StringBuilder(Integer.toString(partidas.size()));

        for(Partida partida : partidas.values()){
            resultado.append(";").append(partida.getId())
                    .append(";").append(partida.getPlayer1())
                    .append(";").append(partida.getPlayer2());
        }

        return resultado.toString();
    }

    public static String criaPartida(Jogador player){
        if(partidas.get(player.getPartida()) != null) return "Você já está em uma partida.";
        if(partidas.size() >= 5) return "Não foi possível criar a partida: número máximo atingido.";

        StringBuilder resultado = new StringBuilder();
        Partida partida = new Partida();
        partida.addPlayer(player);
        resultado.append(partida.getId()).append(";").append(player.getUsername());

        return resultado.toString();
    }

    public static String entrarPartida(Jogador player, String id){
        if(player.getPartida() != -1) return "Você já está em uma partida.";
        if(id == "") return "ID da partida não foi informado.";
        //https://stackoverflow.com/questions/18711896/how-can-i-prevent-java-lang-numberformatexception-for-input-string-n-a
        if(!id.matches("\\d+")) return "ID da partida inválido.";
        Partida partida  = partidas.get(Integer.parseInt(id));
        if(partida == null) return "Partida não foi encontrada.";
        if(!partida.addPlayer(player)) return "Partida está cheia.";

        return partida.getId() +
                ";" + partida.getPlayer1() +
                ";" + partida.getPlayer2();
    }

    public static String sairPartida(Jogador player){
        if(player.getPartida() != -1){
            Partida partida = partidas.get(player.getPartida());
            if(partida.player1 == player){
                partida.player1 = null;
                player.setPartida(-1);
                if(partida.player1 == null){
                    partidas.remove(partida.getId());
                }
                return "Você saiu da partida " + partida.getId() + ".";
            }
            else if(partida.player2 == player){
                partida.player2 = null;
                player.setPartida(-1);
                if(partida.player1 == null){
                    partidas.remove(partida.getId());
                }
                return "Você saiu da partida " + partida.getId() + ".";
            }
        }
        return "Você não está em nenhuma partida.";
    }

    @Override
    public void run() {
    
    }
}
