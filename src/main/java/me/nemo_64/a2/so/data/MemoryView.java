package me.nemo_64.a2.so.data;

import java.util.Collection;
import java.util.Iterator;

public interface MemoryView extends Iterable<MemorySection> {

	Collection<FreeMemorySection> getFreeSpaces(int minSize);

	default Collection<FreeMemorySection> getFreeSpaces() {
		return getFreeSpaces(0);
	}

	@Override
	default Iterator<MemorySection> iterator() {
		return getMemory().iterator();
	}

	Collection<MemorySection> getMemory();

	Collection<ProcessMemorySection> getProcesses();

	MemorySection getMemorySectionAt(int pos);

	int getLength();

}
