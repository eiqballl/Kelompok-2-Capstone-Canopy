import glob
import re

for path in ['src/main/java/view/AdminFrame.java', 'src/main/java/view/DireksiFrame.java', 'src/main/java/view/PetugasFrame.java']:
    with open(path, 'r', encoding='utf-8') as f:
        code = f.read()

    # List of known long warning/info message strings to wrap
    replacements = [
        ("Total panen harus lebih dari nol dan hasil tidak layak tidak boleh melebihi total panen (karena hasil tidak layak merupakan bagian dari total panen kotor).",
         "<html><div style='width: 320px;'>Total panen harus lebih dari nol dan hasil tidak layak tidak boleh melebihi total panen (karena hasil tidak layak merupakan bagian dari total panen kotor).</div></html>"),
        ("Nilai rincian panen harus berupa bilangan bulat non-negatif.",
         "<html><div style='width: 300px;'>Nilai rincian panen harus berupa bilangan bulat non-negatif.</div></html>"),
        ("Hasil tidak layak harus berupa bilangan bulat non-negatif (bukan pecahan).",
         "<html><div style='width: 300px;'>Hasil tidak layak harus berupa bilangan bulat non-negatif (bukan pecahan).</div></html>"),
        ("Pilih blok dan gunakan tanggal YYYY-MM-DD.",
         "<html><div style='width: 300px;'>Pilih blok lahan dan gunakan format tanggal YYYY-MM-DD yang valid.</div></html>"),
        ("Masukkan rentang tanggal valid dengan tanggal awal sebelum atau sama dengan tanggal akhir.",
         "<html><div style='width: 300px;'>Masukkan rentang tanggal valid dengan tanggal awal sebelum atau sama dengan tanggal akhir.</div></html>"),
        ("Blok tidak dapat dihapus karena masih memiliki data panen.",
         "<html><div style='width: 300px;'>Blok lahan tidak dapat dihapus karena masih memiliki data aktivitas panen terkait.</div></html>"),
        ("Kode, nama, luas positif, dan komoditas wajib diisi.",
         "<html><div style='width: 300px;'>Kode blok, nama lahan, luas positif (&gt; 0), dan komoditas wajib diisi.</div></html>"),
        ("Nama, username, dan password wajib diisi!",
         "<html><div style='width: 300px;'>Nama lengkap, username, dan password wajib diisi!</div></html>"),
        ("Pilih pegawai pada tabel terlebih dahulu!",
         "<html><div style='width: 300px;'>Pilih pegawai pada tabel terlebih dahulu!</div></html>"),
        ("Pilih blok pada tabel terlebih dahulu!",
         "<html><div style='width: 300px;'>Pilih blok pada tabel terlebih dahulu!</div></html>"),
        ("Pilih data panen pada tabel terlebih dahulu!",
         "<html><div style='width: 300px;'>Pilih data panen pada tabel terlebih dahulu!</div></html>"),
        ("Pilih data evaluasi pada tabel terlebih dahulu!",
         "<html><div style='width: 300px;'>Pilih data evaluasi pada tabel terlebih dahulu!</div></html>"),
        ("Tanggal rekap harus valid dan tanggal awal tidak boleh melewati tanggal akhir.",
         "<html><div style='width: 300px;'>Tanggal rekap harus valid dan tanggal awal tidak boleh melewati tanggal akhir.</div></html>"),
        ("Data panen pada kriteria/periode ini belum tersedia.",
         "<html><div style='width: 300px;'>Data panen pada kriteria/periode ini belum tersedia.</div></html>")
    ]

    for old, new in replacements:
        if old in code and not f"<html><div style='width: 3" in code[code.find(old)-30:code.find(old)+10]:
            code = code.replace(f'"{old}"', f'"{new}"')

    with open(path, 'w', encoding='utf-8') as f:
        f.write(code)

print("Popup warnings wrapped successfully!")

