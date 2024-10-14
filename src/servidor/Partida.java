package servidor;

public class Partida {
    private Jogador player1;
    private Jogador player2;
    private String classeP1;
    private String classeP2;
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

    protected Jogador getPlayer1(){
        return this.player1;
    }

    protected Jogador getPlayer2(){
        return this.player2;
    }

    protected String getClasseP1(){
        return this.classeP1;
    }

    protected String getClasseP2(){
        return this.classeP2;
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

    protected String comecaCombate(){
        this.flagCombate = true;
        
        String resultado = "";
        resultado += this.player1.getUsername() + ";" + this.classeP1 + ";";
        resultado += this.player2.getUsername() + ";" + this.classeP2;
        return resultado;
    }

    protected String comecaRodada(int jogador){
        String resultado = "";
        if(jogador == 1){
            resultado += this.getJogadaP1() + ";";
        }
        else{
            resultado += this.getJogadaP2() + ";";
        }
        resultado += this.getPlayer1().getUsername() + ";" + this.getClasseP1() + ";" + Jogo.getAtributos(this.getClasseP1()) + ";";
        resultado += this.getPlayer2().getUsername() + ";" + this.getClasseP2() + ";" + Jogo.getAtributos(this.getClasseP2()) + ";";
        resultado += this.rodada;
        return resultado;
    }
    
    //303
    protected void setClasse(Jogador player, String classe){
        if(this.flagCombate){
            player.write("303 Combate já iniciado");
            return;
        }
        
        classe = Jogo.getClasse(classe);
        if(classe == "invalido"){
            player.write("303 Classe inválida");
            return;
        }

        if(player == this.player1){
            this.classeP1 = classe;
            if(this.classeP2 == null){
                this.player1.write("303 Classe " + classe + " selecionada");
                this.player1.write("306 Aguardando o outro jogador selecionar...");
                return;
            }
            else{
                Jogo.comecaJogada(this);
                this.player2.write("400 " + comecaCombate());
                this.player2.write("401 " + comecaRodada(2));
                this.player1.write("303 Classe " + classe + " selecionada");
                this.player1.write("400 " + comecaCombate());
                this.player1.write("401 " + comecaRodada(1));
                return;
            }
        }
        else if(player == this.player2){
            this.classeP2 = classe;
            if(this.classeP1 == null){
                this.player2.write("303 Classe " + classe + " selecionada");
                this.player2.write("306 Aguardando o outro jogador selecionar...");
                return;
            }
            else{
                Jogo.comecaJogada(this);
                this.player1.write("400 " + comecaCombate());
                this.player1.write("401 " + comecaRodada(2));
                this.player2.write("303 Classe " + classe + " selecionada");
                this.player2.write("400 " + comecaCombate());
                this.player2.write("401 " + comecaRodada(1));
                return;
            }
        }

        player.write("303 Jogador não encontrado");
        return;
    }
}
