package me.nemo_64.a2.so;

import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            General.getFrame().setVisible(true);
        });
    }

}
