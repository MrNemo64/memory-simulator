package me.nemo_64.a2.so.data;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.*;
import java.util.function.Supplier;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import me.nemo_64.a2.so.General;
import me.nemo_64.a2.so.algorithm.MemoryAlgorithm;
import me.nemo_64.a2.so.algorithm.MemoryAlgorithmBestSpace;
import me.nemo_64.a2.so.event.MemorySectionAddedEvent;
import me.nemo_64.a2.so.event.MemorySectionListener;
import me.nemo_64.a2.so.event.MemorySectionRemovedEvent;
import me.nemo_64.a2.so.event.StepListener;

public class MemoryManager implements Supplier<MemoryView>, StepListener {

  private final Memory memory = new Memory();
  private final List<MemorySectionListener> memoryListeners = new ArrayList<>();

  private MemoryAlgorithm memoryAlgorithm;

  public MemoryManager() {
    this.memoryAlgorithm = new MemoryAlgorithmBestSpace();
    General.addStepListener(this);
  }

  public Memory getMemory() {
    return memory;
  }

  public void dumpMemory(Writer w) {
    try {
      w.write(String.valueOf(General.getTimeManager().get()));
      w.write(" ");
      Iterator<MemorySection> mem = memory.getMemory().iterator();
      while (mem.hasNext()) {
        MemorySection section = mem.next();
        w.append(section.serialize());
        if (mem.hasNext())
          w.append(" ");
      }
      w.append("\n");
      General.infof("Memory exported");
    } catch (IOException e) {
      General.errorf("Could not export memory: %s", e.getMessage());
    }
  }

  public void dumpMemoryToFile() {
    JFileChooser fc = new JFileChooser();
    int op = fc.showDialog(null, null);
    switch (op) {
      case JFileChooser.CANCEL_OPTION: {
        break;
      }
      case JFileChooser.ERROR_OPTION: {
        JOptionPane.showMessageDialog(null, "Could not open file.", "Unexpected error.",
            JOptionPane.ERROR_MESSAGE);
        General.errorf("Error trying to open the file to export the processes");
        break;
      }
      case JFileChooser.APPROVE_OPTION: {
        File file = fc.getSelectedFile();
        try (FileWriter w = new FileWriter(file)) {
          dumpMemory(w);
        } catch (IOException e) {
          JOptionPane.showMessageDialog(null, "Could not open file.", "Unexpected error.",
              JOptionPane.ERROR_MESSAGE);
          General.errorf("Error trying to open the file to export the processes");
        }
        break;
      }
      default:
        throw new IllegalArgumentException("Unexpected value: " + op);
    }
  }

  @Override
  public void step(float time) {
    float actualTime = General.getCurrentTime();
    memory.getProcesses().forEach((pms) -> {
      if (pms.getMemoryElement().arriveTime() + pms.getMemoryElement().duration() <= actualTime) {
        memory.removeMemorySection(pms.getStartPosition());
        General.infof("%s finished", pms.getMemoryElement().name());
      }
    });
    General.getProcessManager().getProcessesThatStartOn(General.getCurrentTime()).forEach((p) -> {
      if (memoryAlgorithm.addProcessToMemory(memory, p))
        General.infof("%s added to memory", p.name());
    });
  }

  void triggerMemorySectionAdded(MemorySection... ms) {
    MemorySectionAddedEvent e = new MemorySectionAddedEvent(ms);
    memoryListeners.forEach((l) -> l.memorySectionAdded(e));
  }

  void triggerMemorySectionRemoved(MemorySection... ms) {
    MemorySectionRemovedEvent e = new MemorySectionRemovedEvent(ms);
    memoryListeners.forEach((l) -> l.memorySectionRemoved(e));
  }

  public boolean addMemoryListener(MemorySectionListener e) {
    return memoryListeners.add(Objects.requireNonNull(e));
  }

  public boolean removeMemoryListener(MemorySectionListener e) {
    return memoryListeners.remove(Objects.requireNonNull(e));
  }

  public MemoryAlgorithm getMemoryAlgorithm() {
    return memoryAlgorithm;
  }

  public void setMemoryAlgorithm(MemoryAlgorithm memoryAlgorithm) {
    this.memoryAlgorithm = Objects.requireNonNull(memoryAlgorithm);
  }

  @Override
  public MemoryView get() {
    return memory;
  }

}
