package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Date;
import java.util.List;
import config.Koneksi;

public class DataStore {
    private static final DataStore INSTANCE = new DataStore();
    public static DataStore getInstance() { return INSTANCE; }

    private String currentUser = "admin_sawit";
    private boolean offlineMode = true;

    public boolean isOfflineMode() { return offlineMode; }

    private final List<BlokLahan> blokList = new ArrayList<>();
    private final List<Pegawai> pegawaiList = new ArrayList<>();
    private final List<Panen> panenList = new ArrayList<>();
    private final List<Evaluasi> evaluasiList = new ArrayList<>();
    private final List<Riwayat> riwayatList = new ArrayList<>();

    private int panenCounter = 1;
    private int evalCounter = 1;
    private int riwayatCounter = 1;

    public DataStore() {
        initSampleData();
        loadFromDatabase();
    }

    private void initSampleData() {
        blokList.add(new BlokLahan("SW001", "Blok Sawit A01", 12.5, "Sawit", "2018"));
        blokList.add(new BlokLahan("SW002", "Blok Sawit A02", 10.0, "Sawit", "2019"));
        blokList.add(new BlokLahan("KR001", "Blok Karet B01", 8.0, "Karet", "2015"));
        blokList.add(new BlokLahan("KR002", "Blok Karet B02", 15.0, "Karet", "2016"));

        pegawaiList.add(new Pegawai("U001", "Ahmad Fauzi", "ahmad.admin", "admin123", "081234560001", "Admin"));
        pegawaiList.add(new Pegawai("U003", "Budi Santoso", "budi.direksi", "direksi123", "081234560003", "Direksi"));
        pegawaiList.add(new Pegawai("U006", "Andi Saputra", "andi.petugas", "petugas123", "081234560006", "Petugas"));

        addPanenInternal(new Panen("PN001", "2026-10-25", "SW001 - Blok Sawit A01", "Sawit", 100, 250, 1250.0, 45.0, "Baik"));
        addPanenInternal(new Panen("PN002", "2026-10-26", "KR001 - Blok Karet B01", "Karet", 680, 28, 680.0, 20.0, "Cukup"));
        addPanenInternal(new Panen("PN003", "2026-10-27", "SW002 - Blok Sawit A02", "Sawit", 80, 180, 980.0, 35.0, "Baik"));

        evaluasiList.add(new Evaluasi("EV001", "2026-10-01", "2026-10-31", 2910.0, 3.44, "Selesai",
            "Pertahankan produktivitas dan cek sanitasi pokok.",
            "Perawatan blok SW001 dan sortasi brondolan telah selesai dilaksanakan."));

        riwayatList.add(new Riwayat("RW001", "2026-10-25 08:30:15", "Tambah Data", "budi_petugas", "PN001", "-", "Input awal panen Sawit SW001"));
        riwayatList.add(new Riwayat("RW002", "2026-10-26 14:10:22", "Tambah Data", "budi_petugas", "PN002", "-", "Input awal panen Karet KR001"));
        riwayatList.add(new Riwayat("RW003", "2026-10-27 16:45:00", "Pembaruan Data", "admin_sawit", "PN001", "Total: 1200 Kg", "Total: 1250 Kg"));
    }

    private void addPanenInternal(Panen p) {
        panenList.add(p);
        panenCounter++;
    }

    private boolean hasSawitBeratCol(Connection c) {
        try (ResultSet rs = c.getMetaData().getColumns(null, null, "sawit", "jumlah_berat_tandan")) {
            return rs.next();
        } catch (Exception e) {
            return false;
        }
    }

    private boolean hasEvaluasiTglAwal(Connection c) {
        try (ResultSet rs = c.getMetaData().getColumns(null, null, "evaluasi_panen", "tanggal_awal")) {
            return rs.next();
        } catch (Exception e) {
            return false;
        }
    }

    public synchronized BlokLahan findBlokByKode(String kodeOrCombo) {
        if (kodeOrCombo == null) return null;
        String raw = kodeOrCombo.trim();
        String kode = raw.contains(" - ") ? raw.split(" - ")[0].trim() : raw;
        for (BlokLahan b : blokList) {
            if (b.getKodeBlok().equalsIgnoreCase(kode)) {
                return b;
            }
        }
        return null;
    }

