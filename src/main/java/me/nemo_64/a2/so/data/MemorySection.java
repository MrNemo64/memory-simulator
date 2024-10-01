package me.nemo_64.a2.so.data;

import me.nemo_64.a2.so.ui.MemoryDisplay.MemoryDisplayDrawer;

public interface MemorySection extends Comparable<MemorySection> {

	void draw(MemoryDisplayDrawer g);

	boolean isFreeSpace();

	int getStartPosition();

	MemoryElement getMemoryElement();

	public default int getSize() {
		return getMemoryElement().size();
	}

	public default boolean containsPosition(int pos) {
		return getStartPosition() <= pos && pos <= getEndPosition();
	}

	@Override
	public default int compareTo(MemorySection o) {
		return Integer.compare(getStartPosition(), o.getStartPosition());
	}

	public default String serialize() {
		return "[" + getStartPosition() + " " + getMemoryElement().name() + " " + getSize() + "]";
	}

	public default int getEndPosition() {
		return getStartPosition() + getSize() - 1;
	}

}