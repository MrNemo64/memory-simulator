package me.nemo_64.a2.so.data;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import me.nemo_64.a2.so.General;
import me.nemo_64.a2.so.util.SortedArrayList;

public class Memory implements MemoryView {

	public static final int MEMORY_LENGTH = 2000;

	final List<MemorySection> memory = new SortedArrayList<>((ms1, ms2) -> {
		return Integer.compare(ms1.getStartPosition(), ms2.getStartPosition());
	});
	private int length;

	public Memory() {
		this(MEMORY_LENGTH);
	}

	public Memory(int length) {
		this.length = length;
	}

	public boolean removeMemorySection(int pos) {
		checkIndex(pos);
		for (int i = 0; i < memory.size(); i++) {
			MemorySection ms = memory.get(i);
			if (ms.containsPosition(pos)) {
				General.getMemoryManager().triggerMemorySectionRemoved(memory.remove(i));
				return true;
			}
		}
		return false;
	}

	public boolean addMemoryElement(MemoryElement element, int pos) {
		Objects.requireNonNull(element);
		checkIndex(pos);
		if (element instanceof FreeSpace)
			throw new IllegalArgumentException("Cannot add free space");
		if (pos + element.size() - 1 >= getLength()) {
			General.warnf("Tryed to add %s at the position %s but it is too big", element.name(),
					General.formatMemoryPosition(pos));
			return false;
		}

		if (isEmpty()) {
			MemorySection newMs = element.createSection(pos);
			this.memory.add(newMs);
			General.getMemoryManager().triggerMemorySectionAdded(newMs);
			return true;
		}

		MemorySection ms = getMemorySectionAt(pos);
		if (!ms.isFreeSpace()) {
			General.warnf("Tried to add %s in an ocupied position (%s) by %s",
					element.name(), General.formatMemoryPosition(pos), ms.getMemoryElement().name());
			return false;
		}
		int avilable = ms.getEndPosition() - pos + 1;
		if (avilable < element.size()) {
			General.warnf("Tried to add %s in at the position %s, but %s is too big",
					element.name(), General.formatMemoryPosition(pos), element.name());
			return false;
		}
		MemorySection newMs = element.createSection(pos);
		if (this.memory.add(newMs)) {
			General.getMemoryManager().triggerMemorySectionAdded(newMs);
		}
		return true;
	}

	@Override
	public MemorySection getMemorySectionAt(int pos) {
		checkIndex(pos);
		Collection<MemorySection> memory = getMemory();
		for (MemorySection sec : memory) {
			if (sec.containsPosition(pos)) {
				return sec;
			}
		}
		throw new RuntimeException(""); // if we get here, something went wrong
	}

	@Override
	public Collection<MemorySection> getMemory() {
		if (this.memory.size() == 0) // empty memory
			return Arrays.asList(new FreeSpace(getLength()).createSection(0));

		List<MemorySection> list = new SortedArrayList<>((ms1, ms2) -> {
			return Integer.compare(ms1.getStartPosition(), ms2.getStartPosition());
		});
		// is the beginning of the memory occupied?
		MemorySection before = this.memory.get(0);
		if (before.getStartPosition() != 0) {
			list.add(new FreeSpace(before.getStartPosition()).createSection(0));
		}
		list.add(before);

		// body
		for (int i = 1; i < this.memory.size(); i++) {
			MemorySection sec = this.memory.get(i);
			int spaceBetween = sec.getStartPosition() - before.getEndPosition() - 1;
			if (spaceBetween > 0) {
				// there is free space to fill
				list.add(new FreeSpace(spaceBetween).createSection(before.getEndPosition() + 1));
			}
			list.add(sec);
			before = sec;
		}

		// is the end ocupied?
		before = list.get(list.size() - 1);
		if (before.getEndPosition() < getLength() - 1) {
			list.add(new FreeSpace(getLength() - before.getEndPosition() - 1)
					.createSection(before.getEndPosition() + 1));
		}

		return list;
	}

	@Override
	public Collection<FreeMemorySection> getFreeSpaces(int minSize) {
		return getMemory().stream().flatMap((ms) -> {
			return ms instanceof FreeMemorySection fms && fms.getSize() >= minSize ? Stream.of(fms) : Stream.of();
		}).toList();
	}

	@Override
	public Collection<ProcessMemorySection> getProcesses() {
		return getMemory().stream().flatMap((ms) -> {
			return ms instanceof ProcessMemorySection pms ? Stream.of(pms) : Stream.of();
		}).toList();
	}

	public int getLength() {
		return length;
	}

	public boolean isEmpty() {
		return memory.size() == 0;
	}

	private void checkIndex(int i) {
		if (i < 0 || i >= length)
			throw new IndexOutOfBoundsException(i);
	}

}
