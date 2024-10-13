package logs;

import javax.swing.*;
import java.io.OutputStream;
import java.io.PrintStream;

public class LogFrame {
    private JFrame frame;

    public LogFrame(boolean debugMode) {

        if (!debugMode) {
            System.setErr(new PrintStream(new OutputStream() {
                @Override
                public void write(int b) {
                }
            }));
            return;
        }

        this.frame = new JFrame("Console de Logs");
        JTextArea textArea = new JTextArea(20, 50);
        textArea.setEditable(false);
        textArea.setFocusable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        frame.add(scrollPane);

        frame.setMinimumSize(frame.getPreferredSize());
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // Redireciona System.err para o JTextArea usando um PrintStream
        PrintStream printStream = new PrintStream(new JTextAreaOutputStream(textArea));
        System.setErr(printStream); // Redireciona System.err
    }

    public void dispose() {
        if (frame != null) frame.dispose();
        System.err.close();
    }
}
