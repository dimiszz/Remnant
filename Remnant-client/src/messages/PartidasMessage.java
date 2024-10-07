package messages;

import java.util.ArrayList;

public class PartidasMessage {
    public ArrayList<PartidaMessage> partidas;
    public PartidasMessage() {
        this.partidas = new ArrayList<>();
    }

    public void addPartida(String id, String jogador1, String jogador2){
        this.partidas.add(new PartidaMessage(id, jogador1, jogador2));
    }
}
