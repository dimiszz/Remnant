package servidor;

// Classe do jogador com os atributos necessários para o combate
public class Jogador {
    private final Usuario user;
    private Classe classe;
    private String turno;
    private String jogada;
    
    public Jogador(Usuario user){
        this.user = user;
        this.classe = null;
        this.turno = null;
        this.jogada = null;
    }

    protected Usuario getUser(){
        return this.user;
    }

    protected Classe getClasse(){
        return this.classe;
    }

    protected String getTurno(){
        return this.turno;
    }

    protected String getJogada(){
        return this.jogada;
    }

    protected void setClasse(Classe classe){
        this.classe = classe;
    }

    protected void setTurno(String turno){
        this.turno = turno;
    }

    protected void setJogada(String jogada){
        this.jogada = jogada;
    }

    @Override
    public String toString(){
        return this.user.getUsername();
    }
}