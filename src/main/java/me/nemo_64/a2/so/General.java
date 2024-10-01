package me.nemo_64.a2.so;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import me.nemo_64.a2.so.data.MemoryManager;
import me.nemo_64.a2.so.data.ProcessManager;
import me.nemo_64.a2.so.event.StepListener;
import me.nemo_64.a2.so.ui.DebugConsole;
import me.nemo_64.a2.so.ui.Frame;
import me.nemo_64.a2.so.util.TimeManager;

public class General {

    private static float currentTime = 0;

    private static final List<StepListener> stepListeners = new ArrayList<>();
    private static final MemoryManager memoryManager = new MemoryManager();
    private static final ProcessManager processManager = new ProcessManager();
    private static final Frame frame = new Frame();

    private General() {
    }

    public static void step() {
        float avance = frame.getTimeManager().get();
        currentTime += avance;
        infof("Stepping %.1f seccond(s). Actual time: %.1f", avance, getCurrentTime());
        for (StepListener listener : stepListeners)
            listener.step(avance);
    }

    public static boolean addStepListener(StepListener e) {
        return stepListeners.add(Objects.requireNonNull(e));
    }

    public static boolean removeStepListener(StepListener e) {
        return stepListeners.remove(Objects.requireNonNull(e));
    }

    public static Frame getFrame() {
        return frame;
    }

    public static float getCurrentTime() {
        return currentTime;
    }

    public static String formatMemoryPosition(int p) {
        return isUseHexForDirections() ? "0x" + Integer.toHexString(p) : String.valueOf(p);
    }

    public static boolean isUseHexForDirections() {
        return frame.isUseHexForDirections();
    }

    public static MemoryManager getMemoryManager() {
        return memoryManager;
    }

    public static ProcessManager getProcessManager() {
        return processManager;
    }

    public static TimeManager getTimeManager() {
        return frame.getTimeManager();
    }

    public static DebugConsole getConsole() {
        return frame.getConsole();
    }

    public static float getStepTimeSize() {
        return frame.getTimeManager().get();
    }

    public static void infof(String msg, Object... f) {
        getConsole().info(String.format(msg, f));
    }

    public static void warnf(String msg, Object... f) {
        getConsole().warn(String.format(msg, f));
    }

    public static void errorf(String msg, Object... f) {
        getConsole().error(String.format(msg, f));
    }

    public static Color randomColor() {
        Random r = new Random();
        return new Color(r.nextInt(180), r.nextInt(180), r.nextInt(180));
    }

}