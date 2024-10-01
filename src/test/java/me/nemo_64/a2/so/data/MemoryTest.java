package me.nemo_64.a2.so.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import static me.nemo_64.a2.so.data.Utils.*;

public class MemoryTest {

    @Test
    public void testAddMemoryElelemnt() {
        Memory m = new Memory(10);

        MemorySection ms1 = procMs(1, 2, 1);
        MemorySection ms2 = procMs(2, 2, 4);
        MemorySection ms3 = procMs(3, 3, 6);
        MemorySection ms4 = procMs(4, 1, 9);

        assertThrows(IllegalArgumentException.class, () -> m.addMemoryElement(new FreeSpace(1), 0));
        assertFalse(m.addMemoryElement(proc(5, 2), 9));

        assertTrue(m.addMemoryElement(proc(1, 2), 1));
        assertTrue(m.addMemoryElement(proc(2, 2), 4));
        assertTrue(m.addMemoryElement(proc(3, 3), 6));
        assertTrue(m.addMemoryElement(proc(4, 1), 9));

        assertFalse(m.addMemoryElement(proc(5, 2), 3));
        assertFalse(m.addMemoryElement(proc(5, 2), 1));

        List<MemorySection> expected = Arrays.asList(frSpMs(1, 0), ms1, frSpMs(1, 3), ms2, ms3, ms4);
        List<MemorySection> actual = new ArrayList<>(m.getMemory());
        assertEquals(expected.size(), actual.size());
        for (int i = 0; i < expected.size(); i++)
            assertEquals(expected.get(i), actual.get(i));
    }

    @Test
    public void testGetMemorySectionAt() {
        Memory m = new Memory(10);
        MemorySection ms1 = procMs(1, 2, 1);
        MemorySection ms2 = procMs(2, 2, 4);
        MemorySection ms3 = procMs(3, 3, 6);

        m.memory.add(ms1);
        m.memory.add(ms2);
        m.memory.add(ms3);

        assertEquals(frSpMs(1, 0), m.getMemorySectionAt(0));
        assertEquals(ms1, m.getMemorySectionAt(1));
        assertEquals(ms1, m.getMemorySectionAt(2));
        assertEquals(frSpMs(1, 3), m.getMemorySectionAt(3));
        assertEquals(ms2, m.getMemorySectionAt(4));
        assertEquals(ms2, m.getMemorySectionAt(5));
        assertEquals(ms3, m.getMemorySectionAt(6));
        assertEquals(ms3, m.getMemorySectionAt(7));
        assertEquals(ms3, m.getMemorySectionAt(8));
        assertEquals(frSpMs(1, 9), m.getMemorySectionAt(9));
    }

    @Test
    public void testGetMemory() {
        Memory m = new Memory(10);
        MemorySection ms1 = procMs(1, 2, 1);
        MemorySection ms2 = procMs(2, 2, 4);
        MemorySection ms3 = procMs(3, 3, 6);

        m.memory.add(ms1);
        m.memory.add(ms2);
        m.memory.add(ms3);

        List<MemorySection> expected = Arrays.asList(frSpMs(1, 0), ms1, frSpMs(1, 3), ms2, ms3, frSpMs(1, 9));
        List<MemorySection> actual = new ArrayList<>(m.getMemory());
        assertEquals(expected.size(), actual.size());
        for (int i = 0; i < expected.size(); i++)
            assertEquals(expected.get(i), actual.get(i));
    }

}
