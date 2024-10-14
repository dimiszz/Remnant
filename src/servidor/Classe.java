package servidor;

import javax.management.OperationsException;

public class Classe {
    /*
    - Guerreiro   vida: 72  defesa: 7  físico: 22  inteligência: 10
    - Feiticeiro  vida: 65  defesa: 5  físico: 6   inteligência: 26
    - Assassino   vida: 68  defesa: 4  físico: 20  inteligência: 16
    */

    private String classe;
    private int vida;
    private int defesa;
    private int fisico;
    private int inteligencia;

    public Classe(String classe, int vida, int defesa, int fisico, int inteligencia) {
        this.classe = classe;
        this.vida = vida;
        this.defesa = defesa;
        this.fisico = fisico;
        this.inteligencia = inteligencia;
    }

    public static Classe Guerreiro(){
        return new Classe("Guerreiro",72, 7, 22, 10);
    }

    public static Classe Feiticeiro(){
        return new Classe("Feiticeiro", 65, 5, 6, 26);
    }

    public static Classe Assassino(){
        return new Classe("Assassino",68, 4, 20, 16);
    }

    public String getAtributos(){
        return this.vida + ";" + this.defesa + ";" + this.fisico + ";" + this.inteligencia;
    }

    @Override
    public String toString() {
        return this.classe;
    }
}
