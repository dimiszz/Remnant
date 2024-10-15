package servidor;

import java.util.Random;

public class Jogo {
    /*
    - Guerreiro   vida: 72  defesa: 7  físico: 22  inteligência: 10
    - Feiticeiro  vida: 65  defesa: 5  físico: 6   inteligência: 26
    - Paladino   vida: 68  defesa: 4  físico: 20  inteligência: 16
    */

    public static Random random = new Random();

    protected static Classe getClasse(String classe){
        switch(classe){
            case "Guerreiro":
                return Classe.Guerreiro();
            case "Feiticeiro":
                return Classe.Feiticeiro();
            case "Paladino":
                return Classe.Paladino();
            default:
                return null;
        }
    }

    protected static void escolheTurno(Partida partida){
        int rodada = random.nextInt(2);
        if(rodada == 0){
            partida.setTurnoP2("Defesa");
            partida.setTurnoP1("Ataque");
        }
        else{
            partida.setTurnoP1("Defesa");
            partida.setTurnoP2("Ataque");
        }
    }

    protected static Boolean escolheJogada(Partida partida, Jogador playerAtual, Jogador playerOutro, String turno, String jogada){
        if(turno.equals("Ataque")){
            switch (jogada) {
                case "Fisico":
                    playerAtual.write("304 Ataque físico selecionado.");
                    playerOutro.write("304 " + playerOutro.getUsername() + " selecionou a jogada.");
                    return true;
                case "Magico":
                    playerAtual.write("304 Ataque mágico selecionado.");
                    playerOutro.write("304 " + playerOutro.getUsername() + " selecionou a jogada.");
                    return true;
                case "Especial":
                    playerAtual.write("304 Ataque especial selecionado.");
                    playerOutro.write("304 " + playerOutro.getUsername() + " selecionou a jogada.");
                    return true;
                default:
                    playerAtual.write("304 Jogada inválida");
                    return false;
            }
        }
        else{
            switch (jogada) {
                case "Fisico":
                    playerAtual.write("304 Defesa normal selecionado.");
                    playerOutro.write("304 " + playerOutro.getUsername() + " selecionou a jogada.");
                    return true;
                case "Magico":
                    playerAtual.write("304 Escudo mágico selecionado.");
                    playerOutro.write("304 " + playerOutro.getUsername() + " selecionou a jogada.");
                    return true;
                case "Counter":
                    playerAtual.write("304 Counter selecionado.");
                    playerOutro.write("304 " + playerOutro.getUsername() + " selecionou a jogada.");
                    return true;
                default:
                    playerAtual.write("304 Jogada inválida");
                    return false;
            }
        }
    }

    protected static int calculaDano(Classe classe, String jogada){
        int dano, desvio;
        desvio = random.nextInt(10) - 5;
        switch (jogada) {
            case "Fisico":
                dano = classe.getFisico();
                return dano + desvio;
            case "Magico":
                dano = classe.getInteligencia();
                return dano + desvio;
            case "Especial":
                dano = classe.getFisico() + classe.getInteligencia();
                return dano + desvio;
            default:
                return -1;
        }
    }
}
