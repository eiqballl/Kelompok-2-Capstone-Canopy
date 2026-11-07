package util;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.Locale;
import java.util.function.IntConsumer;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JViewport;
import javax.swing.KeyStroke;
import javax.swing.Scrollable;
import javax.swing.SwingConstants;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;

/** Small presentation-only improvements on top of the existing Swing look and feel. */
public final class UiStyles {
    public static final Color TEXT = new Color(51, 65, 85);
    public static final Color MUTED = new Color(100, 116, 139);
    public static final Color BORDER = new Color(226, 232, 240);
    public static final Color SURFACE = Color.WHITE;
    public static final Color SURFACE_SOFT = new Color(248, 250, 252);
    public static final Color ACCENT = new Color(21, 94, 239);
    public static final Color DANGER = new Color(185, 28, 28);
    public static final Color SUCCESS = new Color(21, 128, 61);
    public static final Color WARNING_TEXT = new Color(146, 64, 14);
    public static final Color OFFLINE_ON_DARK = new Color(253, 230, 138);
    public static final Font BODY = new Font("Segoe UI", Font.PLAIN, 13);

    // Nimbus lightens filled controls; these shades retain readable white labels.
    private static final Color ACCENT_SOLID = new Color(16, 66, 172);
    private static final Color DANGER_SOLID = new Color(153, 27, 27);
    private static final Color SUCCESS_SOLID = new Color(18, 72, 39);
    private static final Color WARNING_SOFT = new Color(254, 243, 199);
    private static final Color SELECTION = new Color(232, 240, 255);
    private static final Color SECONDARY = new Color(241, 245, 249);

    private UiStyles() {
    }

    public static void polish(Container root) {
        for (Component child : root.getComponents()) {
            if (child instanceof JTable) {
                polishTable((JTable) child);
                continue;
            }
            if (child instanceof JButton) polishButton((JButton) child);
            if (child instanceof JCheckBox && child.getFont().getSize() < 12) {
                child.setFont(child.getFont().deriveFont(12f));
            }
            if (child instanceof JTextArea) {
                JTextArea area = (JTextArea) child;
                area.setFont(BODY);
                area.setMargin(new Insets(6, 8, 6, 8));
            }
            if (child instanceof JComponent) {
                JComponent component = (JComponent) child;
                Border border = component.getBorder();
                if (border instanceof LineBorder && Color.BLACK.equals(((LineBorder) border).getLineColor())) {
                    component.setBorder(BorderFactory.createLineBorder(BORDER));
                }
            }
            if (child instanceof JScrollPane) {
                JScrollPane scroll = (JScrollPane) child;
                scroll.getVerticalScrollBar().setUnitIncrement(16);
                if (scroll.getViewport().getView() instanceof JTable) {
                    scroll.getViewport().setBackground(SURFACE);
                    scroll.setBorder(BorderFactory.createLineBorder(BORDER));
                }
            }
            if (child instanceof Container) polish((Container) child);
            if (child instanceof JSplitPane) polishSplit((JSplitPane) child);
        }
    }

    public static void polishDashboard(JFrame frame) {
        polish(frame.getContentPane());
        frame.setMinimumSize(new Dimension(900, 560));
        fitToScreen(frame);
        frame.setLocationRelativeTo(null);
    }

    public static void fitToScreen(Window window) {
        Rectangle screen = window.getGraphicsConfiguration().getBounds();
        Insets insets = Toolkit.getDefaultToolkit().getScreenInsets(window.getGraphicsConfiguration());
        int width = screen.width - insets.left - insets.right;
        int height = screen.height - insets.top - insets.bottom;
        Dimension minimum = window.getMinimumSize();
        window.setMinimumSize(new Dimension(Math.min(minimum.width, width), Math.min(minimum.height, height)));
        window.setSize(Math.min(window.getWidth(), width), Math.min(window.getHeight(), height));
    }

    public static void secondary(JButton button) {
        button.setBackground(SECONDARY);
        button.setForeground(TEXT);
    }

    public static void offlineHeader(JLabel label) {
        label.setText("Mode offline | Data contoh, tidak tersimpan");
        label.setForeground(OFFLINE_ON_DARK);
        label.setToolTipText("Data contoh dapat dicoba, tetapi perubahan tidak tersimpan ke database.");
    }

