package logs;

import javax.swing.*;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class JTextAreaOutputStream extends java.io.OutputStream {
    private final JTextArea textArea;

    public JTextAreaOutputStream(JTextArea textArea) {
        this.textArea = textArea;
    }

    @Override
    public void write(int b) {
        SwingUtilities.invokeLater(() -> {
            String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            textArea.append("[" + timestamp + "] " + (char) b);
            textArea.setCaretPosition(textArea.getDocument().getLength());
        });
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        String str = new String(b, off, len);
        SwingUtilities.invokeLater(() -> {
            String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            textArea.append("[" + timestamp + "] " + str);
            textArea.setCaretPosition(textArea.getDocument().getLength());
        });
    }
}
