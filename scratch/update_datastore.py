with open('src/main/java/model/DataStore.java', 'r', encoding='utf-8') as f:
    text = f.read()

# Helper method
helper = """    private String formatPanenAuditDetail(Panen p) {
        if (p == null) return "-";
        return String.format("Blok: %s | Tgl: %s | Komoditas: %s | Total: %.1f Kg | Reject: %.1f Kg | Mutu: %s",
                p.getBlokLahan(), p.getTanggal(), p.getKomoditas(), p.getTotalPanen(), p.getTidakLayak(), p.getKualitas());
    }
"""

# Insert helper before addPanen
text = text.replace('public synchronized void addPanen(Panen p) {', helper + '\n    public synchronized void addPanen(Panen p) {')

# In addPanen
text = text.replace(
    'logRiwayat("Tambah Data", p.getIdPanen(), "-", "Komoditas: " + p.getKomoditas() + ", Total: " + p.getTotalPanen() + " Kg");',
    'logRiwayat("Tambah Data", p.getIdPanen(), "-", formatPanenAuditDetail(p));'
)

# In updatePanen
old_update = """                String before = "Total: " + old.getTotalPanen() + " Kg, Kualitas: " + old.getKualitas();
                String after = "Total: " + p.getTotalPanen() + " Kg, Kualitas: " + p.getKualitas();
                panenList.set(i, p);
                logRiwayat("Pembaruan Data", p.getIdPanen(), before, after);"""

new_update = """                String before = formatPanenAuditDetail(old);
                String after = formatPanenAuditDetail(p);
                panenList.set(i, p);
                logRiwayat("Pembaruan Data", p.getIdPanen(), before, after);"""

text = text.replace(old_update, new_update)

# In deletePanen
old_delete = """                Panen old = panenList.remove(i);
                logRiwayat("Hapus Data", idPanen, "Total: " + old.getTotalPanen() + " Kg", "Dihapus dari sistem");"""

new_delete = """                Panen old = panenList.remove(i);
                logRiwayat("Hapus Data", idPanen, formatPanenAuditDetail(old), "Dihapus permanen dari sistem");"""

text = text.replace(old_delete, new_delete)

with open('src/main/java/model/DataStore.java', 'w', encoding='utf-8') as f:
    f.write(text)

print("DataStore.java updated!")

