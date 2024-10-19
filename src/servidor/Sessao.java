package servidor;

import java.util.HashMap;

// Classe para gerenciar as sessões dos usuários
public class Sessao {
    private static final HashMap<Integer, Sessao> sessoes = new HashMap<>();
    private static final int maximoSessoes = 5;
    private static int livre = 0;
    private final int id;
    private Usuario user1;
    private Usuario user2;
    private Partida partida;

    public Sessao(){
        this.id = livre;
        sessoes.put(livre, this);
        livre++;
        this.user1 = null;
        this.user2 = null;
        this.partida = null;
    }

    private int getId(){
        return id;
    }

    private String getuser1(){
        String p1 = "[VAGO]";
        if (user1 != null) p1 = this.user1.getUsername();
        return p1;
    }

    private String getuser2(){
        String p2 = "[VAGO]";
        if (user2 != null) p2 = this.user2.getUsername();
        return p2;
    }

    private synchronized boolean addUsuario(Usuario user){
        if(this.user1 == null) this.user1 = user;
        else if(this.user2 == null) this.user2 = user;
        else return false;

        user.setSessao(this.id);
        return true;
    }

    private synchronized void removeUsuario(Usuario user){
        if(this.user1 == user){
            this.user1 = null;
            user.removeSessao();
        }
        else if(this.user2 == user){
            this.user2 = null;
            user.removeSessao();
        }
    }

    protected static void listarSessoes(Usuario user){
        StringBuilder resultado = new StringBuilder(Integer.toString(sessoes.size()));

        for(Sessao sessao : sessoes.values()){
            resultado.append(";").append(sessao.getId())
                    .append(";").append(sessao.getuser1())
                    .append(";").append(sessao.getuser2());
        }

        user.write("203 " + resultado.toString());
    }

    protected synchronized static void criaSessao(Usuario user){
        if(sessoes.get(user.getSessao()) != null){
            user.write("204 Você já está em uma sessão.");
            return;
        }
        if(sessoes.size() >= Sessao.maximoSessoes){
            user.write("204 Não foi possível criar a sessão: número máximo atingido.");
            return;
        }

        Sessao sessao = new Sessao();
        sessao.addUsuario(user);
        user.write("204 " + sessao.getId() + ";" + user.getUsername());
        user.broadcast("207 " + user.getUsername() + ";" + sessao.getId());
    }

    // Aqui na entrada da sessão já começa a partida se tiver dois jogadores
    protected synchronized static void entrarSessao(Usuario user, String id){
        if(user.estaEmSessao()){
            user.write("205 Você já está em uma sessão.");
            return;
        }
        //https://stackoverflow.com/questions/18711896/how-can-i-prevent-java-lang-numberformatexception-for-input-string-n-a
        if(!id.matches("\\d+")){
            user.write("205 ID da sessao inválido.");
            return;
        }

        Sessao sessao  = sessoes.get(Integer.parseInt(id));
        if(sessao == null){
            user.write("205 Sessão não foi encontrada.");
            return;
        }

        if(!sessao.addUsuario(user)){
            user.write("205 Sessão está cheia.");
            return;
        }

        user.write("205 " + sessao.getId() + ";" + sessao.getuser1() + ";" + sessao.getuser2());

        if(sessao.user1 != null && sessao.user2 != null){
            sessao.iniciaSessao(user);
        }
    }

    protected static void sairSessao(Usuario user){
        if(user.getSessao() == -1){
            user.write("206 Você não está em nenhuma sessão.");
            return;
        }

        Sessao sessao = sessoes.get(user.getSessao());
        if(sessao == null) return;

        sessao.removeUsuario(user);
        user.write("206 Você saiu da sessão " + sessao.getId() + ".");

        if(sessao.user1 == null && sessao.user2 == null){
            sessoes.remove(sessao.getId());
        }
    }

    protected static void escolheClasse(Usuario user, String classe){
        sessoes.get(user.getSessao()).partida.setClasse(user, classe);
    }

    protected static void escolheCombate(Usuario user, String jogada){
        sessoes.get(user.getSessao()).partida.combate(user, jogada);
    }

    protected synchronized static void fecharPartida(Usuario user){
        Sessao sessao = sessoes.get(user.getSessao());

        Usuario oponente = sessao.getOponente(user);

        sessao.partida = null;
        user.setPartida(false);
        user.write("305 Você saiu da partida.");
        sairSessao(user);

        if (oponente == null) return;

        oponente.setPartida(false);
        oponente.write("305 Seu oponente saiu da partida.");
        sairSessao(oponente);
    }

    private Usuario getOponente(Usuario user) {
        return (this.user1 == user) ? this.user2 : this.user1;
    }

    protected static void escrever(Usuario user, String mensagem){
        Sessao sessao = sessoes.get(user.getSessao());
        if (sessao == null) return;

        Usuario oponente = sessao.getOponente(user);

        oponente.write("1202 " + user + ";" + mensagem);
    }

    private void iniciaSessao(Usuario user){
        Usuario oponente = getOponente(user);

        user.setPartida(true);
        oponente.setPartida(true);
        this.partida = new Partida(this.user1, this.user2);

        oponente.write("306 " + user.getUsername() + " entrou na partida!");

        oponente.write("300");
        user.write("300");

        user.write("1201 " + oponente);
        oponente.write("1201 " + user);
    }
}