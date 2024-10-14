package servidor;

public class Partida {
    private Jogador player1;
    private Jogador player2;
    private String classeP1;
    private String classeP2;
    private Boolean flagCombate;

    public Partida(Jogador player1, Jogador player2){
        this.player1 = player1;
        this.player2 = player2;
        this.classeP1 = null;
        this.classeP2 = null;
        this.flagCombate = false;
    }
    
    protected String setClasse(Jogador player, String classe){
        if(this.flagCombate) return "Combate já iniciado";
        if(player == this.player1){
            this.classeP1 = classe;
            if(this.classeP2 != null) this.flagCombate = true;
            return "Classe " + classe + " selecionada";
        }
        if(player == this.player2){
            this.classeP2 = classe;
            if(this.classeP1 != null) this.flagCombate = true;
            return "Classe " + classe + " selecionada";
        }
        else return "Jogador não encontrado";
    }
}
