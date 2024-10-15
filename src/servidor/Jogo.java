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
            partida.getPlayer1().setTurno("Ataque");
            partida.getPlayer2().setTurno("Defesa");
        }
        else{
            partida.getPlayer2().setTurno("Ataque");
            partida.getPlayer1().setTurno("Defesa");
        }
    }

    protected static Boolean escolheJogada(Partida partida, Usuario user1, Usuario user2, String turno, String jogada){
        if(turno.equals("Ataque")){
            switch (jogada) {
                case "Fisico":
                    user1.write("304 Ataque físico selecionado.");
                    user2.write("304 " + user2.getUsername() + " selecionou a jogada.");
                    return true;
                case "Magico":
                    user1.write("304 Ataque mágico selecionado.");
                    user2.write("304 " + user2.getUsername() + " selecionou a jogada.");
                    return true;
                case "Especial":
                    user1.write("304 Ataque especial selecionado.");
                    user2.write("304 " + user2.getUsername() + " selecionou a jogada.");
                    return true;
                default:
                    user1.write("304 Jogada inválida");
                    return false;
            }
        }
        else{
            switch (jogada) {
                case "Fisico":
                    user1.write("304 Defesa normal selecionado.");
                    user2.write("304 " + user2.getUsername() + " selecionou a jogada.");
                    return true;
                case "Magico":
                    user1.write("304 Escudo mágico selecionado.");
                    user2.write("304 " + user2.getUsername() + " selecionou a jogada.");
                    return true;
                case "Counter":
                    user1.write("304 Counter selecionado.");
                    user2.write("304 " + user2.getUsername() + " selecionou a jogada.");
                    return true;
                default:
                    user1.write("304 Jogada inválida");
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
