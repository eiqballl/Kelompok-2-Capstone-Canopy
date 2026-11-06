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
| Kelola data pegawai/pengguna (CRUD) | ✅ | ❌ | ❌ |
| Kelola data panen sawit & karet (CRUD) | ✅ | ✅ | ❌ |
| Rekapitulasi panen (filter komoditas, blok, periode) | ✅ | ✅ | ✅ |
| Evaluasi panen (CRUD) | ✅ | Lihat & Tambah (Khusus Fitur Catatan Petugas) | ✅ |

Fitur pendukung:

- **Hitung metrik** panen (total produksi dan persentase hasil tidak layak) serta penentuan kualitas otomatis (Baik/Cukup/Kurang).
- **Kalkulasi otomatis**: total sawit (tandan + brondolan) dan berat bersih karet (berat kotor dikurangi kadar air).
- **Filter multi-blok** (`PilihBlokDialog`) dan **date picker kalender**.
- **Laporan tindak lanjut** dan **rangkuman eksekutif** untuk direksi.
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
| 1 | Koneksi database dan login | `gambar 1` |
| 2 | Menu Petugas: kelola data panen | `gambar 2` |
| 3 | Menu Petugas: rekapitulasi & evaluasi | `gambar 3` |
| 4 | Menu Admin: blok lahan, pegawai, data panen | `gambar 4` |
| 5 | Menu Admin: rekapitulasi & evaluasi | `gambar 5` |
| 6 | Menu Direksi: rekapitulasi & evaluasi | `gambar 6` |

### 5.3 ERD

Basis data `db_pt_canopy` memiliki **11 tabel** dan menerapkan konsep **Supertype–Subtype**:

- `user` → supertype bagi `admin`, `direksi`, `petugas`
- `produk_panen` → supertype bagi `sawit` dan `karet`
- Tabel lain: `blok_lahan`, `aktivitas_panen`, `evaluasi_panen`,

**ERD Logical**

<img width="2872" height="1415" alt="Logical_FINAL2" src="https://github.com/user-attachments/assets/4868ef15-aeec-464b-8b00-4e9247f4a01a" />


**ERD Relational**

<img width="2094" height="1040" alt="Relational_FINAL2" src="https://github.com/user-attachments/assets/e835efc8-baba-412c-a1c9-773693bde336" />


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

### Encapsulation
Atribut kelas model dideklarasikan `private` dan hanya diakses lewat getter/setter, misalnya pada `Pegawai`, `Panen`, `BlokLahan`, dan `Evaluasi`. Kelas `Koneksi` juga menyembunyikan objek `Connection` sebagai `private static` dan hanya membukanya lewat `getKoneksi()`. Kelas `*Mapper` dibuat `final` dengan *package-private* sehingga hanya dapat dipakai oleh package `dao`.

```java
public class Pegawai {
    private String idPegawai;
    private String namaPegawai;
    public String getIdPegawai() { return idPegawai; }
    public void setIdPegawai(String idPegawai) { this.idPegawai = idPegawai; }
}
```

### Inheritance
- `PegawaiDAO`, `BlokLahanDAO`, `EvaluasiDAO`, dan `RiwayatDAO` mewarisi `BaseDAO<T>`.
- Kelas GUI mewarisi komponen Swing: `LoginFrame`, `AdminFrame`, `PetugasFrame`, `DireksiFrame` → `JFrame`; `CalendarDialog`, `PilihBlokDialog` → `JDialog`; `DateTextField` → `JTextField`; `CoverPanel`, `CalendarPanel` → `JPanel`.

```java
public class PegawaiDAO extends BaseDAO<Pegawai> { ... }
public class AdminFrame extends javax.swing.JFrame { ... }
```

### Polymorphism
- **Overriding**: setiap DAO mengimplementasikan ulang method `findAll()`, `findById()`, `save()`, `update()`, dan `delete()` dari `BaseDAO` dengan `@Override`. Kelas `Panen` meng-override method dari interface `KalkulasiPanen`.
- **Overloading**: `tentukanKualitas()` dan `tentukanKualitas(ambangBaik, ambangCukup)`, `findAll()` dan `findAll(String keyword)`, serta `hitungTotalSawit(...)` dengan dua versi parameter.

```java
@Override
public String tentukanKualitas() { return tentukanKualitas(5.0, 10.0); }
```

### Abstraction
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

### 8.4 Panduan Penggunaan GUI

**1. Login** - masukkan username dan password, lalu klik **Masuk**. Sistem mengarahkan ke dashboard sesuai peran.

<!-- screenshot halaman login -->

**2. Dashboard Admin** - tab *Kelola Blok Lahan*, *Kelola Data Pegawai*, *Kelola Data Panen*, *Rekapitulasi*, dan *Evaluasi Panen*.

<!-- screenshot dashboard admin -->

**3. Dashboard Petugas** - tab *Input Panen*, *Rekapitulasi*, dan *Evaluasi Panen*. Pilih komoditas sawit atau karet, isi form, lalu klik **Simpan Panen**.

<!-- screenshot dashboard petugas -->

**4. Dashboard Direksi** - tab *Lihat Blok Lahan*, *Rekapitulasi*, dan *Evaluasi Panen*. Direksi dapat menghitung metrik dan membuat evaluasi beserta catatan tindak lanjut.

<!-- screenshot dashboard direksi -->

**5. Rekapitulasi** - gunakan **Pilih Blok Lahan**, filter periode, lalu klik **Tampilkan Rekapitulasi**.

<!-- screenshot rekapitulasi -->

**6. Evaluasi Panen** - pilih rentang tanggal, klik **Hitung Metrik**, isi status, catatan evaluasi, catatan tindak lanjut, lalu **Simpan Evaluasi**.

<!-- screenshot evaluasi panen -->

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
