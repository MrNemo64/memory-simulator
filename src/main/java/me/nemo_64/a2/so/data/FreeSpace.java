package me.nemo_64.a2.so.data;

public record FreeSpace(int size) implements MemoryElement {

	@Override
	public String name() {
		return "empty";
	}

	@Override
	public MemorySection createSection(int pos) {
		return new FreeMemorySection(this, pos);
	}

}