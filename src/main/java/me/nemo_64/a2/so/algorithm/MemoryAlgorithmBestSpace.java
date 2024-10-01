package me.nemo_64.a2.so.algorithm;

import java.util.ArrayList;
import java.util.List;

import me.nemo_64.a2.so.data.FreeMemorySection;
import me.nemo_64.a2.so.data.Memory;
import me.nemo_64.a2.so.data.Process;

public class MemoryAlgorithmBestSpace implements MemoryAlgorithm {

	@Override
	public boolean addProcessToMemory(Memory memory, Process process) {
		List<FreeMemorySection> avilable = new ArrayList<>(memory.getFreeSpaces(process.size()));
		if (avilable.size() == 0)
			return false;
		FreeMemorySection minSec = avilable.get(0);
		for (int i = 1; i < avilable.size(); i++) {
			FreeMemorySection actual = avilable.get(i);
			if (actual.getSize() < minSec.getSize())
				minSec = actual;
		}
		return memory.addMemoryElement(process, minSec.getStartPosition());
	}

	@Override
	public String toString() {
		return "Best space";
	}
}
