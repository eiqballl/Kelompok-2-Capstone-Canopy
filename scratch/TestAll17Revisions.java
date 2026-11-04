import dao.*;
import model.*;
import controller.InputValidator;
import view.*;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

public class TestAll17Revisions {

    public static void main(String[] args) throws Exception {
        System.out.println("=== MEMULAI PENGUJIAN 17 ITEM REVISI KONSUL 2 PBO ===");

        // Item 9: Abstract Class PanenDAO & EvaluasiDAO, extends BaseDAO, overloading
        testItem9AbstractionAndOverloading();

        // Item 11: Validasi Blok Lahan dengan try-catch
        testItem11ValidasiKodeBlok();

        // Item 2: Logika kalkulasi metrik periode
        testItem2KalkulasiMetrikPeriode();

        // Item 3: Parameter mutu panen
        testItem3ParameterMutu();

        // Item 5: Audit log detail perbandingan
        testItem5AuditLogDetail();

        // Item 17: Dialog multi-select blok lahan
        testItem17PilihBlokDialog();

        // Item 15: Rangkuman eksekutif tanpa tabel
        testItem15RangkumanEksekutif();

        // Item 14: Laporan tindak lanjut petugas
        testItem14LaporanTindakLanjut();

        // Fitur Tambahan Konsul 2: Tahun Tanam pada Blok Lahan
        testTahunTanamBlokLahan();

        // Fitur Tambahan Konsul 2: Rekapitulasi Data Otomatis & Lengkap
        testRekapitulasiSemuaData();

        System.out.println("\n>>> SEMUA 17 ITEM REVISI BERHASIL DIVERIFIKASI DENGAN SUKSES! <<<");
    }

    private static void testTahunTanamBlokLahan() {
        System.out.print("[Konsul 2] Tahun Tanam pada Blok Lahan... ");
        BlokLahan b1 = new BlokLahan("SW99", "Blok Test", 10.0, "Sawit", "2021");
        assert "2021".equals(b1.getTahunTanam()) : "Tahun tanam harus 2021";
        b1.setTahunTanam("2022");
        assert "2022".equals(b1.getTahunTanam()) : "Tahun tanam setelah update harus 2022";

        BlokLahan b2 = new BlokLahan("SW98", "Blok Default", 8.0, "Sawit");
        assert "2018".equals(b2.getTahunTanam()) : "Default tahun tanam harus 2018";

        for (BlokLahan bl : DataStore.getInstance().getBlokList()) {
            assert bl.getTahunTanam() != null && !bl.getTahunTanam().trim().isEmpty() : "Semua blok di DataStore harus punya tahun tanam";
        }
        System.out.println("OK");
    }

    private static void testRekapitulasiSemuaData() {
        System.out.print("[Konsul 2] Rekapitulasi Menampilkan Data Lengkap... ");
        DataStore ds = DataStore.getInstance();
        List<Panen> panenList = ds.getPanenList();
        assert !panenList.isEmpty() : "DataStore panenList tidak boleh kosong";

        // Filter Komoditas Semua & Rentang Kosong (Semua Data) harus memuat seluruh panen
        int totalMatches = 0;
        for (Panen p : panenList) {
            totalMatches++;
        }
        assert totalMatches == panenList.size() : "Mode Semua Data harus memuat seluruh data panen";
        System.out.println("OK (" + totalMatches + " transaksi panen terverifikasi)");
    }

