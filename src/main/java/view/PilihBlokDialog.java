package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import model.BlokLahan;
import model.DataStore;

public class PilihBlokDialog extends JDialog {

    private final Set<String> selectedCodes = new HashSet<>();
    private final List<JCheckBox> checkBoxes = new ArrayList<>();
    private boolean confirmed = false;

    public PilihBlokDialog(Frame parent, Set<String> initialSelected) {
        super(parent, "Pilih Blok Lahan Rekapitulasi", true);
        if (initialSelected != null) {
            this.selectedCodes.addAll(initialSelected);
        }
        initUI();
        util.UiStyles.polish(getContentPane());
        util.UiStyles.closeOnEscape(this);
        pack();
        util.UiStyles.fitToScreen(this);
        setLocationRelativeTo(parent);
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        setPreferredSize(new Dimension(420, 480));
        setMinimumSize(new Dimension(380, 400));

        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setBackground(new Color(248, 250, 252));
        pnlHeader.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(226, 232, 240)),
            BorderFactory.createEmptyBorder(12, 16, 12, 16)
        ));

        JLabel lblTitle = new JLabel("Filter Berdasarkan Blok Lahan");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTitle.setForeground(new Color(15, 23, 42));

        JLabel lblSubtitle = new JLabel("Pilih satu atau beberapa blok lahan yang ingin dianalisis.");
        lblSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSubtitle.setForeground(new Color(100, 116, 139));

        JPanel pnlTitleBox = new JPanel();
        pnlTitleBox.setLayout(new BoxLayout(pnlTitleBox, BoxLayout.Y_AXIS));
        pnlTitleBox.setOpaque(false);
        pnlTitleBox.add(lblTitle);
        pnlTitleBox.add(Box.createVerticalStrut(4));
        pnlTitleBox.add(lblSubtitle);

        pnlHeader.add(pnlTitleBox, BorderLayout.CENTER);
        add(pnlHeader, BorderLayout.NORTH);

        JPanel pnlList = new JPanel();
        pnlList.setLayout(new BoxLayout(pnlList, BoxLayout.Y_AXIS));
        pnlList.setBackground(Color.WHITE);
        pnlList.setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 16));

        List<BlokLahan> blokList = DataStore.getInstance().getBlokList();
        boolean selectAllByDefault = selectedCodes.isEmpty();

        for (BlokLahan bl : blokList) {
            String labelText = String.format("%s - %s (%s, %.1f Ha)",
                bl.getKodeBlok(), bl.getNamaLahan(), bl.getKomoditas(), bl.getLuasLahan());
            JCheckBox cb = new JCheckBox(labelText);
            cb.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            cb.setOpaque(false);
            cb.setActionCommand(bl.getKodeBlok());
            cb.setToolTipText(labelText);
            cb.setSelected(selectAllByDefault || selectedCodes.contains(bl.getKodeBlok()));
            checkBoxes.add(cb);
            pnlList.add(cb);
            pnlList.add(Box.createVerticalStrut(8));
        }
        if (blokList.isEmpty()) {
            JLabel empty = new JLabel("<html>Belum ada blok lahan.<br>Minta Admin menambahkan blok lahan.</html>");
            empty.setFont(util.UiStyles.BODY);
            empty.setForeground(util.UiStyles.MUTED);
            pnlList.add(empty);
        }

        JScrollPane scrollPane = new JScrollPane(pnlList);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(12);
        add(scrollPane, BorderLayout.CENTER);

        JPanel pnlBottom = new JPanel(new BorderLayout(8, 8));
        pnlBottom.setBackground(Color.WHITE);
        pnlBottom.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(226, 232, 240)),
            BorderFactory.createEmptyBorder(10, 16, 10, 16)
        ));

        JPanel pnlQuick = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        pnlQuick.setOpaque(false);
        JButton btnPilihSemua = new JButton("Pilih Semua");
        btnPilihSemua.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnPilihSemua.addActionListener(e -> {
            for (JCheckBox cb : checkBoxes) cb.setSelected(true);
        });

        JButton btnHapusSemua = new JButton("Hapus Pilihan");
        btnHapusSemua.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnHapusSemua.addActionListener(e -> {
            for (JCheckBox cb : checkBoxes) cb.setSelected(false);
        });

        pnlQuick.add(btnPilihSemua);
        pnlQuick.add(btnHapusSemua);
        pnlBottom.add(pnlQuick, BorderLayout.NORTH);

        JPanel pnlSubmit = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        pnlSubmit.setOpaque(false);
        JButton btnBatal = new JButton("Batal");
        btnBatal.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnBatal.addActionListener(e -> dispose());

        JButton btnTerapkan = new JButton("Terapkan Filter");
        btnTerapkan.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnTerapkan.setBackground(new Color(21, 94, 239));
        btnTerapkan.setForeground(Color.WHITE);
        btnTerapkan.addActionListener(e -> {
            selectedCodes.clear();
            for (JCheckBox cb : checkBoxes) {
                if (cb.isSelected()) {
                    selectedCodes.add(cb.getActionCommand());
                }
            }
            confirmed = true;
            dispose();
        });

        pnlSubmit.add(btnBatal);
        pnlSubmit.add(btnTerapkan);
        pnlBottom.add(pnlSubmit, BorderLayout.SOUTH);
        getRootPane().setDefaultButton(btnTerapkan);

        add(pnlBottom, BorderLayout.SOUTH);
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public Set<String> getSelectedBlokCodes() {
        return new HashSet<>(selectedCodes);
    }

    public String getSummaryText() {
        int total = DataStore.getInstance().getBlokList().size();
        if (selectedCodes.isEmpty() || selectedCodes.size() == total) {
            return "Semua Blok";
        }
        if (selectedCodes.size() == 1) {
            return selectedCodes.iterator().next();
        }
        return selectedCodes.size() + " Blok Dipilih";
    }
}
