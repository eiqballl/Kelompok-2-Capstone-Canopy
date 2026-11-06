-- CanopyONE Database Schema & Seed Data (Revisi 1)
CREATE DATABASE IF NOT EXISTS db_pt_canopy;
  USE db_pt_canopy;

-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Generation Time: Oct 29, 2026 at 02:11 PM
-- Server version: 8.0.30
-- PHP Version: 8.1.10

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `db_pt_canopy`
--

-- --------------------------------------------------------

--
-- Table structure for table `admin`
--

  CREATE TABLE `admin` (
    `id_user` varchar(10) NOT NULL,
    `bagian_administrasi` varchar(100) DEFAULT NULL
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

  --
  -- Dumping data for table `admin`
  --

  INSERT INTO `admin` (`id_user`, `bagian_administrasi`) VALUES
  ('U001', 'Administrasi Data Perkebunan'),
  ('U002', 'Administrasi Hasil Panen');

  -- --------------------------------------------------------

  --
  -- Table structure for table `aktivitas_panen`
  --

  CREATE TABLE `aktivitas_panen` (
    `id_panen` varchar(10) NOT NULL,
    `tanggal_panen` date DEFAULT NULL,
    `total_jumlah_panen` decimal(10,2) DEFAULT NULL,
    `hasil_panen_tidak_layak` int DEFAULT NULL,
    `kualitas_panen` varchar(15) DEFAULT NULL,
    `kode_blok_lahan` varchar(10) DEFAULT NULL,
    `id_evaluasi` varchar(10) DEFAULT NULL,
    `id_user` varchar(10) DEFAULT NULL
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

  --
  -- Dumping data for table `aktivitas_panen`
  --

  INSERT INTO `aktivitas_panen` (`id_panen`, `tanggal_panen`, `total_jumlah_panen`, `hasil_panen_tidak_layak`, `kualitas_panen`, `kode_blok_lahan`, `id_evaluasi`, `id_user`) VALUES
  ('PN001', '2026-09-01', '4500.00', 120, 'Baik', 'SW001', 'EV001', 'U006'),
  ('PN002', '2026-09-02', '5200.00', 150, 'Baik', 'SW002', 'EV002', 'U006'),
  ('PN003', '2026-09-03', '4800.00', 180, 'Cukup', 'SW003', 'EV003', 'U006'),
  ('PN004', '2026-09-04', '6100.00', 100, 'Baik', 'SW004', 'EV004', 'U006'),
  ('PN005', '2026-09-05', '3900.00', 200, 'Cukup', 'SW005', 'EV005', 'U006'),
  ('PN006', '2026-09-06', '7200.00', 130, 'Baik', 'SW006', 'EV006', 'U007'),
  ('PN007', '2026-09-07', '5600.00', 160, 'Baik', 'SW007', 'EV007', 'U007'),
  ('PN008', '2026-09-08', '4700.00', 210, 'Cukup', 'SW008', 'EV008', 'U007'),
  ('PN009', '2026-09-09', '4900.00', 110, 'Baik', 'SW001', 'EV009', 'U007'),
  ('PN010', '2026-09-10', '5400.00', 140, 'Baik', 'SW002', 'EV010', 'U007'),
  ('PN011', '2026-09-11', '4600.00', 250, 'Kurang', 'SW003', 'EV011', 'U008'),
  ('PN012', '2026-09-12', '6300.00', 120, 'Baik', 'SW004', 'EV012', 'U008'),
  ('PN013', '2026-09-13', '4100.00', 180, 'Cukup', 'SW005', 'EV013', 'U008'),
  ('PN014', '2026-09-14', '7500.00', 100, 'Baik', 'SW006', 'EV014', 'U008'),
  ('PN015', '2026-09-15', '5800.00', 150, 'Baik', 'SW007', 'EV015', 'U008'),
  ('PN016', '2026-09-16', '5000.00', 190, 'Cukup', 'SW008', NULL, 'U009'),
  ('PN017', '2026-09-17', '1850.00', 60, 'Baik', 'KR001', NULL, 'U009'),
  ('PN018', '2026-09-18', '2100.00', 85, 'Baik', 'KR002', NULL, 'U009'),
  ('PN019', '2026-09-19', '1650.00', 100, 'Cukup', 'KR003', NULL, 'U009'),
  ('PN020', '2026-09-20', '2300.00', 70, 'Baik', 'KR004', NULL, 'U009'),
  ('PN021', '2026-09-21', '2700.00', 90, 'Baik', 'KR005', NULL, 'U010'),
  ('PN022', '2026-09-22', '1950.00', 120, 'Cukup', 'KR006', NULL, 'U010'),
  ('PN023', '2026-09-23', '2200.00', 80, 'Baik', 'KR007', NULL, 'U010'),
  ('PN024', '2026-09-24', '1900.00', 65, 'Baik', 'KR001', NULL, 'U010'),
  ('PN025', '2026-09-25', '2250.00', 95, 'Baik', 'KR002', NULL, 'U010'),
  ('PN026', '2026-09-26', '1700.00', 140, 'Kurang', 'KR003', NULL, 'U011'),
  ('PN027', '2026-09-27', '2450.00', 75, 'Baik', 'KR004', NULL, 'U011'),
  ('PN028', '2026-09-28', '2850.00', 110, 'Baik', 'KR005', NULL, 'U011'),
  ('PN029', '2026-09-29', '2050.00', 125, 'Cukup', 'KR006', NULL, 'U011'),
  ('PN030', '2026-09-30', '2350.00', 90, 'Baik', 'KR007', NULL, 'U011');

  -- --------------------------------------------------------

  --
  -- Table structure for table `blok_lahan`
  --

  CREATE TABLE `blok_lahan` (
    `kode_blok_lahan` varchar(10) NOT NULL,
    `nama_blok_lahan` varchar(50) DEFAULT NULL,
    `luas_blok_lahan` int DEFAULT NULL,
    `komoditas` varchar(25) DEFAULT NULL,
    `id_user` varchar(10) DEFAULT NULL
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

  --
  -- Dumping data for table `blok_lahan`
  --

  INSERT INTO `blok_lahan` (`kode_blok_lahan`, `nama_blok_lahan`, `luas_blok_lahan`, `komoditas`, `id_user`) VALUES
  ('KR001', 'Blok Karet B01', 20, 'Karet', 'U001'),
  ('KR002', 'Blok Karet B02', 24, 'Karet', 'U001'),
  ('KR003', 'Blok Karet B03', 18, 'Karet', 'U001'),
  ('KR004', 'Blok Karet B04', 26, 'Karet', 'U001'),
  ('KR005', 'Blok Karet B05', 30, 'Karet', 'U001'),
  ('KR006', 'Blok Karet B06', 22, 'Karet', 'U001'),
  ('KR007', 'Blok Karet B07', 25, 'Karet', 'U001'),
  ('SW001', 'Blok Sawit A01', 25, 'Sawit', 'U001'),
  ('SW002', 'Blok Sawit A02', 30, 'Sawit', 'U001'),
  ('SW003', 'Blok Sawit A03', 28, 'Sawit', 'U001'),
  ('SW004', 'Blok Sawit A04', 35, 'Sawit', 'U001'),
  ('SW005', 'Blok Sawit A05', 22, 'Sawit', 'U001'),
  ('SW006', 'Blok Sawit A06', 40, 'Sawit', 'U001'),
  ('SW007', 'Blok Sawit A07', 32, 'Sawit', 'U001'),
  ('SW008', 'Blok Sawit A08', 27, 'Sawit', 'U001');

  -- --------------------------------------------------------

  --
  -- Table structure for table `direksi`
  --

  CREATE TABLE `direksi` (
    `id_user` varchar(10) NOT NULL,
    `jabatan_direksi` varchar(100) DEFAULT NULL
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

  --
  -- Dumping data for table `direksi`
  --

  INSERT INTO `direksi` (`id_user`, `jabatan_direksi`) VALUES
  ('U003', 'Direktur Utama'),
  ('U004', 'Direktur Operasional'),
  ('U005', 'Direktur Produksi');

  -- --------------------------------------------------------

  --
  -- Table structure for table `evaluasi_panen`
  --

  CREATE TABLE `evaluasi_panen` (
    `id_evaluasi` varchar(10) NOT NULL,
    `tanggal_awal` date DEFAULT NULL,
    `tanggal_evaluasi` date DEFAULT NULL,
    `status` varchar(10) NOT NULL,
    `catatan_evaluasi` varchar(200) NOT NULL,
    `catatan_tindak_lanjut` varchar(200) DEFAULT NULL,
    `aktivitas_panen_id_panen` varchar(10) DEFAULT NULL,
    `id_user` varchar(10) NOT NULL
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

  --
  -- Dumping data for table `evaluasi_panen`
  --

  INSERT INTO `evaluasi_panen` (`id_evaluasi`, `tanggal_evaluasi`, `status`, `catatan_tindak_lanjut`, `aktivitas_panen_id_panen`, `id_user`, `catatan_evaluasi`) VALUES
  ('EV001', '2026-09-02', 'Selesai', 'Hasil panen memenuhi standar kualitas.', 'PN001', 'U003', 'Hasil panen memenuhi target produksi.'),
  ('EV002', '2026-09-03', 'Selesai', 'Produktivitas blok dalam kondisi baik.', 'PN002', 'U003', 'Kualitas panen sesuai standar perusahaan.'),
  ('EV003', '2026-09-04', 'Proses', 'Perlu peningkatan pemeriksaan kematangan buah.', 'PN003', 'U003', 'Ditemukan buah dengan tingkat kematangan kurang merata.'),
  ('EV004', '2026-09-05', 'Selesai', 'Hasil panen sesuai target produksi.', 'PN004', 'U003', 'Produktivitas blok menunjukkan hasil yang baik.'),
  ('EV005', '2026-09-06', 'Proses', 'Perlu evaluasi kualitas hasil panen.', 'PN005', 'U003', 'Jumlah hasil panen tidak layak perlu dikurangi.'),
  ('EV006', '2026-09-07', 'Selesai', 'Hasil produksi sangat baik.', 'PN006', 'U004', 'Hasil produksi meningkat dibandingkan periode sebelumnya.'),
  ('EV007', '2026-09-08', 'Selesai', 'Kualitas panen memenuhi standar perusahaan.', 'PN007', 'U004', 'Kualitas hasil panen masih dalam kondisi baik.'),
  ('EV008', '2026-09-09', 'Pending', 'Menunggu pemeriksaan hasil panen tidak layak.', 'PN008', 'U004', 'Ditemukan peningkatan jumlah hasil panen tidak layak.'),
  ('EV009', '2026-09-10', 'Selesai', 'Hasil panen stabil dan sesuai standar.', 'PN009', 'U004', 'Hasil panen memenuhi standar kualitas.'),
  ('EV010', '2026-09-11', 'Selesai', 'Produktivitas mengalami peningkatan.', 'PN010', 'U004', 'Produktivitas perkebunan mengalami peningkatan.'),
  ('EV011', '2026-09-12', 'Proses', 'Perlu pengawasan teknik pemanenan.', 'PN011', 'U005', 'Kualitas panen kurang baik dan memerlukan pemeriksaan.'),
  ('EV012', '2026-09-13', 'Selesai', 'Target hasil panen telah tercapai.', 'PN012', 'U005', 'Jumlah produksi sesuai target yang ditentukan.'),
  ('EV013', '2026-09-14', 'Proses', 'Perlu perbaikan penanganan buah panen.', 'PN013', 'U005', 'Ditemukan beberapa hasil panen dengan kualitas kurang baik.'),
  ('EV014', '2026-09-15', 'Selesai', 'Hasil panen memenuhi standar produksi.', 'PN014', 'U005', 'Hasil panen menunjukkan produktivitas yang baik.'),
  ('EV015', '2026-09-16', 'Selesai', 'Kualitas dan jumlah panen dalam kondisi baik.', 'PN015', 'U005', 'Kondisi hasil panen secara keseluruhan cukup baik.');

  -- --------------------------------------------------------

  --
  -- Table structure for table `karet`
  --

  CREATE TABLE `karet` (
    `id_produk` varchar(10) NOT NULL,
    `kadar_air_karet` int DEFAULT NULL,
    `berat_kotor_karet` int DEFAULT NULL
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

  --
  -- Dumping data for table `karet`
  --

  INSERT INTO `karet` (`id_produk`, `kadar_air_karet`, `berat_kotor_karet`) VALUES
  ('PR016', 35, 1850),
  ('PR017', 32, 2100),
  ('PR018', 38, 1650),
  ('PR019', 30, 2300),
  ('PR020', 28, 2700),
  ('PR021', 40, 1950),
  ('PR022', 33, 2200),
  ('PR023', 25, 1900),
  ('PR024', 27, 2250),
  ('PR025', 42, 1700),
  ('PR026', 26, 2450),
  ('PR027', 24, 2850),
  ('PR028', 39, 2050),
  ('PR029', 31, 2350),
  ('PR030', 34, 1200);

  -- --------------------------------------------------------

  --
  -- Table structure for table `petugas`
  --

  CREATE TABLE `petugas` (
    `id_user` varchar(10) NOT NULL,
    `wilayah_penugasan` varchar(100) DEFAULT NULL
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

  --
  -- Dumping data for table `petugas`
  --

  INSERT INTO `petugas` (`id_user`, `wilayah_penugasan`) VALUES
  ('U006', 'Blok Sawit A01'),
  ('U007', 'Blok Sawit A02'),
  ('U008', 'Blok Sawit A03'),
  ('U009', 'Blok Sawit A04'),
  ('U010', 'Blok Sawit A05'),
  ('U011', 'Blok Sawit A06'),
  ('U012', 'Blok Sawit A07'),
  ('U013', 'Blok Sawit A08'),
  ('U014', 'Blok Karet B01'),
  ('U015', 'Blok Karet B02'),
  ('U016', 'Blok Karet B03'),
  ('U017', 'Blok Karet B04'),
  ('U018', 'Blok Karet B05'),
  ('U019', 'Blok Karet B06'),
  ('U020', 'Blok Karet B07');

  -- --------------------------------------------------------

  --
  -- Table structure for table `produk_panen`
  --

  CREATE TABLE `produk_panen` (
    `id_produk` varchar(10) NOT NULL,
    `aktivitas_panen_id_panen` varchar(10) DEFAULT NULL,
    `nama_produk` varchar(50) DEFAULT NULL,
    `tahun_tanam` varchar(4) DEFAULT NULL
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

  --
  -- Dumping data for table `produk_panen`
  --

  INSERT INTO `produk_panen` (`id_produk`, `aktivitas_panen_id_panen`, `nama_produk`, `tahun_tanam`) VALUES
  ('PR001', 'PN001', 'Sawit', '2015'),
  ('PR002', 'PN002', 'Sawit', '2016'),
  ('PR003', 'PN003', 'Sawit', '2017'),
  ('PR004', 'PN004', 'Sawit', '2014'),
  ('PR005', 'PN005', 'Sawit', '2018'),
  ('PR006', 'PN006', 'Sawit', '2013'),
  ('PR007', 'PN007', 'Sawit', '2015'),
  ('PR008', 'PN008', 'Sawit', '2017'),
  ('PR009', 'PN009', 'Sawit', '2015'),
  ('PR010', 'PN010', 'Sawit', '2016'),
  ('PR011', 'PN011', 'Sawit', '2017'),
  ('PR012', 'PN012', 'Sawit', '2014'),
  ('PR013', 'PN013', 'Sawit', '2018'),
  ('PR014', 'PN014', 'Sawit', '2013'),
  ('PR015', 'PN015', 'Sawit', '2015'),
  ('PR016', 'PN017', 'Karet', '2014'),
  ('PR017', 'PN018', 'Karet', '2015'),
  ('PR018', 'PN019', 'Karet', '2016'),
  ('PR019', 'PN020', 'Karet', '2013'),
  ('PR020', 'PN021', 'Karet', '2012'),
  ('PR021', 'PN022', 'Karet', '2017'),
  ('PR022', 'PN023', 'Karet', '2015'),
  ('PR023', 'PN024', 'Karet', '2014'),
  ('PR024', 'PN025', 'Karet', '2015'),
  ('PR025', 'PN026', 'Karet', '2016'),
  ('PR026', 'PN027', 'Karet', '2013'),
  ('PR027', 'PN028', 'Karet', '2012'),
  ('PR028', 'PN029', 'Karet', '2017'),
  ('PR029', 'PN030', 'Karet', '2015'),
  ('PR030', 'PN030', 'Karet', '2015');

  -- --------------------------------------------------------

  --
  -- Table structure for table `sawit`
  --

  CREATE TABLE `sawit` (
    `id_produk` varchar(10) NOT NULL,
    `jumlah_berat_tandan` int DEFAULT NULL COMMENT 'Berat tandan buah segar dalam kilogram (kg) per 1 truk Hino',
    `jumlah_berat_brondolan` int DEFAULT NULL COMMENT 'Berat brondolan dalam kilogram (kg) per 1 truk Hino'
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

  --
  -- Dumping data for table `sawit`
  --

  INSERT INTO `sawit` (`id_produk`, `jumlah_berat_tandan`, `jumlah_berat_brondolan`) VALUES
  ('PR001', 1365, 135),
  ('PR002', 1440, 160),
  ('PR003', 1530, 170),
  ('PR004', 1620, 180),
  ('PR005', 1275, 225),
  ('PR006', 1700, 200),
  ('PR007', 1530, 170),
  ('PR008', 1440, 160),
  ('PR009', 1530, 170),
  ('PR010', 1620, 180),
  ('PR011', 1360, 140),
  ('PR012', 1700, 200),
  ('PR013', 1530, 170),
  ('PR014', 1800, 200),
  ('PR015', 1620, 180);

  -- --------------------------------------------------------

  --
  -- Table structure for table `user`
  --

  CREATE TABLE `user` (
    `id_user` varchar(10) NOT NULL,
    `nama` varchar(100) DEFAULT NULL,
    `username` varchar(50) DEFAULT NULL,
    `password` varchar(100) DEFAULT NULL,
    `no_telepon` varchar(15) DEFAULT NULL
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

  --
  -- Dumping data for table `user`
  --

  INSERT INTO `user` (`id_user`, `nama`, `username`, `password`, `no_telepon`) VALUES
  ('U001', 'Ahmad Fauzi', 'ahmad.admin', 'admin123', '081234560001'),
  ('U002', 'Siti Rahmawati', 'siti.admin', 'admin123', '081234560002'),
  ('U003', 'Budi Santoso', 'budi.direksi', 'direksi123', '081234560003'),
  ('U004', 'Dewi Anggraini', 'dewi.direksi', 'direksi123', '081234560004'),
  ('U005', 'Rizky Pratama', 'rizky.direksi', 'direksi123', '081234560005'),
  ('U006', 'Andi Saputra', 'andi.petugas', 'petugas123', '081234560006'),
  ('U007', 'Muhammad Iqbal', 'iqbal.petugas', 'petugas123', '081234560007'),
  ('U008', 'Rudi Hartono', 'rudi.petugas', 'petugas123', '081234560008'),
  ('U009', 'Agus Setiawan', 'agus.petugas', 'petugas123', '081234560009'),
  ('U010', 'Fajar Maulana', 'fajar.petugas', 'petugas123', '081234560010'),
  ('U011', 'Dimas Firmansyah', 'dimas.petugas', 'petugas123', '081234560011'),
  ('U012', 'Hendra Wijaya', 'hendra.petugas', 'petugas123', '081234560012'),
  ('U013', 'Yusuf Ramadhan', 'yusuf.petugas', 'petugas123', '081234560013'),
  ('U014', 'Arif Hidayat', 'arif.petugas', 'petugas123', '081234560014'),
  ('U015', 'Rizal Kurniawan', 'rizal.petugas', 'petugas123', '081234560015'),
  ('U016', 'Eko Prasetyo', 'eko.petugas', 'petugas123', '081234560016'),
  ('U017', 'Wahyu Nugroho', 'wahyu.petugas', 'petugas123', '081234560017'),
  ('U018', 'Bayu Pamungkas', 'bayu.petugas', 'petugas123', '081234560018'),
  ('U019', 'Taufik Hidayat', 'taufik.petugas', 'petugas123', '081234560019'),
  ('U020', 'Reza Firmansyah', 'reza.petugas', 'petugas123', '081234560020');

  --
  -- Indexes for dumped tables
  --

  --
  -- Indexes for table `admin`
  --
  ALTER TABLE `admin`
    ADD PRIMARY KEY (`id_user`);

  --
  -- Indexes for table `aktivitas_panen`
  --
  ALTER TABLE `aktivitas_panen`
    ADD PRIMARY KEY (`id_panen`),
    ADD KEY `fk_aktivitas_blok` (`kode_blok_lahan`),
    ADD KEY `fk_aktivitas_evaluasi` (`id_evaluasi`),
    ADD KEY `fk_aktivitas_panen_user` (`id_user`);

  --
  -- Indexes for table `blok_lahan`
  --
  ALTER TABLE `blok_lahan`
    ADD PRIMARY KEY (`kode_blok_lahan`),
    ADD KEY `fk_blok_lahan_user` (`id_user`);

  --
  -- Indexes for table `direksi`
  --
  ALTER TABLE `direksi`
    ADD PRIMARY KEY (`id_user`);

  --
  -- Indexes for table `evaluasi_panen`
  --
  ALTER TABLE `evaluasi_panen`
    ADD PRIMARY KEY (`id_evaluasi`),
    ADD KEY `fk_evaluasi_aktivitas` (`aktivitas_panen_id_panen`),
    ADD KEY `fk_evaluasi_panen_user` (`id_user`);

  --
  -- Indexes for table `karet`
  --
  ALTER TABLE `karet`
    ADD PRIMARY KEY (`id_produk`);

  --
  -- Indexes for table `petugas`
  --
  ALTER TABLE `petugas`
    ADD PRIMARY KEY (`id_user`);

  --
  -- Indexes for table `produk_panen`
  --
  ALTER TABLE `produk_panen`
    ADD PRIMARY KEY (`id_produk`),
    ADD KEY `fk_produk_aktivitas` (`aktivitas_panen_id_panen`);

  --
  -- Indexes for table `sawit`
  --
  ALTER TABLE `sawit`
    ADD PRIMARY KEY (`id_produk`);

  --
  -- Indexes for table `user`
  --
  ALTER TABLE `user`
    ADD PRIMARY KEY (`id_user`);

  --
  -- Constraints for dumped tables
  --

  --
  -- Constraints for table `admin`
  --
  ALTER TABLE `admin`
    ADD CONSTRAINT `fk_admin_user` FOREIGN KEY (`id_user`) REFERENCES `user` (`id_user`);

  --
  -- Constraints for table `aktivitas_panen`
  --
  ALTER TABLE `aktivitas_panen`
    ADD CONSTRAINT `fk_aktivitas_blok` FOREIGN KEY (`kode_blok_lahan`) REFERENCES `blok_lahan` (`kode_blok_lahan`),
    ADD CONSTRAINT `fk_aktivitas_evaluasi` FOREIGN KEY (`id_evaluasi`) REFERENCES `evaluasi_panen` (`id_evaluasi`),
    ADD CONSTRAINT `fk_aktivitas_panen_user` FOREIGN KEY (`id_user`) REFERENCES `user` (`id_user`);

  --
  -- Constraints for table `blok_lahan`
  --
  ALTER TABLE `blok_lahan`
    ADD CONSTRAINT `fk_blok_lahan_user` FOREIGN KEY (`id_user`) REFERENCES `user` (`id_user`);

  --
  -- Constraints for table `direksi`
  --
  ALTER TABLE `direksi`
    ADD CONSTRAINT `fk_direksi_user` FOREIGN KEY (`id_user`) REFERENCES `user` (`id_user`);

  --
  -- Constraints for table `evaluasi_panen`
  --
  ALTER TABLE `evaluasi_panen`
    ADD CONSTRAINT `fk_evaluasi_aktivitas` FOREIGN KEY (`aktivitas_panen_id_panen`) REFERENCES `aktivitas_panen` (`id_panen`),
    ADD CONSTRAINT `fk_evaluasi_panen_user` FOREIGN KEY (`id_user`) REFERENCES `user` (`id_user`);

  --
  -- Constraints for table `karet`
  --
  ALTER TABLE `karet`
    ADD CONSTRAINT `fk_karet_produk` FOREIGN KEY (`id_produk`) REFERENCES `produk_panen` (`id_produk`);

  --
  -- Constraints for table `petugas`
  --
  ALTER TABLE `petugas`
    ADD CONSTRAINT `fk_petugas_user` FOREIGN KEY (`id_user`) REFERENCES `user` (`id_user`);

  --
  -- Constraints for table `produk_panen`
  --
  ALTER TABLE `produk_panen`
    ADD CONSTRAINT `fk_produk_aktivitas` FOREIGN KEY (`aktivitas_panen_id_panen`) REFERENCES `aktivitas_panen` (`id_panen`);

  --
  -- Constraints for table `sawit`
  --
  ALTER TABLE `sawit`
    ADD CONSTRAINT `fk_sawit_produk` FOREIGN KEY (`id_produk`) REFERENCES `produk_panen` (`id_produk`);
  COMMIT;

  /*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
  /*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
  /*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;


  -- --------------------------------------------------------
  -- Table structure for table 
  iwayat_perubahan (Audit Log)
  -- --------------------------------------------------------

  CREATE TABLE IF NOT EXISTS 
  iwayat_perubahan (
    id_riwayat varchar(10) NOT NULL,
    waktu_perubahan datetime NOT NULL,
    jenis_tindakan varchar(50) NOT NULL,
    id_user varchar(50) NOT NULL,
    id_panen varchar(10) DEFAULT NULL,
    data_sebelum text,
    data_setelah text,
    PRIMARY KEY (id_riwayat)
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