    private void loadFromDatabase() {
        try {
            Connection c = Koneksi.getKoneksi();
            if (c == null) {
                offlineMode = true;
                return;
            }
            offlineMode = false;

            List<BlokLahan> tempBlok = new ArrayList<>();
            List<Pegawai> tempPegawai = new ArrayList<>();
            List<Panen> tempPanen = new ArrayList<>();
            List<Evaluasi> tempEvaluasi = new ArrayList<>();
            List<Riwayat> tempRiwayat = new ArrayList<>();

            ensureBlokLahanTahunTanam(c);
            Statement st = c.createStatement();

            ResultSet rsBlok = st.executeQuery("SELECT kode_blok_lahan, nama_blok_lahan, luas_blok_lahan, komoditas, tahun_tanam FROM blok_lahan");
            while (rsBlok.next()) {
                String kode = rsBlok.getString("kode_blok_lahan");
                String nama = rsBlok.getString("nama_blok_lahan");
                double luas = rsBlok.getDouble("luas_blok_lahan");
                String kom = rsBlok.getString("komoditas");
                String thn = "2018";
                try {
                    thn = rsBlok.getString("tahun_tanam");
                    if (thn == null || thn.trim().isEmpty()) thn = "2018";
                } catch (Exception ignored) {}
                tempBlok.add(new BlokLahan(kode, nama, luas, kom, thn));
            }
            rsBlok.close();

            ResultSet rsUser = st.executeQuery(
                "SELECT u.id_user, u.nama, u.username, u.password, u.no_telepon, " +
                "CASE WHEN a.id_user IS NOT NULL THEN 'Admin' " +
                "     WHEN d.id_user IS NOT NULL THEN 'Direksi' " +
                "     ELSE 'Petugas' END AS role, " +
                "COALESCE(a.bagian_administrasi, d.jabatan_direksi, p.wilayah_penugasan, '') AS detail_role " +
                "FROM user u " +
                "LEFT JOIN admin a ON u.id_user = a.id_user " +
                "LEFT JOIN direksi d ON u.id_user = d.id_user " +
                "LEFT JOIN petugas p ON u.id_user = p.id_user"
            );
            while (rsUser.next()) {
                tempPegawai.add(new Pegawai(
                    rsUser.getString("id_user"),
                    rsUser.getString("nama"),
                    rsUser.getString("username"),
                    rsUser.getString("password"),
                    rsUser.getString("no_telepon"),
                    rsUser.getString("role"),
                    rsUser.getString("detail_role")
                ));
            }
            rsUser.close();

            boolean hasBeratSawit = hasSawitBeratCol(c);
            String colSawit1 = hasBeratSawit ? "sw.jumlah_berat_tandan" : "sw.jumlah_tandan";
            String colSawit2 = hasBeratSawit ? "sw.jumlah_berat_brondolan" : "sw.jumlah_brondolan";

            ResultSet rsPanen = st.executeQuery(
                "SELECT ap.id_panen, ap.tanggal_panen, ap.total_jumlah_panen, ap.hasil_panen_tidak_layak, ap.kualitas_panen, " +
                "       ap.kode_blok_lahan, bl.nama_blok_lahan, bl.komoditas, " +
                "       pp.nama_produk, pp.tahun_tanam, " +
                "       " + colSawit1 + " AS tandan_val, " + colSawit2 + " AS brondolan_val, " +
                "       kr.kadar_air_karet, kr.berat_kotor_karet " +
                "FROM aktivitas_panen ap " +
                "LEFT JOIN blok_lahan bl ON ap.kode_blok_lahan = bl.kode_blok_lahan " +
                "LEFT JOIN produk_panen pp ON pp.aktivitas_panen_id_panen = ap.id_panen " +
                "LEFT JOIN sawit sw ON sw.id_produk = pp.id_produk " +
                "LEFT JOIN karet kr ON kr.id_produk = pp.id_produk"
            );
            while (rsPanen.next()) {
                String idP = rsPanen.getString("id_panen");
                String tgl = rsPanen.getString("tanggal_panen");
                String kodeB = rsPanen.getString("kode_blok_lahan");
                String namaB = rsPanen.getString("nama_blok_lahan");
                String blokStr = (namaB != null && !namaB.isEmpty()) ? (kodeB + " - " + namaB) : kodeB;
                String kom = rsPanen.getString("komoditas");
                if (kom == null || kom.isEmpty()) kom = "Sawit";
                int in1 = "Sawit".equalsIgnoreCase(kom) ? rsPanen.getInt("tandan_val") : rsPanen.getInt("berat_kotor_karet");
                int in2 = "Sawit".equalsIgnoreCase(kom) ? rsPanen.getInt("brondolan_val") : rsPanen.getInt("kadar_air_karet");
                double tot = rsPanen.getDouble("total_jumlah_panen");
                double rej = rsPanen.getDouble("hasil_panen_tidak_layak");
                String qual = rsPanen.getString("kualitas_panen");
                tempPanen.add(new Panen(idP, tgl, blokStr, kom, in1, in2, tot, rej, qual));
                try {
                    int num = Integer.parseInt(idP.replaceAll("\\D+", ""));
                    if (num >= panenCounter) panenCounter = num + 1;
                } catch (Exception ignored) {}
            }
            rsPanen.close();

            ensureEvaluasiColumns(c);
            boolean hasTglAwal = hasEvaluasiTglAwal(c);
            String evalQuery = hasTglAwal
                ? "SELECT id_evaluasi, COALESCE(tanggal_awal, tanggal_evaluasi) AS tanggal_awal, tanggal_evaluasi, " +
                  "       status, catatan_tindak_lanjut, catatan_evaluasi FROM evaluasi_panen"
                : "SELECT id_evaluasi, tanggal_evaluasi AS tanggal_awal, tanggal_evaluasi, " +
                  "       status, catatan_tindak_lanjut, catatan_evaluasi FROM evaluasi_panen";

            ResultSet rsEval = st.executeQuery(evalQuery);
            while (rsEval.next()) {
                String idEv = rsEval.getString("id_evaluasi");
                String tglAwal = rsEval.getString("tanggal_awal");
                String tglAkhir = rsEval.getString("tanggal_evaluasi");
                String status = normalizeStatus(rsEval.getString("status"));
                String cPegawai = rsEval.getString("catatan_tindak_lanjut");
                String cDireksi = rsEval.getString("catatan_evaluasi");
                double[] metrics = calculateMetrics(tglAwal, tglAkhir);
                tempEvaluasi.add(new Evaluasi(idEv, tglAwal, tglAkhir, metrics[0], metrics[1], status, cDireksi, cPegawai));
                try {
                    int num = Integer.parseInt(idEv.replaceAll("\\D+", ""));
                    if (num >= evalCounter) evalCounter = num + 1;
                } catch (Exception ignored) {}
            }
            rsEval.close();

            try {
                ResultSet rsHistory = st.executeQuery(
                    "SELECT id_riwayat, waktu_perubahan, jenis_tindakan, id_user, id_panen, data_sebelum, data_setelah FROM riwayat_perubahan ORDER BY waktu_perubahan DESC, id_riwayat DESC"
                );
                while (rsHistory.next()) {
                    String id = rsHistory.getString("id_riwayat");
                    tempRiwayat.add(new Riwayat(id, rsHistory.getString("waktu_perubahan"),
                            rsHistory.getString("jenis_tindakan"), rsHistory.getString("id_user"),
                            rsHistory.getString("id_panen"), rsHistory.getString("data_sebelum"),
                            rsHistory.getString("data_setelah")));
                    try {
                        int num = Integer.parseInt(id.replaceAll("\\D+", ""));
                        if (num >= riwayatCounter) riwayatCounter = num + 1;
                    } catch (Exception ignored) {}
                }
                rsHistory.close();
            } catch (Exception ignored) {}

            st.close();

            if (!tempBlok.isEmpty()) {
                blokList.clear();
                blokList.addAll(tempBlok);
            }
            if (!tempPegawai.isEmpty()) {
                pegawaiList.clear();
                pegawaiList.addAll(tempPegawai);
            }
            if (!tempPanen.isEmpty()) {
                panenList.clear();
                panenList.addAll(tempPanen);
            }
            if (!tempEvaluasi.isEmpty()) {
                evaluasiList.clear();
                evaluasiList.addAll(tempEvaluasi);
            }
            if (!tempRiwayat.isEmpty()) {
                riwayatList.clear();
                riwayatList.addAll(tempRiwayat);
            }

        } catch (Exception e) {
            System.err.println("Gagal memuat database canopy: " + e.getMessage());
        }
    }


