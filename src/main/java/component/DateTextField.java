package component;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import view.CalendarDialog;

public class DateTextField extends JTextField {

    private static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final JButton picker;

    public DateTextField() {
        super();
        setToolTipText("Format yyyy-MM-dd. Tekan Alt+Down atau F4 untuk membuka kalender.");
        picker = new JButton(new CalendarIcon());
        picker.setFocusable(true);
        picker.setFocusPainted(true);
        picker.setBorderPainted(false);
        picker.setMargin(new Insets(0, 0, 0, 0));
        picker.setOpaque(false);
        picker.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        picker.setToolTipText("Pilih tanggal (Alt+Down atau F4)");
        picker.getAccessibleContext().setAccessibleName("Pilih tanggal");
        picker.addActionListener(e -> openPicker());
        add(picker);
        Insets margin = getMargin();
        setMargin(new Insets(margin.top, margin.left, margin.bottom, margin.right + 28));
        getInputMap().put(KeyStroke.getKeyStroke("alt DOWN"), "open-calendar");
        getInputMap().put(KeyStroke.getKeyStroke("F4"), "open-calendar");
        getActionMap().put("open-calendar", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent event) {
                openPicker();
            }
        });
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) openPicker();
            }
        });
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                positionPicker();
            }

            @Override
            public void componentShown(ComponentEvent e) {
                positionPicker();
            }
        });
    }

    private void positionPicker() {
        int size = Math.max(24, Math.min(getHeight() - 4, 28));
        picker.setBounds(Math.max(0, getWidth() - size - 4), Math.max(0, (getHeight() - size) / 2), size, size);
    }

    @Override
    public void doLayout() {
        super.doLayout();
        positionPicker();
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (picker != null) picker.setEnabled(enabled && isEditable());
    }

    @Override
    public void setEditable(boolean editable) {
        super.setEditable(editable);
        if (picker != null) picker.setEnabled(editable && isEnabled());
    }

    public void openPicker() {
        if (!isEnabled() || !isEditable()) return;
        String result = CalendarDialog.showDialog(this, getText());
        if (result != null) setText(result);
        requestFocusInWindow();
    }

    public LocalDate getDate() {
        String text = getText() == null ? "" : getText().trim();
        if (text.isEmpty()) return null;
        try {
            return LocalDate.parse(text, FORMAT);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    private static class CalendarIcon implements Icon {
        @Override
        public int getIconWidth() {
            return 16;
        }

        @Override
        public int getIconHeight() {
            return 16;
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(c.isEnabled() ? util.UiStyles.TEXT : util.UiStyles.MUTED);
            g2.drawRoundRect(x + 1, y + 2, 13, 12, 3, 3);
            g2.fillRect(x + 1, y + 2, 14, 4);
            g2.setColor(Color.WHITE);
            g2.drawLine(x + 4, y + 1, x + 4, y + 4);
            g2.drawLine(x + 11, y + 1, x + 11, y + 4);
            g2.setColor(c.isEnabled() ? util.UiStyles.TEXT : util.UiStyles.MUTED);
            for (int row = 0; row < 2; row++) {
                for (int col = 0; col < 3; col++) {
                    g2.fillRect(x + 3 + col * 4, y + 9 + row * 3, 2, 2);
                }
            }
            g2.dispose();
        }
    }
}
