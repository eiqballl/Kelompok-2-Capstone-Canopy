<!-- Hallmark · pre-emit critique: P5 H4 E4 S5 R5 V3 -->
# Audit & Optimasi UI/UX — CanopyONE

Tanggal: 7 November 2026.

## Hasil dan batas perubahan

Tampilan dioptimalkan ringan, bukan didesain ulang. Branding biru, foto perkebunan, Segoe UI, Nimbus, susunan tab, serta alur fitur tetap dipertahankan. Perbaikan berfokus pada keterbacaan, ukuran jendela, konten panjang, dan akses keyboard.

- Styling bersama berada di `src/main/java/util/UiStyles.java`; penerapannya dilakukan di luar `initComponents()` hasil NetBeans.
- Model, DAO, controller, konfigurasi koneksi, `InputValidator.java`, `pom.xml`, dan aset gambar tidak diubah oleh pekerjaan ini.
- Tidak ada file `.form` yang diedit dalam optimasi ini. Perubahan pengguna yang sudah ada di `README.md`, `DireksiFrame.java`, dan `DireksiFrame.form`, termasuk tab **Lihat Pegawai**, tetap dipertahankan.
- Tidak dilakukan commit atau push.

## Lingkup dan cakupan

Stack: aplikasi desktop Java Swing, NetBeans GUI Builder, Nimbus, dan target Java 8. `README.md` menjadi acuan fitur, istilah, dan stack; tidak ditemukan dokumen desain, `AGENTS.md`, `CLAUDE.md`, `CONTRIBUTING.md`, atau `CODING_STANDARDS.md` di proyek.

Permukaan yang ditinjau:

- Login.
- Seluruh 12 tab dashboard: Admin (5), Petugas (3), Direksi (4).
- `DateTextField`, kalender modal, dan kalender panel.
- Dialog pilih blok, laporan tindak lanjut, dan rangkuman eksekutif.
- Data contoh/offline, ukuran normal dan sempit, konten panjang, beberapa keadaan kosong, fokus keyboard, serta keadaan tombol aktif/nonaktif.

Pratinjau menggunakan skala Java2D 100% dan 200%, bukan mengubah pengaturan Windows. Pada skala 200%, area kerja logis adalah 960 × 520: dashboard hasil penyesuaian berukuran 960 × 520 dan login 900 × 520. Pratinjau paksa yang lebih besar tetap disimpan sebagai stress test; hasil `screen-fit` yang digunakan untuk menilai apakah jendela muat di layar.

| Domain | Bukti yang diperiksa | Hasil |
| --- | --- | --- |
| Accessibility | Label Swing, accessible names, keyboard login/kalender, fokus picker, auto-scroll, disabled/read-only | 3 temuan diperbaiki; pembaca layar **Not verified** |
| Layout | Seluruh tab dan dialog, scrollbar, split pane, ukuran minimum, batas layar/taskbar | 2 temuan diperbaiki pada ukuran yang diuji |
| Writing | Petunjuk tanggal, pesan offline/kosong, istilah dan judul rangkuman | 1 temuan diperbaiki; pesan CRUD/database tidak diaudit menyeluruh |
| Typography | Tabel, ringkasan rekap, catatan panjang, filter, tahun kalender | 2 temuan diperbaiki; nilai panjang tetap dapat dipotong secara visual dengan tooltip nilai lengkap |
| Colors | Sampling tampilan awal dan probe Nimbus memakai `UiStyles` aktual pada 6 peran tombol | 2 temuan diperbaiki; 72 pasangan keadaan aktif lolos di masing-masing skala |
| UI | Border, footer, penekanan aksi, tampilan fokus/hover/pressed bawaan Nimbus | 1 penyempurnaan diterapkan; tidak menambahkan animasi atau dependensi |

Lingkupnya desktop: reflow web/mobile 320 px, RTL, dan pseudo-localization tidak diuji. Cakupan ini bukan audit aksesibilitas atau alur CRUD end-to-end.

## Temuan dan perbaikan berperingkat

