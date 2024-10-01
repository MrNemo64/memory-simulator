package me.nemo_64.a2.so.data;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import me.nemo_64.a2.so.General;
import me.nemo_64.a2.so.event.ProcessAddedEvent;
import me.nemo_64.a2.so.event.ProcessListener;
import me.nemo_64.a2.so.event.ProcessRemovedEvent;

public class ProcessManager {

	private final List<Process> processes = new ArrayList<>();
	private final List<ProcessListener> processListeners = new ArrayList<>();

	public void loadProcesses(Collection<Process> processes) {
		List<Process> added = new ArrayList<>(processes.size());
		for (Process p : processes) {
			if (this.processes.add(p)) {
				added.add(p);
			}
		}
		triggerProcessAdded(added.toArray(Process[]::new));
		General.infof("Loaded %d processes", added.size());
	}

	public void loadProcessFromFile() {
		JFileChooser fc = new JFileChooser();
		int op = fc.showDialog(null, null);
		switch (op) {
			case JFileChooser.CANCEL_OPTION: {
				break;
			}
			case JFileChooser.ERROR_OPTION: {
				JOptionPane.showMessageDialog(null, "Could not open file.", "Unexpected error.",
						JOptionPane.ERROR_MESSAGE);
				General.errorf("Error trying to open the file to add the processes.");
				break;
			}
			case JFileChooser.APPROVE_OPTION: {
				File file = fc.getSelectedFile();
				try (FileReader reader = new FileReader(file)) {
					List<Process> processes = Process.deserializeAll(reader);
					loadProcesses(processes);
				} catch (IOException e) {
					JOptionPane.showMessageDialog(null, "Could not open file.", "Unexpected error.",
							JOptionPane.ERROR_MESSAGE);
					General.errorf("Error trying to open the file to add the processes.");
				}
				break;
			}
			default:
				throw new IllegalArgumentException("Unexpected value: " + op);
		}
	}

	void triggerProcessAdded(Process... processes) {
		ProcessAddedEvent e = new ProcessAddedEvent(processes);
		processListeners.forEach((l) -> l.processAdded(e));
	}

	void triggerProcessRemoved(Process... processes) {
		ProcessRemovedEvent e = new ProcessRemovedEvent(processes);
		processListeners.forEach((l) -> l.processRemoved(e));
	}

	public boolean addProcessListener(ProcessListener e) {
		return processListeners.add(Objects.requireNonNull(e));
	}

	public boolean removeProcessListener(ProcessListener e) {
		return processListeners.remove(Objects.requireNonNull(e));
	}

	public List<Process> getProcesses() {
		return processes;
	}

	public Collection<Process> getProcessesThatStartOn(float moment) {
		return processes.stream().filter((p) -> p.arriveTime() == moment).toList();
	}

}