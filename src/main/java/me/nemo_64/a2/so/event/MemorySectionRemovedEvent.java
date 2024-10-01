package me.nemo_64.a2.so.event;

import me.nemo_64.a2.so.data.MemorySection;

public class MemorySectionRemovedEvent extends MemorySectionEvent {
    public MemorySectionRemovedEvent(MemorySection[] sections) {
        super(sections);
    }
}
