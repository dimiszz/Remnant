package cliente;

public class CodificadorCliente {
    public static String codifica(String mensagem){
        String comando, conteudo, str;

        if(mensagem.contains(" ")){
            comando = mensagem.substring(0, mensagem.indexOf(' '));
            conteudo = mensagem.substring(mensagem.indexOf(' ') + 1);
        }
        else{
            comando = mensagem;
            conteudo = "";
        }

        switch(comando){
            case "/ajuda":
                str = "101";
                break;
            case "/listar_partidas":
                str = "103";
                break;
            case "/criar_partida":
                str = "104";
                break;
            case "/fechar":
                str = "999";
                break;
            default:
                str = "COMANDO INVÁLIDO!";
                break;
        };
        return str;
    }
}