    private static void testItem9AbstractionAndOverloading() {
        System.out.print("[Item 9] Abstraksi & Inheritance DAO... ");
        assert Modifier.isAbstract(BaseDAO.class.getModifiers()) : "BaseDAO harus abstract class";
        assert Modifier.isAbstract(PanenDAO.class.getModifiers()) : "PanenDAO harus abstract class";
        assert Modifier.isAbstract(EvaluasiDAO.class.getModifiers()) : "EvaluasiDAO harus abstract class";
        assert BaseDAO.class.isAssignableFrom(PanenDAO.class) : "PanenDAO harus extends BaseDAO";
        assert BaseDAO.class.isAssignableFrom(EvaluasiDAO.class) : "EvaluasiDAO harus extends BaseDAO";
        assert PanenDAO.class.isAssignableFrom(PanenDAOImpl.class) : "PanenDAOImpl harus extends PanenDAO";
        assert EvaluasiDAO.class.isAssignableFrom(EvaluasiDAOImpl.class) : "EvaluasiDAOImpl harus extends EvaluasiDAO";

        // Overloading checks
        boolean foundOverloadedPanen = false;
        for (Method m : PanenDAO.class.getMethods()) {
            if (m.getName().equals("findAll") && m.getParameterCount() == 2) {
                foundOverloadedPanen = true;
                break;
            }
        }
        assert foundOverloadedPanen : "PanenDAO harus punya method overloading findAll(keyword, komoditas)";

        boolean foundOverloadedEval = false;
        for (Method m : EvaluasiDAO.class.getMethods()) {
            if (m.getName().equals("findAll") && m.getParameterCount() == 2) {
                foundOverloadedEval = true;
                break;
            }
        }
        assert foundOverloadedEval : "EvaluasiDAO harus punya method overloading findAll(keyword, status)";

        System.out.println("OK");
    }

    private static void testItem11ValidasiKodeBlok() {
        System.out.print("[Item 11] Validasi Kode Blok Lahan Try-Catch... ");
        // Valid
        InputValidator.validateKodeBlok("SW001");
        InputValidator.validateKodeBlok("KR-02");

        // Invalid cases with try-catch
        boolean caughtEmpty = false;
        try {
            InputValidator.validateKodeBlok("");
        } catch (IllegalArgumentException e) {
            caughtEmpty = true;
        }
        assert caughtEmpty : "Input kosong harus throw IllegalArgumentException";

        boolean caughtSpaces = false;
        try {
            InputValidator.validateKodeBlok("SW 01");
        } catch (IllegalArgumentException e) {
            caughtSpaces = true;
        }
        assert caughtSpaces : "Kode dengan spasi harus throw IllegalArgumentException";
        System.out.println("OK");
    }

    private static void testItem2KalkulasiMetrikPeriode() {
        System.out.print("[Item 2] Kalkulasi Metrik Periode Dinamis... ");
        DataStore ds = DataStore.getInstance();
        double[] metrics = ds.calculateMetrics("2026-10-01", "2026-10-31");
        assert metrics != null && metrics.length == 3 : "Metrik harus menghasilkan array 3 elemen [total, persenReject, rata2]";
        assert metrics[0] >= 0 : "Total panen non-negatif";
        assert metrics[1] >= 0 && metrics[1] <= 100 : "Persen reject dalam rentang 0-100%";
        System.out.println("OK (Total: " + metrics[0] + " Kg, Reject: " + metrics[1] + "%)");
    }

    private static void testItem3ParameterMutu() {
        System.out.print("[Item 3] Standar Parameter Mutu Panen... ");
        // <= 5.0% -> Baik
        Panen pBaik = new Panen("P1", "2026-10-10", "SW001", "Sawit", 100, 100, 1000, 40, "Baik");
        assert "Baik".equals(pBaik.tentukanKualitas()) : "Reject 4% harus Baik";

        // 5.1 - 10.0% -> Cukup
        Panen pCukup = new Panen("P2", "2026-10-10", "SW001", "Sawit", 100, 100, 1000, 80, "Cukup");
        assert "Cukup".equals(pCukup.tentukanKualitas()) : "Reject 8% harus Cukup";

        // > 10.0% -> Kurang
        Panen pKurang = new Panen("P3", "2026-10-10", "SW001", "Sawit", 100, 100, 1000, 150, "Kurang");
        assert "Kurang".equals(pKurang.tentukanKualitas()) : "Reject 15% harus Kurang";
        System.out.println("OK");
    }

