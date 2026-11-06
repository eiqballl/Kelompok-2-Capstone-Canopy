package view;

import component.DateTextField;
import controller.InputValidator;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import model.BlokLahan;
import model.DataStore;
import model.Evaluasi;
import model.Panen;

public class PetugasFrame extends javax.swing.JFrame {
    private final java.util.Set<String> selectedBlokFilter = new java.util.HashSet<>();

    private String selectedPanenId = null;
    private final DecimalFormat df = new DecimalFormat("#,##0.00");

    public PetugasFrame() {
        initComponents();
        applyBranding();
        setLocationRelativeTo(null);
        if (DataStore.getInstance().isOfflineMode()) {
            lblUserInfo.setText("MODE OFFLINE (data contoh, tidak tersimpan ke database)");
            lblUserInfo.setForeground(new java.awt.Color(220, 38, 38));
        }
        initCustomData();
        refreshEvalTable();
    }

    private void applyBranding() {
        java.awt.Image appIcon = util.Images.load("logo.png");
        if (appIcon != null) setIconImage(appIcon);
        pnlHeader.setBorder(new component.ImageBorder("header-bg.png", new java.awt.Color(4, 47, 46), 3));
        javax.swing.ImageIcon brandIcon = util.Images.icon("logo.png", 30);
        if (brandIcon != null) {
            lblLogoHeader.setIcon(brandIcon);
            lblLogoHeader.setIconTextGap(10);
        }
        lblLogoHeader.setForeground(java.awt.Color.WHITE);
        lblUserInfo.setForeground(new java.awt.Color(198, 226, 219));
    }

