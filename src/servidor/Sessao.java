package servidor;

import java.util.HashMap;

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

    protected static void criaSessao(Usuario user){
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
    }

    protected static void entrarSessao(Usuario user, String id){
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

        if(sessao.user1 != null && sessao.user2 != null){
            sessao.user1.setPartida(true);
            sessao.user2.setPartida(true);
            sessao.partida = new Partida(sessao.user1, sessao.user2);

            if(sessao.user1 == user){
                sessao.user2.write("306 " + user.getUsername() + " entrou na partida!");
                sessao.user2.write("300");
            }
            else if(sessao.user2 == user){
                sessao.user1.write("306 " + user.getUsername() + " entrou na partida!");
                sessao.user1.write("300");
            }
            user.write("205 " + sessao.getId() + ";" + sessao.getuser1() + ";" + sessao.getuser2());
            user.write("300");

            sessao.user1.write("1201 " + sessao.user2.getUsername());
            sessao.user2.write("1201 " + sessao.user1.getUsername());
            return;
        }

        user.write("205 " + sessao.getId() + ";" + sessao.getuser1() + ";" + sessao.getuser2());
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
        return;
    }

    protected static void escolheClasse(Usuario user, String classe){
        sessoes.get(user.getSessao()).partida.setClasse(user, classe);
    }

    protected static void escolheCombate(Usuario user, String jogada){
        sessoes.get(user.getSessao()).partida.combate(user, jogada);
    }

    protected synchronized static void sairPartida(Usuario user){
        Sessao sessao = sessoes.get(user.getSessao());

        if (sessao != null) sessao.partida = null;
        user.setPartida(false);
        user.write("\"305 Você saiu da partida.");

        if(sessao != null) {
            if (sessao.user1 == user && sessao.user2 != null) {
                sessao.user2.setPartida(false);
                sessao.user2.write("305 Seu oponente saiu da partida.");
                sairSessao(sessao.user2);
            } else if (sessao.user2 == user && sessao.user1 != null) {
                sessao.user1.setPartida(false);
                sessao.user1.write("305 Seu oponente saiu da partida.");
                sairSessao(sessao.user1);
            }
        }
        sairSessao(user);
    }

    protected static void escrever(Usuario userAtual, String mensagem){
        Sessao sessao = sessoes.get(userAtual.getSessao());
        if (sessao == null) return;
        Usuario userOutro = sessao.user2;
        if(sessao.user2 == userAtual) userOutro = sessao.user1;
        // testar se usuário userOutro é nulo.

        userOutro.write("1202 " + userAtual.getUsername() + ";" + mensagem);
    }
}