package servidor;

import static servidor.Classe.random;

public class Jogo {

    protected static Classe getClasse(String classe){
        switch(classe){
            case "guerreiro":
                return Classe.Guerreiro();
            case "geiticeiro":
                return Classe.Feiticeiro();
            case "paladino":
                return Classe.Paladino();
            default:
                return null;
        }
    }


    protected static void escolheTurno(Partida partida){
        int rodada = random.nextInt(2);
        if(rodada == 0){
            partida.getPlayer1().setTurno("Ataque");
            partida.getPlayer2().setTurno("Defesa");
        }
        else{
            partida.getPlayer2().setTurno("Ataque");
            partida.getPlayer1().setTurno("Defesa");
        }
    }

    protected static Boolean escolheJogada(Jogador player1, Jogador player2, String jogada){
        String acao;
        if(player1.getTurno().equals("Ataque")){
            switch(jogada){
                case "fisico":
                    acao = "Ataque Físico";
                    break;
                case "magico":
                    acao = "Ataque Mágico";
                    break;
                case "especial":
                    acao = "Ataque Especial";
                    break;
                default:
                    player1.getUser().write("304 Jogada inválida.");
                    return false;
            }
        }
        else{
            switch (jogada) {
                case "escudo":
                    acao = "Escudo Físico";
                    break;
                case "barreira":
                    acao = "Barreira Mágica";
                    break;
                case "counter":
                    acao = "Counter";
                    break;
                default:
                    player1.getUser().write("304 Jogada inválida.");
                    return false;
            }
        }

        player1.getUser().write("304 " + acao + " selecionado.");
        player2.getUser().write("304 " + player2 + " selecionou a jogada.");
        return true;
    }

    // Considera todas as possibilidades de combate e retorna se alguém foi morto.
    protected static boolean realizaCombate(Jogador player1, Jogador player2){
        Jogador playerAtacante = player1.getTurno().equals("Ataque") ? player1 : player2;
        Jogador playerDefensor = (playerAtacante == player1) ? player2 : player1;

        Classe atacante = playerAtacante.getClasse();
        Classe defensor = playerDefensor.getClasse();

        int dano = atacante.calculaDano(playerAtacante.getJogada());

        switch(playerAtacante.getJogada()){
            case "fisico":
                switch(playerDefensor.getJogada()){
                    case "escudo":
                        dano -= defensor.getDefesa();
                        break;
                    case "barreira":
                        break;
                    case "counter":
                        break;
                }
                break;
            case "magico":
                switch(playerDefensor.getJogada()){
                    case "escudo":
                        dano -= defensor.getDefesa()/2;
                        break;
                    case "barreira":
                        dano -= defensor.getDefesa();
                        dano -= defensor.getInteligencia()/3;
                        break;
                    case "counter":
                        break;
                }
                break;
            case "especial":
                switch(playerDefensor.getJogada()){
                    case "escudo":
                        dano -= defensor.getDefesa();
                        break;
                    case "barreira":
                        break;
                    // Unico caso especial em que o atacante recebe dano
                    case "counter":
                        return causaDano(playerDefensor, playerAtacante, dano);
                }
                break;
        }

        return causaDano(playerAtacante, playerDefensor, dano);
    }

    public static boolean causaDano(Jogador atacante, Jogador defensor, int dano){
        String resultado;

        if(dano < 0) dano = 0;

        defensor.getClasse().tomaDano(dano);

        resultado = "402 1;" + atacante.getJogada() + ";" + defensor.getUser().getUsername() + ";";
        resultado += defensor.getJogada() + ";" + dano + ";" + defensor.getClasse().getVida();
        atacante.getUser().write(resultado);

        resultado = "402 0;" + defensor.getJogada() + ";" + atacante.getUser().getUsername() + ";";
        resultado += atacante.getJogada() + ";" + dano + ";" + defensor.getClasse().getVida();
        defensor.getUser().write(resultado);

        return defensor.getClasse().morto();
    }


    public static void main(String[] args){

    }
}