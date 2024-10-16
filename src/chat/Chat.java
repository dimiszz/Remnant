package chat;

import java.text.SimpleDateFormat;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Date;
import java.util.concurrent.BlockingQueue;
// import java.util.Scanner;

// https://stackoverflow.com/questions/35283349/how-to-make-enter-key-and-submit-button-have-same-actionevent
// https://www.geeksforgeeks.org/java-swing-jtextarea/
// Classe para a interface gráfica do chat
public class Chat extends JFrame implements ActionListener {
    private static Chat chat;
    private static BlockingQueue<String> messageQueue;

    private final JTextArea chatArea;
    private final JTextField messageField;
    private final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

    public Chat(String user) {
        super(user);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout(5,0));

        Font font = new Font("Arial", Font.PLAIN, 14);

        chatArea = new JTextArea();
        chatArea.setFont(font);
        chatArea.setEditable(false);
        chatArea.setFocusable(false);
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);
        chatArea.setBorder(BorderFactory.createCompoundBorder(
                chatArea.getBorder(),
                BorderFactory.createEmptyBorder(5, 8, 0, 0)));


        JScrollPane scroll = new JScrollPane(chatArea);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        this.add(scroll, BorderLayout.CENTER);


        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        messageField = new JTextField(20);
        messageField.setFont(font);
        JButton sendButton = new JButton("Enviar");
        sendButton.addActionListener(this);

        // https://pt.stackoverflow.com/questions/260167/keylistener-para-v%C3%A1rios-jtextfields
        // https://stackoverflow.com/questions/4419667/detect-enter-press-in-jtextfield
        messageField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    sendMessage(); // Call  
                }
            }
        });
        panel.add(messageField);
        panel.add(sendButton);

        this.add(panel, BorderLayout.SOUTH);

        this.setMinimumSize(new Dimension(200, 400));
    }


    private void sendMessage() {
        try {
            String message = messageField.getText();
            if (!message.isBlank()) {
                Thread.sleep(50);
                System.err.println("Tentando enviar mensagem: " + message);
                writeMessage("Você", message);
                if (messageQueue != null && !messageQueue.offer("/escrever " + message))
                    System.err.println("Não foi possível enviar a mensagem: " + message);

                messageField.setText("");
            }
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void writeMessage(String remetente, String message) {
        if (message != null && !message.isBlank()) {
            chatArea.append("[" + sdf.format(new Date()) + "] ");
            chatArea.append(remetente + ": " + message + "\n");
        }
    }

    public void actionPerformed(ActionEvent e) {
        sendMessage();
    }

    public static void finalizaChat() {
        if (chat != null)
            Chat.chat.dispose();
    }

    public static void iniciaChat(String user) {
        chat = new Chat(user);
        chat.pack();
        chat.setVisible(true);
    }

    public static void write(String remetente, String message){
        chat.writeMessage(remetente, message);
    }

    public static void addMessageQueue(BlockingQueue<String> messageQueue){
        Chat.messageQueue = messageQueue;
    }

    /* APENAS TESTE DO CHAT */
    // public static void main(String[] args) throws InterruptedException {

    //     //SwingUtilities.invokeLater(() -> new Chat());

    //     Chat.finalizaChat();

    //     Chat.iniciaChat("Dimirti");

    //     Scanner sn = new Scanner(System.in);

    //     String message = "";
    //     while(!message.equals("exit")){
    //         message = sn.nextLine();
    //     }

    //     Chat.finalizaChat();

    //     Thread.sleep(1000);

    //     Chat.iniciaChat("Prado");

    //     message = "";
    //     while(!message.equals("exit")){
    //         message = sn.nextLine();
    //     }

    //     Chat.finalizaChat();

    //     sn.close();
    // }
}