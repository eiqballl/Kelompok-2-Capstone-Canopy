import re

# Read AdminFrame.java
with open('src/main/java/view/AdminFrame.java', 'r', encoding='utf-8') as f:
    code = f.read()

# 1. Update splitEval divider location to 340
code = code.replace('splitEval.setDividerLocation(420);', 'splitEval.setDividerLocation(340);')

# 2. Add chkShowPasswordPgw instantiation & variable
if 'chkShowPasswordPgw = new javax.swing.JCheckBox();' not in code:
    code = code.replace(
        'txtPasswordPgw = new javax.swing.JPasswordField();',
        'txtPasswordPgw = new javax.swing.JPasswordField();\n        chkShowPasswordPgw = new javax.swing.JCheckBox();'
    )
    
    # In initComponents setup
    target_pwd_setup = 'txtPasswordPgw.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N'
    replacement_pwd_setup = target_pwd_setup + '\n\n        chkShowPasswordPgw.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N\n        chkShowPasswordPgw.setText("Lihat Password");\n        chkShowPasswordPgw.addActionListener(new java.awt.event.ActionListener() {\n            public void actionPerformed(java.awt.event.ActionEvent evt) {\n                chkShowPasswordPgwActionPerformed(evt);\n            }\n        });'
    code = code.replace(target_pwd_setup, replacement_pwd_setup, 1)

    # In layout horizontal
    code = code.replace(
        '.addComponent(txtPasswordPgw)\n',
        '.addComponent(txtPasswordPgw)\n                    .addComponent(chkShowPasswordPgw)\n'
    )

    # In layout vertical
    code = code.replace(
        '.addComponent(txtPasswordPgw, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)\n                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)\n                .addComponent(lblNoTelp)',
        '.addComponent(txtPasswordPgw, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)\n                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)\n                .addComponent(chkShowPasswordPgw)\n                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)\n                .addComponent(lblNoTelp)'
    )

    # In variables
    code = code.replace(
        'private javax.swing.JPasswordField txtPasswordPgw;',
        'private javax.swing.JPasswordField txtPasswordPgw;\n    private javax.swing.JCheckBox chkShowPasswordPgw;'
    )

    # Add handler method
    handler = """    private void chkShowPasswordPgwActionPerformed(java.awt.event.ActionEvent evt) {
        if (chkShowPasswordPgw.isSelected()) {
            txtPasswordPgw.setEchoChar((char) 0);
        } else {
            txtPasswordPgw.setEchoChar('\\u2022');
        }
    }
"""
    code = code.replace('private void btnSimpanPgwActionPerformed', handler + '\n    private void btnSimpanPgwActionPerformed')

# 3. Keterangan parameter pada lblTidakLayak
code = code.replace(
    'lblTidakLayak.setText("Hasil Panen Tidak Layak (Kg):");',
    'lblTidakLayak.setText("<html>Hasil Panen Tidak Layak (Kg): <font color=\'#64748b\'>(Baik &le;5%, Cukup 5.1-10%, Kurang &gt;10%)</font></html>");'
)
code = code.replace(
    'txtTidakLayak.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N',
    'txtTidakLayak.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N\n        txtTidakLayak.setToolTipText("Standar Mutu: Baik (<=5%), Cukup (5.1-10%), Kurang (>10%)");'
)

# 4. Validasi try-catch pada btnSimpanBlokActionPerformed
old_val_blok = """            if (!controller.InputValidator.isValidKodeBlok(kode)) {
                JOptionPane.showMessageDialog(this, "Format kode blok tidak valid! Gunakan 3-10 karakter (huruf, angka, tanda hubung '-', garis bawah '_', tanpa spasi). Contoh: SW001 atau KR001.", "Format Salah", JOptionPane.WARNING_MESSAGE);
                return;
            }"""

new_val_blok = """            // Validasi kode blok lahan menggunakan exception (try-catch)
            try {
                controller.InputValidator.validateKodeBlok(kode);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, "<html><div style='width:300px;'>" + ex.getMessage() + "</div></html>", "Format Kode Blok Salah", JOptionPane.WARNING_MESSAGE);
                txtKodeBlok.requestFocusInWindow();
                return;
            }"""
code = code.replace(old_val_blok, new_val_blok)

with open('src/main/java/view/AdminFrame.java', 'w', encoding='utf-8') as f:
    f.write(code)

print("AdminFrame.java basic patches applied!")

