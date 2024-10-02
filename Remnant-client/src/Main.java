import java.io.*;
import java.net.Socket;
import java.util.*;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static HashMap<String, String> decodificarMensagem(String mensagem){
        int i, y, bin = 0;
        String key = "";
        String value = "";
        HashMap<String, String> map = new HashMap<String, String>();
        for(i = 0, y = 0; i < mensagem.length()-1; i++){
            if(mensagem.charAt(i) == '\"'){
                y = i+1;
                StringBuilder m1 = new StringBuilder();
                while(mensagem.charAt(y) != '\"'){
                    m1.append(mensagem.charAt(y));
                    y++;
                }
                i = y+1;
                if(bin % 2 == 0) key = m1.toString();
                else {
                    value = m1.toString();
                    map.put(key, value);
                }
                bin++;
            }
        }

        return map;
    }

    public static void main(String[] args) {
        try {
            Socket cliente = new Socket("127.0.0.1",12345);

            Scanner t = new Scanner(System.in);

            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(cliente.getOutputStream()));
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
            String code = "0";
            String menssage = "";



            while(!Objects.equals(code, "10")){
                System.out.print("Digite o código: ");
                code = t.nextLine();
                System.out.print("Digite a mensagem: ");
                menssage = t.nextLine();

                bufferedWriter.write(communicateMessage(code, menssage));
                bufferedWriter.newLine();
                bufferedWriter.flush();

                String mensagem = bufferedReader.readLine();

                HashMap<String, String> map = decodificarMensagem(mensagem);

                System.out.println(map.get("Message"));

                switch(map.get("Code")){
                    case "0":
                        System.out.println("Empate!");
                        break;
                    case "1":
                        System.out.println("1 ponto para você! Continue a batalha!");
                        break;
                    case "2":
                        System.out.println("Ele conseguiu dessa vez, mas ainda tem jogo!");
                        break;
                    case "10":
                        System.out.println("Você venceu! Obrigado por jogar!");
                        code = "10";
                    default:
                        System.out.println("Escreva \"Pedra\", \"Papel\" ou \"Tesoura\" na mensagem!");
                }
            }
            bufferedWriter.close();
            bufferedReader.close();
            cliente.close();
            System.out.println("Conexão encerrada");
        }
        catch(Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
//        String mensagem = "{\"Code\": \"1\", \"Message\": \"Olá\"}";
//        HashMap<String,String> answer = decodificarMensagem(mensagem);
//        System.out.println(answer.get("Message"));

    }

    public static String communicateMessage(String Code, String Message){
        return "{\"Code\": \""+ Code + "\", \"Message\": \"" + Message +  "\"}";
    }
}