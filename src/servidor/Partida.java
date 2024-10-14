package servidor;

public class Partida {
    private final Jogador player1;
    private final Jogador player2;
    private Classe classeP1;
    private Classe classeP2;
    private String jogadaP1;
    private String jogadaP2;
    private int rodada;
    private Boolean flagCombate;

    public Partida(Jogador player1, Jogador player2){
        this.player1 = player1;
        this.player2 = player2;
        this.classeP1 = null;
        this.classeP2 = null;
        rodada = 0;
        this.flagCombate = false;
    }

    protected String getJogadaP1(){
        return this.jogadaP1;
    }

    protected String getJogadaP2(){
        return this.jogadaP2;
    }

    protected void setJogadaP1(String jogada){
        this.jogadaP1 = jogada;
    }

    protected void setJogadaP2(String jogada){
        this.jogadaP2 = jogada;
    }

    protected void proxRodada(){
        this.rodada++;
    }

    protected void comecaCombate(){
        this.flagCombate = true;

        String resultado = this.player1.getUsername() + ";" + this.classeP1 + ";"
                + this.player2.getUsername() + ";" + this.classeP2;

        Jogo.comecaJogada(this);
        this.player1.write("400 " + resultado);
        this.player1.write("401 " + comecaRodada(1));
        this.player2.write("400 " + resultado);
        this.player2.write("401 " + comecaRodada(2));

    }

    protected String comecaRodada(int jogador){
        String resultado = "";
        if(jogador == 1){
            resultado += this.getJogadaP1() + ";";
        }
        else{
            resultado += this.getJogadaP2() + ";";
        }
        resultado += this.player1.getUsername() + ";" + this.classeP1 + ";" + this.classeP1.getAtributos() + ";";
        resultado += this.player2.getUsername() + ";" + this.classeP2 + ";" + this.classeP2.getAtributos() + ";";
        resultado += this.rodada;
        return resultado;
    }

    //303
    protected void setClasse(Jogador player, String classe){
        if(this.flagCombate){
            player.write("303 Combate já iniciado");
            return;
        }

        Classe classe_selecionada = Jogo.getClasse(classe);
        if(classe_selecionada == null){
            player.write("303 Classe inválida");
            return;
        }

        if (player != this.player1 && player != this.player2){
            player.write("303 Jogador não encontrado");
            return;
        }

        System.out.println("Jogador " + player.getUsername() + "escolheu " + classe_selecionada);

        if(player == this.player1) this.classeP1 = classe_selecionada;
        else this.classeP2 = classe_selecionada;

        player.write("303 Classe " + classe_selecionada + " selecionada");

        if (this.classeP1 == null || this.classeP2 == null) {
            this.player1.write("306 Aguardando o outro jogador selecionar...");
        }

        else comecaCombate();
    }
}
