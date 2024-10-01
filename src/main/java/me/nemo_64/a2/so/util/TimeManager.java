package me.nemo_64.a2.so.util;

import java.awt.event.ActionEvent;
import java.io.Serial;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Supplier;

import javax.swing.*;

import me.nemo_64.a2.so.General;
import net.miginfocom.swing.MigLayout;

public class TimeManager extends JPanel implements Supplier<Integer> {

    @Serial
    private static final long serialVersionUID = -6485865594728449388L;

    private final JToggleButton autoButton;
    private final JButton stepButton;

    private final Timer timer = new Timer();
    private long autoStepSize = 1000l;

    public TimeManager() {
        setLayout(new MigLayout("", "[fill][][]", "[][fill]"));
        setBorder(BorderFactory.createTitledBorder("Time"));
        this.autoButton = new JToggleButton("Automatic");
        this.stepButton = new JButton("Step");

        this.autoButton.addActionListener(this::autoMode);
        this.stepButton.addActionListener((e) -> {
            General.step();
        });

        add(autoButton, "growx");
        add(stepButton, "growx");
    }

    private void autoMode(ActionEvent e) {
        if (isAuto()) {
            stepButton.setEnabled(false);
            timer.schedule(createTask(), autoStepSize);
        } else {
            stepButton.setEnabled(true);
        }
    }

    private TimerTask createTask() {
        return new TimerTask() {
            @Override
            public void run() {
                if (isAuto()) {
                    General.step();
                    timer.schedule(createTask(), autoStepSize);
                } else {
                    stepButton.setEnabled(true);
                }
            }
        };
    }

    public boolean isAuto() {
        return autoButton.isSelected();
    }

    @Override
    public Integer get() {
        return 1;
    }

}