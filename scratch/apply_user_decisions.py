with open('src/main/java/view/AdminFrame.java', 'r', encoding='utf-8') as f:
    af = f.read()

# 1. Hide btnLihatRiwayat in AdminFrame
af = af.replace(
    'btnHitungMetrik = new javax.swing.JButton();',
    'btnHitungMetrik = new javax.swing.JButton();\n        btnLihatRiwayat.setVisible(false);'
)

# 2. In AdminFrame, set default status always to "Proses" for new calculations
af = af.replace(
    'cmbStatusEval.setSelectedItem(metrics[1] <= 5.0 ? "Selesai" : "Proses");',
    'cmbStatusEval.setSelectedItem("Proses");'
)

with open('src/main/java/view/AdminFrame.java', 'w', encoding='utf-8') as f:
    f.write(af)

# 3. In DireksiFrame, set default status always to "Proses" for new calculations
with open('src/main/java/view/DireksiFrame.java', 'r', encoding='utf-8') as f:
    df = f.read()

df = df.replace(
    'cmbStatusEval.setSelectedItem(metrics[1] <= 5.0 ? "Selesai" : "Proses");',
    'cmbStatusEval.setSelectedItem("Proses");'
)

with open('src/main/java/view/DireksiFrame.java', 'w', encoding='utf-8') as f:
    f.write(df)

print("User decisions applied successfully!")

