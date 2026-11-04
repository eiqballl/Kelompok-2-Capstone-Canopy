import re

print("Patching DireksiFrame.java and PetugasFrame.java...")

# ==============================================================================
# 1. DireksiFrame.java
# ==============================================================================
with open('src/main/java/view/DireksiFrame.java', 'r', encoding='utf-8') as f:
    df_code = f.read()

# Tab titles in initComponents
df_code = df_code.replace(
    'tabMain.addTab("1. Rekapitulasi Hasil Panen", pnlTabRekap);',
    'tabMain.addTab("Rekapitulasi", pnlTabRekap);'
)
df_code = df_code.replace(
    'tabMain.addTab("2. Evaluasi Hasil Panen", pnlTabEval);',
    'tabMain.addTab("Evaluasi Panen", pnlTabEval);'
)

# splitEval divider location to 340
df_code = df_code.replace(
    'splitEval.setDividerLocation(420);',
    'splitEval.setDividerLocation(340);'
)

# lblFilterBlok text
df_code = df_code.replace(
    'lblFilterBlok.setText("Filter Blok:");',
    'lblFilterBlok.setText("Filter Mutu Panen:");'
)

# Add selectedBlokFilter field
if 'private final java.util.Set<String> selectedBlokFilter' not in df_code:
    df_code = df_code.replace(
        'public class DireksiFrame extends javax.swing.JFrame {',
        'public class DireksiFrame extends javax.swing.JFrame {\n    private final java.util.Set<String> selectedBlokFilter = new java.util.HashSet<>();'
    )

# Add btnPilihBlok and btnRangkumanRekap variable declarations
if 'private javax.swing.JButton btnPilihBlok;' not in df_code:
    df_code = df_code.replace(
        'private javax.swing.JButton btnTampilkanRekap;',
        'private javax.swing.JButton btnPilihBlok;\n    private javax.swing.JButton btnRangkumanRekap;\n    private javax.swing.JButton btnTampilkanRekap;'
    )

# In initComponents, instantiate them
if 'btnPilihBlok = new javax.swing.JButton();' not in df_code:
    df_code = df_code.replace(
        'btnTampilkanRekap = new javax.swing.JButton();',
        'btnPilihBlok = new javax.swing.JButton();\n        btnRangkumanRekap = new javax.swing.JButton();\n        btnTampilkanRekap = new javax.swing.JButton();'
    )

# Properties setup for btnPilihBlok and btnRangkumanRekap
target_df_btn = 'btnTampilkanRekap.setBackground(new java.awt.Color(21, 94, 239));'
new_df_btn = """        btnPilihBlok.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
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

if 'btnPilihBlok.setText(' not in df_code:
    df_code = df_code.replace(target_df_btn, new_df_btn)

# Horizontal layout in pnlFilterRekap
old_df_h = """                              <Group type="103" groupAlignment="0" attributes="0">
                                  <Component id="cmbModeRekap" alignment="0" max="32767" attributes="0"/>
                                  <Component id="cmbFilterKomoditas" alignment="0" max="32767" attributes="0"/>
                                  <Component id="txtTglAwal" alignment="0" max="32767" attributes="0"/>
                                  <Component id="txtTglAkhir" alignment="0" max="32767" attributes="0"/>
                                  <Component id="btnTampilkanRekap" alignment="0" max="32767" attributes="0"/>
                                  <Component id="btnResetRekap" alignment="0" max="32767" attributes="0"/>
                                  <Group type="102" alignment="0" attributes="0">
                                      <Group type="103" groupAlignment="0" attributes="0">
                                          <Component id="lblModeRekap" alignment="0" min="-2" max="-2" attributes="0"/>
                                          <Component id="lblFilterKomoditas" alignment="0" min="-2" max="-2" attributes="0"/>
                                          <Component id="lblTglAwal" alignment="0" min="-2" max="-2" attributes="0"/>
                                          <Component id="lblTglAkhir" alignment="0" min="-2" max="-2" attributes="0"/>
                                      </Group>
                                      <EmptySpace min="0" pref="0" max="32767" attributes="0"/>
                                  </Group>
                              </Group>"""

# Replace java swing group layout for pnlFilterRekap in DireksiFrame
old_df_h_java = """                .addGroup(pnlFilterRekapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
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

new_df_h_java = """                .addGroup(pnlFilterRekapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
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

if old_df_h_java in df_code:
    df_code = df_code.replace(old_df_h_java, new_df_h_java)

old_df_v_java = """                .addComponent(lblModeRekap)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmbModeRekap, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblFilterKomoditas)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmbFilterKomoditas, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblTglAwal)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtTglAwal, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblTglAkhir)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtTglAkhir, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28)
                .addComponent(btnTampilkanRekap, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnResetRekap, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)"""

new_df_v_java = """                .addComponent(lblModeRekap)
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

