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

public class DireksiFrame extends javax.swing.JFrame {
    private final java.util.Set<String> selectedBlokFilter = new java.util.HashSet<>();

    private String selectedEvalId = null;
    private final DecimalFormat df = new DecimalFormat("#,##0.00");

    public DireksiFrame() {
        initComponents();
        applyBranding();
        setLocationRelativeTo(null);
        if (DataStore.getInstance().isOfflineMode()) {
            lblUserInfo.setText("MODE OFFLINE (data contoh, tidak tersimpan ke database)");
            lblUserInfo.setForeground(new java.awt.Color(220, 38, 38));
        }
        initCustomData();
        refreshBlokTable();
        refreshRekapTable();
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

        tblBlokLahan.setAutoCreateRowSorter(true);
        tblRekap.setAutoCreateRowSorter(true);
        tblEvaluasi.setAutoCreateRowSorter(true);
        cmbStatusEval.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"Proses", "Selesai", "Pending"}));

        javax.swing.event.DocumentListener evalListener = new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { autoUpdateMetrikPeriode(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { autoUpdateMetrikPeriode(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { autoUpdateMetrikPeriode(); }
        };
        txtEvalAwal.getDocument().addDocumentListener(evalListener);
        txtEvalAkhir.getDocument().addDocumentListener(evalListener);
        tblEvaluasi.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblEvaluasiMouseClicked(evt);
            }
        });
        tabMain.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                int idx = tabMain.getSelectedIndex();
                switch (idx) {
                    case 0: refreshBlokTable(); break;
                    case 1: refreshRekapTable(); break;
                    case 2: refreshEvalTable(); break;
                }
            }
        });
        refreshBlokTable();
        refreshEvalTable();
        LocalDate today = LocalDate.now();
        String start = today.withDayOfMonth(1).toString();
        txtEvalAwal.setText(start);
        txtEvalAkhir.setText(today.toString());
        cmbModeRekap.setSelectedItem("Semua Data");
        handleModeRekapChange();
    }

    public void refreshBlokTable() {
        if (tblBlokLahan == null) return;
        DefaultTableModel model = (DefaultTableModel) tblBlokLahan.getModel();
        model.setRowCount(0);
        for (BlokLahan b : DataStore.getInstance().getBlokList()) {
            model.addRow(new Object[]{
                b.getKodeBlok(),
                b.getNamaLahan(),
                df.format(b.getLuasLahan()),
                b.getKomoditas()
            });
        }
    }

    public void refreshRekapTable() {
        btnTampilkanRekapActionPerformed(null);
    }

    public void loadFilterBlok() {
        if (cmbFilterBlok == null) return;
        String sel = cmbFilterBlok.getSelectedItem() == null ? "Semua Blok" : cmbFilterBlok.getSelectedItem().toString();
        cmbFilterBlok.removeAllItems();
        cmbFilterBlok.addItem("Semua Blok");
        for (BlokLahan b : DataStore.getInstance().getBlokList()) {
            cmbFilterBlok.addItem(b.getKodeBlok());
        }
        cmbFilterBlok.setSelectedItem(sel);
    }

    public void refreshEvalTable() {
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

    private void resetEvalForm() {
        selectedEvalId = null;
        LocalDate today = LocalDate.now();
        txtEvalAwal.setText(today.withDayOfMonth(1).toString());
        txtEvalAkhir.setText(today.toString());
        lblTotalBulan.setText("0.00 Kg");
        lblPersenTidakLayak.setText("0.00%");
        cmbStatusEval.setSelectedItem("Proses");
        txtCatatanTindakLanjut.setText("");
        if (txtCatatanEvaluasi != null) {
            txtCatatanEvaluasi.setText("");
        }
        if (txtCatatanPegawaiView != null) {
            txtCatatanPegawaiView.setText("");
        }
        tblEvaluasi.clearSelection();
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlHeader = new javax.swing.JPanel();
        lblLogoHeader = new javax.swing.JLabel();
        lblUserInfo = new javax.swing.JLabel();
        btnLogout = new javax.swing.JButton();
        tabMain = new javax.swing.JTabbedPane();
        pnlTabBlok = new javax.swing.JPanel();
        lblDaftarBlok = new javax.swing.JLabel();
        scrollBlok = new javax.swing.JScrollPane();
        tblBlokLahan = new javax.swing.JTable();
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
        pnlTabEval = new javax.swing.JPanel();
        splitEval = new javax.swing.JSplitPane();
        pnlFormEval = new javax.swing.JPanel();
        lblEvalPeriode = new javax.swing.JLabel();
        lblEvalAwal = new javax.swing.JLabel();
        txtEvalAwal = new DateTextField();
        lblEvalAkhir = new javax.swing.JLabel();
        txtEvalAkhir = new DateTextField();
        btnHitungMetrik = new javax.swing.JButton();
        pnlMetrikCard = new javax.swing.JPanel();
        lblTitleTotalBulan = new javax.swing.JLabel();
        lblTotalBulan = new javax.swing.JLabel();
        lblTitlePersen = new javax.swing.JLabel();
        lblPersenTidakLayak = new javax.swing.JLabel();
        lblStatusEval = new javax.swing.JLabel();
        cmbStatusEval = new javax.swing.JComboBox<>();
        lblCatatan = new javax.swing.JLabel();
        scrollCatatan = new javax.swing.JScrollPane();
        txtCatatanTindakLanjut = new javax.swing.JTextArea();
        lblCatatanEvaluasi = new javax.swing.JLabel();
        scrollCatatanEvaluasi = new javax.swing.JScrollPane();
        txtCatatanEvaluasi = new javax.swing.JTextArea();
        lblCatatanPegawaiView = new javax.swing.JLabel();
        scrollCatatanPegawaiView = new javax.swing.JScrollPane();
        txtCatatanPegawaiView = new javax.swing.JTextArea();
        btnSimpanEval = new javax.swing.JButton();
        btnPerbaruiEval = new javax.swing.JButton();
        btnHapusEval = new javax.swing.JButton();
        btnResetEval = new javax.swing.JButton();
        pnlTabelEval = new javax.swing.JPanel();
        lblDaftarEval = new javax.swing.JLabel();
        scrollEval = new javax.swing.JScrollPane();
        tblEvaluasi = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("CanopyONE - Dashboard Direksi");
        setPreferredSize(new java.awt.Dimension(1200, 750));

        pnlHeader.setBackground(new java.awt.Color(255, 255, 255));
        pnlHeader.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(229, 231, 235)));

        lblLogoHeader.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblLogoHeader.setForeground(new java.awt.Color(21, 94, 239));
        lblLogoHeader.setText("CanopyONE Panel Direksi");

        lblUserInfo.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        lblUserInfo.setForeground(new java.awt.Color(75, 85, 99));
        lblUserInfo.setText("Monitoring Kinerja & Evaluasi Hasil Panen Komoditas");

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

        lblDaftarBlok.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblDaftarBlok.setText("Daftar Blok Lahan Perkebunan:");

        tblBlokLahan.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        tblBlokLahan.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Kode Blok", "Nama Blok / Lahan", "Luas Lahan (Ha)", "Komoditas"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblBlokLahan.setRowHeight(26);
        scrollBlok.setViewportView(tblBlokLahan);

        javax.swing.GroupLayout pnlTabBlokLayout = new javax.swing.GroupLayout(pnlTabBlok);
        pnlTabBlok.setLayout(pnlTabBlokLayout);
        pnlTabBlokLayout.setHorizontalGroup(
            pnlTabBlokLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTabBlokLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlTabBlokLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(scrollBlok, javax.swing.GroupLayout.DEFAULT_SIZE, 1150, Short.MAX_VALUE)
                    .addGroup(pnlTabBlokLayout.createSequentialGroup()
                        .addComponent(lblDaftarBlok)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        pnlTabBlokLayout.setVerticalGroup(
            pnlTabBlokLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTabBlokLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblDaftarBlok)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(scrollBlok, javax.swing.GroupLayout.DEFAULT_SIZE, 510, Short.MAX_VALUE)
                .addContainerGap())
        );

        tabMain.addTab("Lihat Blok Lahan", pnlTabBlok);

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

        splitEval.setDividerLocation(340);
        splitEval.setDividerSize(6);

        pnlFormEval.setBackground(new java.awt.Color(255, 255, 255));
        pnlFormEval.setBorder(javax.swing.BorderFactory.createEmptyBorder(15, 15, 15, 15));

        lblEvalPeriode.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblEvalPeriode.setText("Periode Tanggal Evaluasi:");

        lblEvalAwal.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblEvalAwal.setText("Dari Tanggal:");

        txtEvalAwal.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        txtEvalAwal.setText("2026-10-01");

        lblEvalAkhir.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblEvalAkhir.setText("Sampai Tanggal:");

        txtEvalAkhir.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        txtEvalAkhir.setText("2026-10-31");

        btnHitungMetrik.setBackground(new java.awt.Color(21, 94, 239));
        btnHitungMetrik.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnHitungMetrik.setForeground(new java.awt.Color(255, 255, 255));
        btnHitungMetrik.setText("Hitung Metrik");
        btnHitungMetrik.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnHitungMetrik.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHitungMetrikActionPerformed(evt);
            }
        });

        pnlMetrikCard.setBackground(new java.awt.Color(248, 250, 252));
        pnlMetrikCard.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblTitleTotalBulan.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        lblTitleTotalBulan.setForeground(new java.awt.Color(100, 116, 139));
        lblTitleTotalBulan.setText("TOTAL HASIL PERIODE:");

        lblTotalBulan.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblTotalBulan.setForeground(new java.awt.Color(15, 23, 42));
        lblTotalBulan.setText("2,910.00 Kg");

        lblTitlePersen.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        lblTitlePersen.setForeground(new java.awt.Color(100, 116, 139));
        lblTitlePersen.setText("PERSENTASE TIDAK LAYAK:");

        lblPersenTidakLayak.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblPersenTidakLayak.setForeground(new java.awt.Color(220, 38, 38));
        lblPersenTidakLayak.setText("3.44%");

        javax.swing.GroupLayout pnlMetrikCardLayout = new javax.swing.GroupLayout(pnlMetrikCard);
        pnlMetrikCard.setLayout(pnlMetrikCardLayout);
        pnlMetrikCardLayout.setHorizontalGroup(
            pnlMetrikCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMetrikCardLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(pnlMetrikCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblTitleTotalBulan)
                    .addComponent(lblTotalBulan))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(pnlMetrikCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblTitlePersen)
                    .addComponent(lblPersenTidakLayak))
                .addGap(18, 18, 18))
        );
        pnlMetrikCardLayout.setVerticalGroup(
            pnlMetrikCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMetrikCardLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(pnlMetrikCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTitleTotalBulan)
                    .addComponent(lblTitlePersen))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlMetrikCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTotalBulan)
                    .addComponent(lblPersenTidakLayak))
                .addContainerGap(10, Short.MAX_VALUE))
        );

        lblStatusEval.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblStatusEval.setText("Status Evaluasi :");

        cmbStatusEval.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N

        lblCatatan.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblCatatan.setText("Catatan Tindak Lanjut:");

        txtCatatanTindakLanjut.setColumns(20);
        txtCatatanTindakLanjut.setLineWrap(true);
        txtCatatanTindakLanjut.setRows(2);
        txtCatatanTindakLanjut.setWrapStyleWord(true);
        scrollCatatan.setViewportView(txtCatatanTindakLanjut);

        lblCatatanEvaluasi.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblCatatanEvaluasi.setText("Catatan Evaluasi:");

        txtCatatanEvaluasi.setColumns(20);
        txtCatatanEvaluasi.setLineWrap(true);
        txtCatatanEvaluasi.setRows(2);
        txtCatatanEvaluasi.setWrapStyleWord(true);
        scrollCatatanEvaluasi.setViewportView(txtCatatanEvaluasi);

        lblCatatanPegawaiView.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblCatatanPegawaiView.setText("Catatan Petugas:");

        txtCatatanPegawaiView.setEditable(false);
        txtCatatanPegawaiView.setBackground(new java.awt.Color(241, 245, 249));
        txtCatatanPegawaiView.setColumns(20);
        txtCatatanPegawaiView.setLineWrap(true);
        txtCatatanPegawaiView.setRows(2);
        txtCatatanPegawaiView.setWrapStyleWord(true);
        scrollCatatanPegawaiView.setViewportView(txtCatatanPegawaiView);

        btnSimpanEval.setBackground(new java.awt.Color(21, 94, 239));
        btnSimpanEval.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnSimpanEval.setForeground(new java.awt.Color(255, 255, 255));
        btnSimpanEval.setText("Simpan Evaluasi");
        btnSimpanEval.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSimpanEvalActionPerformed(evt);
            }
        });

        btnPerbaruiEval.setBackground(new java.awt.Color(245, 158, 11));
        btnPerbaruiEval.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnPerbaruiEval.setForeground(new java.awt.Color(255, 255, 255));
        btnPerbaruiEval.setText("Perbarui");
        btnPerbaruiEval.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPerbaruiEvalActionPerformed(evt);
            }
        });

        btnHapusEval.setBackground(new java.awt.Color(239, 68, 68));
        btnHapusEval.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnHapusEval.setForeground(new java.awt.Color(255, 255, 255));
        btnHapusEval.setText("Hapus");
        btnHapusEval.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHapusEvalActionPerformed(evt);
            }
        });

        btnResetEval.setBackground(new java.awt.Color(107, 114, 128));
        btnResetEval.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnResetEval.setForeground(new java.awt.Color(255, 255, 255));
        btnResetEval.setText("Reset");
        btnResetEval.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetEvalActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlFormEvalLayout = new javax.swing.GroupLayout(pnlFormEval);
        pnlFormEval.setLayout(pnlFormEvalLayout);
        pnlFormEvalLayout.setHorizontalGroup(
            pnlFormEvalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlFormEvalLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlFormEvalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnHitungMetrik, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlMetrikCard, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cmbStatusEval, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(scrollCatatan)
                    .addComponent(scrollCatatanEvaluasi)
                    .addComponent(scrollCatatanPegawaiView)
                    .addGroup(pnlFormEvalLayout.createSequentialGroup()
                        .addGroup(pnlFormEvalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblEvalAwal)
                            .addComponent(txtEvalAwal, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(pnlFormEvalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblEvalAkhir)
                            .addComponent(txtEvalAkhir, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(pnlFormEvalLayout.createSequentialGroup()
                        .addGroup(pnlFormEvalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblEvalPeriode)
                            .addComponent(lblStatusEval)
                            .addComponent(lblCatatan)
                            .addComponent(lblCatatanEvaluasi)
                            .addComponent(lblCatatanPegawaiView))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(pnlFormEvalLayout.createSequentialGroup()
                        .addComponent(btnSimpanEval, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnPerbaruiEval, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnHapusEval, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnResetEval, javax.swing.GroupLayout.PREFERRED_SIZE, 63, Short.MAX_VALUE)))
                .addContainerGap())
        );
        pnlFormEvalLayout.setVerticalGroup(
            pnlFormEvalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlFormEvalLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblEvalPeriode)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlFormEvalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblEvalAwal)
                    .addComponent(lblEvalAkhir))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlFormEvalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtEvalAwal, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtEvalAkhir, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnHitungMetrik, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pnlMetrikCard, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblStatusEval)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmbStatusEval, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblCatatan)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scrollCatatan, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblCatatanEvaluasi)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scrollCatatanEvaluasi, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblCatatanPegawaiView)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scrollCatatanPegawaiView, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(pnlFormEvalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSimpanEval, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnPerbaruiEval, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnHapusEval, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnResetEval, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(50, Short.MAX_VALUE))
        );

        splitEval.setLeftComponent(pnlFormEval);

        pnlTabelEval.setBackground(new java.awt.Color(255, 255, 255));
        pnlTabelEval.setBorder(javax.swing.BorderFactory.createEmptyBorder(15, 15, 15, 15));

        lblDaftarEval.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblDaftarEval.setText("Daftar Riwayat Rekam Evaluasi Hasil Panen:");

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

        javax.swing.GroupLayout pnlTabelEvalLayout = new javax.swing.GroupLayout(pnlTabelEval);
        pnlTabelEval.setLayout(pnlTabelEvalLayout);
        pnlTabelEvalLayout.setHorizontalGroup(
            pnlTabelEvalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTabelEvalLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlTabelEvalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(scrollEval, javax.swing.GroupLayout.DEFAULT_SIZE, 712, Short.MAX_VALUE)
                    .addGroup(pnlTabelEvalLayout.createSequentialGroup()
                        .addComponent(lblDaftarEval)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        pnlTabelEvalLayout.setVerticalGroup(
            pnlTabelEvalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTabelEvalLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblDaftarEval)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(scrollEval, javax.swing.GroupLayout.DEFAULT_SIZE, 507, Short.MAX_VALUE)
                .addContainerGap())
        );

        splitEval.setRightComponent(pnlTabelEval);

        javax.swing.GroupLayout pnlTabEvalLayout = new javax.swing.GroupLayout(pnlTabEval);
        pnlTabEval.setLayout(pnlTabEvalLayout);
        pnlTabEvalLayout.setHorizontalGroup(
            pnlTabEvalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(splitEval)
        );
        pnlTabEvalLayout.setVerticalGroup(
            pnlTabEvalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(splitEval)
        );

        tabMain.addTab("Evaluasi Panen", pnlTabEval);

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

        private void autoUpdateMetrikPeriode() {
        String tglAwal = txtEvalAwal.getText().trim();
        String tglAkhir = txtEvalAkhir.getText().trim();
        if (InputValidator.isValidDate(tglAwal) && InputValidator.isValidDate(tglAkhir) && InputValidator.isValidDateRange(tglAwal, tglAkhir)) {
            double[] metrics = DataStore.getInstance().calculateMetrics(tglAwal, tglAkhir);
            lblTotalBulan.setText("<html>" + df.format(metrics[0]) + " Kg<br><span style='font-size:10px;font-weight:normal;color:#64748b;'>Rata-rata: " + df.format(metrics[2]) + " Kg/bln</span></html>");
            lblPersenTidakLayak.setText(df.format(metrics[1]) + "%");
            if (cmbStatusEval != null) {
                cmbStatusEval.setSelectedItem("Proses");
            }
        }
    }

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

    private void btnHitungMetrikActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHitungMetrikActionPerformed
        String tglAwal = txtEvalAwal.getText().trim();
        String tglAkhir = txtEvalAkhir.getText().trim();
        if (!InputValidator.isValidDateRange(tglAwal, tglAkhir)) {
            JOptionPane.showMessageDialog(this, "<html><div style='width: 300px;'>Masukkan rentang tanggal valid dengan tanggal awal sebelum atau sama dengan tanggal akhir.</div></html>", "Rentang Tanggal Tidak Valid", JOptionPane.WARNING_MESSAGE);
            return;
        }

        double[] metrics = DataStore.getInstance().calculateMetrics(tglAwal, tglAkhir);

        lblTotalBulan.setText("<html>" + df.format(metrics[0]) + " Kg<br><span style=\'font-size:10px;font-weight:normal;color:#64748b;\'>Rata-rata: " + df.format(metrics[2]) + " Kg/bln</span></html>");
        lblPersenTidakLayak.setText(df.format(metrics[1]) + "%");
        
        String defaultStatus = (metrics[0] > 0 && metrics[1] <= 4.0) ? "Selesai" : "Proses";
        cmbStatusEval.setSelectedItem(defaultStatus);

        if (metrics[0] <= 0) {
            JOptionPane.showMessageDialog(this, "<html><div style='width: 300px;'>Data hasil panen pada rentang periode ini belum tersedia.</div></html>", "Informasi", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_btnHitungMetrikActionPerformed

    private void btnSimpanEvalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSimpanEvalActionPerformed
        String tglAwal = txtEvalAwal.getText().trim();
        String tglAkhir = txtEvalAkhir.getText().trim();
        if (!InputValidator.isValidDateRange(tglAwal, tglAkhir)) {
            JOptionPane.showMessageDialog(this, "<html><div style='width: 300px;'>Masukkan rentang tanggal valid dengan tanggal awal sebelum atau sama dengan tanggal akhir.</div></html>", "Rentang Tanggal Tidak Valid", JOptionPane.WARNING_MESSAGE);
            return;
        }

        for (Evaluasi existingEv : DataStore.getInstance().getEvaluasiList()) {
            if (existingEv.getTglAwal().equals(tglAwal) && existingEv.getTglAkhir().equals(tglAkhir)) {
                JOptionPane.showMessageDialog(this,
                    "<html><div style='width: 320px;'><b>Evaluasi Duplikat:</b> Evaluasi untuk rentang periode <b>"
                    + tglAwal + "</b> s.d. <b>" + tglAkhir + "</b> sudah ada dalam sistem dengan ID: <b>"
                    + existingEv.getIdEval() + "</b>.<br><br>Silakan perbarui data yang ada jika ingin mengubah arahan atau status evaluasi.</div></html>",
                    "Peringatan Duplikasi", JOptionPane.WARNING_MESSAGE);
                return;
            }
        }

        double[] metrics = DataStore.getInstance().calculateMetrics(tglAwal, tglAkhir);

        String status = cmbStatusEval.getSelectedItem() != null ? cmbStatusEval.getSelectedItem().toString() : "Proses";
        if (status.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Status evaluasi wajib dipilih (Proses, Selesai, atau Pending)!", "Validasi Gagal", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String cTindakLanjut = txtCatatanTindakLanjut.getText().trim();
        String cEvaluasi = txtCatatanEvaluasi != null ? txtCatatanEvaluasi.getText().trim() : "-";
        String cPetugas = (txtCatatanPegawaiView != null && !txtCatatanPegawaiView.getText().trim().startsWith("(")) ? txtCatatanPegawaiView.getText().trim() : "-";

        String newId = DataStore.getInstance().generateEvalId();

        Evaluasi ev = new Evaluasi(newId, tglAwal, tglAkhir, metrics[0], metrics[1], status, cTindakLanjut, cEvaluasi, cPetugas);
        DataStore.getInstance().addEvaluasi(ev);

        JOptionPane.showMessageDialog(this, "Evaluasi panen berhasil disimpan dengan ID: " + newId);
        refreshEvalTable();
        resetEvalForm();
    }//GEN-LAST:event_btnSimpanEvalActionPerformed

    private void btnPerbaruiEvalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPerbaruiEvalActionPerformed
        if (selectedEvalId == null) {
            JOptionPane.showMessageDialog(this, "<html><div style='width: 300px;'>Pilih data evaluasi pada tabel terlebih dahulu!</div></html>", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String tglAwal = txtEvalAwal.getText().trim();
        String tglAkhir = txtEvalAkhir.getText().trim();
        if (!InputValidator.isValidDateRange(tglAwal, tglAkhir)) {
            JOptionPane.showMessageDialog(this, "<html><div style='width: 300px;'>Masukkan rentang tanggal valid dengan tanggal awal sebelum atau sama dengan tanggal akhir.</div></html>", "Rentang Tanggal Tidak Valid", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this,
            "Apakah Anda yakin ingin menyimpan perubahan evaluasi?",
            "Konfirmasi Perubahan",
            JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        double[] metrics = DataStore.getInstance().calculateMetrics(tglAwal, tglAkhir);
        String status = cmbStatusEval.getSelectedItem() != null ? cmbStatusEval.getSelectedItem().toString() : "Proses";
        if (status.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Status evaluasi wajib dipilih (Proses, Selesai, atau Pending)!", "Validasi Gagal", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String cTindakLanjut = txtCatatanTindakLanjut.getText().trim();
        String cEvaluasi = txtCatatanEvaluasi != null ? txtCatatanEvaluasi.getText().trim() : "-";

        Evaluasi existing = null;
        for (Evaluasi item : DataStore.getInstance().getEvaluasiList()) {
            if (item.getIdEval().equalsIgnoreCase(selectedEvalId)) {
                existing = item;
                break;
            }
        }
        String cPetugas = existing != null ? existing.getCatatanPetugas() : "-";
        Evaluasi ev = new Evaluasi(selectedEvalId, tglAwal, tglAkhir, metrics[0], metrics[1], status, cTindakLanjut, cEvaluasi, cPetugas);
        DataStore.getInstance().updateEvaluasi(ev);

        JOptionPane.showMessageDialog(this, "Data evaluasi berhasil diperbarui!");
        refreshEvalTable();
        resetEvalForm();
    }//GEN-LAST:event_btnPerbaruiEvalActionPerformed

    private void btnHapusEvalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusEvalActionPerformed
        if (selectedEvalId == null) {
            JOptionPane.showMessageDialog(this, "<html><div style='width: 300px;'>Pilih data evaluasi pada tabel terlebih dahulu!</div></html>", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this,
            "Apakah Anda yakin ingin menghapus data evaluasi yang dipilih?",
            "Konfirmasi Hapus",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                DataStore.getInstance().deleteEvaluasi(selectedEvalId);
                JOptionPane.showMessageDialog(this, "Data berhasil dihapus!");
                refreshEvalTable();
                resetEvalForm();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "<html><div style='width: 320px;'><b>Gagal Menghapus Evaluasi:</b><br>" + ex.getMessage() + "</div></html>", "Peringatan", JOptionPane.WARNING_MESSAGE);
            }
        }
    }//GEN-LAST:event_btnHapusEvalActionPerformed

    private void btnResetEvalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetEvalActionPerformed
        resetEvalForm();
    }//GEN-LAST:event_btnResetEvalActionPerformed

    private void tblEvaluasiMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblEvaluasiMouseClicked
        int row = tblEvaluasi.getSelectedRow();
        if (row != -1 && row < DataStore.getInstance().getEvaluasiList().size()) {
            Evaluasi ev = DataStore.getInstance().getEvaluasiList().get(row);
            selectedEvalId = ev.getIdEval();
            txtEvalAwal.setText(ev.getTglAwal());
            txtEvalAkhir.setText(ev.getTglAkhir());
            lblTotalBulan.setText("<html>" + df.format(ev.getTotalPanen()) + " Kg<br><span style=\'font-size:10px;font-weight:normal;color:#64748b;\'>Rata-rata: " + df.format(ev.getRataRataPerBulan()) + " Kg/bln</span></html>");
            lblPersenTidakLayak.setText(df.format(ev.getPersenTidakLayak()) + "%");
            String statusVal = DataStore.normalizeStatus(ev.getStatus());
            cmbStatusEval.setSelectedItem(statusVal);
            txtCatatanTindakLanjut.setText(ev.getCatatanTindakLanjut());
            if (txtCatatanEvaluasi != null) {
                txtCatatanEvaluasi.setText(ev.getCatatanEvaluasi() == null || "-".equals(ev.getCatatanEvaluasi()) ? "" : ev.getCatatanEvaluasi());
            }
            if (txtCatatanPegawaiView != null) {
                txtCatatanPegawaiView.setText(ev.getCatatanPetugas() == null || "-".equals(ev.getCatatanPetugas()) ? "(Belum ada catatan dari petugas)" : ev.getCatatanPetugas());
            }
        }
    }//GEN-LAST:event_tblEvaluasiMouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnHapusEval;
    private javax.swing.JButton btnHitungMetrik;
    private javax.swing.JButton btnLogout;
    private javax.swing.JButton btnPerbaruiEval;
    private javax.swing.JButton btnPilihBlok;
    private javax.swing.JButton btnResetEval;
    private javax.swing.JButton btnResetRekap;
    private javax.swing.JButton btnSimpanEval;
    private javax.swing.JButton btnTampilkanRekap;
    private javax.swing.JComboBox<String> cmbFilterBlok;
    private javax.swing.JComboBox<String> cmbFilterKomoditas;
    private javax.swing.JComboBox<String> cmbModeRekap;
    private javax.swing.JComboBox<String> cmbStatusEval;
    private javax.swing.JLabel lblCatatan;
    private javax.swing.JLabel lblCatatanEvaluasi;
    private javax.swing.JLabel lblCatatanPegawaiView;
    private javax.swing.JLabel lblDaftarBlok;
    private javax.swing.JLabel lblDaftarEval;
    private javax.swing.JLabel lblDaftarRekap;
    private javax.swing.JLabel lblEvalAkhir;
    private javax.swing.JLabel lblEvalAwal;
    private javax.swing.JLabel lblEvalPeriode;
    private javax.swing.JLabel lblFilterBlok;
    private javax.swing.JLabel lblFilterKomoditas;
    private javax.swing.JLabel lblLogoHeader;
    private javax.swing.JLabel lblModeRekap;
    private javax.swing.JLabel lblPersenTidakLayak;
    private javax.swing.JLabel lblStatusEval;
    private javax.swing.JLabel lblTglAkhir;
    private javax.swing.JLabel lblTglAwal;
    private javax.swing.JLabel lblTitlePersen;
    private javax.swing.JLabel lblTitleTotalBulan;
    private javax.swing.JLabel lblTotalBulan;
    private javax.swing.JLabel lblTotalRekap;
    private javax.swing.JLabel lblUserInfo;
    private javax.swing.JPanel pnlFilterRekap;
    private javax.swing.JPanel pnlFormEval;
    private javax.swing.JPanel pnlHeader;
    private javax.swing.JPanel pnlMetrikCard;
    private javax.swing.JPanel pnlTabBlok;
    private javax.swing.JPanel pnlTabEval;
    private javax.swing.JPanel pnlTabRekap;
    private javax.swing.JPanel pnlTabelEval;
    private javax.swing.JPanel pnlTabelRekap;
    private javax.swing.JScrollPane scrollBlok;
    private javax.swing.JScrollPane scrollCatatan;
    private javax.swing.JScrollPane scrollCatatanEvaluasi;
    private javax.swing.JScrollPane scrollCatatanPegawaiView;
    private javax.swing.JScrollPane scrollEval;
    private javax.swing.JScrollPane scrollRekap;
    private javax.swing.JSplitPane splitEval;
    private javax.swing.JSplitPane splitRekap;
    private javax.swing.JTabbedPane tabMain;
    private javax.swing.JTable tblBlokLahan;
    private javax.swing.JTable tblEvaluasi;
    private javax.swing.JTable tblRekap;
    private javax.swing.JTextArea txtCatatanEvaluasi;
    private javax.swing.JTextArea txtCatatanPegawaiView;
    private javax.swing.JTextArea txtCatatanTindakLanjut;
    private javax.swing.JTextField txtEvalAkhir;
    private javax.swing.JTextField txtEvalAwal;
    private javax.swing.JTextField txtTglAkhir;
    private javax.swing.JTextField txtTglAwal;
    // End of variables declaration//GEN-END:variables
}
