-- ========================================================
-- Database Schema & Sample Data: CanopyONE
-- Mengikuti struktur database relasional Minpro-4 PBO
-- ========================================================

CREATE DATABASE IF NOT EXISTS canopy;
USE canopy;

-- 1. Tabel User (Akun sistem)
CREATE TABLE IF NOT EXISTS user (
    id_user VARCHAR(20) PRIMARY KEY,
    nama VARCHAR(100) NOT NULL,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    no_telepon VARCHAR(20)
);

-- Tabel Role/Turunan User
CREATE TABLE IF NOT EXISTS admin (
    id_user VARCHAR(20) PRIMARY KEY,
    FOREIGN KEY (id_user) REFERENCES user(id_user) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS direksi (
    id_user VARCHAR(20) PRIMARY KEY,
    FOREIGN KEY (id_user) REFERENCES user(id_user) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS petugas (
    id_user VARCHAR(20) PRIMARY KEY,
    FOREIGN KEY (id_user) REFERENCES user(id_user) ON DELETE CASCADE
);

-- 2. Tabel Blok Lahan
CREATE TABLE IF NOT EXISTS blok_lahan (
    kode_blok_lahan VARCHAR(20) PRIMARY KEY,
    nama_blok_lahan VARCHAR(100) NOT NULL,
    luas_blok_lahan DECIMAL(10,2) NOT NULL,
    komoditas VARCHAR(50) NOT NULL,
    tahun_tanam INT DEFAULT 2020
);

-- 3. Tabel Aktivitas Panen
CREATE TABLE IF NOT EXISTS aktivitas_panen (
    id_panen VARCHAR(20) PRIMARY KEY,
    tanggal_panen DATE NOT NULL,
    kode_blok_lahan VARCHAR(20) NOT NULL,
    total_jumlah_panen DECIMAL(12,2) NOT NULL,
    hasil_panen_tidak_layak DECIMAL(12,2) DEFAULT 0,
    kualitas_panen VARCHAR(30) NOT NULL,
    jumlah_tandan INT DEFAULT 0,
    jumlah_brondolan INT DEFAULT 0,
    kadar_air_karet DECIMAL(5,2) DEFAULT 0,
    berat_kotor_karet DECIMAL(12,2) DEFAULT 0,
    FOREIGN KEY (kode_blok_lahan) REFERENCES blok_lahan(kode_blok_lahan) ON UPDATE CASCADE
);

-- 4. Tabel Evaluasi Panen
CREATE TABLE IF NOT EXISTS evaluasi_panen (
    id_evaluasi VARCHAR(20) PRIMARY KEY,
    tanggal_evaluasi DATE NOT NULL,
    status VARCHAR(50) NOT NULL,
    catatan_tindak_lanjut TEXT,
    aktivitas_panen_id_panen VARCHAR(20),
    FOREIGN KEY (aktivitas_panen_id_panen) REFERENCES aktivitas_panen(id_panen) ON DELETE SET NULL
);

-- 5. Tabel Riwayat Perubahan (Audit Trail)
CREATE TABLE IF NOT EXISTS riwayat_perubahan (
    id_riwayat VARCHAR(20) PRIMARY KEY,
    waktu_perubahan DATETIME NOT NULL,
    jenis_tindakan VARCHAR(50) NOT NULL,
    id_user VARCHAR(50) NOT NULL,
    id_panen VARCHAR(20),
    data_sebelum TEXT,
    data_setelah TEXT
);

-- ========================================================
-- Data Awal (Seeds)
-- ========================================================

-- User & Akun
INSERT INTO user (id_user, nama, username, password, no_telepon) VALUES
    ('P001', 'Budi Santoso', 'budi_petugas', '12345', '081234567890'),
    ('P002', 'Siti Rahma', 'admin_sawit', 'admin123', '081298765432'),
    ('P003', 'Hendro Wijaya', 'direksi_agri', 'direksi123', '081122334455')
ON DUPLICATE KEY UPDATE nama=VALUES(nama), password=VALUES(password);

INSERT IGNORE INTO petugas (id_user) VALUES ('P001');
INSERT IGNORE INTO admin (id_user) VALUES ('P002');
INSERT IGNORE INTO direksi (id_user) VALUES ('P003');

-- Blok Lahan
INSERT INTO blok_lahan (kode_blok_lahan, nama_blok_lahan, luas_blok_lahan, komoditas, tahun_tanam) VALUES
    ('SW001', 'Blok Sawit A01', 12.50, 'Sawit', 2018),
    ('SW002', 'Blok Sawit A02', 10.00, 'Sawit', 2019),
    ('KR001', 'Blok Karet B01', 8.00, 'Karet', 2017),
    ('KR002', 'Blok Karet B02', 15.00, 'Karet', 2016)
ON DUPLICATE KEY UPDATE nama_blok_lahan=VALUES(nama_blok_lahan);

-- Panen
INSERT INTO aktivitas_panen (id_panen, tanggal_panen, kode_blok_lahan, total_jumlah_panen, hasil_panen_tidak_layak, kualitas_panen, jumlah_tandan, jumlah_brondolan) VALUES
    ('PN001', '2026-10-25', 'SW001', 1250.00, 45.00, 'Baik', 100, 250),
    ('PN002', '2026-10-26', 'KR001', 680.00, 20.00, 'Cukup', 680, 28),
    ('PN003', '2026-10-27', 'SW002', 980.00, 35.00, 'Baik', 80, 180)
ON DUPLICATE KEY UPDATE total_jumlah_panen=VALUES(total_jumlah_panen);

-- Evaluasi
INSERT INTO evaluasi_panen (id_evaluasi, tanggal_evaluasi, status, catatan_tindak_lanjut, aktivitas_panen_id_panen) VALUES
    ('EV001', '2026-10-31', 'Sesuai Target', 'Produktivitas stabil, perawatan blok SW001 berjalan optimal.', 'PN001')
ON DUPLICATE KEY UPDATE status=VALUES(status);

-- Riwayat
INSERT INTO riwayat_perubahan (id_riwayat, waktu_perubahan, jenis_tindakan, id_user, id_panen, data_sebelum, data_setelah) VALUES
    ('RW001', '2026-10-25 08:30:15', 'Tambah Data', 'budi_petugas', 'PN001', '-', 'Input awal panen Sawit SW001'),
    ('RW002', '2026-10-26 14:10:22', 'Tambah Data', 'budi_petugas', 'PN002', '-', 'Input awal panen Karet KR001'),
    ('RW003', '2026-10-27 16:45:00', 'Pembaruan Data', 'admin_sawit', 'PN001', 'Total: 1200 Kg', 'Total: 1250 Kg')
ON DUPLICATE KEY UPDATE jenis_tindakan=VALUES(jenis_tindakan);

