package me.nemo_64.a2.so.event;

import java.util.Objects;

import me.nemo_64.a2.so.data.Process;

public class ProcessEvent {
    private final Process[] processes;

    public ProcessEvent(Process... processes) {
        Objects.requireNonNull(processes, "processes cannot be null");
        if (processes.length == 0)
            throw new IllegalArgumentException("No processes were specified");
        for (int i = 0; i < processes.length; i++)
            Objects.requireNonNull(processes[i], "null element at index " + i + " of processes");
        this.processes = processes;
    }

    public Process[] getProcesses() {
        return processes;
    }

}