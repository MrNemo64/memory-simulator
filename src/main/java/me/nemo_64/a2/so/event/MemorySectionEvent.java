package me.nemo_64.a2.so.event;

import java.util.Objects;

import me.nemo_64.a2.so.data.MemorySection;

public class MemorySectionEvent {

    private final MemorySection sections[];

    public MemorySectionEvent(MemorySection[] sections) {
        Objects.requireNonNull(sections, "sections cannot be null");
        if (sections.length == 0)
            throw new IllegalArgumentException("No sections were specified");
        for (int i = 0; i < sections.length; i++)
            Objects.requireNonNull(sections[i], "null element at index " + i + " of sections");
        this.sections = sections;
    }

    public MemorySection[] getSections() {
        return sections;
    }
}