    public static void polishButton(JButton button) {
        if (button.getText() == null || button.getText().isEmpty()) return;
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setFocusPainted(true);
        if (!Color.WHITE.equals(button.getForeground())) return;
        Color background = button.getBackground();
        if (ACCENT.equals(background)) {
            button.setBackground(ACCENT_SOLID);
        } else if (new Color(239, 68, 68).equals(background)) {
            button.setBackground(DANGER_SOLID);
        } else if (new Color(245, 158, 11).equals(background)) {
            button.setBackground(WARNING_SOFT);
            button.setForeground(WARNING_TEXT);
        } else if (new Color(107, 114, 128).equals(background)) {
            secondary(button);
        } else if (new Color(22, 163, 74).equals(background)) {
            button.setBackground(SUCCESS_SOLID);
        }
        if ("Nimbus".equals(UIManager.getLookAndFeel().getID())) {
            // Use Nimbus's normal painters for default buttons too: its pale default
            // palette ignores the control's background. Keep native hover and focus.
            UIDefaults overrides = new UIDefaults();
            String[][] states = {
                {"Default", "Enabled"}, {"Default+Focused", "Focused"},
                {"Default+MouseOver", "MouseOver"}, {"Default+Focused+MouseOver", "Focused+MouseOver"},
                {"Default+Pressed", "Pressed"}, {"Default+Focused+Pressed", "Focused+Pressed"}
            };
            for (String[] state : states) {
                Object painter = UIManager.get("Button[" + state[1] + "].backgroundPainter");
                if (painter != null) overrides.put("Button[" + state[0] + "].backgroundPainter", painter);
            }
            button.putClientProperty("Nimbus.Overrides.InheritDefaults", true);
            button.putClientProperty("Nimbus.Overrides", overrides);
        }
    }

    private static void polishTable(JTable table) {
        table.setFont(BODY);
        table.setRowHeight(32);
        table.setFillsViewportHeight(true);
        table.setShowVerticalLines(false);
        table.setShowHorizontalLines(true);
        table.setGridColor(BORDER);
        table.setBackground(SURFACE);
        table.setForeground(TEXT);
        table.setSelectionBackground(SELECTION);
        table.setSelectionForeground(TEXT);
        table.setDefaultRenderer(Object.class, new ReadableTableRenderer());
        table.getTableHeader().setFont(BODY.deriveFont(Font.BOLD));
        table.getTableHeader().setPreferredSize(new Dimension(0, 32));
        table.getTableHeader().setForeground(TEXT);

        // Wide datasets keep complete dates and headings instead of squeezing every column.
        if (table.getColumnCount() >= 7) {
            table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            FontMetrics body = table.getFontMetrics(BODY);
            FontMetrics heading = table.getFontMetrics(table.getTableHeader().getFont());
            for (int index = 0; index < table.getColumnCount(); index++) {
                TableColumn column = table.getColumnModel().getColumn(index);
                int width = Math.max(72, heading.stringWidth(table.getColumnName(index)) + 24);
                for (int row = 0; row < Math.min(table.getRowCount(), 200); row++) {
                    Object value = table.getValueAt(row, index);
                    if (value != null) width = Math.max(width, Math.min(360, body.stringWidth(value.toString()) + 24));
                }
                column.setMinWidth(Math.max(64, heading.stringWidth(table.getColumnName(index)) + 24));
                column.setPreferredWidth(width);
            }
        }
    }

    private static void polishSplit(JSplitPane split) {
        Component form = split.getLeftComponent();
        if (!(form instanceof JPanel)) return;
        int divider = split.getDividerLocation();
        int minimumWidth = form.getMinimumSize().width;
        JScrollPane scroll = scrollContent((JComponent) form);
        scroll.setMinimumSize(new Dimension(minimumWidth + 20, 0));
        split.setLeftComponent(scroll);
        split.setDividerLocation(Math.max(divider, minimumWidth + 21));
        split.setContinuousLayout(true);
        split.setResizeWeight(0.0);
    }

    public static JScrollPane scrollContent(JComponent content) {
        JScrollPane scroll = new JScrollPane(new ScrollableContent(content));
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.getHorizontalScrollBar().setUnitIncrement(16);
        return scroll;
    }