Prioritas di bawah menggambarkan dampak **sebelum perbaikan**, bukan masalah yang masih terbuka. Semua perbaikan pada tabel telah diterapkan. Koreksi tahun kalender ditemukan saat verifikasi dan ditutup sebelum pemeriksaan akhir.

| Severity | Domain | Location | Before | After | Why |
| --- | --- | --- | --- | --- | --- |
| HIGH | Colors | `src/main/java/util/UiStyles.java:132` | Sampling awal: teks putih Masuk 4.29:1, Hapus 3.04:1, Perbarui 1.88:1 pada latar hasil render | Warna isi diperbaiki; Perbarui memakai teks gelap pada latar amber muda. Override terbatas untuk painter tombol default Nimbus; minimum probe akhir 4.81:1 | Teks kontrol perlu mencapai 4.5:1; warna deklarasi saja tidak mewakili gradient Nimbus |
| HIGH | Layout | `src/main/java/util/UiStyles.java:104`, `src/main/java/util/UiStyles.java:202`, `src/main/java/util/UiStyles.java:223`, `src/main/java/view/LaporanTindakLanjutDialog.java:133`, `src/main/java/view/RangkumanEksekutifDialog.java:174` | Jendela besar dan form panjang dapat menyembunyikan konten/tombol pada layar pendek | Ukuran mengikuti area kerja layar; form dapat digulir; divider menghormati lebar minimum; kontrol yang menerima fokus otomatis dibawa ke area terlihat | Kontrol penting harus tetap dapat dijangkau tanpa mengubah susunan form |
| HIGH | Accessibility | `src/main/java/view/AdminFrame.java:45`, `src/main/java/view/PetugasFrame.java:39`, `src/main/java/view/DireksiFrame.java:44`, `src/main/java/view/LoginFrame.java:18`, `src/main/java/view/LaporanTindakLanjutDialog.java:121`, `src/main/java/component/DateTextField.java:41`, `src/main/java/view/CalendarDialog.java:113`, `src/main/java/view/CalendarPanel.java:70` | Label visual belum terhubung ke input/tabel; ikon picker dan navigasi kalender belum memiliki nama deskriptif | `setLabelFor()` untuk input/tabel, accessible names untuk picker/navigasi, dan tanggal lengkap pada tombol hari | Nama dan hubungan label harus tersedia melalui API aksesibilitas, bukan hanya secara visual |
| HIGH | Colors | `src/main/java/util/UiStyles.java:121`, `src/main/java/view/AdminFrame.java:41`, `src/main/java/view/PetugasFrame.java:36`, `src/main/java/view/DireksiFrame.java:40` | Logout memakai warna merah yang sama dengan Hapus; Hitung Metrik bersaing dengan aksi Simpan | Logout dan Hitung Metrik menjadi aksi sekunder; Hapus tetap merah dan berlabel jelas | Warna bahaya tidak boleh menyamakan aksi biasa dengan penghapusan |
| HIGH | Typography | `src/main/java/util/UiStyles.java:168`, `src/main/java/util/UiStyles.java:238`, `src/main/java/util/UiStyles.java:300`, `src/main/java/view/LaporanTindakLanjutDialog.java:87`, `src/main/java/view/RangkumanEksekutifDialog.java:56`, `src/main/java/view/RangkumanEksekutifDialog.java:208` | Kolom lebar dipaksa menyempit, ringkasan rekap satu baris, dan teks panjang/karakter HTML dapat kehilangan keterbacaan | Baris tabel 32 px, angka rata kanan, kolom lebar dengan gulir horizontal, tooltip lengkap, rekap tiga baris, pembungkusan teks, dan escaping HTML pada tampilan | Pemotongan tidak boleh menghilangkan jalan untuk membaca nilai lengkap; data dan perhitungan tidak diubah |
| HIGH | Accessibility | `src/main/java/component/DateTextField.java:34`, `src/main/java/component/DateTextField.java:46`, `src/main/java/view/CalendarDialog.java:220`, `src/main/java/view/CalendarPanel.java:174`, `src/main/java/util/UiStyles.java:257`, `src/main/java/util/UiStyles.java:275`, `src/main/java/view/LoginFrame.java:20` | Picker dan hari kalender tidak ikut fokus keyboard; login belum memiliki tombol default | F4/Alt+Down membuka kalender; Tab/Space mencapai picker; panah berpindah hari; Enter memilih; satu hari masuk urutan Tab; fokus kembali ke input. Enter login dan Escape dialog memakai mekanisme Swing | Pemilihan tanggal dan aksi utama harus memiliki jalur keyboard, dengan fokus terlihat dan tidak terjebak pada setiap hari |
| HIGH | Layout | `src/main/java/view/PilihBlokDialog.java:47`, `src/main/java/view/PilihBlokDialog.java:131`, `src/main/java/view/PilihBlokDialog.java:156` | Aksi pilihan cepat dan konfirmasi disandingkan dalam satu baris footer yang tidak muat pada dialog kecil | Preferred size dipakai bersama `pack()`; footer menjadi dua baris, dengan konfirmasi tetap di bawah | Perbaikan lokal mencegah tombol terpotong tanpa mengubah pilihan/filter |
| HIGH | Typography | `src/main/java/view/CalendarDialog.java:138`, `src/main/java/view/CalendarPanel.java:84` | Sebelum koreksi terakhir, lebar tahun 56/59 px menyisakan 31/34 px untuk renderer yang memerlukan 36 px; tahun masih terpotong | Preferred width 72 px pada kedua kalender: ruang efektif 47 px, kebutuhan renderer 36 px; tahun lengkap terlihat pada kedua skala | Prototype value saja tidak memperhitungkan ruang panah dan padding renderer Nimbus |
| MEDIUM | Accessibility | `src/main/java/component/DateTextField.java:85`, `src/main/java/component/DateTextField.java:91`, `src/main/java/component/DateTextField.java:96` | Picker tertanam tidak mengikuti keadaan disabled/read-only input induknya | Enabled/editable diteruskan ke picker; pembukaan kalender ditolak saat input tidak dapat diedit | Keadaan kontrol harus konsisten dengan kemampuan input |
| MEDIUM | Writing | `src/main/java/util/UiStyles.java:126`, `src/main/java/view/LoginFrame.java:47`, `src/main/java/view/PilihBlokDialog.java:96`, `src/main/java/view/RangkumanEksekutifDialog.java:98` | Peringatan offline panjang; daftar blok kosong tanpa petunjuk; judul KPI memakai istilah campuran | Pesan offline singkat tetap menjelaskan data contoh tidak tersimpan; blok kosong meminta penambahan oleh Admin; judul menggunakan Total hasil panen/Hasil tidak layak | Status dan langkah berikutnya lebih mudah dipahami tanpa menulis ulang seluruh copy aplikasi |
| LOW | UI | `src/main/java/util/UiStyles.java:85`, `src/main/java/view/PilihBlokDialog.java:108`, `src/main/java/view/LaporanTindakLanjutDialog.java:135`, `src/main/java/view/RangkumanEksekutifDialog.java:177` | Garis hitam tegas pada panel dan permukaan footer tidak konsisten | Border netral tipis dan footer selaras dengan permukaan dialog; indikator fokus native tetap dipertahankan | Mengurangi gangguan visual dan memperjelas pengelompokan; bukan perubahan struktur atau tema |

