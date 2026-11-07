# 🌴 CanopyONE

**Sistem Informasi Pencatatan dan Pemantauan Hasil Panen Perkebunan Sawit dan Karet pada PT Canopy Berbasis Java**

Project Akhir Praktikum Sistem Basis Data & Capstone PBO (Kelompok 2)
Program Studi Sistem Informasi, Fakultas Teknik, Universitas Mulawarman (2026)

---

## Daftar Isi

1. [Nama Program](#1-nama-program)
2. [Deskripsi Singkat](#2-deskripsi-singkat)
3. [Fitur Utama](#3-fitur-utama)
4. [Tech Stack](#4-tech-stack)
5. [Perancangan Sistem](#5-perancangan-sistem)
6. [Struktur Folder](#6-struktur-folder)
7. [Penerapan 4 Pilar OOP](#7-penerapan-4-pilar-oop)
8. [Cara Penggunaan](#8-cara-penggunaan)
9. [Kontributor](#9-kontributor)

---

## Nama Program

**CanopyONE** 

## 1. Deskripsi Singkat

CanopyONE adalah aplikasi desktop berbasis Java Swing untuk mencatat dan memantau hasil panen kelapa sawit dan karet di PT Canopy. Aplikasi ini menggantikan pencatatan manual di atas kertas yang rawan salah catat, dokumen hilang, dan sulit dicari kembali. Seluruh data tersimpan terpusat di MySQL, dapat difilter berdasarkan komoditas, blok lahan, dan periode, serta dapat dievaluasi oleh pihak direksi.

Pengembangan sistem ini sejalan dengan **SDG 9** (Industri, Inovasi, dan Infrastruktur) dan **SDG 12** (Konsumsi dan Produksi yang Bertanggung Jawab).

## 3. Fitur Utama

| Fitur | Admin | Petugas | Direksi |
|---|:---:|:---:|:---:|
| Login berbasis peran (role-based access) | ✅ | ✅ | ✅ |
| Kelola blok lahan (CRUD) | ✅ | lihat | lihat |
| Kelola data pegawai/pengguna (CRUD) | ✅ | ❌ | lihat |
| Kelola data panen sawit & karet (CRUD) | ✅ | ✅ | ❌ |
| Rekapitulasi panen (filter komoditas, blok, periode) | ✅ | ✅ | ✅ |
| Evaluasi panen (CRUD) | ✅ | Lihat & Tambah (Khusus Fitur Catatan Petugas) | ✅ |

Fitur pendukung:

- **Hitung metrik** panen (total produksi dan persentase hasil tidak layak) serta penentuan kualitas otomatis (Baik/Cukup/Kurang).
- **Kalkulasi otomatis**: total sawit (tandan + brondolan) dan berat bersih karet (berat kotor dikurangi kadar air).
- **Filter multi-blok** (`PilihBlokDialog`) dan **date picker kalender**.
- **Laporan tindak lanjut** (`LaporanTindakLanjutDialog`) untuk evaluasi panen.
- **Riwayat perubahan** data panen (data sebelum dan sesudah).
- **Validasi input** (`InputValidator`) untuk angka, tanggal, dan string.

## 4. Tech Stack

| Kategori | Teknologi |
|---|---|
| Bahasa | Java (target JDK 1.8) |
| Database | MySQL 8.x |
| GUI Framework | Java Swing (NetBeans GUI Builder, file `.form`, Look & Feel Nimbus) |
| Build Tool | Apache Maven |
| Library | `mysql-connector-j` 8.3.0 (JDBC), `java.sql`, `javax.swing`, `java.awt` |
| Tools | NetBeans IDE, phpMyAdmin / MySQL Workbench, Git & GitHub |

## 5. Perancangan Sistem

### 5.1 Use Case Diagram

Terdapat tiga aktor: **Admin**, **Petugas **, dan **Direksi**. Semua aktor wajib login sebelum mengakses fitur sesuai hak aksesnya.

<img width="1552" height="2312" alt="Use Case Pa Canopy (ACC1) (1)" src="https://github.com/user-attachments/assets/ed440250-93ff-44f8-a40c-0a8be52a90f5" />


### 5.2 Flowchart

| No | Flowchart | Gambar |
|---|---|---|
| 1 | Koneksi database dan login | `<img width="632" height="1056" alt="FINAL FLOWCHART CANOPYYY-Menu Utama drawio" src="https://github.com/user-attachments/assets/5935eef4-0f13-4d2c-b3f0-0997ebd92f68" />` |
| 2 | Menu Petugas: kelola data panen, rekapitulasi & evaluasi | `<img width="2343" height="3112" alt="FINAL FLOWCHART CANOPYYY-Menu Petugas drawio" src="https://github.com/user-attachments/assets/951604e0-cdcb-41ed-94e9-45ebaabfccfb" />` |
| 3 | Menu Admin: kelola blok lahan, pegawai, data panen, rekapitulasi & evaluasi  | `<img width="2622" height="6842" alt="FINAL FLOWCHART CANOPYYY-Menu Admin drawio" src="https://github.com/user-attachments/assets/09578f1c-e6f4-40ea-8603-f35f12c5b5ba" />` |
| 4 | Menu Direksi: lihat blok lahan, pegawai, rekapitulasi & evaluasi | `<img width="2645" height="2957" alt="FINAL FLOWCHART CANOPYYY-Menu Direksi drawio" src="https://github.com/user-attachments/assets/b0542724-c2ac-460a-ab67-683658872df1" />` |

### 5.3 ERD

Basis data `db_pt_canopy` memiliki **11 tabel** dan menerapkan konsep **Supertype–Subtype**:

- `user` → supertype bagi `admin`, `direksi`, `petugas`
- `produk_panen` → supertype bagi `sawit` dan `karet`
- Tabel lain: `blok_lahan`, `aktivitas_panen`, `evaluasi_panen`,

**ERD Logical**

<img width="4846" height="2384" alt="Logical_CANOPY(FIXXXXXXXXXXXXX)" src="https://github.com/user-attachments/assets/4be1cdb4-40fd-4286-9da9-4e1f27eeece9" />


**ERD Relational**

<img width="4532" height="2230" alt="Relational_CANOPY(FIXXXXXXXXXXXX)" src="https://github.com/user-attachments/assets/87918e3c-ba69-46eb-98d2-eeca006aeb85" />



## 6. Struktur Folder

```
Kelompok-2-Capstone-Canopy/
├── database/
│   └── canopy.sql                  
├── lib/
│   └── mysql-connector-j-8.3.0.jar 
├── src/main/
│   ├── java/
│   │   ├── main/
│   │   │   └── CanopyApp.java      
│   │   ├── config/                 
│   │   ├── model/                  
│   │   │   ├── Pegawai, BlokLahan, Panen, Evaluasi, Riwayat
│   │   │   ├── KalkulasiPanen      
│   │   │   └── DataStore           
│   │   ├── dao/                    
│   │   ├── controller/             
│   │   ├── view/                   
│   │   │                           
│   │   ├── component/              
│   │   └── util/                   
│   └── resources/images/           
├── pom.xml                         
├── cp.txt
└── .gitignore
```

## 7. Penerapan 4 Pilar OOP

### 1) Encapsulation
Atribut kelas model dideklarasikan `private` dan hanya diakses lewat getter/setter, misalnya pada `Pegawai`, `Panen`, `BlokLahan`, dan `Evaluasi`. Kelas `Koneksi` juga menyembunyikan objek `Connection` sebagai `private static` dan hanya membukanya lewat `getKoneksi()`. Kelas `*Mapper` dibuat `final` dengan *package-private* sehingga hanya dapat dipakai oleh package `dao`.

```java
public class Pegawai {
    private String idPegawai;
    private String namaPegawai;
    public String getIdPegawai() { return idPegawai; }
    public void setIdPegawai(String idPegawai) { this.idPegawai = idPegawai; }
}
```

### 2) Inheritance
- `PegawaiDAO`, `BlokLahanDAO`, `EvaluasiDAO`, dan `RiwayatDAO` mewarisi `BaseDAO<T>`.
- Kelas GUI mewarisi komponen Swing: `LoginFrame`, `AdminFrame`, `PetugasFrame`, `DireksiFrame` → `JFrame`; `CalendarDialog`, `PilihBlokDialog` → `JDialog`; `DateTextField` → `JTextField`; `CoverPanel`, `CalendarPanel` → `JPanel`.

```java
public class PegawaiDAO extends BaseDAO<Pegawai> { ... }
public class AdminFrame extends javax.swing.JFrame { ... }
```

### 3) Polymorphism
- **Overriding**: setiap DAO mengimplementasikan ulang method `findAll()`, `findById()`, `save()`, `update()`, dan `delete()` dari `BaseDAO` dengan `@Override`. Kelas `Panen` meng-override method dari interface `KalkulasiPanen`.
- **Overloading**: `tentukanKualitas()` dan `tentukanKualitas(ambangBaik, ambangCukup)`, `findAll()` dan `findAll(String keyword)`, serta `hitungTotalSawit(...)` dengan dua versi parameter.

```java
@Override
public String tentukanKualitas() { return tentukanKualitas(5.0, 10.0); }
```

### 4) Abstraction
- `BaseDAO<T>` adalah **abstract class generik** yang mendefinisikan kontrak operasi CRUD tanpa implementasi.
- `KalkulasiPanen` adalah **interface** yang mendefinisikan kontrak perhitungan panen (`hitungPersentaseTidakLayak()`, `isMemenuhiStandar()`, `tentukanKualitas()`).

```java
public abstract class BaseDAO<T> {
    public abstract List<T> findAll() throws SQLException;
    public abstract void save(T entity) throws SQLException;
}
```

## 8. Cara Penggunaan

### 8.1 Prasyarat

- JDK 8 atau lebih baru
- MySQL Server 8.x (default `localhost:3306`, user `root`)
- Apache Maven (opsional jika dijalankan lewat NetBeans)

### 8.2 Instalasi

```bash
# 1. Clone repository
git clone https://github.com/eiqballl/Kelompok-2-Capstone-Canopy.git
cd Kelompok-2-Capstone-Canopy

# 2. Import database (membuat db_pt_canopy beserta data awal)
mysql -u root -p < database/canopy.sql

# 3. Build & jalankan
mvn clean compile exec:java -Dexec.mainClass=main.CanopyApp
```

Alternatif: buka project di **NetBeans**, lalu *Run* kelas `main.CanopyApp`.

> **Catatan koneksi:** `Koneksi.java` mencoba terhubung ke database `db_pt_canopy` (atau `canopy`) di `localhost`/`127.0.0.1` dengan user `root` dan password kosong atau `root`. Sesuaikan jika konfigurasi MySQL Anda berbeda.

### 8.3 Akun Demo

| Role | Username | Password |
|---|---|---|
| Admin | `ahmad.admin` | `admin123` |
| Direksi | `budi.direksi` | `direksi123` |
| Petugas | `andi.petugas` | `petugas123` |

8.4 Panduan Penggunaan GUI

1. Login

    Masukkan username dan password, lalu klik Masuk. Sistem memeriksa data pada tabel user, kemudian mengarahkan pengguna ke dashboard sesuai perannya (Admin, Petugas, atau Direksi). Jika username atau password salah, sistem menampilkan pesan kesalahan.

<img width="881" height="610" alt="Halaman Login" src="https://github.com/user-attachments/assets/5cbc60e2-5905-4845-8810-72e5b648445c" />
2. Dashboard Admin

    Admin memiliki akses penuh terhadap data blok lahan, pegawai, panen, rekapitulasi, dan evaluasi melalui lima tab.
    
    a) Kelola Blok Lahan
    
    Tab ini untuk mengelola data area perkebunan (kode blok, nama blok, luas, komoditas, dan tahun tanam). Isi form lalu klik Tambah untuk menyimpan. Untuk mengubah atau menghapus, pilih baris pada tabel, lalu klik Perbarui atau Hapus. Tombol Reset mengosongkan form.
    
    <img width="1179" height="705" alt="Kelola Blok Lahan" src="https://github.com/user-attachments/assets/18027d87-39a3-4354-a274-071d10c7b34b" />
    
    b) Kelola Data Pegawai
    
    Tab ini untuk mengelola akun pengguna (nama, username, password, nomor telepon, dan peran). Admin dapat menambah, memperbarui, dan menghapus pegawai. Menghapus pegawai yang sudah mencatat panen menampilkan peringatan karena data panen terkait ikut terhapus.
    
    <img width="1178" height="705" alt="Kelola Data Pegawai" src="https://github.com/user-attachments/assets/7d314bce-4955-4cd8-a5fc-897b68e77e9a" />
    
    c) Kelola Data Panen
    
    Tab ini untuk mencatat dan mengelola hasil panen sawit dan karet. Pilih komoditas, blok lahan, dan tanggal panen. Isi data hasil panen: untuk sawit berupa berat tandan dan brondolan, untuk karet berupa berat kotor dan kadar air. Isi juga jumlah hasil tidak layak. Sistem menghitung total panen dan kualitas (Baik, Cukup, atau Kurang) secara otomatis. Klik Simpan Panen untuk menyimpan, atau Perbarui dan Hapus untuk data yang dipilih dari tabel.
    
    <img width="1176" height="707" alt="Kelola Data Panen" src="https://github.com/user-attachments/assets/3ed92b15-f8ae-4f72-888e-3770b5404767" />
    
    d) Rekapitulasi
    
    Tab ini menampilkan ringkasan hasil panen. Gunakan Pilih Blok Lahan untuk memilih blok, atur filter komoditas, mutu panen, dan periode tanggal, lalu klik Tampilkan Rekapitulasi. Tabel hasil dan total panen (Kg) muncul di bagian bawah. Klik Reset Filter untuk mengembalikan tampilan semula.
    
    <img width="1175" height="708" alt="Rekapitulasi Admin" src="https://github.com/user-attachments/assets/d0e5bdd3-d455-4396-ab69-17ac1d33e40a" />
    
    e) Evaluasi Panen
    
    Tab ini untuk menilai hasil produksi pada suatu periode. Pilih rentang tanggal, lalu klik Hitung Metrik untuk menampilkan total hasil periode dan persentase hasil tidak layak. Isi status, catatan evaluasi, dan catatan tindak lanjut, lalu klik Simpan Evaluasi. Evaluasi yang tersimpan muncul pada daftar riwayat evaluasi dan dapat diperbarui atau dihapus.

<img width="1365" height="720" alt="Evaluasi Panen Admin" src="https://github.com/user-attachments/assets/8c244caa-06f4-4d30-b138-7e0097e6c819" />
3. Dashboard Petugas

    Petugas bertugas mencatat hasil panen di lapangan dan hanya dapat mengakses tiga tab.
    
    a) Input Panen
    
    Petugas memilih komoditas, blok lahan, dan tanggal panen, lalu mengisi berat hasil panen dan jumlah yang tidak layak. Klik Simpan Panen untuk menyimpan. Data yang pernah dicatat dapat diubah atau dihapus dari tabel Daftar Hasil Panen.

    <img width="1179" height="705" alt="Input Panen" src="https://github.com/user-attachments/assets/50f05e93-af39-450f-9ce4-92a21b982d6b" />

    b) Rekapitulasi
    
    Petugas dapat melihat rekap panen berdasarkan komoditas, blok lahan, mutu panen, dan periode. Gunakan Pilih Blok Lahan, isi filter, lalu klik Tampilkan Rekapitulasi. Klik Tampilkan Semua Data untuk menghapus filter.

    <img width="1177" height="708" alt="Rekapitulasi Petugas" src="https://github.com/user-attachments/assets/435bc249-a617-44c6-8764-d83d3fd2767c" />
    
    c) Evaluasi Panen
    
    Petugas hanya dapat melihat daftar evaluasi yang dibuat Admin atau Direksi, beserta statusnya. Tombol Lapor Tindak Lanjut membuka formulir untuk melaporkan tindak lanjut atas evaluasi yang dipilih.
    
    <img width="1183" height="707" alt="Evaluasi Panen Petugas" src="https://github.com/user-attachments/assets/89da9d41-3da2-429e-9cca-5a8502be8725" />
    
    Formulir Lapor Tindak Lanjut berisi catatan petugas atas tindak lanjut yang sudah dilakukan. Catatan ini kemudian dapat dibaca oleh Admin dan Direksi.
    
<img width="496" height="489" alt="Formulir Lapor Tindak Lanjut" src="https://github.com/user-attachments/assets/2170719a-a898-4d90-a1a2-88ca6d7f11ef" />  
4. Dashboard Direksi
   
    Direksi berfokus pada pemantauan dan evaluasi hasil produksi. Data blok lahan dan pegawai hanya dapat dilihat, tidak diubah.
    
    a) Lihat Blok Lahan
    
    Menampilkan daftar blok lahan perkebunan (kode, nama, luas, komoditas, dan tahun tanam) dalam mode baca saja.
    
    <img width="1177" height="704" alt="Lihat Blok Lahan" src="https://github.com/user-attachments/assets/90847a06-cdd8-42fa-95b1-655f3d01d4d6" />
    
    b) Lihat Pegawai
    
    Menampilkan daftar pegawai beserta perannya dalam mode baca saja, sebagai informasi pihak yang mencatat dan mengelola data.
    
    <img width="1365" height="717" alt="Lihat Pegawai" src="https://github.com/user-attachments/assets/2dba089e-707e-46d5-a5b6-1c5922cf853c" />
    
    c) Rekapitulasi
    
    Direksi memantau hasil panen secara keseluruhan. Gunakan Pilih Blok Lahan, filter periode, lalu klik Tampilkan Rekapitulasi. Ringkasan jumlah transaksi dan total panen ditampilkan di bagian bawah tabel.
    
    <img width="1365" height="719" alt="Rekapitulasi Direksi" src="https://github.com/user-attachments/assets/adf3d5b1-f1fe-40d3-b4ff-2ff1c70a53e3" />

    d) Evaluasi Panen
    
    Direksi menilai hasil produksi per periode. Pilih rentang tanggal, klik Hitung Metrik, isi status, catatan evaluasi, dan catatan tindak lanjut, lalu klik Simpan Evaluasi. Direksi juga dapat memperbarui atau menghapus evaluasi, dan membaca catatan tindak lanjut dari petugas.
    
    <img width="1365" height="719" alt="Evaluasi Panen Direksi" src="https://github.com/user-attachments/assets/4a22d139-99a5-4b4d-b48a-1efebc259362" />


## 9. Kontributor

**Kelompok 2** - Program Studi Sistem Informasi, Universitas Mulawarman

| No | Nama | NIM |
|:--:|---|---|
| 1 | Daffa Arkhabista | 2509116018 |
| 2 | Muhammad Fahriel | 2509116050 |
| 3 | Alya Aulia | 2509116060 |
| 4 | Iqbal Nurriz Ramadhan | 2509116067 |
| 5 | Nabila Viviana Asri | 2509116098 |

---

<p align="center">Dibuat dengan oleh Kelompok 2 - 2026</p>
