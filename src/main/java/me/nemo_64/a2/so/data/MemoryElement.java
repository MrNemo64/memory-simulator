package me.nemo_64.a2.so.data;

public interface MemoryElement {

	public int size();

	public String name();

	public MemorySection createSection(int pos);

}