if old_df_v_java in df_code:
    df_code = df_code.replace(old_df_v_java, new_df_v_java)

# In initCustomData in DireksiFrame
old_df_init = """        cmbFilterKomoditas.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"Semua", "Sawit", "Karet"}));
        cmbModeRekap.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"Rentang Tanggal"}));"""

new_df_init = """        cmbFilterKomoditas.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"Semua", "Sawit", "Karet"}));
        lblFilterBlok.setText("Filter Mutu Panen:");
        cmbFilterBlok.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"Semua Mutu", "Baik", "Cukup", "Kurang"}));
        cmbFilterBlok.addActionListener(e -> btnTampilkanRekapActionPerformed(null));
        cmbFilterKomoditas.addActionListener(e -> btnTampilkanRekapActionPerformed(null));

        cmbModeRekap.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"Semua Data", "Rentang Tanggal", "Bulan Ini", "Hari Ini"}));
        cmbModeRekap.addActionListener(e -> handleModeRekapChange());

        tblRekap.setAutoCreateRowSorter(true);
        tblEvaluasi.setAutoCreateRowSorter(true);

        javax.swing.event.DocumentListener evalListener = new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { autoUpdateMetrikPeriode(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { autoUpdateMetrikPeriode(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { autoUpdateMetrikPeriode(); }
        };
        txtEvalAwal.getDocument().addDocumentListener(evalListener);
        txtEvalAkhir.getDocument().addDocumentListener(evalListener);"""

df_code = df_code.replace(old_df_init, new_df_init)

# Helpers in DireksiFrame
df_helpers = """    private void autoUpdateMetrikPeriode() {
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

if 'private void autoUpdateMetrikPeriode()' not in df_code:
    df_code = df_code.replace('private void btnTampilkanRekapActionPerformed', df_helpers + '\n    private void btnTampilkanRekapActionPerformed')

# Update btnTampilkanRekapActionPerformed in DireksiFrame
old_df_tampilkan = """    private void btnTampilkanRekapActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTampilkanRekapActionPerformed
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

