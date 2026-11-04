import re

with open('src/main/java/view/AdminFrame.java', 'r', encoding='utf-8') as f:
    code = f.read()

# 1. Add fields if not present
if 'private final java.util.Set<String> selectedBlokFilter' not in code:
    code = code.replace(
        'private String selectedBlokKode = null;',
        'private String selectedBlokKode = null;\n    private final java.util.Set<String> selectedBlokFilter = new java.util.HashSet<>();'
    )

# 2. Add btnPilihBlok and btnRangkumanRekap variables if not present
if 'private javax.swing.JButton btnPilihBlok;' not in code:
    code = code.replace(
        'private javax.swing.JButton btnTampilkanRekap;',
        'private javax.swing.JButton btnPilihBlok;\n    private javax.swing.JButton btnRangkumanRekap;\n    private javax.swing.JButton btnTampilkanRekap;'
    )

# 3. In initComponents, instantiate them
if 'btnPilihBlok = new javax.swing.JButton();' not in code:
    code = code.replace(
        'btnTampilkanRekap = new javax.swing.JButton();',
        'btnPilihBlok = new javax.swing.JButton();\n        btnRangkumanRekap = new javax.swing.JButton();\n        btnTampilkanRekap = new javax.swing.JButton();'
    )

# 4. In initComponents setup properties
target_setup = 'btnTampilkanRekap.setBackground(new java.awt.Color(21, 94, 239));'
new_setup = """        btnPilihBlok.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        btnPilihBlok.setText("Pilih Blok Lahan (Semua)");
        btnPilihBlok.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPilihBlokActionPerformed(evt);
            }
        });

        btnRangkumanRekap.setBackground(new java.awt.Color(22, 163, 74));
        btnRangkumanRekap.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnRangkumanRekap.setForeground(new java.awt.Color(255, 255, 255));
        btnRangkumanRekap.setText("Lihat Rangkuman Eksekutif");
        btnRangkumanRekap.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRangkumanRekapActionPerformed(evt);
            }
        });

        btnTampilkanRekap.setBackground(new java.awt.Color(21, 94, 239));"""

if 'btnPilihBlok.setText(' not in code:
    code = code.replace(target_setup, new_setup)

# 5. In pnlFilterRekapLayout, update horizontal & vertical groups
old_h = """                .addGroup(pnlFilterRekapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cmbModeRekap, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cmbFilterKomoditas, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtTglAwal)
                    .addComponent(txtTglAkhir)
                    .addComponent(btnTampilkanRekap, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnResetRekap, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(pnlFilterRekapLayout.createSequentialGroup()
                        .addGroup(pnlFilterRekapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblModeRekap)
                            .addComponent(lblFilterKomoditas)
                            .addComponent(lblTglAwal)
                            .addComponent(lblTglAkhir))
                        .addGap(0, 0, Short.MAX_VALUE)))"""

new_h = """                .addGroup(pnlFilterRekapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cmbModeRekap, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cmbFilterKomoditas, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cmbFilterBlok, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnPilihBlok, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtTglAwal)
                    .addComponent(txtTglAkhir)
                    .addComponent(btnTampilkanRekap, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnRangkumanRekap, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnResetRekap, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(pnlFilterRekapLayout.createSequentialGroup()
                        .addGroup(pnlFilterRekapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblModeRekap)
                            .addComponent(lblFilterKomoditas)
                            .addComponent(lblFilterBlok)
                            .addComponent(lblTglAwal)
                            .addComponent(lblTglAkhir))
                        .addGap(0, 0, Short.MAX_VALUE)))"""

if old_h in code:
    code = code.replace(old_h, new_h)

old_v = """                .addComponent(lblModeRekap)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmbModeRekap, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblFilterKomoditas)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmbFilterKomoditas, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblTglAwal)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtTglAwal, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblTglAkhir)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtTglAkhir, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24)
                .addComponent(btnTampilkanRekap, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnResetRekap, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)"""

new_v = """                .addComponent(lblModeRekap)
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
                .addComponent(btnRangkumanRekap, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnResetRekap, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)"""

if old_v in code:
    code = code.replace(old_v, new_v)

# 6. Update initCustomData()
old_init_custom = """        cmbFilterKomoditas.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"Semua", "Sawit", "Karet"}));
        cmbModeRekap.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"Rentang Tanggal"}));"""

