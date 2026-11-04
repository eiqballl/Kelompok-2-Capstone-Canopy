package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.text.DecimalFormat;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import model.Panen;

public class RangkumanEksekutifDialog extends JDialog {

    private final List<Panen> dataset;
    private final String filterInfo;
    private final DecimalFormat df = new DecimalFormat("#,##0.00");

    public RangkumanEksekutifDialog(Frame parent, List<Panen> dataset, String filterInfo) {
        super(parent, "Rangkuman Eksekutif & Analisis Kualitas Panen", true);
        this.dataset = dataset;
        this.filterInfo = filterInfo;
        initUI();
        pack();
        setLocationRelativeTo(parent);
    }

    private void initUI() {
        setLayout(new BorderLayout(12, 12));
        setSize(650, 580);
        setMinimumSize(new Dimension(580, 520));

        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setBackground(new Color(248, 250, 252));
        pnlHeader.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(226, 232, 240)),
            BorderFactory.createEmptyBorder(16, 20, 16, 20)
        ));

        JLabel lblTitle = new JLabel("Rangkuman Eksekutif & Evaluasi Kualitas");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitle.setForeground(new Color(15, 23, 42));

        JLabel lblSubtitle = new JLabel("<html><b>Kriteria Filter:</b> " + (filterInfo == null || filterInfo.isEmpty() ? "Semua Data" : filterInfo) + "</html>");
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

        double totalPanen = 0;
        double totalReject = 0;
        int count = dataset.size();
        int baik = 0, cukup = 0, kurang = 0;

        for (Panen p : dataset) {
            totalPanen += p.getTotalPanen();
            totalReject += p.getTidakLayak();
            if ("Baik".equalsIgnoreCase(p.getKualitas())) baik++;
            else if ("Cukup".equalsIgnoreCase(p.getKualitas())) cukup++;
            else kurang++;
        }

        double pctBaik = count > 0 ? (baik * 100.0 / count) : 0;
        double pctCukup = count > 0 ? (cukup * 100.0 / count) : 0;
        double pctKurang = count > 0 ? (kurang * 100.0 / count) : 0;
        double avgRejectPct = totalPanen > 0 ? (totalReject / totalPanen * 100.0) : 0;
        double avgPerPanen = count > 0 ? (totalPanen / count) : 0;

        JPanel pnlContent = new JPanel();
        pnlContent.setLayout(new BoxLayout(pnlContent, BoxLayout.Y_AXIS));
        pnlContent.setBackground(Color.WHITE);
        pnlContent.setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));

        JPanel pnlKpi = new JPanel(new GridLayout(1, 3, 12, 0));
        pnlKpi.setOpaque(false);
        pnlKpi.setMaximumSize(new Dimension(Short.MAX_VALUE, 90));

        pnlKpi.add(createCard("Total Panen Bersih", df.format(totalPanen) + " Kg", count + " Transaksi Panen", new Color(30, 41, 59)));
        pnlKpi.add(createCard("Kualitas Standar Baik", df.format(pctBaik) + "%", baik + " dari " + count + " transaksi", new Color(22, 163, 74)));
        pnlKpi.add(createCard("Tingkat Penolakan (Reject)", df.format(avgRejectPct) + "%", df.format(totalReject) + " Kg tidak layak", avgRejectPct > 5.0 ? new Color(220, 38, 38) : new Color(37, 99, 235)));

        pnlContent.add(pnlKpi);
        pnlContent.add(Box.createVerticalStrut(16));

        JPanel pnlBreakdown = new JPanel(new BorderLayout(8, 8));
        pnlBreakdown.setBackground(new Color(248, 250, 252));
        pnlBreakdown.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
            BorderFactory.createEmptyBorder(12, 14, 12, 14)
        ));

        JLabel lblBreakdownTitle = new JLabel("Komposisi Tingkat Kualitas Panen (Sesuai Parameter)");
        lblBreakdownTitle.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblBreakdownTitle.setForeground(new Color(15, 23, 42));

        String breakdownHtml = String.format(
            "<html><div style='line-height: 1.6; font-size: 12px; color: #334155;'>"
            + "<span style='color: #16a34a; font-weight: bold;'>&#9679; Mutu Baik (&le; 5%% Tidak Layak):</span> %d transaksi (%s%%)<br>"
            + "<span style='color: #d97706; font-weight: bold;'>&#9679; Mutu Cukup (5.1%% - 10%% Tidak Layak):</span> %d transaksi (%s%%)<br>"
            + "<span style='color: #dc2626; font-weight: bold;'>&#9679; Mutu Kurang (&gt; 10%% Tidak Layak):</span> %d transaksi (%s%%)<br>"
            + "<span style='color: #64748b;'>Rata-rata produksi per aktivitas panen: <b>%s Kg</b></span>"
            + "</div></html>",
            baik, df.format(pctBaik), cukup, df.format(pctCukup), kurang, df.format(pctKurang), df.format(avgPerPanen)
        );
        JLabel lblBreakdownDesc = new JLabel(breakdownHtml);

        pnlBreakdown.add(lblBreakdownTitle, BorderLayout.NORTH);
        pnlBreakdown.add(lblBreakdownDesc, BorderLayout.CENTER);
        pnlContent.add(pnlBreakdown);
        pnlContent.add(Box.createVerticalStrut(16));

        JPanel pnlSaran = new JPanel(new BorderLayout(8, 8));
        pnlSaran.setBackground(new Color(254, 252, 232));
        pnlSaran.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(254, 240, 138), 1),
            BorderFactory.createEmptyBorder(14, 14, 14, 14)
        ));

        JLabel lblSaranTitle = new JLabel("Analisis Performa & Rekomendasi Manajerial:");
        lblSaranTitle.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblSaranTitle.setForeground(new Color(133, 77, 14));

        StringBuilder saranText = new StringBuilder("<html><div style='line-height: 1.5; font-size: 12px; color: #713f12;'>");

        if (count == 0) {
            saranText.append("Tidak ada data panen yang tercatat pada filter ini untuk dianalisis.");
        } else {
            if (pctBaik >= 80.0) {
                saranText.append("<b>Kondisi Unggul:</b> Lebih dari 80% hasil panen memenuhi kualifikasi Mutu Baik. SOP pemangkasan pelepah, sortasi brondolan, dan kebersihan alur sadap karet dinilai sangat efektif. <i>Saran: Pertahankan jadwal rotasi panen dan pertahankan kalibrasi timbangan digital di TPH.</i><br><br>");
            } else if (pctBaik >= 60.0) {
                saranText.append("<b>Kondisi Cukup Stabil:</b> Mayoritas hasil panen memenuhi standar kelayakan, namun masih ditemukan porsi mutu cukup dan kurang. <i>Saran: Perketat pengawasan mandor panen terhadap brondolan tertinggal di piringan dan tingkatkan ketelitian pembuangan getah beku kotor.</i><br><br>");
            } else {
                saranText.append("<b>Perhatian Khusus / Perlu Evaluasi Cepat:</b> Proporsi mutu baik di bawah 60% dengan tingkat penolakan mencapai ")
                         .append(df.format(avgRejectPct))
                         .append("%. <i>Saran: Segera jadwalkan evaluasi lapangan, berikan pembinaan teknis cara pemotongan TBS matang optimum kepada pemanen, dan lakukan pengecekan kebersihan wadah lateks.</i><br><br>");
            }

            if (kurang > 0) {
                saranText.append("<b>Tindak Lanjut Lapangan:</b> Terdapat <b>")
                         .append(kurang)
                         .append("</b> aktivitas panen berkategori Kurang. Petugas lapangan diinstruksikan melaporkan tindak lanjut perbaikan melalui menu Evaluasi Panen.");
            } else {
                saranText.append("<b>Catatan Mutu:</b> Tidak ada aktivitas panen yang berada di kategori mutu Kurang (>10% tidak layak). Performa kualitas sangat terjaga.");
            }
        }
        saranText.append("</div></html>");

        JLabel lblSaranDesc = new JLabel(saranText.toString());
        pnlSaran.add(lblSaranTitle, BorderLayout.NORTH);
        pnlSaran.add(lblSaranDesc, BorderLayout.CENTER);

        pnlContent.add(pnlSaran);

        JScrollPane scroll = new JScrollPane(pnlContent);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getVerticalScrollBar().setUnitIncrement(14);
        add(scroll, BorderLayout.CENTER);

        JPanel pnlFooter = new JPanel(new FlowLayout(FlowLayout.RIGHT, 14, 12));
        pnlFooter.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(226, 232, 240)));

        JButton btnTutup = new JButton("Tutup Rangkuman");
        btnTutup.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnTutup.setBackground(new Color(21, 94, 239));
        btnTutup.setForeground(Color.WHITE);
        btnTutup.addActionListener(e -> dispose());
        pnlFooter.add(btnTutup);

        add(pnlFooter, BorderLayout.SOUTH);
    }

    private JPanel createCard(String title, String mainValue, String subValue, Color valueColor) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(new Color(248, 250, 252));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
            BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));

        JLabel lblT = new JLabel(title);
        lblT.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblT.setForeground(new Color(100, 116, 139));

        JLabel lblV = new JLabel(mainValue);
        lblV.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblV.setForeground(valueColor);

        JLabel lblS = new JLabel(subValue);
        lblS.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblS.setForeground(new Color(148, 163, 184));

        card.add(lblT);
        card.add(Box.createVerticalStrut(4));
        card.add(lblV);
        card.add(Box.createVerticalStrut(2));
        card.add(lblS);

        return card;
    }
}