new_df_tampilkan = """    private void btnTampilkanRekapActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTampilkanRekapActionPerformed
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

if old_df_tampilkan in df_code:
    df_code = df_code.replace(old_df_tampilkan, new_df_tampilkan)

# Reset rekap
old_df_reset = """        cmbFilterKomoditas.setSelectedIndex(0);
        txtTglAwal.setText("");
        txtTglAkhir.setText("");
        refreshRekapTable();"""

new_df_reset = """        cmbModeRekap.setSelectedIndex(0);
        cmbFilterKomoditas.setSelectedIndex(0);
        cmbFilterBlok.setSelectedIndex(0);
        selectedBlokFilter.clear();
        btnPilihBlok.setText("Pilih Blok Lahan (Semua)");
        txtTglAwal.setText("");
        txtTglAkhir.setText("");
        txtTglAwal.setEnabled(true);
        txtTglAkhir.setEnabled(true);
        refreshRekapTable();"""

df_code = df_code.replace(old_df_reset, new_df_reset)

with open('src/main/java/view/DireksiFrame.java', 'w', encoding='utf-8') as f:
    f.write(df_code)
print("DireksiFrame.java patched!")

# ==============================================================================
# 2. PetugasFrame.java
# ==============================================================================
with open('src/main/java/view/PetugasFrame.java', 'r', encoding='utf-8') as f:
    pf_code = f.read()

# Tab titles in initComponents
pf_code = pf_code.replace(
    'tabMain.addTab("1. Kelola Data Panen", pnlTabPanen);',
    'tabMain.addTab("Input Panen", pnlTabPanen);'
)
pf_code = pf_code.replace(
    'tabMain.addTab("2. Rekapitulasi Hasil Panen", pnlTabRekap);',
    'tabMain.addTab("Rekapitulasi", pnlTabRekap);'
)
pf_code = pf_code.replace(
    'tabMain.addTab("3. Evaluasi Hasil Panen", pnlTabEvaluasi);',
    'tabMain.addTab("Evaluasi Panen", pnlTabEvaluasi);'
)

# lblTidakLayak text and tooltip
pf_code = pf_code.replace(
    'lblTidakLayak.setText("Hasil Panen Tidak Layak (Kg):");',
    'lblTidakLayak.setText("<html>Hasil Panen Tidak Layak (Kg): <font color=\'#64748b\'>(Baik &le;5%, Cukup 5.1-10%, Kurang &gt;10%)</font></html>");'
)
pf_code = pf_code.replace(
    'txtTidakLayak.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N',
    'txtTidakLayak.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N\n        txtTidakLayak.setToolTipText("Standar Mutu: Baik (<=5%), Cukup (5.1-10%), Kurang (>10%)");'
)

# lblFilterBlok text
pf_code = pf_code.replace(
    'lblFilterBlok.setText("Filter Blok:");',
    'lblFilterBlok.setText("Filter Mutu Panen:");'
)

# Add selectedBlokFilter field
if 'private final java.util.Set<String> selectedBlokFilter' not in pf_code:
    pf_code = pf_code.replace(
        'public class PetugasFrame extends javax.swing.JFrame {',
        'public class PetugasFrame extends javax.swing.JFrame {\n    private final java.util.Set<String> selectedBlokFilter = new java.util.HashSet<>();'
    )

# Add btnPilihBlok, btnRangkumanRekap, btnLaporEval variable declarations
if 'private javax.swing.JButton btnPilihBlok;' not in pf_code:
    pf_code = pf_code.replace(
        'private javax.swing.JButton btnTampilkanRekap;',
        'private javax.swing.JButton btnPilihBlok;\n    private javax.swing.JButton btnRangkumanRekap;\n    private javax.swing.JButton btnTampilkanRekap;'
    )
if 'private javax.swing.JButton btnLaporEval;' not in pf_code:
    pf_code = pf_code.replace(
        'private javax.swing.JButton btnRefreshEval;',
        'private javax.swing.JButton btnLaporEval;\n    private javax.swing.JButton btnRefreshEval;'
    )

# In initComponents, instantiate them
if 'btnPilihBlok = new javax.swing.JButton();' not in pf_code:
    pf_code = pf_code.replace(
        'btnTampilkanRekap = new javax.swing.JButton();',
        'btnPilihBlok = new javax.swing.JButton();\n        btnRangkumanRekap = new javax.swing.JButton();\n        btnTampilkanRekap = new javax.swing.JButton();'
    )
if 'btnLaporEval = new javax.swing.JButton();' not in pf_code:
    pf_code = pf_code.replace(
        'btnRefreshEval = new javax.swing.JButton();',
        'btnLaporEval = new javax.swing.JButton();\n        btnRefreshEval = new javax.swing.JButton();'
    )

# Setup properties for btnPilihBlok, btnRangkumanRekap, btnLaporEval
if 'btnPilihBlok.setText(' not in pf_code:
    pf_code = pf_code.replace(target_df_btn, new_df_btn)

target_lapor_setup = 'btnRefreshEval.setBackground(new java.awt.Color(21, 94, 239));'
new_lapor_setup = """        btnLaporEval.setBackground(new java.awt.Color(22, 163, 74));
        btnLaporEval.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        btnLaporEval.setForeground(new java.awt.Color(255, 255, 255));
        btnLaporEval.setText("Lapor Tindak Lanjut");
        btnLaporEval.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLaporEvalActionPerformed(evt);
            }
        });

        btnRefreshEval.setBackground(new java.awt.Color(21, 94, 239));"""

if 'btnLaporEval.setText(' not in pf_code:
    pf_code = pf_code.replace(target_lapor_setup, new_lapor_setup)

# In layout horizontal & vertical for pnlFilterRekap in PetugasFrame
if old_df_h_java in pf_code:
    pf_code = pf_code.replace(old_df_h_java, new_df_h_java)
if old_df_v_java in pf_code:
    pf_code = pf_code.replace(old_df_v_java, new_df_v_java)

# In pnlTabEvaluasiLayout in PetugasFrame
old_pf_eval_h = """                    .addGroup(pnlTabEvaluasiLayout.createSequentialGroup()
                        .addComponent(lblDaftarEval)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnRefreshEval, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))"""

new_pf_eval_h = """                    .addGroup(pnlTabEvaluasiLayout.createSequentialGroup()
                        .addComponent(lblDaftarEval)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnLaporEval, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnRefreshEval, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))"""

if old_pf_eval_h in pf_code:
    pf_code = pf_code.replace(old_pf_eval_h, new_pf_eval_h)

old_pf_eval_v = """                .addGroup(pnlTabEvaluasiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblDaftarEval)
                    .addComponent(btnRefreshEval, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))"""

new_pf_eval_v = """                .addGroup(pnlTabEvaluasiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblDaftarEval)
                    .addComponent(btnLaporEval, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnRefreshEval, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))"""

if old_pf_eval_v in pf_code:
    pf_code = pf_code.replace(old_pf_eval_v, new_pf_eval_v)

# In initCustomData in PetugasFrame
old_pf_init = """        cmbFilterKomoditas.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"Semua", "Sawit", "Karet"}));
        cmbModeRekap.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"Rentang Tanggal"}));"""

new_pf_init = """        cmbFilterKomoditas.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"Semua", "Sawit", "Karet"}));
        lblFilterBlok.setText("Filter Mutu Panen:");
        cmbFilterBlok.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"Semua Mutu", "Baik", "Cukup", "Kurang"}));
        cmbFilterBlok.addActionListener(e -> btnTampilkanRekapActionPerformed(null));
        cmbFilterKomoditas.addActionListener(e -> btnTampilkanRekapActionPerformed(null));

        cmbModeRekap.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"Semua Data", "Rentang Tanggal", "Bulan Ini", "Hari Ini"}));
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
        });"""

pf_code = pf_code.replace(old_pf_init, new_pf_init)

# Helpers in PetugasFrame
pf_helpers = """    private void handleModeRekapChange() {
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
"""

if 'private void btnLaporEvalActionPerformed' not in pf_code:
    pf_code = pf_code.replace('private void btnTampilkanRekapActionPerformed', pf_helpers + '\n    private void btnTampilkanRekapActionPerformed')

# Update btnTampilkanRekapActionPerformed in PetugasFrame
if old_df_tampilkan in pf_code:
    pf_code = pf_code.replace(old_df_tampilkan, new_df_tampilkan)

# Reset rekap in PetugasFrame
pf_code = pf_code.replace(old_df_reset, new_df_reset)

with open('src/main/java/view/PetugasFrame.java', 'w', encoding='utf-8') as f:
    f.write(pf_code)
print("PetugasFrame.java patched!")