    private void initCustomData() {
        cmbFilterKomoditas.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"Semua", "Sawit", "Karet"}));
        lblFilterBlok.setText("Filter Mutu Panen:");
        cmbFilterBlok.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"Semua Mutu", "Baik", "Cukup", "Kurang"}));
        cmbFilterBlok.addActionListener(e -> btnTampilkanRekapActionPerformed(null));
        cmbFilterKomoditas.addActionListener(e -> btnTampilkanRekapActionPerformed(null));

        cmbModeRekap.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"Semua Data", "Rentang Tanggal", "Bulan Ini", "Tahun Ini", "Hari Ini"}));
        cmbModeRekap.addActionListener(e -> handleModeRekapChange());

        tblPanen.setAutoCreateRowSorter(true);
        tblRekap.setAutoCreateRowSorter(true);
        tblEvaluasi.setAutoCreateRowSorter(true);

        tblEvaluasi.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    btnLaporEvalActionPerformed(null);
                }
            }
        });
        tblPanen.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblPanenMouseClicked(evt);
            }
        });
        cmbBlokLahan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Object sel = cmbBlokLahan.getSelectedItem();
                if (sel != null) {
                    model.BlokLahan b = model.DataStore.getInstance().findBlokByKode(sel.toString());
                    if (b != null) {
                        if ("Sawit".equalsIgnoreCase(b.getKomoditas())) {
                            if (!rbSawit.isSelected()) {
                                rbSawit.setSelected(true);
                                updateKomoditasLabels();
                            }
                        } else if ("Karet".equalsIgnoreCase(b.getKomoditas())) {
                            if (!rbKaret.isSelected()) {
                                rbKaret.setSelected(true);
                                updateKomoditasLabels();
                            }
                        }
                    }
                }
            }
        });
        tabMain.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                int idx = tabMain.getSelectedIndex();
                switch (idx) {
                    case 0: refreshPanenTable(); loadBlokComboBox(); break;
                    case 1: refreshRekapTable(); break;
                    case 2: refreshEvalTable(); break;
                }
            }
        });
        loadBlokComboBox();
        refreshPanenTable();
        txtTanggalPanen.setText(LocalDate.now().toString());
        cmbModeRekap.setSelectedItem("Semua Data");
        handleModeRekapChange();
    }

    private void loadBlokComboBox() {
        String selected = cmbBlokLahan.getSelectedItem() == null ? null : cmbBlokLahan.getSelectedItem().toString();
        String commodity = rbSawit.isSelected() ? "Sawit" : "Karet";
        cmbBlokLahan.removeAllItems();
        List<BlokLahan> bloks = DataStore.getInstance().getBlokList();
        for (BlokLahan b : bloks) {
            if (commodity.equalsIgnoreCase(b.getKomoditas())) cmbBlokLahan.addItem(b.toString());
        }
        if (selected != null) cmbBlokLahan.setSelectedItem(selected);
    }

    public void refreshEvalTable() {
        if (tblEvaluasi == null) return;
        DefaultTableModel model = (DefaultTableModel) tblEvaluasi.getModel();
        model.setRowCount(0);
        int no = 1;
        for (Evaluasi ev : DataStore.getInstance().getEvaluasiList()) {
            model.addRow(new Object[]{
                no++,
                ev.getTglAwal() + " s/d " + ev.getTglAkhir(),
                df.format(ev.getTotalPanen()) + " Kg (Rata-rata: " + df.format(ev.getRataRataPerBulan()) + " Kg/bln)",
                df.format(ev.getPersenTidakLayak()) + "%",
                ev.getStatus(),
                ev.getCatatanTindakLanjut(),
                ev.getCatatanEvaluasi(),
                ev.getCatatanPetugas()
            });
        }
    }


    private Panen buildPanen(String id) {
        String block = cmbBlokLahan.getSelectedItem() == null ? "" : cmbBlokLahan.getSelectedItem().toString();
        model.BlokLahan foundBlok = model.DataStore.getInstance().findBlokByKode(block);
        String commodity = foundBlok != null ? foundBlok.getKomoditas() : (rbSawit.isSelected() ? "Sawit" : "Karet");
        if ("Sawit".equalsIgnoreCase(commodity)) {
            if (!rbSawit.isSelected()) {
                rbSawit.setSelected(true);
                updateKomoditasLabels();
            }
        } else {
            if (!rbKaret.isSelected()) {
                rbKaret.setSelected(true);
                updateKomoditasLabels();
            }
        }
        String input1 = txtInput1.getText().trim();
        String input2 = txtInput2.getText().trim();
        String rejectText = txtTidakLayak.getText().trim();
        String thnTanam = txtTahunTanamPanen.getText().trim();
        String date = txtTanggalPanen.getText().trim();
        if (block.isEmpty()) {
            JOptionPane.showMessageDialog(this, "<html><div style='width: 300px;'><b>Validasi Gagal:</b> Blok lahan wajib dipilih dan tidak boleh kosong!</div></html>", "Input Tidak Valid", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        if (input1.isEmpty() || !InputValidator.isNonNegativeNumber(input1)) {
            JOptionPane.showMessageDialog(this, "<html><div style='width: 300px;'><b>Validasi Gagal:</b> Kolom " + lblInput1.getText().replace(":", "") + " wajib diisi dan berupa angka non-negatif!</div></html>", "Input Tidak Valid", JOptionPane.WARNING_MESSAGE);
            txtInput1.requestFocus();
            return null;
        }
        if (input2.isEmpty() || !InputValidator.isNonNegativeNumber(input2)) {
            JOptionPane.showMessageDialog(this, "<html><div style='width: 300px;'><b>Validasi Gagal:</b> Kolom " + lblInput2.getText().replace(":", "") + " wajib diisi dan berupa angka non-negatif!</div></html>", "Input Tidak Valid", JOptionPane.WARNING_MESSAGE);
            txtInput2.requestFocus();
            return null;
        }
        if (rejectText.isEmpty() || !InputValidator.isNonNegativeNumber(rejectText)) {
            JOptionPane.showMessageDialog(this, "<html><div style='width: 300px;'><b>Validasi Gagal:</b> Hasil tidak layak wajib diisi dan harus berupa angka non-negatif (isi 0 jika tidak ada reject)!</div></html>", "Input Tidak Valid", JOptionPane.WARNING_MESSAGE);
            txtTidakLayak.requestFocus();
            return null;
        }
        if (thnTanam.isEmpty() || !thnTanam.matches("\\d{4}")) {
            JOptionPane.showMessageDialog(this, "<html><div style='width: 300px;'><b>Validasi Gagal:</b> Tahun tanam produk wajib diisi dengan 4 digit angka yang valid (contoh: 2018)!</div></html>", "Input Tidak Valid", JOptionPane.WARNING_MESSAGE);
            txtTahunTanamPanen.requestFocus();
            return null;
        }
        if (date.isEmpty() || !InputValidator.isValidDate(date)) {
            JOptionPane.showMessageDialog(this, "<html><div style='width: 300px;'><b>Validasi Gagal:</b> Tanggal panen wajib diisi dengan format YYYY-MM-DD yang valid!</div></html>", "Input Tidak Valid", JOptionPane.WARNING_MESSAGE);
            txtTanggalPanen.requestFocus();
            return null;
        }
        double first = Double.parseDouble(input1);
        double second = Double.parseDouble(input2);
        double reject = Double.parseDouble(rejectText);
        double total = "Sawit".equalsIgnoreCase(commodity) ? Panen.hitungTotalSawit(first, second) : first;
        if (total <= 0 || reject > total) {
            JOptionPane.showMessageDialog(this, "<html><div style='width: 320px;'>Total panen harus lebih dari nol dan hasil tidak layak tidak boleh melebihi total panen (karena hasil tidak layak merupakan bagian dari total panen kotor).</div></html>", "Input Tidak Valid", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        Panen panen = new Panen(id, date, block, commodity, first, second, total, reject, "", thnTanam);
        panen.setKualitas(panen.tentukanKualitas());
        return panen;
    }

    public void refreshPanenTable() {
        DefaultTableModel model = (DefaultTableModel) tblPanen.getModel();
        model.setRowCount(0);
        List<Panen> list = DataStore.getInstance().getPanenList();
        for (Panen p : list) {
            model.addRow(new Object[]{
                p.getIdPanen(),
                p.getTanggal(),
                p.getBlokLahan(),
                p.getKomoditas(),
                p.getTahunTanam(),
                df.format(p.getTotalPanen()),
                p.getKualitas()
            });
        }
    }

    public void refreshRekapTable() {
        btnTampilkanRekapActionPerformed(null);
    }

    private void resetPanenForm() {
        selectedPanenId = null;
        rbSawit.setSelected(true);
        updateKomoditasLabels();
        loadBlokComboBox();
        if (cmbBlokLahan.getItemCount() > 0) cmbBlokLahan.setSelectedIndex(0);
        txtInput1.setText("");
        txtInput2.setText("");
        txtTidakLayak.setText("");
        txtTahunTanamPanen.setText("");
        txtTanggalPanen.setText(LocalDate.now().toString());
        tblPanen.clearSelection();
    }

    private void updateKomoditasLabels() {
        if (rbSawit.isSelected()) {
            lblInput1.setText("Jumlah Berat Total Tandan (Kg):");
            lblInput2.setText("Berat Brondolan (Kg):");
        } else {
            lblInput1.setText("Berat Kotor Karet (Kg):");
            lblInput2.setText("Kadar Air Karet (%):");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnGroupKomoditas = new javax.swing.ButtonGroup();
        pnlHeader = new javax.swing.JPanel();
        lblLogoHeader = new javax.swing.JLabel();
        lblUserInfo = new javax.swing.JLabel();
        btnLogout = new javax.swing.JButton();
        tabMain = new javax.swing.JTabbedPane();
        pnlTabPanen = new javax.swing.JPanel();
        splitPanen = new javax.swing.JSplitPane();
        pnlFormPanen = new javax.swing.JPanel();
        lblKomoditas = new javax.swing.JLabel();
        rbSawit = new javax.swing.JRadioButton();
        rbKaret = new javax.swing.JRadioButton();
        lblBlok = new javax.swing.JLabel();
        cmbBlokLahan = new javax.swing.JComboBox<>();
        lblInput1 = new javax.swing.JLabel();
        txtInput1 = new javax.swing.JTextField();
        lblInput2 = new javax.swing.JLabel();
        txtInput2 = new javax.swing.JTextField();
        lblTidakLayak = new javax.swing.JLabel();
        txtTidakLayak = new javax.swing.JTextField();
        lblTahunTanamPanen = new javax.swing.JLabel();
        txtTahunTanamPanen = new javax.swing.JTextField();
        lblTanggal = new javax.swing.JLabel();
        txtTanggalPanen = new DateTextField();
        btnSimpan = new javax.swing.JButton();
        btnPerbarui = new javax.swing.JButton();
        btnHapus = new javax.swing.JButton();
        btnReset = new javax.swing.JButton();
        pnlTabelPanen = new javax.swing.JPanel();
        lblDaftarPanen = new javax.swing.JLabel();
        scrollPanen = new javax.swing.JScrollPane();
        tblPanen = new javax.swing.JTable();
        pnlTabRekap = new javax.swing.JPanel();
        splitRekap = new javax.swing.JSplitPane();
        pnlFilterRekap = new javax.swing.JPanel();
        lblModeRekap = new javax.swing.JLabel();
        cmbModeRekap = new javax.swing.JComboBox<>();
        lblFilterKomoditas = new javax.swing.JLabel();
        cmbFilterKomoditas = new javax.swing.JComboBox<>();
        lblFilterBlok = new javax.swing.JLabel();
        cmbFilterBlok = new javax.swing.JComboBox<>();
        lblTglAwal = new javax.swing.JLabel();
        txtTglAwal = new DateTextField();
        lblTglAkhir = new javax.swing.JLabel();
        txtTglAkhir = new DateTextField();
        btnPilihBlok = new javax.swing.JButton();
        btnTampilkanRekap = new javax.swing.JButton();
        btnResetRekap = new javax.swing.JButton();
        pnlTabelRekap = new javax.swing.JPanel();
        lblDaftarRekap = new javax.swing.JLabel();
        scrollRekap = new javax.swing.JScrollPane();
        tblRekap = new javax.swing.JTable();
        lblTotalRekap = new javax.swing.JLabel();
        pnlTabEvaluasi = new javax.swing.JPanel();
        lblDaftarEval = new javax.swing.JLabel();
        btnLaporEval = new javax.swing.JButton();
        scrollEval = new javax.swing.JScrollPane();
        tblEvaluasi = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("CanopyONE - Menu Pegawai Lapangan");
        setPreferredSize(new java.awt.Dimension(1200, 750));

        pnlHeader.setBackground(new java.awt.Color(255, 255, 255));
        pnlHeader.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(229, 231, 235)));

        lblLogoHeader.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblLogoHeader.setForeground(new java.awt.Color(21, 94, 239));
        lblLogoHeader.setText("CanopyONE Panel Petugas");

        lblUserInfo.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        lblUserInfo.setForeground(new java.awt.Color(75, 85, 99));
        lblUserInfo.setText("Petugas: Lapangan Sawit & Karet");

        btnLogout.setBackground(new java.awt.Color(239, 68, 68));
        btnLogout.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnLogout.setForeground(new java.awt.Color(255, 255, 255));
        btnLogout.setText("Logout");
        btnLogout.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnLogout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLogoutActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlHeaderLayout = new javax.swing.GroupLayout(pnlHeader);
        pnlHeader.setLayout(pnlHeaderLayout);
        pnlHeaderLayout.setHorizontalGroup(
            pnlHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlHeaderLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(pnlHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblLogoHeader)
                    .addComponent(lblUserInfo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnLogout, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20))
        );
        pnlHeaderLayout.setVerticalGroup(
            pnlHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlHeaderLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(pnlHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnLogout, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pnlHeaderLayout.createSequentialGroup()
                        .addComponent(lblLogoHeader)
                        .addGap(2, 2, 2)
                        .addComponent(lblUserInfo)))
                .addContainerGap(12, Short.MAX_VALUE))
        );

        tabMain.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N

        splitPanen.setDividerLocation(420);
        splitPanen.setDividerSize(6);

        pnlFormPanen.setBackground(new java.awt.Color(255, 255, 255));
        pnlFormPanen.setBorder(javax.swing.BorderFactory.createEmptyBorder(15, 15, 15, 15));

        lblKomoditas.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblKomoditas.setText("Komoditas Panen:");

        btnGroupKomoditas.add(rbSawit);
        rbSawit.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        rbSawit.setSelected(true);
        rbSawit.setText("Kelapa Sawit");
        rbSawit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbSawitActionPerformed(evt);
            }
        });

        btnGroupKomoditas.add(rbKaret);
        rbKaret.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        rbKaret.setText("Karet");
        rbKaret.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbKaretActionPerformed(evt);
            }
        });

        lblBlok.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblBlok.setText("Pilih Blok Lahan:");

        cmbBlokLahan.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N

        lblInput1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblInput1.setText("Jumlah Berat Total Tandan (Kg):");

        txtInput1.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N

        lblInput2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblInput2.setText("Jumlah Brondolan (Kg):");

        txtInput2.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N

        lblTidakLayak.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblTidakLayak.setText("Hasil Panen Tidak Layak (Kg):");

        txtTidakLayak.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N

        lblTahunTanamPanen.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblTahunTanamPanen.setText("Tahun Tanam Produk:");

        txtTahunTanamPanen.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N

        lblTanggal.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblTanggal.setText("Tanggal Panen (YYYY-MM-DD):");

        txtTanggalPanen.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N

        btnSimpan.setBackground(new java.awt.Color(21, 94, 239));
        btnSimpan.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnSimpan.setForeground(new java.awt.Color(255, 255, 255));
        btnSimpan.setText("Simpan Panen");
        btnSimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSimpanActionPerformed(evt);
            }
        });

        btnPerbarui.setBackground(new java.awt.Color(245, 158, 11));
        btnPerbarui.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnPerbarui.setForeground(new java.awt.Color(255, 255, 255));
        btnPerbarui.setText("Perbarui");
        btnPerbarui.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPerbaruiActionPerformed(evt);
            }
        });

        btnHapus.setBackground(new java.awt.Color(239, 68, 68));
        btnHapus.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnHapus.setForeground(new java.awt.Color(255, 255, 255));
        btnHapus.setText("Hapus");
        btnHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHapusActionPerformed(evt);
            }
        });

        btnReset.setBackground(new java.awt.Color(107, 114, 128));
        btnReset.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnReset.setForeground(new java.awt.Color(255, 255, 255));
        btnReset.setText("Reset");
        btnReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlFormPanenLayout = new javax.swing.GroupLayout(pnlFormPanen);
        pnlFormPanen.setLayout(pnlFormPanenLayout);
        pnlFormPanenLayout.setHorizontalGroup(
            pnlFormPanenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlFormPanenLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlFormPanenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cmbBlokLahan, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtInput1)
                    .addComponent(txtInput2)
                    .addComponent(txtTidakLayak)
                    .addComponent(txtTahunTanamPanen)
                    .addComponent(txtTanggalPanen)
                    .addGroup(pnlFormPanenLayout.createSequentialGroup()
                        .addGroup(pnlFormPanenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblKomoditas)
                            .addGroup(pnlFormPanenLayout.createSequentialGroup()
                                .addComponent(rbSawit)
                                .addGap(18, 18, 18)
                                .addComponent(rbKaret))
                            .addComponent(lblBlok)
                            .addComponent(lblInput1)
                            .addComponent(lblInput2)
                            .addComponent(lblTidakLayak)
                            .addComponent(lblTahunTanamPanen)
                            .addComponent(lblTanggal))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(pnlFormPanenLayout.createSequentialGroup()
                        .addComponent(btnSimpan, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnPerbarui, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnHapus, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 69, Short.MAX_VALUE)))
                .addContainerGap())
        );
        pnlFormPanenLayout.setVerticalGroup(
            pnlFormPanenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlFormPanenLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblKomoditas)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlFormPanenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rbSawit)
                    .addComponent(rbKaret))
                .addGap(12, 12, 12)
                .addComponent(lblBlok)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmbBlokLahan, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblInput1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtInput1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblInput2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtInput2, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblTidakLayak)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtTidakLayak, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblTahunTanamPanen)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtTahunTanamPanen, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblTanggal)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtTanggalPanen, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24)
                .addGroup(pnlFormPanenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSimpan, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnPerbarui, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnHapus, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(87, Short.MAX_VALUE))
        );

        splitPanen.setLeftComponent(pnlFormPanen);

        pnlTabelPanen.setBackground(new java.awt.Color(255, 255, 255));
        pnlTabelPanen.setBorder(javax.swing.BorderFactory.createEmptyBorder(15, 15, 15, 15));

        lblDaftarPanen.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblDaftarPanen.setText("Daftar Hasil Panen:");

        tblPanen.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        tblPanen.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID Panen", "Tanggal", "Blok Lahan", "Komoditas", "Tahun Tanam", "Total Panen (Kg)", "Kualitas"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblPanen.setRowHeight(26);
        scrollPanen.setViewportView(tblPanen);

        javax.swing.GroupLayout pnlTabelPanenLayout = new javax.swing.GroupLayout(pnlTabelPanen);
        pnlTabelPanen.setLayout(pnlTabelPanenLayout);
        pnlTabelPanenLayout.setHorizontalGroup(
            pnlTabelPanenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTabelPanenLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlTabelPanenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(scrollPanen, javax.swing.GroupLayout.DEFAULT_SIZE, 712, Short.MAX_VALUE)
                    .addGroup(pnlTabelPanenLayout.createSequentialGroup()
                        .addComponent(lblDaftarPanen)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        pnlTabelPanenLayout.setVerticalGroup(
            pnlTabelPanenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTabelPanenLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblDaftarPanen)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(scrollPanen, javax.swing.GroupLayout.DEFAULT_SIZE, 507, Short.MAX_VALUE)
                .addContainerGap())
        );

        splitPanen.setRightComponent(pnlTabelPanen);

        javax.swing.GroupLayout pnlTabPanenLayout = new javax.swing.GroupLayout(pnlTabPanen);
        pnlTabPanen.setLayout(pnlTabPanenLayout);
        pnlTabPanenLayout.setHorizontalGroup(
            pnlTabPanenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(splitPanen)
        );
        pnlTabPanenLayout.setVerticalGroup(
            pnlTabPanenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(splitPanen)
        );

        tabMain.addTab("Kelola Data Panen", pnlTabPanen);

        splitRekap.setDividerLocation(380);
        splitRekap.setDividerSize(6);

        pnlFilterRekap.setBackground(new java.awt.Color(255, 255, 255));
        pnlFilterRekap.setBorder(javax.swing.BorderFactory.createEmptyBorder(15, 15, 15, 15));

        lblModeRekap.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblModeRekap.setText("Mode Rekapitulasi:");

        cmbModeRekap.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N

        lblFilterKomoditas.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblFilterKomoditas.setText("Filter Komoditas:");

        cmbFilterKomoditas.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N

        lblFilterBlok.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblFilterBlok.setText("Filter Mutu Panen:");

        cmbFilterBlok.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N

        lblTglAwal.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblTglAwal.setText("Tanggal Awal (YYYY-MM-DD):");

        txtTglAwal.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        txtTglAwal.setText("2026-10-01");

        lblTglAkhir.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblTglAkhir.setText("Tanggal Akhir (YYYY-MM-DD):");

        txtTglAkhir.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        txtTglAkhir.setText("2026-10-31");

        btnPilihBlok.setText("Pilih Blok Lahan (Semua)");
        btnPilihBlok.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPilihBlokActionPerformed(evt);
            }
        });

        btnTampilkanRekap.setBackground(new java.awt.Color(21, 94, 239));
        btnTampilkanRekap.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        btnTampilkanRekap.setForeground(new java.awt.Color(255, 255, 255));
        btnTampilkanRekap.setText("Tampilkan Rekapitulasi");
        btnTampilkanRekap.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTampilkanRekapActionPerformed(evt);
            }
        });

        btnResetRekap.setBackground(new java.awt.Color(107, 114, 128));
        btnResetRekap.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnResetRekap.setForeground(new java.awt.Color(255, 255, 255));
        btnResetRekap.setText("Tampilkan Semua Data");
        btnResetRekap.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetRekapActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlFilterRekapLayout = new javax.swing.GroupLayout(pnlFilterRekap);
        pnlFilterRekap.setLayout(pnlFilterRekapLayout);
        pnlFilterRekapLayout.setHorizontalGroup(
            pnlFilterRekapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlFilterRekapLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlFilterRekapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cmbModeRekap, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cmbFilterKomoditas, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cmbFilterBlok, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnPilihBlok, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtTglAwal)
                    .addComponent(txtTglAkhir)
                    .addComponent(btnTampilkanRekap, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnResetRekap, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(pnlFilterRekapLayout.createSequentialGroup()
                        .addGroup(pnlFilterRekapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblModeRekap)
                            .addComponent(lblFilterKomoditas)
                            .addComponent(lblFilterBlok)
                            .addComponent(lblTglAwal)
                            .addComponent(lblTglAkhir))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        pnlFilterRekapLayout.setVerticalGroup(
            pnlFilterRekapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlFilterRekapLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblModeRekap)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmbModeRekap, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblFilterKomoditas)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmbFilterKomoditas, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblFilterBlok)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmbFilterBlok, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnPilihBlok, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblTglAwal)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtTglAwal, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblTglAkhir)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtTglAkhir, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnTampilkanRekap, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnResetRekap, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(128, Short.MAX_VALUE))
        );

        splitRekap.setLeftComponent(pnlFilterRekap);

        pnlTabelRekap.setBackground(new java.awt.Color(255, 255, 255));
        pnlTabelRekap.setBorder(javax.swing.BorderFactory.createEmptyBorder(15, 15, 15, 15));

        lblDaftarRekap.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblDaftarRekap.setText("Hasil Rekapitulasi Data Panen:");

        tblRekap.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        tblRekap.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID Panen", "Tanggal", "Blok Lahan", "Komoditas", "Total (Kg)", "Tidak Layak (Kg)", "Kualitas"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblRekap.setRowHeight(26);
        scrollRekap.setViewportView(tblRekap);

        lblTotalRekap.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        lblTotalRekap.setForeground(new java.awt.Color(21, 94, 239));
        lblTotalRekap.setText("Total Hasil Panen: 0.00 Kg");

        javax.swing.GroupLayout pnlTabelRekapLayout = new javax.swing.GroupLayout(pnlTabelRekap);
        pnlTabelRekap.setLayout(pnlTabelRekapLayout);
        pnlTabelRekapLayout.setHorizontalGroup(
            pnlTabelRekapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTabelRekapLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlTabelRekapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(scrollRekap, javax.swing.GroupLayout.DEFAULT_SIZE, 752, Short.MAX_VALUE)
                    .addGroup(pnlTabelRekapLayout.createSequentialGroup()
                        .addGroup(pnlTabelRekapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblDaftarRekap)
                            .addComponent(lblTotalRekap))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        pnlTabelRekapLayout.setVerticalGroup(
            pnlTabelRekapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTabelRekapLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblDaftarRekap)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(scrollRekap, javax.swing.GroupLayout.DEFAULT_SIZE, 465, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(lblTotalRekap)
                .addContainerGap())
        );

        splitRekap.setRightComponent(pnlTabelRekap);

        javax.swing.GroupLayout pnlTabRekapLayout = new javax.swing.GroupLayout(pnlTabRekap);
        pnlTabRekap.setLayout(pnlTabRekapLayout);
        pnlTabRekapLayout.setHorizontalGroup(
            pnlTabRekapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(splitRekap)
        );
        pnlTabRekapLayout.setVerticalGroup(
            pnlTabRekapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(splitRekap)
        );

        tabMain.addTab("Rekapitulasi", pnlTabRekap);

        lblDaftarEval.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblDaftarEval.setText("Daftar Riwayat Rekam Evaluasi Hasil Panen:");

        btnLaporEval.setBackground(new java.awt.Color(22, 163, 74));
        btnLaporEval.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        btnLaporEval.setForeground(new java.awt.Color(255, 255, 255));
        btnLaporEval.setText("Lapor Tindak Lanjut");
        btnLaporEval.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLaporEvalActionPerformed(evt);
            }
        });

        tblEvaluasi.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        tblEvaluasi.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "No.", "Periode", "Total Panen", "Tidak Layak (%)", "Status", "Catatan Tindak Lanjut", "Catatan Evaluasi", "Catatan Petugas"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblEvaluasi.setRowHeight(26);
        scrollEval.setViewportView(tblEvaluasi);

        javax.swing.GroupLayout pnlTabEvaluasiLayout = new javax.swing.GroupLayout(pnlTabEvaluasi);
        pnlTabEvaluasi.setLayout(pnlTabEvaluasiLayout);
        pnlTabEvaluasiLayout.setHorizontalGroup(
            pnlTabEvaluasiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTabEvaluasiLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlTabEvaluasiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(scrollEval, javax.swing.GroupLayout.DEFAULT_SIZE, 1150, Short.MAX_VALUE)
                    .addGroup(pnlTabEvaluasiLayout.createSequentialGroup()
                        .addComponent(lblDaftarEval)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnLaporEval, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        pnlTabEvaluasiLayout.setVerticalGroup(
            pnlTabEvaluasiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTabEvaluasiLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlTabEvaluasiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblDaftarEval)
                    .addComponent(btnLaporEval, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(scrollEval, javax.swing.GroupLayout.DEFAULT_SIZE, 510, Short.MAX_VALUE)
                .addContainerGap())
        );

        tabMain.addTab("Evaluasi Panen", pnlTabEvaluasi);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlHeader, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(tabMain)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(pnlHeader, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tabMain))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnLogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLogoutActionPerformed
        new LoginFrame().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnLogoutActionPerformed

    private void rbSawitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbSawitActionPerformed
        updateKomoditasLabels();
        loadBlokComboBox();
    }//GEN-LAST:event_rbSawitActionPerformed

    private void rbKaretActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbKaretActionPerformed
        updateKomoditasLabels();
        loadBlokComboBox();
    }//GEN-LAST:event_rbKaretActionPerformed

    private void tblPanenMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPanenMouseClicked
        int selectedRow = tblPanen.getSelectedRow();
        if (selectedRow != -1) {
            selectedPanenId = tblPanen.getValueAt(selectedRow, 0).toString();
            txtTanggalPanen.setText(tblPanen.getValueAt(selectedRow, 1).toString());
            String blok = tblPanen.getValueAt(selectedRow, 2).toString();
            String komoditas = tblPanen.getValueAt(selectedRow, 3).toString();
            if ("Sawit".equalsIgnoreCase(komoditas)) {
                rbSawit.setSelected(true);
            } else {
                rbKaret.setSelected(true);
            }
            updateKomoditasLabels();
            loadBlokComboBox();
            cmbBlokLahan.setSelectedItem(blok);

            for (Panen p : DataStore.getInstance().getPanenList()) {
                if (p.getIdPanen().equalsIgnoreCase(selectedPanenId)) {
                    txtInput1.setText(p.getInput1() % 1 == 0 ? String.valueOf((long) p.getInput1()) : String.valueOf(p.getInput1()));
                    txtInput2.setText(p.getInput2() % 1 == 0 ? String.valueOf((long) p.getInput2()) : String.valueOf(p.getInput2()));
                    txtTidakLayak.setText(p.getTidakLayak() % 1 == 0 ? String.valueOf((long) p.getTidakLayak()) : String.valueOf(p.getTidakLayak()));
                    txtTahunTanamPanen.setText(p.getTahunTanam() != null ? p.getTahunTanam() : "2018");
                    break;
                }
            }
        }
    }//GEN-LAST:event_tblPanenMouseClicked

    private void btnSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSimpanActionPerformed
        String newId = DataStore.getInstance().generatePanenId();
        Panen panen = buildPanen(newId);
        if (panen == null) return;
        DataStore.getInstance().addPanen(panen);
        JOptionPane.showMessageDialog(this, "Data panen berhasil disimpan dengan ID: " + newId);
        refreshPanenTable();
        refreshRekapTable();
        resetPanenForm();
    }//GEN-LAST:event_btnSimpanActionPerformed

    private void btnPerbaruiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPerbaruiActionPerformed
        if (selectedPanenId == null) {
            JOptionPane.showMessageDialog(this, "<html><div style='width: 300px;'>Pilih data panen pada tabel terlebih dahulu!</div></html>", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Panen panen = buildPanen(selectedPanenId);
        if (panen == null) return;
        if (!DataStore.getInstance().updatePanen(panen)) return;
        JOptionPane.showMessageDialog(this, "Data panen berhasil diperbarui!");
        refreshPanenTable();
        refreshRekapTable();
        resetPanenForm();
    }//GEN-LAST:event_btnPerbaruiActionPerformed

    private void btnHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusActionPerformed
        if (selectedPanenId == null) {
            JOptionPane.showMessageDialog(this, "<html><div style='width: 300px;'>Pilih data panen pada tabel terlebih dahulu!</div></html>", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
            "Apakah Anda yakin ingin menghapus data yang dipilih?",
            "Konfirmasi Hapus",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                DataStore.getInstance().deletePanen(selectedPanenId);
                JOptionPane.showMessageDialog(this, "Data berhasil dihapus!");
                refreshPanenTable();
                refreshRekapTable();
                resetPanenForm();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "<html><div style='width: 320px;'><b>Gagal Menghapus Panen:</b><br>" + ex.getMessage() + "</div></html>", "Peringatan", JOptionPane.WARNING_MESSAGE);
            }
        }
    }//GEN-LAST:event_btnHapusActionPerformed

    private void btnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetActionPerformed
        resetPanenForm();
    }//GEN-LAST:event_btnResetActionPerformed

        private void handleModeRekapChange() {
        String mode = (String) cmbModeRekap.getSelectedItem();
        if ("Semua Data".equalsIgnoreCase(mode)) {
            txtTglAwal.setText("");
            txtTglAkhir.setText("");
            txtTglAwal.setEnabled(false);
            txtTglAkhir.setEnabled(false);
        } else if ("Hari Ini".equalsIgnoreCase(mode)) {
            String todayStr = LocalDate.now().toString();
            txtTglAwal.setText(todayStr);
            txtTglAkhir.setText(todayStr);
            txtTglAwal.setEnabled(false);
            txtTglAkhir.setEnabled(false);
        } else if ("Bulan Ini".equalsIgnoreCase(mode)) {
            LocalDate now = LocalDate.now();
            txtTglAwal.setText(now.withDayOfMonth(1).toString());
            txtTglAkhir.setText(now.toString());
            txtTglAwal.setEnabled(false);
            txtTglAkhir.setEnabled(false);
        } else if ("Tahun Ini".equalsIgnoreCase(mode)) {
            LocalDate now = LocalDate.now();
            txtTglAwal.setText(now.withDayOfYear(1).toString());
            txtTglAkhir.setText(now.toString());
            txtTglAwal.setEnabled(false);
            txtTglAkhir.setEnabled(false);
        } else { // "Rentang Tanggal"
            txtTglAwal.setEnabled(true);
            txtTglAkhir.setEnabled(true);
        }
        btnTampilkanRekapActionPerformed(null);
    }

    private void btnPilihBlokActionPerformed(java.awt.event.ActionEvent evt) {
        PilihBlokDialog dlg = new PilihBlokDialog(this, selectedBlokFilter);
        dlg.setVisible(true);
        if (dlg.isConfirmed()) {
            selectedBlokFilter.clear();
            selectedBlokFilter.addAll(dlg.getSelectedBlokCodes());
            btnPilihBlok.setText("Blok: " + dlg.getSummaryText());
            btnTampilkanRekapActionPerformed(null);
        }
    }

    private void btnLaporEvalActionPerformed(java.awt.event.ActionEvent evt) {
        int selectedRow = tblEvaluasi.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "<html><div style='width: 300px;'>Pilih salah satu baris evaluasi pada tabel untuk melaporkan tindak lanjut pelaksanaan lapangan.</div></html>",
                "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int modelRow = tblEvaluasi.convertRowIndexToModel(selectedRow);
        java.util.List<Evaluasi> list = DataStore.getInstance().getEvaluasiList();
        if (modelRow >= 0 && modelRow < list.size()) {
            Evaluasi ev = list.get(modelRow);
            LaporanTindakLanjutDialog dlg = new LaporanTindakLanjutDialog(this, ev);
            dlg.setVisible(true);
            if (dlg.isSaved()) {
                refreshEvalTable();
            }
        }
    }

    private void btnTampilkanRekapActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTampilkanRekapActionPerformed
        String filterKom = cmbFilterKomoditas.getSelectedItem() == null ? "Semua" : cmbFilterKomoditas.getSelectedItem().toString();
        String filterMutu = cmbFilterBlok == null || cmbFilterBlok.getSelectedItem() == null ? "Semua Mutu" : cmbFilterBlok.getSelectedItem().toString();
        String mode = cmbModeRekap.getSelectedItem() == null ? "Semua Data" : cmbModeRekap.getSelectedItem().toString();
        String tglAwal = "Semua Data".equalsIgnoreCase(mode) ? "" : txtTglAwal.getText().trim();
        String tglAkhir = "Semua Data".equalsIgnoreCase(mode) ? "" : txtTglAkhir.getText().trim();
        if (!InputValidator.isValidOptionalDateRange(tglAwal, tglAkhir)) {
            JOptionPane.showMessageDialog(this, "<html><div style='width: 300px;'>Tanggal rekap harus valid dan tanggal awal tidak boleh melewati tanggal akhir.</div></html>", "Rentang Tanggal Tidak Valid", JOptionPane.WARNING_MESSAGE);
            return;
        }

        DefaultTableModel model = (DefaultTableModel) tblRekap.getModel();
        model.setRowCount(0);
        List<Panen> list = DataStore.getInstance().getPanenList();
        double grandTotal = 0;
        double totalTidakLayak = 0;
        int count = 0, baik = 0, cukup = 0, kurang = 0;

        for (Panen p : list) {
            boolean matchKom = "Semua".equalsIgnoreCase(filterKom) || p.getKomoditas().equalsIgnoreCase(filterKom);
            boolean matchMutu = "Semua Mutu".equalsIgnoreCase(filterMutu) || p.getKualitas().equalsIgnoreCase(filterMutu);
            boolean matchBlok = selectedBlokFilter.isEmpty() || selectedBlokFilter.contains(p.getKodeBlok());
            boolean matchTgl = true;
            if (!tglAwal.isEmpty() && p.getTanggal().compareTo(tglAwal) < 0) matchTgl = false;
            if (!tglAkhir.isEmpty() && p.getTanggal().compareTo(tglAkhir) > 0) matchTgl = false;

            if (matchKom && matchMutu && matchBlok && matchTgl) {
                grandTotal += p.getTotalPanen();
                totalTidakLayak += p.getTidakLayak();
                count++;
                if ("Baik".equalsIgnoreCase(p.getKualitas())) baik++;
                else if ("Cukup".equalsIgnoreCase(p.getKualitas())) cukup++;
                else kurang++;
                model.addRow(new Object[]{
                    p.getIdPanen(),
                    p.getTanggal(),
                    p.getBlokLahan(),
                    p.getKomoditas(),
                    df.format(p.getTotalPanen()),
                    df.format(p.getTidakLayak()),
                    p.getKualitas()
                });
            }
        }

        double persenReject = grandTotal > 0 ? (totalTidakLayak / grandTotal) * 100.0 : 0.0;
        lblTotalRekap.setText("<html><b>Ringkasan:</b> " + count + " Transaksi &nbsp;|&nbsp; <b>Total Panen:</b> " + df.format(grandTotal) + " Kg &nbsp;|&nbsp; <b>Tidak Layak:</b> <font color='#dc2626'>" + df.format(totalTidakLayak) + " Kg (" + df.format(persenReject) + "%)</font> &nbsp;|&nbsp; <b>Mutu:</b> <font color='#16a34a'>" + baik + " Baik</font>, <font color='#d97706'>" + cukup + " Cukup</font>, <font color='#dc2626'>" + kurang + " Kurang</font></html>");

        if (count == 0 && evt != null) {
            JOptionPane.showMessageDialog(this,
                "<html><div style='width: 300px;'>Data panen pada kriteria/periode ini belum tersedia.</div></html>",
                "Informasi",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_btnTampilkanRekapActionPerformed

    private void btnResetRekapActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetRekapActionPerformed
        cmbFilterKomoditas.setSelectedIndex(0);
        if (cmbFilterBlok != null) cmbFilterBlok.setSelectedIndex(0);
        txtTglAwal.setText("");
        txtTglAkhir.setText("");
        refreshRekapTable();
    }//GEN-LAST:event_btnResetRekapActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup btnGroupKomoditas;
    private javax.swing.JButton btnHapus;
    private javax.swing.JButton btnLaporEval;
    private javax.swing.JButton btnLogout;
    private javax.swing.JButton btnPerbarui;
    private javax.swing.JButton btnPilihBlok;
    private javax.swing.JButton btnReset;
    private javax.swing.JButton btnResetRekap;
    private javax.swing.JButton btnSimpan;
    private javax.swing.JButton btnTampilkanRekap;
    private javax.swing.JComboBox<String> cmbBlokLahan;
    private javax.swing.JComboBox<String> cmbFilterBlok;
    private javax.swing.JComboBox<String> cmbFilterKomoditas;
    private javax.swing.JComboBox<String> cmbModeRekap;
    private javax.swing.JLabel lblBlok;
    private javax.swing.JLabel lblDaftarEval;
    private javax.swing.JLabel lblDaftarPanen;
    private javax.swing.JLabel lblDaftarRekap;
    private javax.swing.JLabel lblFilterBlok;
    private javax.swing.JLabel lblFilterKomoditas;
    private javax.swing.JLabel lblInput1;
    private javax.swing.JLabel lblInput2;
    private javax.swing.JLabel lblKomoditas;
    private javax.swing.JLabel lblLogoHeader;
    private javax.swing.JLabel lblModeRekap;
    private javax.swing.JLabel lblTahunTanamPanen;
    private javax.swing.JLabel lblTanggal;
    private javax.swing.JLabel lblTglAkhir;
    private javax.swing.JLabel lblTglAwal;
    private javax.swing.JLabel lblTidakLayak;
    private javax.swing.JLabel lblTotalRekap;
    private javax.swing.JLabel lblUserInfo;
    private javax.swing.JPanel pnlFilterRekap;
    private javax.swing.JPanel pnlFormPanen;
    private javax.swing.JPanel pnlHeader;
    private javax.swing.JPanel pnlTabEvaluasi;
    private javax.swing.JPanel pnlTabPanen;
    private javax.swing.JPanel pnlTabRekap;
    private javax.swing.JPanel pnlTabelPanen;
    private javax.swing.JPanel pnlTabelRekap;
    private javax.swing.JRadioButton rbKaret;
    private javax.swing.JRadioButton rbSawit;
    private javax.swing.JScrollPane scrollEval;
    private javax.swing.JScrollPane scrollPanen;
    private javax.swing.JScrollPane scrollRekap;
    private javax.swing.JSplitPane splitPanen;
    private javax.swing.JSplitPane splitRekap;
    private javax.swing.JTabbedPane tabMain;
    private javax.swing.JTable tblEvaluasi;
    private javax.swing.JTable tblPanen;
    private javax.swing.JTable tblRekap;
    private javax.swing.JTextField txtInput1;
    private javax.swing.JTextField txtInput2;
    private javax.swing.JTextField txtTahunTanamPanen;
    private javax.swing.JTextField txtTanggalPanen;
    private javax.swing.JTextField txtTglAkhir;
    private javax.swing.JTextField txtTglAwal;
    private javax.swing.JTextField txtTidakLayak;
    // End of variables declaration//GEN-END:variables
}