## Verifikasi yang berhasil

Seluruh SQL ditolak oleh proxy koneksi di harness sementara. Data contoh/fixture hanya digunakan dalam proses pratinjau; tidak ada operasi terhadap MySQL nyata. Pesan `UI preview: database access disabled` dan `UI smoke test: database access disabled` merupakan keluaran yang diharapkan.

| Pemeriksaan | Command / interaksi | Hasil |
| --- | --- | --- |
| Kompatibilitas kompilasi Java 8 | `javac --release 8 -encoding UTF-8` untuk seluruh source dan harness sementara, menggunakan JDK 17 | Lolos; ada peringatan unchecked, bukan error kompilasi |
| Build proyek | `mvn -B test` melalui Maven NetBeans dengan `JAVA_HOME` JDK 17 | `BUILD SUCCESS`; **`No tests to run.`**, bukan kelulusan suite tes proyek |
| Smoke UI terisolasi | `UiSmokeTest` dengan `-Dsun.java2d.uiScale=1` dan `=2`, lalu pengulangan keduanya | Empat run akhir berurutan setelah koreksi tahun: masing-masing **85 assertions, 0 failing groups**, dalam 6 kelompok |
| Keyboard dan keadaan picker | F4, Alt+Down, Tab/Shift+Tab, Space, panah melewati pergantian bulan/tahun, Enter, Escape, disabled/read-only | Lolos pada kedua skala; pemilihan/pembatalan mengembalikan fokus input |
| Login dan fokus form | Checkbox tampilkan password, Enter untuk validasi/kredensial contoh, fokus tombol form yang semula di luar viewport | Lolos; login contoh membuka Admin; form menggulir kontrol yang difokuskan |
| Tabel dan dialog | Renderer angka, selection, tooltip literal/kosong, data tidak berubah, footer minimum, catatan panjang, blok/rangkuman kosong, Escape tanpa submit | Lolos pada fixture yang diuji; bukan pengujian seluruh CRUD |
| Kontras tombol Nimbus | `ButtonPreview` memakai `UiStyles` aktual, 6 peran × 12 keadaan enabled, termasuk default/fokus/hover/pressed | **72 pasangan, 0 kegagalan** pada masing-masing skala; minimum **4.81:1** |
| Inspeksi visual | `UiPreview` pada kedua skala; semua tab, dialog, kalender, dan screenshot fokus | Seluruh 12 tab sempit diperiksa; hasil `screen-fit` muat dalam area kerja; tahun kalender terlihat lengkap |
| Pelestarian kode NetBeans | `javap -p -c` terhadap `baseline-classes` dan `after-classes`; normalisasi offset instruksi, nomor constant pool, whitespace, dan `ldc_w`/`ldc` | `initComponents()` identik dengan baseline sebelum pekerjaan untuk Admin, Petugas, Direksi, Login, CalendarDialog, dan CalendarPanel; diperiksa ulang setelah koreksi terakhir |
| Batas perubahan dan whitespace | `git -c core.safecrlf=false diff --exit-code` pada backend/validasi/build; `git -c core.safecrlf=false diff --check` | Lolos; diff pengguna tetap README 1 penambahan/1 penghapusan dan Direksi `.form` 74 penambahan |

