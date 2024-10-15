package servidor;

//import javax.management.OperationsException;

public class Classe {
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

    public static Classe Paladino(){
        return new Classe("Paladino",68, 4, 20, 16);
    }

    public String getAtributos(){
        return this.vida + ";" + this.defesa + ";" + this.fisico + ";" + this.inteligencia;
    }

    public int getFisico(){
        return this.fisico;
    }

    public int getInteligencia(){
        return this.inteligencia;
    }

    @Override
    public String toString() {
        return this.classe;
    }
}