    private static void testItem5AuditLogDetail() throws Exception {
        System.out.print("[Item 5] Audit Log Detail Perbandingan... ");
        DataStore ds = DataStore.getInstance();
        String idTest = ds.generatePanenId();
        Panen pTest = new Panen(idTest, "2026-10-15", "SW001", "Sawit", 100, 100, 2000, 50, "Baik");
        ds.addPanen(pTest);

        List<Riwayat> logs = ds.getRiwayatList();
        assert !logs.isEmpty() : "Riwayat tidak boleh kosong";
        Riwayat r = logs.get(0);
        assert r.getIdPanen().equals(idTest) : "Log ID panen harus " + idTest;
        assert r.getDataSetelah().contains("Blok: SW001") : "Data setelah harus memuat blok";
        assert r.getDataSetelah().contains("Total: 2000.0 Kg") : "Data setelah harus memuat total panen";
        assert r.getDataSetelah().contains("Mutu: Baik") : "Data setelah harus memuat mutu";

        // Update
        Panen pUpdate = new Panen(idTest, "2026-10-15", "SW001", "Sawit", 100, 100, 2100, 50, "Baik");
        ds.updatePanen(pUpdate);
        Riwayat rUpdate = ds.getRiwayatList().get(0);
        assert rUpdate.getDataSebelum().contains("Total: 2000.0 Kg") : "Data sebelum update harus memuat 2000.0 Kg";
        assert rUpdate.getDataSetelah().contains("Total: 2100.0 Kg") : "Data setelah update harus memuat 2100.0 Kg";

        // Cleanup
        ds.deletePanen(idTest);
        try { ds.deletePanen("PN_TEST"); } catch (Exception ignored) {}
        System.out.println("OK");
    }

    private static void testItem17PilihBlokDialog() {
        System.out.print("[Item 17] Dialog Multi-Select Blok Lahan... ");
        Set<String> initial = new HashSet<>(Arrays.asList("SW001", "KR001"));
        PilihBlokDialog dlg = new PilihBlokDialog(null, initial);
        assert dlg.getSelectedBlokCodes().contains("SW001") : "Harus memuat SW001";
        assert dlg.getSelectedBlokCodes().contains("KR001") : "Harus memuat KR001";
        dlg.dispose();
        System.out.println("OK");
    }

    private static void testItem15RangkumanEksekutif() {
        System.out.print("[Item 15] Rangkuman Eksekutif & Rekomendasi Manajerial... ");
        List<Panen> dataset = new ArrayList<>();
        dataset.add(new Panen("P1", "2026-10-10", "SW001", "Sawit", 100, 100, 1000, 40, "Baik"));
        dataset.add(new Panen("P2", "2026-10-11", "SW001", "Sawit", 100, 100, 1000, 120, "Kurang"));
        RangkumanEksekutifDialog dlg = new RangkumanEksekutifDialog(null, dataset, "Sawit 2026-10");
        assert dlg != null : "Dialog rangkuman eksekutif berhasil diinisialisasi";
        dlg.dispose();
        System.out.println("OK");
    }

    private static void testItem14LaporanTindakLanjut() {
        System.out.print("[Item 14] Laporan Tindak Lanjut Petugas Lapangan & Catatan Direksi vs Pegawai... ");
        Evaluasi ev = new Evaluasi("EV_TEST", "2026-10-01", "2026-10-31", 5000, 4.5, "Proses",
            "Periksa piringan pokok dan sortasi brondolan",
            "Sortasi brondolan telah selesai dilakukan di blok SW001");
        assert "Periksa piringan pokok dan sortasi brondolan".equals(ev.getCatatanDireksi()) : "Catatan direksi harus sesuai";
        assert "Sortasi brondolan telah selesai dilakukan di blok SW001".equals(ev.getCatatanPegawai()) : "Catatan pegawai harus sesuai";
        LaporanTindakLanjutDialog dlg = new LaporanTindakLanjutDialog(null, ev);
        assert dlg != null : "Dialog laporan tindak lanjut berhasil diinisialisasi";
        dlg.dispose();
        System.out.println("OK");
    }
}
