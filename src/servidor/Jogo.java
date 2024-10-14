package servidor;

import java.util.Random;

public class Jogo {
    /*
    - Guerreiro   vida: 72  defesa: 7  físico: 22  inteligência: 10
    - Feiticeiro  vida: 65  defesa: 5  físico: 6   inteligência: 26
    - Assassino   vida: 68  defesa: 4  físico: 20  inteligência: 16
    */

    protected static String getClasse(String classe){
        switch(classe){
            case "Guerreiro":
                return "Guerreiro";
            case "Feiticeiro":
                return "Feiticeiro";
            case "Assassino":
                return "Assassino";
            default:
                return "invalido";
        }
    }

    protected static String getAtributos(String classe){
        switch(classe){
            case "Guerreiro":
                return "72;7;22;10";
            case "Feiticeiro":
                return "65;5;6;26";
            case "Assassino":
                return "68;4;20;16";
            default:
                return "invalido";
        }
    }

    protected static void comecaJogada(Partida partida){
        int rodada = new Random().nextInt(2);
        if(rodada == 0){
            partida.setJogadaP1("Ataque");
            partida.setJogadaP2("Defesa");
        }
        else{
            partida.setJogadaP1("Defesa");
            partida.setJogadaP2("Ataque");
        }
        partida.proxRodada();
    }
}
