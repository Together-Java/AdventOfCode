package org.togetherjava.aoc.visualizer.matrix;

import org.togetherjava.aoc.core.math.matrix.IMatrix;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.*;
import java.util.function.Function;

/**
 * A Swing-based visualizer for Matrix objects that can display real-time updates.
 * Supports customizable cell rendering, automatic refresh, and command-based history navigation.
 */
public class MatrixVisualizer<T> {

    private final JFrame frame;
    private final IMatrix<T> matrix;
    private final MatrixPanel matrixPanel;
    private final Timer refreshTimer;
    private final CommandHistory<T> commandHistory;

    private Function<T, Color> colorMapper;
    private Function<T, String> textMapper;
    private int cellSize = 30;
    private Font font = new Font("Arial", Font.BOLD, 12);
    private boolean showGrid = true;
    private boolean showText = true;
    private Color gridColor = Color.GRAY;
    private Color backgroundColor = Color.WHITE;

    // UI Components for command navigation
    private JButton undoButton;
    private JButton redoButton;
    private JLabel historyLabel;

    // Default color mapping for common types
    private static final Map<Object, Color> DEFAULT_COLORS = new HashMap<>();
    static {
        DEFAULT_COLORS.put(null, Color.WHITE);
        DEFAULT_COLORS.put("", Color.WHITE);
        DEFAULT_COLORS.put(" ", Color.WHITE);
        DEFAULT_COLORS.put(".", Color.LIGHT_GRAY);
        DEFAULT_COLORS.put("#", Color.BLACK);
        DEFAULT_COLORS.put("X", Color.RED);
        DEFAULT_COLORS.put("O", Color.BLUE);
        DEFAULT_COLORS.put(0, Color.WHITE);
        DEFAULT_COLORS.put(1, Color.BLACK);
        DEFAULT_COLORS.put(true, Color.GREEN);
        DEFAULT_COLORS.put(false, Color.RED);
    }

    public MatrixVisualizer(IMatrix<T> matrix) {
        this(matrix, "Matrix Visualizer");
    }

    public MatrixVisualizer(IMatrix<T> matrix, String title) {
        this.frame = new JFrame(title);
        this.matrix = matrix;
        this.matrixPanel = new MatrixPanel();
        this.commandHistory = new CommandHistory<>();

        // Set up default mappers
        this.colorMapper = this::getDefaultColor;
        this.textMapper = this::getDefaultText;
        this.refreshTimer = new Timer(100, e -> refresh());

        initializeUI();

        // Set up auto-refresh timer (refreshes every 100ms by default)
        //this.refreshTimer
        // Start with auto-refresh disabled for manual stepping
        // this.refreshTimer.start();
    }

    private void initializeUI() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Add matrix panel with scroll support
        JScrollPane scrollPane = new JScrollPane(matrixPanel);
        scrollPane.setPreferredSize(new Dimension(800, 600));
        frame.add(scrollPane, BorderLayout.CENTER);

        // Add control panels
        frame.add(createControlPanel(), BorderLayout.SOUTH);
        frame.add(createCommandPanel(), BorderLayout.NORTH);