Minimum kontras hasil probe menurut peran: primary **5.15:1**, danger **4.81:1**, success **4.82:1**, update **6.04:1**, secondary **8.98:1**, hari kalender terpilih **5.02:1**. Angka ini berasal dari sampling latar hasil render di samping area teks, bukan hanya nilai warna deklarasi. Disabled dikecualikan dari ambang 4.5:1; hasil ini tidak mengklaim semua teks/indikator fokus aplikasi sudah diukur.

Run diagnostik awal menunjukkan bahwa fokus Swing belum menjamin event `Robot` sampai dari Windows. Harness akhirnya memastikan penerimaan pelepasan Shift yang tidak mengubah data sebelum menguji F4. Escape juga diuji terpisah: saat tooltip terbuka, Escape pertama menutup tooltip; berikutnya membatalkan kalender. Penyesuaian kesiapan input/pointer hanya ada di harness, bukan workaround keyboard global pada aplikasi.

### Artefak lokal

Direktori bukti: `C:\Users\Fareru\AppData\Local\Temp\opencode\canopy-ui-review-20261107\`.

- `UiSmokeTest.java`, `UiPreview.java`, `ButtonPreview.java`: harness sementara, tidak menjadi bagian test suite Maven/repository.
- `before\`, `after\`, `after-scale2\`: screenshot pembanding, termasuk `calendar.png` dan semua tab `*-screen-fit.png`.
- `smoke-final-scale1.txt`, `smoke-final-scale2.txt`, `smoke-final-repeat-scale1.txt`, `smoke-final-repeat-scale2.txt`: empat hasil akhir 85 pemeriksaan.
- `smoke-final-scale2\date-picker-focused.png` dan `smoke-final-scale2\embedded-calendar.png`: fokus picker dan kalender panel.
- `button-actual-scale1.txt`, `button-actual-scale2.txt`: 72 pasangan kontras per skala.
- `after-layout.txt`, `after-scale2-layout.txt`, `scope-verification.txt`, `maven-final-test.txt`: ukuran, pelestarian initializer, dan build.
- Diagnostik sebelumnya, termasuk `smoke-year-before-fix.txt`, tetap disimpan; bukan hasil akhir.

### Command utama untuk mengulang

Jalankan dari root proyek pada desktop Windows aktif. Tes `Robot` harus berjalan berurutan, tanpa harness GUI lain mengambil fokus. Command smoke memerlukan artefak lokal di atas; bukan command test yang tersedia pada fresh clone.

```powershell
$env:JAVA_HOME = 'C:\Users\Fareru\java\jdk-17.0.20.1+1'
& 'C:\Program Files\Apache NetBeans\java\maven\bin\mvn.cmd' -B test

