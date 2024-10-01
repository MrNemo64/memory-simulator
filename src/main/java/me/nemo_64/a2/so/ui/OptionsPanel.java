package me.nemo_64.a2.so.ui;

import me.nemo_64.a2.so.General;
import me.nemo_64.a2.so.algorithm.MemoryAlgorithm;
import me.nemo_64.a2.so.algorithm.MemoryAlgorithmBestSpace;
import me.nemo_64.a2.so.algorithm.MemoryAlgorithmNextSpace;
import me.nemo_64.a2.so.util.TimeManager;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;

public class OptionsPanel extends JPanel {

    private static final long serialVersionUID = 816209081767333994L;

    private final TimeManager timeManager = new TimeManager();
    private final JComboBox<MemoryAlgorithm> algorithms = new JComboBox<>();

    public OptionsPanel() {
        setLayout(new MigLayout("", "", ""));
        setBorder(BorderFactory.createTitledBorder("Options"));

        algorithms.addItem(new MemoryAlgorithmBestSpace());
        algorithms.addItem(new MemoryAlgorithmNextSpace());

        algorithms.addActionListener((e) -> {
            General.getMemoryManager().setMemoryAlgorithm((MemoryAlgorithm) algorithms.getSelectedItem());
        });

        add(timeManager, "wrap");
        add(new JLabel("Algorithm: "), "split");
        add(algorithms, "");
    }

    public TimeManager getTimeManager() {
        return timeManager;
    }
}
