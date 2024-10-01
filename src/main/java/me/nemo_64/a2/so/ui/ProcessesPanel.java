package me.nemo_64.a2.so.ui;

import me.nemo_64.a2.so.General;
import me.nemo_64.a2.so.data.Process;
import me.nemo_64.a2.so.event.ProcessAddedEvent;
import me.nemo_64.a2.so.event.ProcessListener;
import me.nemo_64.a2.so.event.ProcessRemovedEvent;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ProcessesPanel extends JPanel implements ProcessListener {

    private static final long serialVersionUID = -3906080857482591623L;

    private final JTable table = new JTable();
    private final JButton loadProcessButon = new JButton("Load processes");
    private final TableModel model = new TableModel();
    private final List<Process> processes = new ArrayList<>();

    public ProcessesPanel() {
        setLayout(new MigLayout("", "[grow, fill]", "[grow, fill][grow, fill]"));
        setBorder(BorderFactory.createTitledBorder("Processes"));

        table.setModel(model);
        table.setRowSelectionAllowed(false);
        table.setCellSelectionEnabled(false);
        table.setColumnSelectionAllowed(false);

        loadProcessButon.addActionListener((e) -> General.getProcessManager().loadProcessFromFile());

        JScrollPane jsp = new JScrollPane(table);
        jsp.setPreferredSize(new Dimension(320, 200));
        add(jsp, "grow, wrap");
        add(loadProcessButon, "growx");
        General.getProcessManager().addProcessListener(this);
    }

    @Override
    public void processAdded(ProcessAddedEvent e) {
        for (Process p : e.getProcesses()) {
            processes.add(p);
            model.addRow(createRow(p));
        }
    }

    @Override
    public void processRemoved(ProcessRemovedEvent e) {
        for (Process p : e.getProcesses()) {
            int index = processes.indexOf(p);
            processes.remove(index);
            model.removeRow(index);
        }
    }

    public Object[] createRow(Process process) {
        Object[] row = { process.name(), process.size(), process.arriveTime(),
                process.arriveTime() + process.duration() };
        return row;
    }

    private class TableModel extends DefaultTableModel {

        private static final long serialVersionUID = -2593441843630101667L;

        public TableModel() {
            super(new String[] { "Process", "Size", "Arrival time", "Exit time" }, 0);
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    }
}