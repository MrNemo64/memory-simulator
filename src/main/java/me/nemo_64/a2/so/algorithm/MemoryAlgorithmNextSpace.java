package me.nemo_64.a2.so.algorithm;

import java.util.List;
import java.util.function.Predicate;

import me.nemo_64.a2.so.data.FreeMemorySection;
import me.nemo_64.a2.so.data.Memory;
import me.nemo_64.a2.so.data.MemorySection;
import me.nemo_64.a2.so.data.Process;
import me.nemo_64.a2.so.util.SortedArrayList;

public class MemoryAlgorithmNextSpace implements MemoryAlgorithm {

	private int lastPos;

	public MemoryAlgorithmNextSpace() {
		this(0);
	}

	public MemoryAlgorithmNextSpace(int lastPos) {
		this.lastPos = lastPos;
	}

	@Override
	public boolean addProcessToMemory(Memory memory, Process process) {
		List<FreeMemorySection> available = new SortedArrayList<>((fms1, fms2) -> {
			return Integer.compare(fms1.getStartPosition(), fms2.getStartPosition());
		});
		available.addAll(memory.getFreeSpaces(process.size()));
		if (available.size() == 0)
			return false;
		for (FreeMemorySection sec : available) {
			if (sec.containsPosition(lastPos)) {
				if (fits(sec, lastPos, process.size())) {
					if (memory.addMemoryElement(process, lastPos)) {
						return true;
					}
				}
				break;
			}
		}

		int nextFree = indexThatMatches(available, (ms) -> lastPos <= ms.getStartPosition());
		if (nextFree < 0)
			nextFree = 0;
		MemorySection ms = available.get(nextFree);
		if (memory.addMemoryElement(process, ms.getStartPosition())) {
			lastPos = ms.getStartPosition();
			return true;
		}
		return false;

	}

	private <T> int indexThatMatches(List<T> list, Predicate<T> predicate) {
		for (int i = 0; i < list.size(); i++)
			if (predicate.test(list.get(i)))
				return i;
		return -1;
	}

	private boolean fits(FreeMemorySection sec, int pos, int size) {
		return sec.getEndPosition() - pos >= size;
	}

	@Override
	public String toString() {
		return "Next space";
	}
}