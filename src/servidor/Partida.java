package servidor;

public class Partida {
    private final Jogador player1;
    private final Jogador player2;
    private Classe classeP1;
    private Classe classeP2;
    private String turnoP1;
    private String turnoP2;
    private String jogadaP1;
    private String jogadaP2;
    private int rodada;
    private Boolean combateIniciado;

    public Partida(Jogador player1, Jogador player2){
        this.player1 = player1;
        this.player2 = player2;
        this.classeP1 = null;
        this.classeP2 = null;
        this.jogadaP1 = null;
        this.jogadaP2 = null;
        rodada = 0;
        this.combateIniciado = false;
    }

    protected String getTurnoP1(){
        return this.turnoP1;
    }

    protected String getTurnoP2(){
        return this.turnoP2;
    }

    protected void setTurnoP1(String turno){
        this.turnoP1 = turno;
    }

    protected void setTurnoP2(String turno){
        this.turnoP2 = turno;
    }

    protected void setClasse(Jogador player, String classe){
        if(this.combateIniciado){
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

    protected void comecaCombate(){
        this.combateIniciado = true;

        String str = "";
        str += this.player1.getUsername() + ";" + this.classeP1 + ";";
        str += this.player2.getUsername() + ";" + this.classeP2;

        this.player1.write("400 " + str);
        this.player2.write("400 " + str);
        comecaRodada();
    }

    // Cada rodada tem duas partes, porque cada um precisa atacar e defender uma vez
    // A parte aleatória é só a primeira parte, a segunda parte é sempre o contrário da primeira
    protected void comecaRodada(){
        Jogo.escolheTurno(this);
        this.rodada++;

        String str = "";
        str += this.player1.getUsername() + ";" + this.classeP1 + ";" + this.classeP1.getAtributos() + ";";
        str += this.player2.getUsername() + ";" + this.classeP2 + ";" + this.classeP2.getAtributos() + ";";
        str += this.rodada;

        this.player1.write("401 " + this.getTurnoP1() + ";" + str);
        this.player2.write("401 " + this.getTurnoP2() + ";" + str);
        return;
    }

    protected void combate(Jogador playerAtual, String jogada){
        if(!this.combateIniciado){
            playerAtual.write("304 Combate ainda não iniciado");
            return;
        }        

        String turno;
        Jogador playerOutro;
        if(playerAtual == this.player1){
            turno = this.turnoP1;
            playerOutro = this.player2;
            if(Jogo.escolheJogada(this, playerAtual, playerOutro, turno, jogada)) this.jogadaP1 = jogada;
            else return;
        }
        else{
            turno = this.turnoP2;
            playerOutro = this.player1;
            if(Jogo.escolheJogada(this, playerAtual, playerOutro, turno, jogada)) this.jogadaP2 = jogada;
            else return;
        }

        if(this.jogadaP1 == null || this.jogadaP2 == null){
            playerAtual.write("306 Aguardando o outro jogador selecionar...");
            return;
        }
        else{
            Jogo.calculaDano(classeP1, jogada);
            // Precisa considerar muitos ifs e elses
            return;
        }
    }
}
