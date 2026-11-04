package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.text.DecimalFormat;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import model.DataStore;
import model.Evaluasi;

public class LaporanTindakLanjutDialog extends JDialog {

    private final Evaluasi evaluasi;
    private final DecimalFormat df = new DecimalFormat("#,##0.00");
    private boolean saved = false;

    private JTextArea txtCatatanLapangan;
    private JComboBox<String> cmbStatusLaporan;

    public LaporanTindakLanjutDialog(Frame parent, Evaluasi evaluasi) {
        super(parent, "Laporan Tindak Lanjut Evaluasi Lapangan", true);
        this.evaluasi = evaluasi;
        initUI();
        pack();
        setLocationRelativeTo(parent);
    }

    private void initUI() {
        setLayout(new BorderLayout(12, 12));
        setSize(520, 500);
        setMinimumSize(new Dimension(460, 440));

        // Header
        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setBackground(new Color(248, 250, 252));
        pnlHeader.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(226, 232, 240)),
            BorderFactory.createEmptyBorder(14, 18, 14, 18)
        ));

        JLabel lblTitle = new JLabel("Laporan Pelaksanaan Tindak Lanjut");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblTitle.setForeground(new Color(15, 23, 42));

        JLabel lblSubtitle = new JLabel("Perbarui catatan eksekusi lapangan agar terpantau oleh Admin dan Direksi.");
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

        JPanel pnlBody = new JPanel();
        pnlBody.setLayout(new BoxLayout(pnlBody, BoxLayout.Y_AXIS));
        pnlBody.setBackground(Color.WHITE);
        pnlBody.setBorder(BorderFactory.createEmptyBorder(14, 18, 14, 18));

        JPanel pnlInfo = new JPanel(new BorderLayout(6, 6));
        pnlInfo.setBackground(new Color(241, 245, 249));
        pnlInfo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
            BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));

        String infoHtml = String.format(
            "<html><div style='font-size: 12px; color: #334155; line-height: 1.5;'>"
            + "<b>Periode Evaluasi:</b> %s s/d %s<br>"
            + "<b>Total Hasil Panen:</b> %s Kg &nbsp;|&nbsp; <b>Tidak Layak:</b> <font color='%s'>%s%%</font><br>"
            + "<b>Catatan & Arahan Direksi:</b> <i>%s</i><br>"
            + "<b>Status Saat Ini:</b> <b>%s</b>"
            + "</div></html>",
            evaluasi.getTglAwal(), evaluasi.getTglAkhir(),
            df.format(evaluasi.getTotalPanen()),
            evaluasi.getPersenTidakLayak() > 5.0 ? "#dc2626" : "#16a34a",
            df.format(evaluasi.getPersenTidakLayak()),
            evaluasi.getCatatanDireksi() == null || "-".equals(evaluasi.getCatatanDireksi()) ? "(Belum ada catatan dari Direksi)" : evaluasi.getCatatanDireksi(),
            evaluasi.getStatus()
        );
        JLabel lblInfo = new JLabel(infoHtml);
        pnlInfo.add(lblInfo, BorderLayout.CENTER);
        pnlBody.add(pnlInfo);
        pnlBody.add(Box.createVerticalStrut(12));

        JLabel lblCatatan = new JLabel("Laporan Hasil Tindak Lanjut Lapangan (Catatan Pegawai):");
        lblCatatan.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblCatatan.setForeground(new Color(30, 41, 59));
        pnlBody.add(lblCatatan);
        pnlBody.add(Box.createVerticalStrut(6));

        txtCatatanLapangan = new JTextArea(5, 20);
        txtCatatanLapangan.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtCatatanLapangan.setLineWrap(true);
        txtCatatanLapangan.setWrapStyleWord(true);
        txtCatatanLapangan.setText(evaluasi.getCatatanPegawai() == null || "-".equals(evaluasi.getCatatanPegawai()) ? "" : evaluasi.getCatatanPegawai());

        JScrollPane scrollCatatan = new JScrollPane(txtCatatanLapangan);
        scrollCatatan.setBorder(BorderFactory.createLineBorder(new Color(203, 213, 225), 1));
        pnlBody.add(scrollCatatan);
        pnlBody.add(Box.createVerticalStrut(12));

        JLabel lblStatus = new JLabel("Status Pelaksanaan Evaluasi:");
        lblStatus.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblStatus.setForeground(new Color(30, 41, 59));
        pnlBody.add(lblStatus);
        pnlBody.add(Box.createVerticalStrut(6));

        cmbStatusLaporan = new JComboBox<>(new String[]{"Selesai", "Proses"});
        cmbStatusLaporan.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cmbStatusLaporan.setSelectedItem("Selesai");
        cmbStatusLaporan.setMaximumSize(new Dimension(Short.MAX_VALUE, 34));
        pnlBody.add(cmbStatusLaporan);

        add(pnlBody, BorderLayout.CENTER);

        JPanel pnlFooter = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 12));
        pnlFooter.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(226, 232, 240)));

        JButton btnBatal = new JButton("Batal");
        btnBatal.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnBatal.addActionListener(e -> dispose());

        JButton btnKirim = new JButton("Kirim Laporan Pelaksanaan");
        btnKirim.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnKirim.setBackground(new Color(21, 94, 239));
        btnKirim.setForeground(Color.WHITE);
        btnKirim.addActionListener(e -> {
            String catatan = txtCatatanLapangan.getText().trim();
            if (catatan.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "<html><div style='width: 300px;'>Mohon isikan catatan tindak lanjut lapangan yang telah dilaksanakan.</div></html>",
                    "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String status = cmbStatusLaporan.getSelectedItem().toString();
            evaluasi.setCatatanPegawai(catatan);
            evaluasi.setStatus(status);
            DataStore.getInstance().updateEvaluasi(evaluasi);
            saved = true;
            JOptionPane.showMessageDialog(this,
                "<html><div style='width: 300px;'>Laporan tindak lanjut berhasil disimpan dan diperbarui di database! Status: <b>" + status + "</b></div></html>",
                "Laporan Terkirim", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        });

        pnlFooter.add(btnBatal);
        pnlFooter.add(btnKirim);
        add(pnlFooter, BorderLayout.SOUTH);
    }

    public boolean isSaved() {
        return saved;
    }
}

