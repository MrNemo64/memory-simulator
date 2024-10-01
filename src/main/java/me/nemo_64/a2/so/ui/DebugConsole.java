package me.nemo_64.a2.so.ui;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.*;
import javax.swing.text.*;

import net.miginfocom.swing.MigLayout;

public class DebugConsole extends JPanel {

    private static final long serialVersionUID = 7867884191189006210L;

    public static final Color INFO_COLOR = Color.BLACK;
    public static final Color WARN_COLOR = new Color(255, 128, 0);
    public static final Color ERROR_COLOR = new Color(255, 0, 0);

    private final JTextPane console = new JTextPane();
    private final JButton clearButton = new JButton("Clear");

    public DebugConsole() {
        setLayout(new MigLayout("", "[fill, grow]", "[fill, grow][fill]"));
        setBorder(BorderFactory.createTitledBorder("Console"));
        clearButton.addActionListener((e) -> clear());
        console.setEditable(false);
        console.setPreferredSize(new Dimension(320, 200));
        add(new JScrollPane(console), "grow, wrap");
        add(clearButton, "growx");
    }

    public void clear() {
        console.setText("");
    }

    public void info(String... msgs) {
        for (String msg : msgs) {
            addText("[i]: " + msg, INFO_COLOR);
        }
    }

    public void warn(String... msgs) {
        for (String msg : msgs) {
            addText("[w]: " + msg, WARN_COLOR);
        }
    }

    public void error(String... msgs) {
        for (String msg : msgs) {
            addText("[e]: " + msg, ERROR_COLOR);
        }
    }

    private void addText(String msg, Color c) {
        if (!msg.endsWith("."))
            msg += ".";
        msg += "\n";
        SimpleAttributeSet style = new SimpleAttributeSet();
        StyleConstants.setForeground(style, c);

        try {
            console.getDocument().insertString(console.getDocument().getLength(), msg, style);
        } catch (BadLocationException e) {
        }
    }

}
