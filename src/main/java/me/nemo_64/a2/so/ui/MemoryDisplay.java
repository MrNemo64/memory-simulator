package me.nemo_64.a2.so.ui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.geom.Rectangle2D;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.function.Supplier;

import javax.swing.*;
import javax.swing.event.ChangeListener;

import me.nemo_64.a2.so.General;
import me.nemo_64.a2.so.data.MemorySection;
import me.nemo_64.a2.so.data.MemoryView;
import me.nemo_64.a2.so.event.MemorySectionAddedEvent;
import me.nemo_64.a2.so.event.MemorySectionListener;
import me.nemo_64.a2.so.event.MemorySectionRemovedEvent;
import net.miginfocom.swing.MigLayout;

public class MemoryDisplay extends JPanel implements MemorySectionListener {

    private static final long serialVersionUID = 2093810403387314462L;

    private Collection<MemorySection> displayed = new ArrayList<>();
    private MemoryView memory;
    private final Supplier<MemoryView> memorySupplier;
    private final JLabel rowsLabel = new JLabel("Rows: ");
    private final JSpinner rowsSpinner = new JSpinner(new SpinnerNumberModel(5, 1, 1000, 1));
    private final JButton exportMemory = new JButton("Export memory");
    private final JPanel canvas = new JPanel() {
        private static final long serialVersionUID = -1674476663753880456L;

        @Override
        public void paint(Graphics g) {
            reDrawMemory((Graphics2D) g, true);
        }

        @Override
        public String getToolTipText(MouseEvent event) {
            float rowHeight = getHeight() / getRows();
            int row = (int) (event.getY() / rowHeight);

            float bitPerRow = (float) memory.getLength() / getRows();
            float conversion = bitPerRow / getWidth();
            int column = (int) (event.getX() * conversion);

            int index = (int) (row * bitPerRow + column);

            MemorySection ms = getSectionAtIndex(index);
            if (ms == null)
                return "";
            return "<html>Memory position: " + General.formatMemoryPosition(index) +
                    "<br>  Element in memory: " + ms.getMemoryElement().name() +
                    "<br>  Start of element: " + General.formatMemoryPosition(ms.getStartPosition()) +
                    "<br>  End of element: " + General.formatMemoryPosition(ms.getEndPosition()) +
                    "<br>  Size of element: " + ms.getSize() + "<html>";
        }

    };

    public MemoryDisplay(Supplier<MemoryView> memorySupplier) {
        this.memorySupplier = Objects.requireNonNull(memorySupplier);
        setBorder(BorderFactory.createTitledBorder("Memory map"));
        setLayout(new MigLayout("", "[fill, grow]", "[fill, grow][]"));
        canvas.setToolTipText("");

        add(canvas, "grow, spanx, wrap");
        add(rowsLabel, "align right, split 2");
        add(rowsSpinner, "growx");
        add(exportMemory, "");

        final ChangeListener listener = (e) -> {
            canvas.repaint();
        };
        rowsSpinner.addChangeListener(listener);
        exportMemory.addActionListener((e) -> General.getMemoryManager().dumpMemoryToFile());

        General.getMemoryManager().addMemoryListener(this);
    }

    public void reDrawMemory(Graphics2D g, boolean requestMemory) {
        if (memory == null || requestMemory) {
            updateMemory();
        }
        g.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        MemoryDisplayDrawer drawer = new MemoryDisplayDrawer(g);

        for (MemorySection ms : displayed) {
            if (ms.getEndPosition() <= ms.getStartPosition()) {
                General.errorf(
                        "%s has an end position (%s) lower or the same as the start position (%s). It won't be displayed",
                        ms.toString(), General.formatMemoryPosition(ms.getEndPosition()),
                        General.formatMemoryPosition(ms.getStartPosition()));
                continue;
            }
            ms.draw(drawer);
        }
    }

    @Override
    public void memorySectionAdded(MemorySectionAddedEvent e) {
        canvas.repaint();
    }

    @Override
    public void memorySectionRemoved(MemorySectionRemovedEvent e) {
        canvas.repaint();
    }

    public int getRows() {
        return (int) rowsSpinner.getValue();
    }

    private void updateMemory() {
        memory = Objects.requireNonNull(memorySupplier.get(), "memorySupplier returned a null memory");
        displayed = Objects.requireNonNull(memory.getMemory(), "memory returned a null memory");
    }

    private MemorySection getSectionAtIndex(int index) {
        if (displayed == null)
            return null;
        for (MemorySection ms : displayed)
            if (ms.containsPosition(index))
                return ms;
        return null;
    }

    public class MemoryDisplayDrawer {

        private final int rowLen = canvas.getWidth();
        private final int canvasLen = rowLen * getRows();
        private final float conversion = (float) canvasLen / memory.getLength();
        private final float rowHeight = canvas.getHeight() / (float) getRows();

        private final Graphics2D g;

        private MemoryDisplayDrawer(Graphics2D g) {
            this.g = g;
        }

        public void draw(int startPosition, int size, Color color) {
            if (startPosition + size > memory.getLength())
                throw new IndexOutOfBoundsException("End position out of the memory: " + (startPosition + size));
            if (startPosition < 0)
                throw new IndexOutOfBoundsException("Start position out of the memory: " + startPosition);
            if (size <= 0)
                throw new IllegalArgumentException("Size invalid");
            final float start = startPosition * conversion;
            final float end = (startPosition + size) * conversion;
            g.setColor(color);
            final int startRow = (int) (start / rowLen);
            final int endRow = (int) (end / rowLen);
            if (startRow == endRow) {
                g.fill(new Rectangle2D.Float(start, startRow * rowHeight, end - start, rowHeight));
            } else {
                final float startPosInRow = start % rowLen;
                final float endPosInRow = end % rowLen;
                g.fill(new Rectangle2D.Float(startPosInRow, startRow * rowHeight, rowLen - startPosInRow, rowHeight)); // start
                g.fill(new Rectangle2D.Float(0, endRow * rowHeight, endPosInRow, rowHeight)); // end
                int rowsInBetween = endRow - startRow - 1;
                if (rowsInBetween > 0) {
                    g.fill(new Rectangle2D.Float(0, (startRow + 1) * rowHeight, rowLen, rowsInBetween * rowHeight));
                }
            }
        }

    }

}
