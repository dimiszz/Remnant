package client;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        try {
            Client cliente = new Client("127.0.0.1",7777);

            MessageHandler messageHandler = new MessageHandler();

            while(cliente.isConnected()){
                String mensagem = cliente.read();
                if (mensagem == null) break;
                System.out.println(messageHandler.handle(mensagem));
                Thread.sleep(3000);
                cliente.write(MessageHandler.communicateMessage("102", ""));
            }

            System.out.println("Conexão encerrada!");
        }
        catch(Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

}