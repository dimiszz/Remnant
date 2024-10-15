package servidor;

public class Partida {
    private final Jogador player1;
    private final Jogador player2;
    private Boolean combateIniciado;
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

        System.out.println("Jogador " + user.getUsername() + "escolheu " + classe_selecionada);

        if(user == this.player1.getUser()) this.player1.setClasse(classe_selecionada);
        else this.player2.setClasse(classe_selecionada);

        user.write("303 Classe " + classe_selecionada + " selecionada");

        if (this.player1.getClasse() == null || this.player2.getClasse() == null) {
            this.player1.getUser().write("306 Aguardando o outro jogador selecionar...");
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

        Jogador playerAtual, playerOutro;
        if(user == this.player1.getUser()){
            playerAtual = this.player1;
            playerOutro = this.player2;
        }
        else{
            playerAtual = this.player2;
            playerOutro = this.player1;
        }

        if(Jogo.escolheJogada(playerAtual, playerOutro, jogada)){
            playerAtual.setJogada(jogada);
        }
        else{
            return;
        }

        if(this.player1.getJogada() == null || this.player2.getJogada() == null){
            playerAtual.getUser().write("306 Aguardando o outro jogador selecionar...");
            return;
        }
        else{
            if(this.player1.getTurno().equals("Ataque")){
                Jogo.realizaCombate(this.player1, this.player2);
            }
            else{
                Jogo.realizaCombate(this.player2, this.player1);
            }
        }
        return;
    }
}