    private static void keepFocusVisible(Container root) {
        for (Component child : root.getComponents()) {
            if (child instanceof JComponent && child.isFocusable()) {
                child.addFocusListener(new FocusAdapter() {
                    @Override
                    public void focusGained(FocusEvent event) {
                        JComponent control = (JComponent) event.getComponent();
                        control.scrollRectToVisible(new Rectangle(0, 0, control.getWidth(), control.getHeight()));
                    }
                });
            }
            if (child instanceof Container) keepFocusVisible((Container) child);
        }
    }

    public static String recapSummary(int count, String total, String rejected, String percentage,
                                      int good, int fair, int poor) {
        return "<html><b>" + count + " transaksi</b> &nbsp;|&nbsp; <b>Total panen:</b> " + total + " Kg"
                + "<br><b>Tidak layak:</b> <font color='" + hex(DANGER) + "'>" + rejected + " Kg (" + percentage + "%)</font>"
                + "<br><b>Mutu:</b> <font color='" + hex(SUCCESS) + "'>" + good + " Baik</font> &nbsp;|&nbsp; "
                + "<font color='" + hex(WARNING_TEXT) + "'>" + fair + " Cukup</font> &nbsp;|&nbsp; "
                + "<font color='" + hex(DANGER) + "'>" + poor + " Kurang</font></html>";
    }

    public static String escapeHtml(String text) {
        if (text == null) return "";
        return text.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;")
                .replace("\"", "&quot;").replace("'", "&#39;").replace("\n", "<br>");
    }

    public static String hex(Color color) {
        return String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
    }

    public static void calendarNavigation(JButton button, IntConsumer move) {
        String[] keys = {"LEFT", "RIGHT", "UP", "DOWN"};
        int[] offsets = {-1, 1, -7, 7};
        for (int index = 0; index < keys.length; index++) {
            final int offset = offsets[index];
            String action = "calendar-move-" + keys[index];
            button.getInputMap().put(KeyStroke.getKeyStroke(keys[index]), action);
            button.getActionMap().put(action, new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent event) {
                    move.accept(offset);
                }
            });
        }
        button.getInputMap().put(KeyStroke.getKeyStroke("ENTER"), "pressed");
        button.getInputMap().put(KeyStroke.getKeyStroke("released ENTER"), "released");
    }

    public static void closeOnEscape(JDialog dialog) {
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke("ESCAPE"), "close-dialog");
        dialog.getRootPane().getActionMap().put("close-dialog", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent event) {
                dialog.dispose();
            }
        });
    }

    private static final class ReadableTableRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean selected,
                                                        boolean focused, int row, int column) {
            super.getTableCellRendererComponent(table, value, selected, focused, row, column);
            if (!selected) {
                setBackground(row % 2 == 0 ? SURFACE : SURFACE_SOFT);
                setForeground(TEXT);
            }
            String heading = table.getColumnName(column).toLowerCase(Locale.ROOT);
            boolean numeric = heading.contains("(kg)") || heading.contains("(%)")
                    || heading.contains("luas") || heading.equals("total panen");
            setHorizontalAlignment(numeric ? SwingConstants.RIGHT : SwingConstants.LEFT);
            setToolTipText(value == null ? null : "<html>" + escapeHtml(value.toString()) + "</html>");
            Border padding = BorderFactory.createEmptyBorder(0, 8, 0, 8);
            setBorder(focused ? BorderFactory.createCompoundBorder(getBorder(), padding) : padding);
            return this;
        }
    }

    private static final class ScrollableContent extends JPanel implements Scrollable {
        private final JComponent content;

        ScrollableContent(JComponent content) {
            super(new BorderLayout());
            this.content = content;
            setBackground(content.getBackground());
            add(content, BorderLayout.CENTER);
            keepFocusVisible(content);
        }

        @Override
        public Dimension getPreferredScrollableViewportSize() {
            return content.getPreferredSize();
        }

        @Override
        public int getScrollableUnitIncrement(Rectangle visible, int orientation, int direction) {
            return 16;
        }

        @Override
        public int getScrollableBlockIncrement(Rectangle visible, int orientation, int direction) {
            return Math.max(16, (orientation == SwingConstants.VERTICAL ? visible.height : visible.width) - 16);
        }

        @Override
        public boolean getScrollableTracksViewportWidth() {
            return getParent() instanceof JViewport && getParent().getWidth() >= content.getMinimumSize().width;
        }

        @Override
        public boolean getScrollableTracksViewportHeight() {
            return getParent() instanceof JViewport && getParent().getHeight() >= getPreferredSize().height;
        }
    }
}