        // Handle window resizing
        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                matrixPanel.revalidate();
                matrixPanel.repaint();
            }
        });

        updateCommandButtons();
        frame.pack();
        frame.setLocationRelativeTo(null);
    }

    private JPanel createCommandPanel() {
        JPanel commandPanel = new JPanel(new FlowLayout());
        commandPanel.setBorder(BorderFactory.createTitledBorder("Command History"));

        undoButton = new JButton("← Undo");
        undoButton.addActionListener(e -> undo());
        commandPanel.add(undoButton);

        redoButton = new JButton("Redo →");
        redoButton.addActionListener(e -> redo());
        commandPanel.add(redoButton);

        JButton clearHistoryButton = new JButton("Clear History");
        clearHistoryButton.addActionListener(e -> {
            commandHistory.clear();
            updateCommandButtons();
            refresh();
        });
        commandPanel.add(clearHistoryButton);

        historyLabel = new JLabel("History: 0/0");
        commandPanel.add(historyLabel);

        return commandPanel;
    }

    private JPanel createControlPanel() {
        JPanel controlPanel = new JPanel(new FlowLayout());
        controlPanel.setBorder(BorderFactory.createTitledBorder("Display Controls"));

        // Cell size slider
        JSlider cellSizeSlider = new JSlider(10, 100, cellSize);
        cellSizeSlider.addChangeListener(e -> {
            cellSize = cellSizeSlider.getValue();
            matrixPanel.revalidate();
            matrixPanel.repaint();
        });
        controlPanel.add(new JLabel("Cell Size:"));
        controlPanel.add(cellSizeSlider);

        JSlider textSizeSlider = new JSlider(10, 100, 12);
        textSizeSlider.addChangeListener(e -> {
            setFontSize(textSizeSlider.getValue());
            matrixPanel.revalidate();
            matrixPanel.repaint();
        });
        controlPanel.add(new JLabel("Font Size:"));
        controlPanel.add(textSizeSlider);

        // Show grid checkbox
        JCheckBox gridCheckbox = new JCheckBox("Show Grid", showGrid);
        gridCheckbox.addActionListener(e -> {
            showGrid = gridCheckbox.isSelected();
            matrixPanel.repaint();
        });
        controlPanel.add(gridCheckbox);

        // Show text checkbox
        JCheckBox textCheckbox = new JCheckBox("Show Text", showText);
        textCheckbox.addActionListener(e -> {
            showText = textCheckbox.isSelected();
            matrixPanel.repaint();
        });
        controlPanel.add(textCheckbox);

        // Auto-refresh controls
        JCheckBox autoRefreshCheckbox = new JCheckBox("Auto Refresh", refreshTimer.isRunning());
        autoRefreshCheckbox.addActionListener(e -> {
            if (autoRefreshCheckbox.isSelected()) {
                startAutoRefresh();
            } else {
                stopAutoRefresh();
            }
        });
        controlPanel.add(autoRefreshCheckbox);

        // Refresh rate slider
        JSlider refreshSlider = new JSlider(50, 1000, 100);
        refreshSlider.addChangeListener(e -> {
            refreshTimer.setDelay(refreshSlider.getValue());
        });
        controlPanel.add(new JLabel("Refresh Rate (ms):"));
        controlPanel.add(refreshSlider);

        // Manual refresh button
        JButton refreshButton = new JButton("Refresh Now");
        refreshButton.addActionListener(e -> refresh());
        controlPanel.add(refreshButton);

        return controlPanel;
    }

    // Public methods for command execution
    public void executeCommand(Command<T> command) {
        commandHistory.execute(command, matrix);
        updateCommandButtons();
        refresh();
    }

    public void setCellValue(int row, int col, T value) {
        executeCommand(new SetCellCommand<>(row, col, value));
    }

    public void fillRegion(int startRow, int startCol, int endRow, int endCol, T value) {
        executeCommand(new FillRegionCommand<>(startRow, startCol, endRow, endCol, value));
    }

    public void undo() {
        if (commandHistory.canUndo()) {
            commandHistory.undo(matrix);
            updateCommandButtons();
            refresh();
        }
    }

    public void redo() {
        if (commandHistory.canRedo()) {
            commandHistory.redo(matrix);
            updateCommandButtons();
            refresh();
        }
    }

    private void updateCommandButtons() {
        if (undoButton != null) {
            undoButton.setEnabled(commandHistory.canUndo());
            redoButton.setEnabled(commandHistory.canRedo());
            historyLabel.setText(String.format("History: %d/%d",
                    commandHistory.getCurrentIndex() + 1,
                    commandHistory.getSize()));
        }
    }

    // Rest of the original methods remain the same
    private Color getDefaultColor(T value) {
        Color color = DEFAULT_COLORS.get(value);
        if (color != null) {
            return color;
        }

        // Generate color based on hash code for unknown values
        if (value != null) {
            int hash = value.hashCode();
            return new Color(
                    Math.abs(hash) % 256,
                    Math.abs(hash >> 8) % 256,
                    Math.abs(hash >> 16) % 256
            );
        }

        return Color.WHITE;
    }

    public JFrame getFrame() {
        return this.frame;
    }

    private String getDefaultText(T value) {
        if (value == null) return "";
        String str = value.toString();
        // Truncate long strings
        return str.length() > 3 ? str.substring(0, 3) : str;
    }

    public void setFontSize(float fontSize) {
        this.font = font.deriveFont(fontSize);
        refresh();
    }

    public void setColorMapper(Function<T, Color> colorMapper) {
        this.colorMapper = colorMapper;
        refresh();
    }

    public void setTextMapper(Function<T, String> textMapper) {
        this.textMapper = textMapper;
        refresh();
    }

    public void setCellSize(int cellSize) {
        this.cellSize = Math.max(5, cellSize);
        matrixPanel.revalidate();
        matrixPanel.repaint();
    }

    public void setShowGrid(boolean showGrid) {
        this.showGrid = showGrid;
        matrixPanel.repaint();
    }

    public void setShowText(boolean showText) {
        this.showText = showText;
        matrixPanel.repaint();
    }

    public void setGridColor(Color gridColor) {
        this.gridColor = gridColor;
        matrixPanel.repaint();
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
        matrixPanel.setBackground(backgroundColor);
        matrixPanel.repaint();
    }

    public void setRefreshRate(int milliseconds) {
        refreshTimer.setDelay(Math.max(10, milliseconds));
    }

    public void startAutoRefresh() {
        refreshTimer.start();
    }

    public void stopAutoRefresh() {
        refreshTimer.stop();
    }

    public void refresh() {
        SwingUtilities.invokeLater(matrixPanel::repaint);
    }

    public void highlightCell(int row, int col, Color highlightColor) {
        matrixPanel.setHighlight(row, col, highlightColor);
        refresh();
    }

    public void clearHighlight() {
        matrixPanel.clearHighlight();
        refresh();
    }

    private class MatrixPanel extends JPanel {
        private int highlightRow = -1;
        private int highlightCol = -1;
        private Color highlightColor = Color.YELLOW;

        public MatrixPanel() {
            setBackground(backgroundColor);
        }

        public void setHighlight(int row, int col, Color color) {
            this.highlightRow = row;
            this.highlightCol = col;
            this.highlightColor = color;
        }

        public void clearHighlight() {
            this.highlightRow = -1;
            this.highlightCol = -1;
        }

        @Override
        public Dimension getPreferredSize() {
            int width = matrix.getCols() * cellSize;
            int height = matrix.getRows() * cellSize;
            return new Dimension(width, height);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();

            // Enable antialiasing for smoother text
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g2d.setFont(font);
            // Draw matrix cells
            for (int row = 0; row < matrix.getRows(); row++) {
                for (int col = 0; col < matrix.getCols(); col++) {
                    int x = col * cellSize;
                    int y = row * cellSize;

                    T value = matrix.get(row, col);

                    // Fill cell with mapped color
                    g2d.setColor(colorMapper.apply(value));
                    g2d.fillRect(x, y, cellSize, cellSize);

                    // Draw highlight if this cell is highlighted
                    if (row == highlightRow && col == highlightCol) {
                        g2d.setColor(highlightColor);
                        g2d.setStroke(new BasicStroke(3));
                        g2d.drawRect(x + 1, y + 1, cellSize - 2, cellSize - 2);
                        g2d.setStroke(new BasicStroke(1));
                    }

                    // Draw grid
                    if (showGrid) {
                        g2d.setColor(gridColor);
                        g2d.drawRect(x, y, cellSize, cellSize);
                    }

                    // Draw text
                    if (showText && value != null) {
                        String text = textMapper.apply(value);
                        if (!text.isEmpty()) {
                            g2d.setColor(getContrastColor(colorMapper.apply(value)));
                            FontMetrics fm = g2d.getFontMetrics();
                            int textX = x + (cellSize - fm.stringWidth(text)) / 2;
                            int textY = y + (cellSize + fm.getAscent()) / 2;
                            g2d.drawString(text, textX, textY);
                        }
                    }
                }
            }

            g2d.dispose();
        }

        // Get contrasting color for text visibility
        private Color getContrastColor(Color backgroundColor) {
            double luminance = (0.299 * backgroundColor.getRed() +
                    0.587 * backgroundColor.getGreen() +
                    0.114 * backgroundColor.getBlue()) / 255;
            return luminance > 0.5 ? Color.BLACK : Color.WHITE;
        }
    }

    // Utility methods to create and show a visualizer
    public static <T> MatrixVisualizer<T> show(IMatrix<T> matrix) {
        return show(matrix, "Matrix Visualizer");
    }

    public static <T> MatrixVisualizer<T> show(IMatrix<T> matrix, String title) {
        MatrixVisualizer<T> visualizer = new MatrixVisualizer<>(matrix, title);
        SwingUtilities.invokeLater(() -> visualizer.getFrame().setVisible(true));
        return visualizer;
    }
}