$jdk = Join-Path $env:JAVA_HOME 'bin'
$audit = 'C:\Users\Fareru\AppData\Local\Temp\opencode\canopy-ui-review-20261107'
$sources = (Get-ChildItem 'src\main\java' -Recurse -Filter '*.java').FullName
& "$jdk\javac.exe" --release 8 -encoding UTF-8 -d "$audit\after-classes" $sources "$audit\UiPreview.java" "$audit\UiSmokeTest.java" "$audit\ButtonPreview.java"
if ($LASTEXITCODE -ne 0) { throw 'Kompilasi gagal' }

foreach ($scale in @(1, 2)) {
    & "$jdk\java.exe" "-Dsun.java2d.uiScale=$scale" -cp "$audit\after-classes;src\main\resources" UiSmokeTest "$audit\smoke-final-scale$scale" debug-input
    if ($LASTEXITCODE -ne 0) { throw "Smoke gagal pada skala $scale" }
}
foreach ($scale in @(1, 2)) {
    & "$jdk\java.exe" "-Dsun.java2d.uiScale=$scale" -cp "$audit\after-classes;src\main\resources" UiSmokeTest "$audit\smoke-final-repeat-scale$scale"
    if ($LASTEXITCODE -ne 0) { throw "Pengulangan smoke gagal pada skala $scale" }
    & "$jdk\java.exe" "-Dsun.java2d.uiScale=$scale" -cp "$audit\after-classes" ButtonPreview "$audit\button-actual-scale$scale.png"
    if ($LASTEXITCODE -ne 0) { throw "Kontras gagal pada skala $scale" }
}
git -c core.safecrlf=false diff --exit-code -- src/main/java/model src/main/java/dao src/main/java/controller src/main/java/config src/main/java/util/InputValidator.java pom.xml
git -c core.safecrlf=false diff --check
```

## Not verified / tindak lanjut

- Database nyata: login online, persistensi, CRUD, filter terhadap data riil, penanganan kegagalan koneksi, dan latensi/loading.
- Seluruh alur CRUD keyboard-only, perpindahan fokus semua dialog, dan konfirmasi penghapusan end-to-end. Dialog pendukung diubah menjadi modeless hanya di harness untuk pemeriksaan ukuran/Escape; kalender modal benar-benar diuji secara modal.
- Pembaca layar/Java Access Bridge, Windows High Contrast, dan pemeriksaan kontras semua teks serta indikator fokus.
- Runtime JDK 8 asli, OS/look-and-feel lain, perpindahan antar-monitor dengan DPI berbeda, RTL, dan string lokalisasi ekstrem.
- Tes permanen proyek: Maven belum memiliki test suite. Harness dan screenshot audit berada di folder sementara dan tidak ikut tersedia jika folder tersebut dibersihkan atau repository dikloning ulang.

## Verdict

**Approve — untuk lingkup visual dan pemeriksaan UI terisolasi yang dijelaskan di atas.** Tidak ada temuan HIGH yang diketahui masih terbuka pada skenario yang diperiksa. Ini bukan sertifikasi aksesibilitas atau jaminan kelulusan alur produksi; pemeriksaan yang berstatus **Not verified** tetap perlu dilakukan dengan lingkungan yang sesuai.