    public synchronized Pegawai findPegawai(String userOrId, String password) {
        if (userOrId == null || password == null) return null;
        String u = userOrId.trim();
        String p = password.trim();
        for (Pegawai peg : pegawaiList) {
            if ((peg.getUsername().equalsIgnoreCase(u)
                    || peg.getIdPegawai().equalsIgnoreCase(u)
                    || peg.getNamaPegawai().equalsIgnoreCase(u))
                    && (peg.getPassword().equals(password) || peg.getPassword().trim().equals(p))) {
                return peg;
            }
        }
        return null;
    }

    public String getCurrentUser() { return currentUser; }
    public void setCurrentUser(String currentUser) { this.currentUser = currentUser; }

    public synchronized List<BlokLahan> getBlokList() { return new ArrayList<>(blokList); }
    public synchronized void addBlok(BlokLahan b) {
        blokList.add(b);
        try {
            Connection c = Koneksi.getKoneksi();
            if (c != null) {
                ensureRiwayatTable(c);
                ensureBlokLahanTahunTanam(c);
                PreparedStatement ps = c.prepareStatement(
                    "INSERT INTO blok_lahan (kode_blok_lahan, nama_blok_lahan, luas_blok_lahan, komoditas, tahun_tanam) VALUES (?, ?, ?, ?, ?) " +
                    "ON DUPLICATE KEY UPDATE nama_blok_lahan=?, luas_blok_lahan=?, komoditas=?, tahun_tanam=?"
                );
                ps.setString(1, b.getKodeBlok());
                ps.setString(2, b.getNamaLahan());
                ps.setDouble(3, b.getLuasLahan());
                ps.setString(4, b.getKomoditas());
                ps.setString(5, b.getTahunTanam());
                ps.setString(6, b.getNamaLahan());
                ps.setDouble(7, b.getLuasLahan());
                ps.setString(8, b.getKomoditas());
                ps.setString(9, b.getTahunTanam());
                ps.executeUpdate();
                ps.close();
            }
        } catch (Exception e) {
            System.err.println("Gagal simpan blok: " + e.getMessage());
        }
    }
    public synchronized boolean updateBlok(BlokLahan b) {
        for (int i = 0; i < blokList.size(); i++) {
            if (blokList.get(i).getKodeBlok().equalsIgnoreCase(b.getKodeBlok())) {
                blokList.set(i, b);
                try {
                    Connection c = Koneksi.getKoneksi();
                    if (c != null) {
                        ensureBlokLahanTahunTanam(c);
                        PreparedStatement ps = c.prepareStatement(
                            "UPDATE blok_lahan SET nama_blok_lahan=?, luas_blok_lahan=?, komoditas=?, tahun_tanam=? WHERE kode_blok_lahan=?"
                        );
                        ps.setString(1, b.getNamaLahan());
                        ps.setDouble(2, b.getLuasLahan());
                        ps.setString(3, b.getKomoditas());
                        ps.setString(4, b.getTahunTanam());
                        ps.setString(5, b.getKodeBlok());
                        ps.executeUpdate();
                        ps.close();
                    }
                } catch (Exception e) {
                    System.err.println("Gagal update blok: " + e.getMessage());
                }
                return true;
            }
        }
        return false;
    }
    public synchronized boolean deleteBlok(String kode) throws Exception {
        Connection c = Koneksi.getKoneksi();
        if (c != null) {
            try (PreparedStatement ps = c.prepareStatement("DELETE FROM blok_lahan WHERE kode_blok_lahan = ?")) {
                ps.setString(1, kode);
                ps.executeUpdate();
            }
        }
        return blokList.removeIf(b -> b.getKodeBlok().equalsIgnoreCase(kode));
    }

