package me.nemo_64.a2.so.data;

import java.util.Objects;
import java.awt.Color;

import me.nemo_64.a2.so.ui.MemoryDisplay.MemoryDisplayDrawer;

public record FreeMemorySection(FreeSpace memoryElement, int startPosition) implements MemorySection {

	public FreeMemorySection(FreeSpace memoryElement, int startPosition) {
		if (startPosition < 0)
			throw new IllegalArgumentException("Start position cannot be negative");
		this.memoryElement = Objects.requireNonNull(memoryElement);
		this.startPosition = startPosition;
	}

	@Override
	public void draw(MemoryDisplayDrawer g) {
		g.draw(getStartPosition(), getSize(), Color.BLACK);
	}

	@Override
	public boolean isFreeSpace() {
		return true;
	}

	@Override
	public int getStartPosition() {
		return startPosition();
	}

	@Override
	public FreeSpace getMemoryElement() {
		return memoryElement;
	}

}
