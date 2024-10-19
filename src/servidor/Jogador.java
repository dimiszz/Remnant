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

    protected String validaJogada(String jogada){
        String acao;
        if(this.turno.equals("Ataque")){
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
                    return "invalido";
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
                    return "invalido";
            }
        }
        return acao;
    }

    protected void setJogada(String jogada){
        this.jogada = jogada;
    }

    protected void write(String message){
        this.user.write(message);
    }

    protected String getUsername(){
        return this.user.getUsername();
    }

    protected boolean checkUser(Usuario user){
        return user.equals(this.user);
    }

    @Override
    public String toString(){
        return this.user.getUsername();
    }

    @Override
    public boolean equals(Object obj){
        if (obj == null || obj.getClass() != getClass()) return false;

        Jogador jogador = (Jogador) obj;
        return this.user.getIdUsuario() == jogador.getUser().getIdUsuario();
    }
}