    public synchronized List<Pegawai> getPegawaiList() { return new ArrayList<>(pegawaiList); }
    public synchronized String generatePegawaiId() {
        int maxId = 0;
        for (Pegawai p : pegawaiList) {
            String id = p.getIdPegawai();
            if (id != null && id.toUpperCase().startsWith("U")) {
                try {
                    int num = Integer.parseInt(id.substring(1));
                    if (num > maxId) maxId = num;
                } catch (NumberFormatException ignored) {}
            }
        }
        return String.format("U%03d", maxId + 1);
    }
    public synchronized Pegawai findPegawaiById(String id) {
        for (Pegawai pegawai : pegawaiList) {
            if (pegawai.getIdPegawai().equalsIgnoreCase(id)) return pegawai;
        }
        return null;
    }
    public synchronized void addPegawai(Pegawai p) {
        pegawaiList.add(p);
        try {
            Connection c = Koneksi.getKoneksi();
            if (c != null) {
                ensureRiwayatTable(c);
                PreparedStatement ps = c.prepareStatement(
                    "INSERT INTO user (id_user, nama, username, password, no_telepon) VALUES (?, ?, ?, ?, ?) " +
                    "ON DUPLICATE KEY UPDATE nama=?, username=?, password=?, no_telepon=?"
                );
                ps.setString(1, p.getIdPegawai());
                ps.setString(2, p.getNamaPegawai());
                ps.setString(3, p.getUsername());
                ps.setString(4, p.getPassword());
                ps.setString(5, p.getNoTelp());
                ps.setString(6, p.getNamaPegawai());
                ps.setString(7, p.getUsername());
                ps.setString(8, p.getPassword());
                ps.setString(9, p.getNoTelp());
                ps.executeUpdate();
                ps.close();
                syncRole(c, p.getIdPegawai(), p.getRole(), p.getDetailRole());
            }
        } catch (Exception e) {
            System.err.println("Gagal simpan user: " + e.getMessage());
        }
    }
    public synchronized boolean updatePegawai(Pegawai p) {
        for (int i = 0; i < pegawaiList.size(); i++) {
            if (pegawaiList.get(i).getIdPegawai().equalsIgnoreCase(p.getIdPegawai()) ||
                pegawaiList.get(i).getUsername().equalsIgnoreCase(p.getUsername())) {
                pegawaiList.set(i, p);
                try {
                    Connection c = Koneksi.getKoneksi();
                    if (c != null) {
                        PreparedStatement ps = c.prepareStatement(
                            "UPDATE user SET nama=?, username=?, password=?, no_telepon=? WHERE id_user=?"
                        );
                        ps.setString(1, p.getNamaPegawai());
                        ps.setString(2, p.getUsername());
                        ps.setString(3, p.getPassword());
                        ps.setString(4, p.getNoTelp());
                        ps.setString(5, p.getIdPegawai());
                        ps.executeUpdate();
                        ps.close();
                        syncRole(c, p.getIdPegawai(), p.getRole(), p.getDetailRole());
                    }
                } catch (Exception e) {
                    System.err.println("Gagal update user: " + e.getMessage());
                }
                return true;
            }
        }
        return false;
    }
    private void syncRole(Connection c, String userId, String role, String detail) throws Exception {
        for (String table : new String[]{"admin", "direksi", "petugas"}) {
            try (PreparedStatement ps = c.prepareStatement("DELETE FROM " + table + " WHERE id_user=?")) {
                ps.setString(1, userId);
                ps.executeUpdate();
            }
        }
        String table = "petugas";
        String col = "wilayah_penugasan";
        if ("Admin".equalsIgnoreCase(role)) {
            table = "admin";
            col = "bagian_administrasi";
        } else if ("Direksi".equalsIgnoreCase(role)) {
            table = "direksi";
            col = "jabatan_direksi";
        }
        try (PreparedStatement ps = c.prepareStatement("INSERT INTO " + table + " (id_user, " + col + ") VALUES (?, ?)")) {
            ps.setString(1, userId);
            ps.setString(2, (detail != null && !detail.trim().isEmpty()) ? detail.trim() : null);
            ps.executeUpdate();
        }
    }

    public synchronized boolean deletePegawai(String idOrUser) throws Exception {
        Connection c = Koneksi.getKoneksi();
        if (c != null) {
            boolean oldAuto = c.getAutoCommit();
            try {
                c.setAutoCommit(false);
                String resolvedId = idOrUser;
                for (Pegawai p : pegawaiList) {
                    if (p.getIdPegawai().equalsIgnoreCase(idOrUser) || p.getUsername().equalsIgnoreCase(idOrUser)) {
                        resolvedId = p.getIdPegawai();
                        break;
                    }
                }
                try (PreparedStatement ps = c.prepareStatement("UPDATE aktivitas_panen SET id_user = NULL WHERE id_user = ?")) {
                    ps.setString(1, resolvedId);
                    ps.executeUpdate();
                }
                try (PreparedStatement ps = c.prepareStatement("UPDATE blok_lahan SET id_user = NULL WHERE id_user = ?")) {
                    ps.setString(1, resolvedId);
                    ps.executeUpdate();
                }
                for (String t : new String[]{"admin", "direksi", "petugas"}) {
                    try (PreparedStatement ps = c.prepareStatement("DELETE FROM " + t + " WHERE id_user = ?")) {
                        ps.setString(1, resolvedId);
                        ps.executeUpdate();
                    }
                }
                try (PreparedStatement ps = c.prepareStatement("DELETE FROM user WHERE id_user = ? OR username = ?")) {
                    ps.setString(1, resolvedId);
                    ps.setString(2, idOrUser);
                    ps.executeUpdate();
                }
                c.commit();
            } catch (Exception ex) {
                c.rollback();
                throw ex;
            } finally {
                c.setAutoCommit(oldAuto);
            }
        }
        return pegawaiList.removeIf(p -> p.getIdPegawai().equalsIgnoreCase(idOrUser) || p.getUsername().equalsIgnoreCase(idOrUser));
    }

