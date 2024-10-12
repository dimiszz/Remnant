package cliente;

public class CodificaMensagem {
    public static String codifica(String mensagem){
        String comando, conteudo, str = "Comando inválido!";;

        System.out.println("Enviando mensagem: " + mensagem);
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
            case "/listar_partidas":
                str = "102";
                break;
            case "/criar_partida":
                str = "103";
                break;
            case "/fechar":
                str = "999";
                break;
        };
        return str;
    }
}