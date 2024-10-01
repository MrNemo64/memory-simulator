package me.nemo_64.a2.so.ui;

import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;

import me.nemo_64.a2.so.General;
import me.nemo_64.a2.so.util.TimeManager;
import net.miginfocom.swing.MigLayout;

public class Frame extends JFrame {

    private static final long serialVersionUID = -6612495266578493402L;

    private final MemoryDisplay memoryDisplay = new MemoryDisplay(General.getMemoryManager());
    private final ProcessesPanel processesPanel = new ProcessesPanel();
    private final DebugConsole console = new DebugConsole();
    private final JButton stepButton = new JButton("Step");
    private final JCheckBox useHexForDirections = new JCheckBox();
    private final OptionsPanel optionsPanel = new OptionsPanel();

    public Frame() {
        setLayout(new MigLayout("", "10[fill, grow]10[fill, grow ]10", "10[fill, grow]10[fill, grow]10"));

        add(memoryDisplay, "grow");
        add(console, "grow, wrap");
        add(processesPanel, "grow");
        add(optionsPanel, "grow");

        stepButton.addActionListener((e) -> {

        });

        setMinimumSize(new Dimension(640, 400));
        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public ProcessesPanel getProcessesPanel() {
        return processesPanel;
    }

    public TimeManager getTimeManager() {
        return optionsPanel.getTimeManager();
    }

    public boolean isUseHexForDirections() {
        return useHexForDirections.isSelected();
    }

    public DebugConsole getConsole() {
        return console;
    }
}