    public synchronized List<Panen> getPanenList() { return new ArrayList<>(panenList); }
    public synchronized String generatePanenId() {
        return String.format("PN%03d", panenCounter++);
    }
        private String formatPanenAuditDetail(Panen p) {
        if (p == null) return "-";
        return String.format(Locale.US, "Blok: %s | Tgl: %s | Komoditas: %s | Total: %.1f Kg | Reject: %.1f Kg | Mutu: %s",
                p.getBlokLahan(), p.getTanggal(), p.getKomoditas(), p.getTotalPanen(), p.getTidakLayak(), p.getKualitas());
    }

    public synchronized void addPanen(Panen p) {
        panenList.add(p);
        logRiwayat("Tambah Data", p.getIdPanen(), "-", formatPanenAuditDetail(p));
        try {
            Connection c = Koneksi.getKoneksi();
            if (c != null) {
                ensureRiwayatTable(c);
                saveAktivitasPanen(c, p, true);
                saveProdukPanen(c, p);
            }
        } catch (Exception e) {
            System.err.println("Gagal simpan panen: " + e.getMessage());
        }
    }

    private void saveAktivitasPanen(Connection c, Panen p, boolean insert) throws Exception {
        String kodeBlok = kodeBlok(p.getBlokLahan());
        String sql = insert
            ? "INSERT INTO aktivitas_panen (id_panen, tanggal_panen, total_jumlah_panen, hasil_panen_tidak_layak, kualitas_panen, kode_blok_lahan, id_user) VALUES (?, ?, ?, ?, ?, ?, ?) " +
              "ON DUPLICATE KEY UPDATE tanggal_panen=?, total_jumlah_panen=?, hasil_panen_tidak_layak=?, kualitas_panen=?, kode_blok_lahan=?, id_user=?"
            : "UPDATE aktivitas_panen SET tanggal_panen=?, total_jumlah_panen=?, hasil_panen_tidak_layak=?, kualitas_panen=?, kode_blok_lahan=? WHERE id_panen=?";
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            if (insert) {
                ps.setString(1, p.getIdPanen());
                ps.setString(2, p.getTanggal());
                ps.setDouble(3, p.getTotalPanen());
                ps.setInt(4, (int) Math.round(p.getTidakLayak()));
                ps.setString(5, p.getKualitas());
                ps.setString(6, kodeBlok);
                ps.setString(7, getCurrentUserId());
                ps.setString(8, p.getTanggal());
                ps.setDouble(9, p.getTotalPanen());
                ps.setInt(10, (int) Math.round(p.getTidakLayak()));
                ps.setString(11, p.getKualitas());
                ps.setString(12, kodeBlok);
                ps.setString(13, getCurrentUserId());
            } else {
                ps.setString(1, p.getTanggal());
                ps.setDouble(2, p.getTotalPanen());
                ps.setInt(3, (int) Math.round(p.getTidakLayak()));
                ps.setString(4, p.getKualitas());
                ps.setString(5, kodeBlok);
                ps.setString(6, p.getIdPanen());
            }
            ps.executeUpdate();
        }
    }

    private void saveProdukPanen(Connection c, Panen p) throws Exception {
        String idProduk = produkId(p.getIdPanen());
        try (PreparedStatement ps = c.prepareStatement(
                "INSERT INTO produk_panen (id_produk, aktivitas_panen_id_panen, nama_produk, tahun_tanam) VALUES (?, ?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE aktivitas_panen_id_panen=?, nama_produk=?, tahun_tanam=?")) {
            ps.setString(1, idProduk);
            ps.setString(2, p.getIdPanen());
            ps.setString(3, p.getKomoditas());
            ps.setString(4, null);
            ps.setString(5, p.getIdPanen());
            ps.setString(6, p.getKomoditas());
            ps.setString(7, null);
            ps.executeUpdate();
        }
        try (PreparedStatement ps = c.prepareStatement("DELETE FROM sawit WHERE id_produk = ?")) {
            ps.setString(1, idProduk);
            ps.executeUpdate();
        }
        try (PreparedStatement ps = c.prepareStatement("DELETE FROM karet WHERE id_produk = ?")) {
            ps.setString(1, idProduk);
            ps.executeUpdate();
        }
        if ("Sawit".equalsIgnoreCase(p.getKomoditas())) {
            boolean hasBerat = hasSawitBeratCol(c);
            String col1 = hasBerat ? "jumlah_berat_tandan" : "jumlah_tandan";
            String col2 = hasBerat ? "jumlah_berat_brondolan" : "jumlah_brondolan";
            try (PreparedStatement ps = c.prepareStatement(
                    "INSERT INTO sawit (id_produk, " + col1 + ", " + col2 + ") VALUES (?, ?, ?)")) {
                ps.setString(1, idProduk);
                ps.setInt(2, (int) Math.round(p.getInput1()));
                ps.setInt(3, (int) Math.round(p.getInput2()));
                ps.executeUpdate();
            }
        } else {
            try (PreparedStatement ps = c.prepareStatement(
                    "INSERT INTO karet (id_produk, kadar_air_karet, berat_kotor_karet) VALUES (?, ?, ?)")) {
                ps.setString(1, idProduk);
                ps.setInt(2, (int) Math.round(p.getInput2()));
                ps.setInt(3, (int) Math.round(p.getInput1()));
                ps.executeUpdate();
            }
        }
    }

    private static String kodeBlok(String blok) {
        String kode = blok == null ? "" : blok;
        if (kode.contains(" - ")) kode = kode.split(" - ")[0].trim();
        return kode;
    }

    private static String produkId(String idPanen) {
        String digits = idPanen == null ? "" : idPanen.replaceAll("\\D+", "");
        if (digits.isEmpty()) digits = "0";
        return String.format("PR%03d", Integer.parseInt(digits));
    }

    public synchronized String getCurrentUserId() {
        for (Pegawai p : pegawaiList) {
            if (p.getUsername().equalsIgnoreCase(currentUser)) return p.getIdPegawai();
        }
        for (Pegawai p : pegawaiList) {
            if ("Admin".equalsIgnoreCase(p.getRole())) return p.getIdPegawai();
        }
        return pegawaiList.isEmpty() ? "U001" : pegawaiList.get(0).getIdPegawai();
    }
    public synchronized boolean updatePanen(Panen p) {
        for (int i = 0; i < panenList.size(); i++) {
            if (panenList.get(i).getIdPanen().equalsIgnoreCase(p.getIdPanen())) {
                Panen old = panenList.get(i);
                String before = formatPanenAuditDetail(old);
                String after = formatPanenAuditDetail(p);
                panenList.set(i, p);
                logRiwayat("Pembaruan Data", p.getIdPanen(), before, after);
                try {
                    Connection c = Koneksi.getKoneksi();
                    if (c != null) {
                        saveAktivitasPanen(c, p, false);
                        saveProdukPanen(c, p);
                    }
                } catch (Exception e) {
                    System.err.println("Gagal update panen: " + e.getMessage());
                }
                return true;
            }
        }
        return false;
    }
    public synchronized boolean deletePanen(String idPanen) throws Exception {
        Connection c = Koneksi.getKoneksi();
        if (c != null) {
            boolean oldAuto = c.getAutoCommit();
            try {
                c.setAutoCommit(false);
                String idProduk = produkId(idPanen);
                try (PreparedStatement ps = c.prepareStatement("DELETE FROM sawit WHERE id_produk = ?")) {
                    ps.setString(1, idProduk);
                    ps.executeUpdate();
                }
                try (PreparedStatement ps = c.prepareStatement("DELETE FROM karet WHERE id_produk = ?")) {
                    ps.setString(1, idProduk);
                    ps.executeUpdate();
                }
                try (PreparedStatement ps = c.prepareStatement("DELETE FROM produk_panen WHERE aktivitas_panen_id_panen = ? OR id_produk = ?")) {
                    ps.setString(1, idPanen);
                    ps.setString(2, idProduk);
                    ps.executeUpdate();
                }
                try (PreparedStatement ps = c.prepareStatement("UPDATE evaluasi_panen SET aktivitas_panen_id_panen = NULL WHERE aktivitas_panen_id_panen = ?")) {
                    ps.setString(1, idPanen);
                    ps.executeUpdate();
                }
                try (PreparedStatement ps = c.prepareStatement("UPDATE aktivitas_panen SET id_evaluasi = NULL WHERE id_panen = ?")) {
                    ps.setString(1, idPanen);
                    ps.executeUpdate();
                }
                try (PreparedStatement ps = c.prepareStatement("DELETE FROM riwayat_perubahan WHERE id_panen = ?")) {
                    ps.setString(1, idPanen);
                    ps.executeUpdate();
                }
                try (PreparedStatement ps = c.prepareStatement("DELETE FROM aktivitas_panen WHERE id_panen = ?")) {
                    ps.setString(1, idPanen);
                    ps.executeUpdate();
                }
                c.commit();
            } catch (Exception ex) {
                c.rollback();
                throw ex;
            } finally {
                c.setAutoCommit(oldAuto);
            }
        }
        for (int i = 0; i < panenList.size(); i++) {
            if (panenList.get(i).getIdPanen().equalsIgnoreCase(idPanen)) {
                Panen old = panenList.remove(i);
                logRiwayat("Hapus Data", idPanen, formatPanenAuditDetail(old), "Dihapus permanen dari sistem");
                return true;
            }
        }
        return false;
    }

    public synchronized List<Evaluasi> getEvaluasiList() { return new ArrayList<>(evaluasiList); }
    public synchronized String generateEvalId() {
        return String.format("EV%03d", evalCounter++);
    }
    public synchronized void addEvaluasi(Evaluasi ev) {
        evaluasiList.add(ev);
        try {
            Connection c = Koneksi.getKoneksi();
            if (c != null) {
                ensureRiwayatTable(c);
                boolean hasTglAwal = hasEvaluasiTglAwal(c);
                if (hasTglAwal) {
                    try (PreparedStatement ps = c.prepareStatement(
                            "INSERT INTO evaluasi_panen (id_evaluasi, tanggal_awal, tanggal_evaluasi, status, catatan_tindak_lanjut, id_user, catatan_evaluasi) VALUES (?, ?, ?, ?, ?, ?, ?) " +
                            "ON DUPLICATE KEY UPDATE tanggal_awal=?, tanggal_evaluasi=?, status=?, catatan_tindak_lanjut=?, id_user=?, catatan_evaluasi=?")) {
                        ps.setString(1, ev.getIdEval());
                        ps.setString(2, ev.getTglAwal());
                        ps.setString(3, ev.getTglAkhir());
                        ps.setString(4, normalizeStatus(ev.getStatus()));
                        ps.setString(5, catatanPegawai(ev));
                        ps.setString(6, getCurrentUserId());
                        ps.setString(7, catatanDireksi(ev));
                        ps.setString(8, ev.getTglAwal());
                        ps.setString(9, ev.getTglAkhir());
                        ps.setString(10, normalizeStatus(ev.getStatus()));
                        ps.setString(11, catatanPegawai(ev));
                        ps.setString(12, getCurrentUserId());
                        ps.setString(13, catatanDireksi(ev));
                        ps.executeUpdate();
                    }
                } else {
                    try (PreparedStatement ps = c.prepareStatement(
                            "INSERT INTO evaluasi_panen (id_evaluasi, tanggal_evaluasi, status, catatan_tindak_lanjut, id_user, catatan_evaluasi) VALUES (?, ?, ?, ?, ?, ?) " +
                            "ON DUPLICATE KEY UPDATE tanggal_evaluasi=?, status=?, catatan_tindak_lanjut=?, id_user=?, catatan_evaluasi=?")) {
                        ps.setString(1, ev.getIdEval());
                        ps.setString(2, ev.getTglAkhir());
                        ps.setString(3, normalizeStatus(ev.getStatus()));
                        ps.setString(4, catatanPegawai(ev));
                        ps.setString(5, getCurrentUserId());
                        ps.setString(6, catatanDireksi(ev));
                        ps.setString(7, ev.getTglAkhir());
                        ps.setString(8, normalizeStatus(ev.getStatus()));
                        ps.setString(9, catatanPegawai(ev));
                        ps.setString(10, getCurrentUserId());
                        ps.setString(11, catatanDireksi(ev));
                        ps.executeUpdate();
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Gagal simpan evaluasi: " + e.getMessage());
        }
    }

    public static String normalizeStatus(String status) {
        if (status == null) return "Proses";
        String value = status.trim();
        if (value.equalsIgnoreCase("Selesai") || value.toLowerCase().contains("selesai") || value.toLowerCase().contains("sesuai")) {
            return "Selesai";
        }
        if (value.equalsIgnoreCase("Proses") || value.toLowerCase().contains("proses") || value.toLowerCase().contains("peningkatan")) {
            return "Proses";
        }
        if (value.equalsIgnoreCase("Pending") || value.toLowerCase().contains("pending") || value.toLowerCase().contains("tunda")) {
            return "Pending";
        }
        return "Proses";
    }

    private static String catatanDireksi(Evaluasi ev) {
        String catatan = ev.getCatatanDireksi() == null ? "" : ev.getCatatanDireksi().trim();
        return catatan.isEmpty() ? "-" : catatan;
    }

    private static String catatanPegawai(Evaluasi ev) {
        String catatan = ev.getCatatanPegawai() == null ? "" : ev.getCatatanPegawai().trim();
        return catatan.isEmpty() ? "-" : catatan;
    }

    private static String catatanEvaluasi(Evaluasi ev) {
        return catatanDireksi(ev);
    }
    public synchronized boolean updateEvaluasi(Evaluasi ev) {
        for (int i = 0; i < evaluasiList.size(); i++) {
            if (evaluasiList.get(i).getIdEval().equalsIgnoreCase(ev.getIdEval())) {
                evaluasiList.set(i, ev);
                try {
                    Connection c = Koneksi.getKoneksi();
                    if (c != null) {
                        boolean hasTglAwal = hasEvaluasiTglAwal(c);
                        if (hasTglAwal) {
                            try (PreparedStatement ps = c.prepareStatement(
                                    "UPDATE evaluasi_panen SET tanggal_awal=?, tanggal_evaluasi=?, status=?, catatan_tindak_lanjut=?, id_user=?, catatan_evaluasi=? WHERE id_evaluasi=?")) {
                                ps.setString(1, ev.getTglAwal());
                                ps.setString(2, ev.getTglAkhir());
                                ps.setString(3, normalizeStatus(ev.getStatus()));
                                ps.setString(4, catatanPegawai(ev));
                                ps.setString(5, getCurrentUserId());
                                ps.setString(6, catatanDireksi(ev));
                                ps.setString(7, ev.getIdEval());
                                ps.executeUpdate();
                            }
                        } else {
                            try (PreparedStatement ps = c.prepareStatement(
                                    "UPDATE evaluasi_panen SET tanggal_evaluasi=?, status=?, catatan_tindak_lanjut=?, id_user=?, catatan_evaluasi=? WHERE id_evaluasi=?")) {
                                ps.setString(1, ev.getTglAkhir());
                                ps.setString(2, normalizeStatus(ev.getStatus()));
                                ps.setString(3, catatanPegawai(ev));
                                ps.setString(4, getCurrentUserId());
                                ps.setString(5, catatanDireksi(ev));
                                ps.setString(6, ev.getIdEval());
                                ps.executeUpdate();
                            }
                        }
                    }
                } catch (Exception e) {
                    System.err.println("Gagal update evaluasi: " + e.getMessage());
                }
                return true;
            }
        }
        return false;
    }
    public synchronized boolean deleteEvaluasi(String idEval) throws Exception {
        Connection c = Koneksi.getKoneksi();
        if (c != null) {
            boolean oldAuto = c.getAutoCommit();
            try {
                c.setAutoCommit(false);
                try (PreparedStatement ps = c.prepareStatement("UPDATE aktivitas_panen SET id_evaluasi = NULL WHERE id_evaluasi = ?")) {
                    ps.setString(1, idEval);
                    ps.executeUpdate();
                }
                try (PreparedStatement ps = c.prepareStatement("DELETE FROM evaluasi_panen WHERE id_evaluasi = ?")) {
                    ps.setString(1, idEval);
                    ps.executeUpdate();
                }
                c.commit();
            } catch (Exception ex) {
                c.rollback();
                throw ex;
            } finally {
                c.setAutoCommit(oldAuto);
            }
        }
        return evaluasiList.removeIf(ev -> ev.getIdEval().equalsIgnoreCase(idEval));
    }

    public synchronized List<Riwayat> getRiwayatList() { return new ArrayList<>(riwayatList); }
    private static void ensureRiwayatTable(Connection c) {
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

    private static void ensureBlokLahanTahunTanam(Connection c) {
        try (Statement st = c.createStatement()) {
            st.execute("ALTER TABLE blok_lahan ADD COLUMN tahun_tanam VARCHAR(4) DEFAULT '2018'");
        } catch (Exception ignored) {}
        try (Statement st = c.createStatement()) {
            st.executeUpdate(
                "UPDATE blok_lahan bl " +
                "JOIN aktivitas_panen ap ON ap.kode_blok_lahan = bl.kode_blok_lahan " +
                "JOIN produk_panen pp ON pp.aktivitas_panen_id_panen = ap.id_panen " +
                "SET bl.tahun_tanam = pp.tahun_tanam " +
                "WHERE (bl.tahun_tanam IS NULL OR bl.tahun_tanam = '' OR bl.tahun_tanam = '2018') " +
                "  AND pp.tahun_tanam IS NOT NULL AND pp.tahun_tanam <> ''"
            );
        } catch (Exception ignored) {}
    }

    private static void ensureEvaluasiColumns(Connection c) {
        try (Statement st = c.createStatement()) {
            st.execute("ALTER TABLE evaluasi_panen ADD COLUMN tanggal_awal DATE DEFAULT NULL");
        } catch (Exception ignored) {}
        try (Statement st = c.createStatement()) {
            st.execute("ALTER TABLE evaluasi_panen ADD COLUMN catatan_evaluasi VARCHAR(255) DEFAULT '-'");
        } catch (Exception ignored) {}
        try (Statement st = c.createStatement()) {
            st.execute("ALTER TABLE evaluasi_panen ADD COLUMN catatan_tindak_lanjut VARCHAR(255) DEFAULT '-'");
        } catch (Exception ignored) {}
        try (Statement st = c.createStatement()) {
            st.executeUpdate("UPDATE evaluasi_panen SET tanggal_awal = tanggal_evaluasi WHERE tanggal_awal IS NULL");
        } catch (Exception ignored) {}
    }

    public synchronized void logRiwayat(String action, String idPanen, String before, String after) {
        String id = String.format("RW%03d", riwayatCounter++);
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        riwayatList.add(0, new Riwayat(id, time, action, currentUser, idPanen, before, after));
        try {
            Connection c = Koneksi.getKoneksi();
            if (c != null) {
                ensureRiwayatTable(c);
                try (PreparedStatement ps = c.prepareStatement(
                        "INSERT INTO riwayat_perubahan (id_riwayat, waktu_perubahan, jenis_tindakan, id_user, id_panen, data_sebelum, data_setelah) VALUES (?, NOW(), ?, ?, ?, ?, ?)")) {
                    ps.setString(1, id);
                    ps.setString(2, action);
                    ps.setString(3, currentUser);
                    ps.setString(4, idPanen);
                    ps.setString(5, before);
                    ps.setString(6, after);
                    ps.executeUpdate();
                }
            }
        } catch (Exception e) {
            System.err.println("Gagal simpan riwayat: " + e.getMessage());
        }
    }

    public static int countCalendarMonths(String startDate, String endDate) {
        if (startDate == null || endDate == null || startDate.trim().isEmpty() || endDate.trim().isEmpty()) {
            return 1;
        }
        try {
            String s = startDate.trim();
            String e = endDate.trim();
            int y1 = Integer.parseInt(s.substring(0, 4));
            int m1 = Integer.parseInt(s.substring(5, 7));
            int y2 = Integer.parseInt(e.substring(0, 4));
            int m2 = Integer.parseInt(e.substring(5, 7));
            int months = (y2 - y1) * 12 + (m2 - m1) + 1;
            return Math.max(1, months);
        } catch (Exception ex) {
            return 1;
        }
    }

    public synchronized double[] calculateMetrics(String startDate, String endDate) {
        double totalKg = 0;
        double totalReject = 0;
        for (Panen p : panenList) {
            boolean inRange = true;
            if (startDate != null && !startDate.trim().isEmpty() && p.getTanggal().compareTo(startDate.trim()) < 0) {
                inRange = false;
            }
            if (endDate != null && !endDate.trim().isEmpty() && p.getTanggal().compareTo(endDate.trim()) > 0) {
                inRange = false;
            }
            if (inRange) {
                totalKg += p.getTotalPanen();
                totalReject += p.getTidakLayak();
            }
        }
        double rejectPercent = totalKg > 0 ? (totalReject / totalKg) * 100.0 : 0.0;
        int months = countCalendarMonths(startDate, endDate);
        double avgPerMonth = totalKg / months;
        return new double[]{ totalKg, rejectPercent, avgPerMonth };
    }
}
