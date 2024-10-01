package me.nemo_64.a2.so.event;

import me.nemo_64.a2.so.data.MemorySection;

public class MemorySectionAddedEvent extends MemorySectionEvent {

	public MemorySectionAddedEvent(MemorySection[] sections) {
		super(sections);
	}

}
