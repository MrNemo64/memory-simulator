package me.nemo_64.a2.so.data;

import static me.nemo_64.a2.so.data.Utils.frSpMs;
import static me.nemo_64.a2.so.data.Utils.procMs;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import me.nemo_64.a2.so.algorithm.MemoryAlgorithmBestSpace;

public class BestSpaceTest {

    @Test
    public void test1() {
        Memory m = new Memory(50);
        MemoryAlgorithmBestSpace a = new MemoryAlgorithmBestSpace();
        MemorySection ms1 = procMs(1, 10, 0);
        MemorySection ms2 = procMs(2, 10, 30);

        m.memory.add(ms1);
        m.memory.add(ms2);
        // P1 Empty P2 Empty
        // 0-9 10-29 30-39 40-49
        // 10 20 10 10

        a.addProcessToMemory(m, new Process("P3", 10, 0, 0));
        // P1 Empty P2 P3
        // 0-9 10-29 30-39 40-49
        // 10 20 10 10

        List<MemorySection> expected = Arrays.asList(ms1, frSpMs(20, 10), ms2, procMs(3, 10, 40));
        List<MemorySection> memory = new ArrayList<>(m.getMemory());

        assertEquals(expected.size(), memory.size());
        for (int i = 0; i < expected.size(); i++)
            assertEquals(expected.get(i), memory.get(i));
    }

    @Test
    public void test2() {
        Memory m = new Memory(50);
        MemoryAlgorithmBestSpace a = new MemoryAlgorithmBestSpace();
        MemorySection ms1 = procMs(1, 10, 0);
        MemorySection ms2 = procMs(2, 10, 20);

        m.memory.add(ms1);
        m.memory.add(ms2);
        // P1 Empty P2 Empty
        // 0-9 10-19 20-29 30-49
        // 10 10 10 20

        a.addProcessToMemory(m, new Process("P3", 10, 0, 0));
        // P1 P3 P2 Empty
        // 0-9 10-19 20-29 30-49
        // 10 10 10 20
        List<MemorySection> expected = Arrays.asList(ms1, procMs(3, 10, 10), ms2, frSpMs(20, 30));
        List<MemorySection> memory = new ArrayList<>(m.getMemory());

        assertEquals(expected.size(), memory.size());
        for (int i = 0; i < expected.size(); i++)
            assertEquals(expected.get(i), memory.get(i));
    }

}
