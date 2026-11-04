with open('src/main/java/model/DataStore.java', 'r', encoding='utf-8') as f:
    text = f.read()

ensure_method = """    private static void ensureRiwayatTable(Connection c) {
        try (Statement st = c.createStatement()) {
            st.execute("CREATE TABLE IF NOT EXISTS riwayat_perubahan ("
                    + "id_riwayat VARCHAR(20) PRIMARY KEY, "
                    + "waktu_perubahan DATETIME DEFAULT CURRENT_TIMESTAMP, "
                    + "jenis_tindakan VARCHAR(50), "
                    + "id_user VARCHAR(20), "
                    + "id_panen VARCHAR(20), "
                    + "data_sebelum TEXT, "
                    + "data_setelah TEXT"
                    + ")");
        } catch (Exception ignored) {}
    }
"""

if 'ensureRiwayatTable' not in text:
    text = text.replace('public synchronized void logRiwayat(', ensure_method + '\n    public synchronized void logRiwayat(')
    text = text.replace('Connection c = Koneksi.getKoneksi();\n            if (c != null) {', 'Connection c = Koneksi.getKoneksi();\n            if (c != null) {\n                ensureRiwayatTable(c);')

with open('src/main/java/model/DataStore.java', 'w', encoding='utf-8') as f:
    f.write(text)

print("DataStore ensureRiwayatTable added!")

