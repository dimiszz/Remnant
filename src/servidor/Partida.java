package servidor;

import static servidor.Classe.random;

// Classe da partida com os métodos necessários para o combate
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

    // Escolhe aleatoriamente quem começa a rodada
    protected void escolheTurno(){
        int rodada = random.nextInt(2);
        if(rodada == 0){
            this.player1.setTurno("Ataque");
            this.player2.setTurno("Defesa");
        }
        else{
            this.player1.setTurno("Defesa");
            this.player2.setTurno("Ataque");
        }
    }

    // Define a classe do jogador se a entrada for válida
    protected void setClasse(Usuario user, String classe){
        if(this.combateIniciado){
            user.write("303 Combate já iniciado.");
            return;
        }

        Classe classe_selecionada = Classe.getClasse(classe);
        if(classe_selecionada == null){
            user.write("303 Classe inválida.");
            return;
        }

        Jogador jogadorAtual = this.player1;
        Jogador outroJogador = this.player2;

        if(this.player2.checkUser(user)){
            jogadorAtual = this.player2;
            outroJogador = this.player1;
        }

        jogadorAtual.setClasse(classe_selecionada);
        jogadorAtual.write("303 Classe " + classe_selecionada + " selecionada");
        outroJogador.write("303 " + jogadorAtual.getUsername() + " selecionou a classe.");


        if (outroJogador.getClasse() == null) {
            user.write("306 Aguardando o outro jogador selecionar...");
        }
        else comecaCombate();
    }

    protected void comecaCombate(){
        this.combateIniciado = true;

        String str = "";
        str += this.player1.getUsername() + ";" + this.player1.getClasse().toString() + ";";
        str += this.player2.getUsername() + ";" + this.player2.getClasse().toString();

        this.player1.write("400 " + str);
        this.player2.write("400 " + str);
        comecaRodada();
    }

    // Cada rodada tem duas partes, porque cada um precisa atacar e defender uma vez
    // A parte aleatória é só a primeira parte, a segunda parte é sempre o contrário da primeira
    protected void comecaRodada(){
        escolheTurno();
        this.rodada++;

        String str = "";
        str += this.player1.getUsername() + ";" + this.player1.getClasse() + ";" + this.player1.getClasse().getAtributos() + ";";
        str += this.player2.getUsername() + ";" + this.player2.getClasse() + ";" + this.player2.getClasse().getAtributos() + ";";
        str += this.rodada;

        this.player1.write("401 " + this.player1.getTurno() + ";" + str);
        this.player2.write("401 " + this.player2.getTurno() + ";" + str);
        return;
    }

    //Recebe o jogador que realiza o combate e a sua jogada
    protected void combate(Usuario user, String jogada){
        if(!this.combateIniciado){
            user.write("304 Combate ainda não iniciado.");
            return;
        }

        Jogador playerAtual = (this.player1.checkUser(user)) ? this.player1 : this.player2;
        Jogador playerOutro = getOponente(playerAtual);


        String acao = playerAtual.validaJogada(jogada);
        if(acao.equals("invalido")) {
            playerAtual.write("304 Jogada inválida.");
            return;
        }
        playerAtual.setJogada(jogada);

        playerAtual.write("304 " + acao + " selecionado.");
        playerOutro.write("304 " + playerAtual + " selecionou a jogada.");

        if(playerOutro.getJogada() == null){
            playerAtual.write("306 Aguardando o outro jogador selecionar...");
            return;
        }

        // Depois de verificar a possibilidade de combate, realiza o combate
        if(Jogo.realizaCombate(playerAtual, playerOutro)){
            finalizaPartida();
            return;
        }

        if(!this.metadeRodada){
            this.metadeRodada = true;
            inverteTurno();
            playerAtual.write("403 " + playerAtual.getTurno());
            playerOutro.write("403 " + playerOutro.getTurno());
        }
        else{
            this.metadeRodada = false;
            comecaRodada();
        }


        playerAtual.setJogada(null);
        playerOutro.setJogada(null);
    }

    // Verifica quem venceu e perdeu e finaliza a partida
    protected void finalizaPartida(){
        Jogador perdedor = (this.player1.getClasse().morto()) ? this.player1 : this.player2;
        Jogador vencedor = (perdedor == this.player1) ? this.player2 : this.player1;


        vencedor.write("404 1");
        perdedor.write("404 0");
        Sessao.fecharPartida(vencedor.getUser());
    }

    private Jogador getOponente(Jogador jogador) {
        return (this.player1 == jogador) ? this.player2 : this.player1;
    }



    // Inverte o turno dos jogadores usado para metade da rodada
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