package me.nemo_64.a2.so.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import me.nemo_64.a2.so.General;

public record Process(String name, int size, float arriveTime, float duration) implements MemoryElement {

	private static int MIN_SIZE = 100;
	private static int MIN_EXE_TIME = 1;

	@Override
	public MemorySection createSection(int pos) {
		return new ProcessMemorySection(this, pos);
	}

	public static int getMinSize() {
		return MIN_SIZE;
	}

	public static void setMinSize(int min) {
		if (min > 0)
			MIN_SIZE = min;
	}

	public static int getMinExeTime() {
		return MIN_EXE_TIME;
	}

	public static void setMinExeTime(int min) {
		if (min > 0)
			MIN_EXE_TIME = min;
	}

	public static Optional<Process> deserialize(String str) {
		String[] parts = str.split(" ");
		if (parts.length != 4) {
			General.warnf("Process with invalid format: %s. The process will not be loaded.", str);
			return Optional.empty();
		}
		String name = parts[0];
		float arriveTime = 0;
		int size = 0;
		float duration = 0;
		try {
			arriveTime = Float.parseFloat(parts[1]);
			if (arriveTime < 0) {
				General.warnf(
						"The process %s has a negative arrival time: %.1f. The process will not be loaded.", name,
						arriveTime);
				return Optional.empty();
			}
		} catch (NumberFormatException e) {
			General.warnf(
					"The process %s has an invalid arrival time: %s. The process will not be loaded.", name, parts[1]);
			return Optional.empty();
		}

		try {
			size = Integer.parseInt(parts[2]);
			if (size < getMinSize()) {
				General.warnf(
						"The process %s has an insufficient size: %d (min size: %d). The process will not be loaded.",
						name, size, getMinSize());
				return Optional.empty();
			}
		} catch (NumberFormatException e) {
			General.warnf(
					"The process %s has an invalid size: %s. The process will not be loaded.", name, parts[2]);
			return Optional.empty();
		}

		try {
			duration = Float.parseFloat(parts[3]);
			if (duration < getMinExeTime()) {
				General.warnf(
						"The process %s has an insufficient duration: %d (min duration: %d). The process will not be loaded.",
						name, duration, getMinExeTime());
				return Optional.empty();
			}
		} catch (NumberFormatException e) {
			General.warnf("The process %s has an invalid duration: %s. The process will not be loaded.", name,
					parts[3]);
			return Optional.empty();
		}

		return Optional.of(new Process(name, size, arriveTime, duration));
	}

	public static List<Process> deserializeAll(Reader r) {
		BufferedReader reader = new BufferedReader(r);
		List<Process> list = new ArrayList<>();
		String str;
		int[] i = { 0 };
		try {
			while ((str = reader.readLine()) != null) {
				deserialize(str).ifPresent((p) -> {
					list.add(p);
					i[0]++;
				});
			}
		} catch (IOException e) {
			General.errorf("Error reading the processes. Could load %d.", i[0]);
		}
		return list;
	}

}