new_init_custom = """        cmbFilterKomoditas.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"Semua", "Sawit", "Karet"}));
        lblFilterBlok.setText("Filter Mutu Panen:");
        cmbFilterBlok.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"Semua Mutu", "Baik", "Cukup", "Kurang"}));
        cmbFilterBlok.addActionListener(e -> btnTampilkanRekapActionPerformed(null));
        cmbFilterKomoditas.addActionListener(e -> btnTampilkanRekapActionPerformed(null));

        cmbModeRekap.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"Semua Data", "Rentang Tanggal", "Bulan Ini", "Hari Ini"}));
        cmbModeRekap.addActionListener(e -> handleModeRekapChange());

        tblRekap.setAutoCreateRowSorter(true);
        tblPanen.setAutoCreateRowSorter(true);
        tblEvaluasi.setAutoCreateRowSorter(true);
        tblBlokLahan.setAutoCreateRowSorter(true);
        tblPegawai.setAutoCreateRowSorter(true);

        javax.swing.event.DocumentListener evalListener = new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { autoUpdateMetrikPeriode(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { autoUpdateMetrikPeriode(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { autoUpdateMetrikPeriode(); }
        };
        txtEvalAwal.getDocument().addDocumentListener(evalListener);
        txtEvalAkhir.getDocument().addDocumentListener(evalListener);"""

code = code.replace(old_init_custom, new_init_custom)

# 7. Add helper methods: autoUpdateMetrikPeriode, handleModeRekapChange, btnPilihBlokActionPerformed, btnRangkumanRekapActionPerformed
helpers = """    private void autoUpdateMetrikPeriode() {
        String tglAwal = txtEvalAwal.getText().trim();
        String tglAkhir = txtEvalAkhir.getText().trim();
        if (InputValidator.isValidDate(tglAwal) && InputValidator.isValidDate(tglAkhir) && InputValidator.isValidDateRange(tglAwal, tglAkhir)) {
            double[] metrics = DataStore.getInstance().calculateMetrics(tglAwal, tglAkhir);
            lblTotalBulan.setText("<html>" + df.format(metrics[0]) + " Kg<br><span style='font-size:10px;font-weight:normal;color:#64748b;'>Rata-rata: " + df.format(metrics[2]) + " Kg/bln</span></html>");
            lblPersenTidakLayak.setText(df.format(metrics[1]) + "%");
            if (cmbStatusEval != null) {
                cmbStatusEval.setSelectedItem(metrics[1] <= 5.0 ? "Selesai" : "Proses");
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

    private void btnRangkumanRekapActionPerformed(java.awt.event.ActionEvent evt) {
        String filterKom = cmbFilterKomoditas.getSelectedItem() == null ? "Semua" : cmbFilterKomoditas.getSelectedItem().toString();
        String filterMutu = cmbFilterBlok.getSelectedItem() == null ? "Semua Mutu" : cmbFilterBlok.getSelectedItem().toString();
        String tglAwal = txtTglAwal.getText().trim();
        String tglAkhir = txtTglAkhir.getText().trim();

        java.util.List<Panen> filteredList = new java.util.ArrayList<>();
        for (Panen p : DataStore.getInstance().getPanenList()) {
            boolean matchKom = "Semua".equalsIgnoreCase(filterKom) || p.getKomoditas().equalsIgnoreCase(filterKom);
            boolean matchTgl = true;
            if (!tglAwal.isEmpty() && p.getTanggal().compareTo(tglAwal) < 0) matchTgl = false;
            if (!tglAkhir.isEmpty() && p.getTanggal().compareTo(tglAkhir) > 0) matchTgl = false;
            boolean matchMutu = "Semua Mutu".equalsIgnoreCase(filterMutu) || p.getKualitas().equalsIgnoreCase(filterMutu);
            boolean matchBlok = selectedBlokFilter.isEmpty() || selectedBlokFilter.contains(p.getKodeBlok());
            if (matchKom && matchTgl && matchMutu && matchBlok) {
                filteredList.add(p);
            }
        }
        String filterInfo = "Komoditas: " + filterKom + " | Mutu: " + filterMutu;
        if (!tglAwal.isEmpty() || !tglAkhir.isEmpty()) {
            filterInfo += " | Periode: " + (tglAwal.isEmpty() ? "Awal" : tglAwal) + " s/d " + (tglAkhir.isEmpty() ? "Akhir" : tglAkhir);
        }
        if (!selectedBlokFilter.isEmpty()) {
            filterInfo += " | Blok: " + String.join(", ", selectedBlokFilter);
        }
        new RangkumanEksekutifDialog(this, filteredList, filterInfo).setVisible(true);
    }
"""

if 'private void autoUpdateMetrikPeriode()' not in code:
    code = code.replace('private void btnTampilkanRekapActionPerformed', helpers + '\n    private void btnTampilkanRekapActionPerformed')

