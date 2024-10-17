package chat;

import java.text.SimpleDateFormat;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.BlockingQueue;
// import java.util.Scanner;

// https://stackoverflow.com/questions/35283349/how-to-make-enter-key-and-submit-button-have-same-actionevent
// https://www.geeksforgeeks.org/java-swing-jtextarea/
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
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
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

    public static void iniciaChatGlobal(String user, ArrayList<String> connectedUsers) {
        chat = new Chat(user); // Manter a criação padrão do Chat

        chat.setMinimumSize(new Dimension(400, 400));
        // Adicionando a área de usuários conectados à janela
        JTextArea userArea = new JTextArea();
        userArea.setEditable(false);
        userArea.setLineWrap(true);
        userArea.setWrapStyleWord(true);
        userArea.setFont(new Font("Arial", Font.PLAIN, 14));
        userArea.setText(String.join("\n", connectedUsers)); // Exibe os usuários conectados
    
        JScrollPane userScroll = new JScrollPane(userArea);
        userScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        userScroll.setPreferredSize(new Dimension(100, 200)); // Definindo largura da área de usuários
    
        chat.add(userScroll, BorderLayout.EAST); // Adiciona a lista de usuários conectados ao lado do chat
    
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
    public static void main(String[] args) throws InterruptedException {

        //SwingUtilities.invokeLater(() -> new Chat());
        ArrayList<String> connectedUsers = new ArrayList<String>();

        connectedUsers.add("User2");
        connectedUsers.add("User1");
        connectedUsers.add("User0");

        Chat.iniciaChatGlobal("ChatGlobal", connectedUsers);
    }
}