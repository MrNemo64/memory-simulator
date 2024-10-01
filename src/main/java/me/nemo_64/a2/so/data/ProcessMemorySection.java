package me.nemo_64.a2.so.data;

import java.util.Objects;
import java.awt.Color;

import me.nemo_64.a2.so.General;
import me.nemo_64.a2.so.ui.MemoryDisplay.MemoryDisplayDrawer;

public record ProcessMemorySection(Process memoryElement, int startPosition, Color color)
        implements MemorySection {

    public ProcessMemorySection(Process memoryElement, int startPosition, Color color) {
        if (startPosition < 0)
            throw new IllegalArgumentException("start position cannot be negative");
        this.memoryElement = Objects.requireNonNull(memoryElement);
        this.startPosition = startPosition;
        this.color = color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        ProcessMemorySection that = (ProcessMemorySection) o;
        return startPosition == that.startPosition && memoryElement.equals(that.memoryElement);
    }

    @Override
    public int hashCode() {
        return Objects.hash(memoryElement, startPosition);
    }

    public ProcessMemorySection(Process memoryElement, int startPosition) {
        this(memoryElement, startPosition, General.randomColor());
    }

    @Override
    public void draw(MemoryDisplayDrawer g) {
        g.draw(getStartPosition(), getSize(), color());
    }

    @Override
    public boolean isFreeSpace() {
        return false;
    }

    @Override
    public int getStartPosition() {
        return startPosition();
    }

    @Override
    public Process getMemoryElement() {
        return memoryElement();
    }

}