# 8. Update btnTampilkanRekapActionPerformed to filter by selectedBlokFilter and cmbFilterBlok
old_tampilkan = """    private void btnTampilkanRekapActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTampilkanRekapActionPerformed
        String filterKom = cmbFilterKomoditas.getSelectedItem() == null ? "Semua" : cmbFilterKomoditas.getSelectedItem().toString();
        String tglAwal = txtTglAwal.getText().trim();
        String tglAkhir = txtTglAkhir.getText().trim();
        if (!InputValidator.isValidOptionalDateRange(tglAwal, tglAkhir)) {
            JOptionPane.showMessageDialog(this, "Tanggal rekap harus valid dan tanggal awal tidak boleh melewati tanggal akhir.", "Rentang Tanggal Tidak Valid", JOptionPane.WARNING_MESSAGE);
            return;
        }

        DefaultTableModel model = (DefaultTableModel) tblRekap.getModel();
        model.setRowCount(0);
        List<Panen> list = DataStore.getInstance().getPanenList();
        double grandTotal = 0;
        int count = 0;

        for (Panen p : list) {
            boolean matchKom = "Semua".equalsIgnoreCase(filterKom) || p.getKomoditas().equalsIgnoreCase(filterKom);
            boolean matchTgl = true;
            if (!tglAwal.isEmpty() && p.getTanggal().compareTo(tglAwal) < 0) matchTgl = false;
            if (!tglAkhir.isEmpty() && p.getTanggal().compareTo(tglAkhir) > 0) matchTgl = false;

            if (matchKom && matchTgl) {
                grandTotal += p.getTotalPanen();
                count++;
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

        lblTotalRekap.setText("Total Hasil Panen: " + df.format(grandTotal) + " Kg (" + count + " data)");

        if (count == 0) {
            JOptionPane.showMessageDialog(this,
                "Data panen pada kriteria/periode ini belum tersedia.",
                "Informasi",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_btnTampilkanRekapActionPerformed"""

new_tampilkan = """    private void btnTampilkanRekapActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTampilkanRekapActionPerformed
        String filterKom = cmbFilterKomoditas.getSelectedItem() == null ? "Semua" : cmbFilterKomoditas.getSelectedItem().toString();
        String filterMutu = cmbFilterBlok.getSelectedItem() == null ? "Semua Mutu" : cmbFilterBlok.getSelectedItem().toString();
        String tglAwal = txtTglAwal.getText().trim();
        String tglAkhir = txtTglAkhir.getText().trim();
        if (!InputValidator.isValidOptionalDateRange(tglAwal, tglAkhir)) {
            JOptionPane.showMessageDialog(this, "<html><div style='width:300px;'>Tanggal rekap harus valid dan tanggal awal tidak boleh melewati tanggal akhir.</div></html>", "Rentang Tanggal Tidak Valid", JOptionPane.WARNING_MESSAGE);
            return;
        }

        DefaultTableModel model = (DefaultTableModel) tblRekap.getModel();
        model.setRowCount(0);
        List<Panen> list = DataStore.getInstance().getPanenList();
        double grandTotal = 0;
        int count = 0, baik = 0, cukup = 0, kurang = 0;

        for (Panen p : list) {
            boolean matchKom = "Semua".equalsIgnoreCase(filterKom) || p.getKomoditas().equalsIgnoreCase(filterKom);
            boolean matchTgl = true;
            if (!tglAwal.isEmpty() && p.getTanggal().compareTo(tglAwal) < 0) matchTgl = false;
            if (!tglAkhir.isEmpty() && p.getTanggal().compareTo(tglAkhir) > 0) matchTgl = false;
            boolean matchMutu = "Semua Mutu".equalsIgnoreCase(filterMutu) || p.getKualitas().equalsIgnoreCase(filterMutu);
            boolean matchBlok = selectedBlokFilter.isEmpty() || selectedBlokFilter.contains(p.getKodeBlok());

            if (matchKom && matchTgl && matchMutu && matchBlok) {
                grandTotal += p.getTotalPanen();
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

        lblTotalRekap.setText("<html><b>Ringkasan:</b> " + count + " Transaksi &nbsp;|&nbsp; <b>Total:</b> " + df.format(grandTotal) + " Kg &nbsp;|&nbsp; <b>Mutu:</b> <font color='#16a34a'>" + baik + " Baik</font>, <font color='#d97706'>" + cukup + " Cukup</font>, <font color='#dc2626'>" + kurang + " Kurang</font></html>");

        if (count == 0 && evt != null) {
            JOptionPane.showMessageDialog(this,
                "<html><div style='width:300px;'>Data panen pada kriteria/periode ini belum tersedia.</div></html>",
                "Informasi",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_btnTampilkanRekapActionPerformed"""

if old_tampilkan in code:
    code = code.replace(old_tampilkan, new_tampilkan)

# 9. Update btnResetRekap to reset all filters
old_reset = """        cmbFilterKomoditas.setSelectedIndex(0);
        txtTglAwal.setText("");
        txtTglAkhir.setText("");
        refreshRekapTable();"""

new_reset = """        cmbModeRekap.setSelectedIndex(0);
        cmbFilterKomoditas.setSelectedIndex(0);
        cmbFilterBlok.setSelectedIndex(0);
        selectedBlokFilter.clear();
        btnPilihBlok.setText("Pilih Blok Lahan (Semua)");
        txtTglAwal.setText("");
        txtTglAkhir.setText("");
        txtTglAwal.setEnabled(true);
        txtTglAkhir.setEnabled(true);
        refreshRekapTable();"""

code = code.replace(old_reset, new_reset)

with open('src/main/java/view/AdminFrame.java', 'w', encoding='utf-8') as f:
    f.write(code)

print("AdminFrame.java patched successfully!")

