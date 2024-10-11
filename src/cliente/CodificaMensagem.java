package cliente;

public class CodificaMensagem {
    public String codifica(String mensagem){
        String comando, conteudo, str;

        System.out.println("Lendo mensagem: " + mensagem);
        if(mensagem.contains(" ")){
            comando = mensagem.substring(0, mensagem.indexOf(' '));
            conteudo = mensagem.substring(mensagem.indexOf(' ') + 1);
        }
        else{
            comando = mensagem;
            conteudo = "";
        }

        switch (comando) {
            case "/ajuda":
                str = "101";
                break;
            case "/fechar":
                str = "999";
                break;
            default:
                str = "Comando inválido!";
        };
        return str;
    }
}