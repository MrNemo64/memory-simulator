package me.nemo_64.a2.so.data;

import org.junit.Test;

import me.nemo_64.a2.so.algorithm.MemoryAlgorithmNextSpace;

import static me.nemo_64.a2.so.data.Utils.*;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NextSpaceTest {

    @Test
    public void test1() {
        Memory m = new Memory(100);
        MemoryAlgorithmNextSpace a = new MemoryAlgorithmNextSpace();
        MemorySection ms1 = procMs(1, 20, 10);
        MemorySection ms2 = procMs(2, 20, 40);
        MemorySection ms3 = procMs(3, 10, 80);
        m.memory.add(ms1);
        m.memory.add(ms2);
        m.memory.add(ms3);
        // Empty P1 Empty P2 Empty P3 Empty
        // 0-9 10-29 30-39 40-59 60-79 80-89 90-99
        // 10 20 10 20 20 10 10
        Process p4 = proc(4, 20);
        a.addProcessToMemory(m, p4);
        // Empty P1 Empty P2 P4 P3 Empty
        // 0-9 10-29 30-39 40-59 60-79 80-89 90-99
        // 10 20 10 20 20 10 10
        Process p5 = proc(5, 10);
        a.addProcessToMemory(m, p5);
        // Empty P1 Empty P2 P4 P3 P5
        // 0-9 10-29 30-39 40-59 60-79 80-89 90-99
        // 10 20 10 20 20 10 10
        Process p6 = proc(6, 10);
        a.addProcessToMemory(m, p6);
        // P6 P1 Empty P2 P4 P3 P5
        // 0-9 10-29 30-39 40-59 60-79 80-89 90-99
        // 10 20 10 20 20 10 10

        List<MemorySection> expected = Arrays.asList(procMs(p6, 0), ms1, frSpMs(10, 30), ms2, procMs(p4, 60), ms3,
                procMs(p5, 90));
        List<MemorySection> memory = new ArrayList<>(m.getMemory());
        assertEquals(expected.size(), memory.size());
        for (int i = 0; i < expected.size(); i++)
            assertEquals(expected.get(i), memory.get(i));
    }

    @Test
    public void test2() {
        Memory m = new Memory(100);
        MemoryAlgorithmNextSpace a = new MemoryAlgorithmNextSpace(60);
        MemorySection ms1 = procMs(1, 10, 60);
        m.memory.add(ms1);
        // Empty P1 Empty
        // 0-59 60-69 70-99
        // 60 10 30
        Process p2 = proc(2, 10);
        a.addProcessToMemory(m, p2);
        // Empty P1 P2 Empty
        // 0-59 60-69 70-79 80-99
        // 60 10 10 20
        Process p3 = proc(3, 10);
        a.addProcessToMemory(m, p3);
        // Empty P1 P2 P3 Empty
        // 0-59 60-69 70-79 80-89 90-99
        // 60 10 10 10 10
        Process p4 = proc(4, 20);
        a.addProcessToMemory(m, p4);
        // P4 Empty P1 P2 P3 Empty
        // 0-19 30-59 60-69 70-79 80-89 90-99
        // 20 30 10 10 10 10
        Process p5 = proc(5, 10);
        a.addProcessToMemory(m, p5);
        // P4 P5 Empty P1 P2 P3 Empty
        // 0-19 20-29 30-59 60-69 70-79 80-89 90-99
        // 20 10 30 10 10 10 10

        List<MemorySection> expected = Arrays.asList(procMs(p4, 0), procMs(p5, 20),
                frSpMs(30, 30), ms1, procMs(p2, 70), procMs(p3, 80), frSpMs(10, 90));
        List<MemorySection> memory = new ArrayList<>(m.getMemory());

        assertEquals(expected.size(), memory.size());
        for (int i = 0; i < expected.size(); i++)
            assertEquals(expected.get(i), memory.get(i));
    }

}
