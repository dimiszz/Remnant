package servidor;

import java.util.Random;

public class Jogo {
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

    protected static Boolean escolheJogada(Jogador player1, Jogador player2, String jogada){
        if(player1.getTurno().equals("Ataque")){
            switch(jogada){
                case "Fisico":
                    player1.getUser().write("304 Ataque físico selecionado.");
                    player2.getUser().write("304 " + player2.getUser().getUsername() + " selecionou a jogada.");
                    return true;
                case "Magico":
                    player1.getUser().write("304 Ataque mágico selecionado.");
                    player2.getUser().write("304 " + player2.getUser().getUsername() + " selecionou a jogada.");
                    return true;
                case "Especial":
                    player1.getUser().write("304 Ataque especial selecionado.");
                    player2.getUser().write("304 " + player2.getUser().getUsername() + " selecionou a jogada.");
                    return true;
                default:
                    player1.getUser().write("304 Jogada inválida.");
                    return false;
            }
        }
        else{
            switch(jogada){
                case "Fisico":
                    player1.getUser().write("304 Defesa normal selecionado.");
                    player2.getUser().write("304 " + player2.getUser().getUsername() + " selecionou a jogada.");
                    return true;
                case "Magico":
                    player1.getUser().write("304 Escudo mágico selecionado.");
                    player2.getUser().write("304 " + player2.getUser().getUsername() + " selecionou a jogada.");
                    return true;
                case "Counter":
                    player1.getUser().write("304 Counter selecionado.");
                    player2.getUser().write("304 " + player2.getUser().getUsername() + " selecionou a jogada.");
                    return true;
                default:
                    player1.getUser().write("304 Jogada inválida.");
                    return false;
            }
        }
    }

    // Considera todas as possibilidades de combate
    protected static void realizaCombate(Jogador atacante, Jogador defensor){
        int dano = calculaDano(atacante.getClasse(), atacante.getJogada());
        int vida = defensor.getClasse().getVida();
        switch(atacante.getJogada()){
            case "Fisico":
                switch(defensor.getJogada()){
                    case "Fisico":
                        dano -= defensor.getClasse().getDefesa();
                        defensor.getClasse().setVida(vida - dano);
                        break;
                    case "Magico":
                        defensor.getClasse().setVida(vida - dano);
                        break;
                    case "Counter":
                        defensor.getClasse().setVida(vida - dano);
                        break;
                }
                break;
            case "Magico":
                switch(defensor.getJogada()){
                    case "Fisico":
                        dano -= defensor.getClasse().getDefesa()/2;
                        defensor.getClasse().setVida(vida - dano);
                        break;
                    case "Magico":
                        dano -= defensor.getClasse().getDefesa();
                        dano -= defensor.getClasse().getInteligencia()/3;
                        defensor.getClasse().setVida(vida - dano);
                        break;
                    case "Counter":
                        defensor.getClasse().setVida(vida - dano);
                        break;
                }
                break;
            case "Especial":
                switch(defensor.getJogada()){
                    case "Fisico":
                        dano -= defensor.getClasse().getDefesa();
                        defensor.getClasse().setVida(vida - dano);
                        break;
                    case "Magico":
                        defensor.getClasse().setVida(vida - dano);
                        break;
                    case "Counter":
                        atacante.getClasse().setVida(vida - dano);
                        break;
                }
                break;
        }
        
    }

    protected static int calculaDano(Classe classe, String jogada){
        int dano, desvio;
        desvio = random.nextInt(11) - 5;
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