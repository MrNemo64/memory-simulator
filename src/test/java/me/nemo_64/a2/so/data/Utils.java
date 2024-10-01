package me.nemo_64.a2.so.data;

public class Utils {

    public static Process proc(int i, int s) {
        return new Process("P" + i, s, 0f, 0f);
    }

    public static FreeSpace frSp(int s) {
        return new FreeSpace(s);
    }

    public static MemorySection frSpMs(FreeSpace s, int p) {
        return new FreeMemorySection(s, p);
    }

    public static MemorySection frSpMs(int s, int p) {
        return frSpMs(frSp(s), p);
    }

    public static MemorySection procMs(Process s, int p) {
        return new ProcessMemorySection(s, p);
    }

    public static MemorySection procMs(int i, int s, int p) {
        return procMs(proc(i, s), p);
    }

}
