package servidor;

import java.util.Random;

public class Jogo {
    /*
    - Guerreiro   vida: 72  defesa: 7  físico: 22  inteligência: 10
    - Feiticeiro  vida: 65  defesa: 5  físico: 6   inteligência: 26
    - Assassino   vida: 68  defesa: 4  físico: 20  inteligência: 16
    */

    public static Random random = new Random();

    protected static Classe getClasse(String classe){
        switch(classe){
            case "Guerreiro":
                return Classe.Guerreiro();
            case "Feiticeiro":
                return Classe.Feiticeiro();
            case "Assassino":
                return Classe.Assassino();
            default:
                return null;
        }
    }

    protected static void comecaJogada(Partida partida){
        int rodada = random.nextInt(2);
        if(rodada == 0){
            partida.setJogadaP2("Defesa");
            partida.setJogadaP1("Ataque");
        }
        else{
            partida.setJogadaP1("Defesa");
            partida.setJogadaP2("Ataque");
        }
        partida.proxRodada();
    }
}
