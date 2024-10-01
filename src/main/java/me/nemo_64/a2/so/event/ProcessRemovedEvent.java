package me.nemo_64.a2.so.event;

import me.nemo_64.a2.so.data.Process;

public class ProcessRemovedEvent extends ProcessEvent {
    public ProcessRemovedEvent(Process... processes) {
        super(processes);
    }
}