package me.nemo_64.a2.so.event;

import me.nemo_64.a2.so.data.Process;

public class ProcessAddedEvent extends ProcessEvent {
    public ProcessAddedEvent(Process... processes) {
        super(processes);
    }
}