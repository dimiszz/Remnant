package servidor;

import java.util.Random;

// Classe das classes de rpg com os atributos e métodos necessários para o combate
public class Classe {
    public static Random random = new Random();
    private final String classe;
    private int vida;
    private final int defesa;
    private final int forca;
    private final int inteligencia;

    public Classe(String classe, int vida, int defesa, int forca, int inteligencia) {
        this.classe = classe;
        this.vida = vida;
        this.defesa = defesa;
        this.forca = forca;
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
        return this.vida + ";" + this.defesa + ";" + this.forca + ";" + this.inteligencia;
    }

    public int getVida(){
        return this.vida;
    }

    public int getDefesa(){
        return this.defesa;
    }

    public int getForca(){
        return this.forca;
    }

    protected int calculaDano(String jogada){
        int dano, desvio;
        desvio = random.nextInt(11) - 5;
        switch (jogada) {
            case "fisico":
                dano = getForca();
                return dano + desvio;
            case "magico":
                dano =getInteligencia();
                return dano + desvio;
            case "especial":
                dano = getForca() + getInteligencia();
                return dano + desvio;
            default:
                System.out.println("BUG TÁ CAINDO EM -1: " + jogada);
                return -1;
        }
    }

    public int getInteligencia(){
        return this.inteligencia;
    }

    public void tomaDano(int dano){
        this.vida -= dano;
        if (this.vida < 0) this.vida = 0;
    }

    public boolean morto(){
        return this.vida <= 0;
    }

    @Override
    public String toString() {
        return this.classe;
    }
}