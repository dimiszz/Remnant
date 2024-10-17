package servidor;

public class Partida {
    private final Jogador player1;
    private final Jogador player2;
    private Boolean combateIniciado;
    private Boolean metadeRodada;
    private int rodada;

    public Partida(Usuario user1, Usuario user2){
        this.player1 = new Jogador(user1);
        this.player2 = new Jogador(user2);
        this.combateIniciado = false;
        this.metadeRodada = false;
        rodada = 0;
    }

    protected Jogador getPlayer1(){
        return this.player1;
    }

    protected Jogador getPlayer2(){
        return this.player2;
    }

    protected void setClasse(Usuario user, String classe){
        if(this.combateIniciado){
            user.write("303 Combate já iniciado.");
            return;
        }

        Classe classe_selecionada = Jogo.getClasse(classe);
        if(classe_selecionada == null){
            user.write("303 Classe inválida.");
            return;
        }

        Jogador jogadorAtual = this.player1;
        Jogador outroJogador = this.player2;

        if(user == this.player2.getUser()){
            jogadorAtual = this.player2;
            outroJogador = this.player1;
        }

        jogadorAtual.setClasse(classe_selecionada);
        jogadorAtual.getUser().write("303 Classe " + classe_selecionada + " selecionada");
        outroJogador.getUser().write("303 " + jogadorAtual.getUser().getUsername() + " selecionou a classe.");


        if (outroJogador.getClasse() == null) {
            user.write("306 Aguardando o outro jogador selecionar...");
        }
        else comecaCombate();
    }

    protected void comecaCombate(){
        this.combateIniciado = true;

        String str = "";
        str += this.player1.getUser().getUsername() + ";" + this.player1.getClasse().toString() + ";";
        str += this.player2.getUser().getUsername() + ";" + this.player2.getClasse().toString();

        this.player1.getUser().write("400 " + str);
        this.player2.getUser().write("400 " + str);
        comecaRodada();
    }

    // Cada rodada tem duas partes, porque cada um precisa atacar e defender uma vez
    // A parte aleatória é só a primeira parte, a segunda parte é sempre o contrário da primeira
    protected void comecaRodada(){
        Jogo.escolheTurno(this);
        this.rodada++;

        String str = "";
        str += this.player1.getUser().getUsername() + ";" + this.player1.getClasse() + ";" + this.player1.getClasse().getAtributos() + ";";
        str += this.player2.getUser().getUsername() + ";" + this.player2.getClasse() + ";" + this.player2.getClasse().getAtributos() + ";";
        str += this.rodada;

        this.player1.getUser().write("401 " + this.player1.getTurno() + ";" + str);
        this.player2.getUser().write("401 " + this.player2.getTurno() + ";" + str);
        return;
    }

    protected void combate(Usuario user, String jogada){
        if(!this.combateIniciado){
            user.write("304 Combate ainda não iniciado.");
            return;
        }

        Jogador playerAtual = (user == this.player1.getUser()) ? this.player1 : this.player2;
        Jogador playerOutro = (playerAtual == this.player1) ? this.player2 : this.player1;


        if(!Jogo.escolheJogada(playerAtual, playerOutro, jogada)) return;

        playerAtual.setJogada(jogada);

        if(playerOutro.getJogada() == null){
            playerAtual.getUser().write("306 Aguardando o outro jogador selecionar...");
            return;
        }

        if(Jogo.realizaCombate(this.player1, this.player2)){
            finalizaPartida();
            return;
        }

        if(!this.metadeRodada){
            this.metadeRodada = true;
            inverteTurno();
            this.player1.getUser().write("403 " + this.player1.getTurno());
            this.player2.getUser().write("403 " + this.player2.getTurno());
        }
        else{
            this.metadeRodada = false;
            comecaRodada();
        }


        this.player1.setJogada(null);
        this.player2.setJogada(null);
        return;
    }

    protected void finalizaPartida(){
        Jogador perdedor = (this.player1.getClasse().morto()) ? this.player1 : this.player2;
        Jogador vencedor = (perdedor == this.player1) ? this.player2 : this.player1;


        vencedor.getUser().write("404 1");
        perdedor.getUser().write("404 0");
        Sessao.sairPartida(vencedor.getUser());
        //Sessao.sairPartida(perdedor.getUser());
    }

    protected void inverteTurno(){
        if(player1.getTurno().equals("Ataque")){
            player1.setTurno("Defesa");
            player2.setTurno("Ataque");
        }
        else{
            player1.setTurno("Ataque");
            player2.setTurno("Defesa");
        }
    }
}