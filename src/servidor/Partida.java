package servidor;

import java.security.spec.RSAOtherPrimeInfo;
import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Phaser;

public class Partida implements Runnable {
    private static final HashMap<Integer, Partida> partidas = new HashMap<>();
    private static final int maximoPartidas = 5;
    private static int livre = 0;
    private final int id;
    private Jogador player1;
    private Jogador player2;
    //https://www.geeksforgeeks.org/arrayblockingqueue-class-in-java/
    BlockingQueue<String> filaRespostas = new ArrayBlockingQueue<>(2);
    //https://www.baeldung.com/java-countdown-latch
    Phaser phaser = new Phaser(1);

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

    private boolean partidaPronta(){
        return this.player1 != null && this.player2 != null;
    }

    private synchronized boolean addPlayer(Jogador player){
        if(this.player1 == null) this.player1 = player;
        else if(this.player2 == null) this.player2 = player;
        else return false;

        player.setPartidaId(this.id);
        return true;
    }
    public boolean removePlayer(Jogador player){
        if(this.player1 == player){
            this.player1 = null;
            player.removePartida();
        }
        else if(this.player2 == player){
            this.player2 = null;
            player.removePartida();
        }
        else return false;
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
        if(partidas.get(player.getPartidaId()) != null) return "Você já está em uma partida.";
        if(partidas.size() >= Partida.maximoPartidas)
            return "Não foi possível criar a partida: número máximo atingido.";

        Partida partida = new Partida();
        partida.addPlayer(player);

        return partida.getId() + ";" + player.getUsername();
    }

    public static String entrarPartida(Jogador player, String id){
        if(player.estaEmPartida()) return "Você já está em uma partida.";
        //https://stackoverflow.com/questions/18711896/how-can-i-prevent-java-lang-numberformatexception-for-input-string-n-a
        if(!id.matches("\\d+")) return "ID da partida inválido.";
        Partida partida  = partidas.get(Integer.parseInt(id));
        if(partida == null) return "Partida não foi encontrada.";
        if(!partida.addPlayer(player)) return "Partida está cheia.";

        if (partida.partidaPronta()) {
            partida.jogadoresProntos();
            Thread.ofPlatform().daemon().start(partida);
        }

        return partida.getId() +
                ";" + partida.getPlayer1() +
                ";" + partida.getPlayer2();
    }

    public static String sairPartida(Jogador player){
        if(player.getPartidaId() == -1) return "Você não está em nenhuma partida.";

        Partida partida = partidas.get(player.getPartidaId());

        if(!partida.removePlayer(player)) return "Você não está na partida " + partida.getId();

        return "Você saiu da partida " + partida.getId() + ".";
    }

    public static Partida getPartida(int id){
        Partida partida = partidas.get(id);
        if (partida == null) System.err.println("Partida não encontrada.");
        return partida;
    }

    public void broadcast(String message){
        this.player1.write(message);
        this.player2.write(message);
    }

    public void jogadoresProntos(){
        this.player1.setEmJogo(true);
        this.player1.setFilaRespostas(this.filaRespostas);
        this.player2.setEmJogo(true);
        this.player2.setFilaRespostas(this.filaRespostas);
    }

    @Override
    public void run() {
        try {
            Thread.sleep(1000);
            broadcast("207");

            this.player1.setPhaser(this.phaser);
            this.player2.setPhaser(this.phaser);

            while(true) {
                System.out.println("Esperando mensagem ser colocada na fila para avançar servidor.");
                phaser.arriveAndAwaitAdvance();

                for (int i = 0; i < 2; i++) {
                    String resposta = filaRespostas.take();
                    String[] dados = resposta.split(";");
                    System.out.println("resposta do cliente " + dados[0] + ": " + resposta);
                    filaRespostas.put(dados[0] + ";obrigado pela rosa.");
                }

                System.out.println("Avisando que as mensagens foram processadas!");
                this.phaser.arriveAndAwaitAdvance();
                System.out.println("Esperando clientes finalizarem suas solicitações.");
                this.phaser.arriveAndAwaitAdvance();
            }
            //phaser.arriveAndAwaitAdvance();

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

}
