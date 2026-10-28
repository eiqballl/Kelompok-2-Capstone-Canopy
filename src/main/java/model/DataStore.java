package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import config.Koneksi;

public class DataStore {
    private static final DataStore INSTANCE = new DataStore();
    public static DataStore getInstance() { return INSTANCE; }

    private String currentUser = "admin_sawit";

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
        blokList.add(new BlokLahan("SW001", "Blok Sawit A01", 12.5, "Sawit", 2018));
        blokList.add(new BlokLahan("SW002", "Blok Sawit A02", 10.0, "Sawit", 2019));
        blokList.add(new BlokLahan("KR001", "Blok Karet B01", 8.0, "Karet", 2017));
        blokList.add(new BlokLahan("KR002", "Blok Karet B02", 15.0, "Karet", 2016));

        pegawaiList.add(new Pegawai("P001", "Budi Santoso", "budi_petugas", "12345", "081234567890", "Petugas"));
        pegawaiList.add(new Pegawai("P002", "Siti Rahma", "admin_sawit", "admin123", "081298765432", "Admin"));
        pegawaiList.add(new Pegawai("P003", "Hendro Wijaya", "direksi_agri", "direksi123", "081122334455", "Direksi"));

        addPanenInternal(new Panen("PN001", "2026-10-25", "SW001 - Blok Sawit A01", "Sawit", 100, 250, 1250.0, 45.0, "Baik"));
        addPanenInternal(new Panen("PN002", "2026-10-26", "KR001 - Blok Karet B01", "Karet", 680, 28, 680.0, 20.0, "Cukup"));
        addPanenInternal(new Panen("PN003", "2026-10-27", "SW002 - Blok Sawit A02", "Sawit", 80, 180, 980.0, 35.0, "Baik"));

        evaluasiList.add(new Evaluasi("EV001", "2026-10-01", "2026-10-31", 2910.0, 3.44, "Sesuai Target", "Produktivitas stabil, perawatan blok SW001 berjalan optimal."));

        riwayatList.add(new Riwayat("RW001", "2026-10-25 08:30:15", "Tambah Data", "budi_petugas", "PN001", "-", "Input awal panen Sawit SW001"));
        riwayatList.add(new Riwayat("RW002", "2026-10-26 14:10:22", "Tambah Data", "budi_petugas", "PN002", "-", "Input awal panen Karet KR001"));
        riwayatList.add(new Riwayat("RW003", "2026-10-27 16:45:00", "Pembaruan Data", "admin_sawit", "PN001", "Total: 1200 Kg", "Total: 1250 Kg"));
    }

    private void addPanenInternal(Panen p) {
        panenList.add(p);
        panenCounter++;
    }

    private void loadFromDatabase() {
        try {
            Connection c = Koneksi.getKoneksi();
            if (c == null) return;

            Statement st = c.createStatement();

            ResultSet rsBlok = st.executeQuery("SELECT kode_blok_lahan, nama_blok_lahan, luas_blok_lahan, komoditas FROM blok_lahan");
            boolean hasBlok = false;
            while (rsBlok.next()) {
                if (!hasBlok) {
                    blokList.clear();
                    hasBlok = true;
                }
                String kode = rsBlok.getString("kode_blok_lahan");
                String nama = rsBlok.getString("nama_blok_lahan");
                double luas = rsBlok.getDouble("luas_blok_lahan");
                String kom = rsBlok.getString("komoditas");
                blokList.add(new BlokLahan(kode, nama, luas, kom, 2020));
            }
            rsBlok.close();

            ResultSet rsUser = st.executeQuery(
                "SELECT u.id_user, u.nama, u.username, u.password, u.no_telepon, " +
                "CASE WHEN a.id_user IS NOT NULL THEN 'Admin' " +
                "     WHEN d.id_user IS NOT NULL THEN 'Direksi' " +
                "     ELSE 'Petugas' END AS role " +
                "FROM user u " +
                "LEFT JOIN admin a ON u.id_user = a.id_user " +
                "LEFT JOIN direksi d ON u.id_user = d.id_user " +
                "LEFT JOIN petugas p ON u.id_user = p.id_user"
            );
            boolean hasUser = false;
            while (rsUser.next()) {
                if (!hasUser) {
                    pegawaiList.clear();
                    hasUser = true;
                }
                pegawaiList.add(new Pegawai(
                    rsUser.getString("id_user"),
                    rsUser.getString("nama"),
                    rsUser.getString("username"),
                    rsUser.getString("password"),
                    rsUser.getString("no_telepon"),
                    rsUser.getString("role")
                ));
            }
            rsUser.close();

            ResultSet rsPanen = st.executeQuery(
                "SELECT ap.id_panen, ap.tanggal_panen, ap.total_jumlah_panen, ap.hasil_panen_tidak_layak, ap.kualitas_panen, " +
                "       ap.kode_blok_lahan, bl.nama_blok_lahan, bl.komoditas, " +
                "       ps.jumlah_tandan, ps.jumlah_brondolan, " +
                "       kr.kadar_air_karet, kr.berat_kotor_karet " +
                "FROM aktivitas_panen ap " +
                "LEFT JOIN blok_lahan bl ON ap.kode_blok_lahan = bl.kode_blok_lahan " +
                "LEFT JOIN produk_panen pp ON ap.id_panen = pp.aktivitas_panen_id_panen " +
                "LEFT JOIN panen_sawit ps ON pp.id_produk = ps.id_produk " +
                "LEFT JOIN karet kr ON pp.id_produk = kr.id_produk"
            );
            boolean hasPanen = false;
            while (rsPanen.next()) {
                if (!hasPanen) {
                    panenList.clear();
                    hasPanen = true;
                }
                String idP = rsPanen.getString("id_panen");
                String tgl = rsPanen.getString("tanggal_panen");
                String kodeB = rsPanen.getString("kode_blok_lahan");
                String namaB = rsPanen.getString("nama_blok_lahan");
                String blokStr = (namaB != null && !namaB.isEmpty()) ? (kodeB + " - " + namaB) : kodeB;
                String kom = rsPanen.getString("komoditas");
                if (kom == null || kom.isEmpty()) kom = "Sawit";
                int in1 = "Sawit".equalsIgnoreCase(kom) ? rsPanen.getInt("jumlah_tandan") : rsPanen.getInt("berat_kotor_karet");
                int in2 = "Sawit".equalsIgnoreCase(kom) ? rsPanen.getInt("jumlah_brondolan") : rsPanen.getInt("kadar_air_karet");
                double tot = rsPanen.getDouble("total_jumlah_panen");
                double rej = rsPanen.getDouble("hasil_panen_tidak_layak");
                String qual = rsPanen.getString("kualitas_panen");
                panenList.add(new Panen(idP, tgl, blokStr, kom, in1, in2, tot, rej, qual));
                try {
                    int num = Integer.parseInt(idP.replaceAll("\\D+", ""));
                    if (num >= panenCounter) panenCounter = num + 1;
                } catch (Exception ignored) {}
            }
            rsPanen.close();

            ResultSet rsEval = st.executeQuery(
                "SELECT ep.id_evaluasi, ep.tanggal_evaluasi, ep.status, ep.catatan_tindak_lanjut, " +
                "       ap.total_jumlah_panen, ap.hasil_panen_tidak_layak " +
                "FROM evaluasi_panen ep " +
                "LEFT JOIN aktivitas_panen ap ON ep.aktivitas_panen_id_panen = ap.id_panen"
            );
            boolean hasEval = false;
            while (rsEval.next()) {
                if (!hasEval) {
                    evaluasiList.clear();
                    hasEval = true;
                }
                String idEv = rsEval.getString("id_evaluasi");
                String tglEv = rsEval.getString("tanggal_evaluasi");
                String status = rsEval.getString("status");
                String catatan = rsEval.getString("catatan_tindak_lanjut");
                double tot = rsEval.getDouble("total_jumlah_panen");
                double rej = rsEval.getDouble("hasil_panen_tidak_layak");
                double pct = tot > 0 ? (rej / tot) * 100.0 : 0.0;
                evaluasiList.add(new Evaluasi(idEv, tglEv, tglEv, tot, pct, status, catatan));
                try {
                    int num = Integer.parseInt(idEv.replaceAll("\\D+", ""));
                    if (num >= evalCounter) evalCounter = num + 1;
                } catch (Exception ignored) {}
            }
            rsEval.close();
            st.close();
        } catch (Exception e) {
            System.err.println("Gagal memuat database canopy: " + e.getMessage());
        }
    }

    public synchronized Pegawai findPegawai(String userOrId, String password) {
        for (Pegawai p : pegawaiList) {
            if ((p.getUsername().equalsIgnoreCase(userOrId) || p.getIdPegawai().equalsIgnoreCase(userOrId))
                    && p.getPassword().equals(password)) {
                return p;
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
                PreparedStatement ps = c.prepareStatement(
                    "INSERT INTO blok_lahan (kode_blok_lahan, nama_blok_lahan, luas_blok_lahan, komoditas) VALUES (?, ?, ?, ?) " +
                    "ON DUPLICATE KEY UPDATE nama_blok_lahan=?, luas_blok_lahan=?, komoditas=?"
                );
                ps.setString(1, b.getKodeBlok());
                ps.setString(2, b.getNamaLahan());
                ps.setInt(3, (int) Math.round(b.getLuasLahan()));
                ps.setString(4, b.getKomoditas());
                ps.setString(5, b.getNamaLahan());
                ps.setInt(6, (int) Math.round(b.getLuasLahan()));
                ps.setString(7, b.getKomoditas());
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
                        PreparedStatement ps = c.prepareStatement(
                            "UPDATE blok_lahan SET nama_blok_lahan=?, luas_blok_lahan=?, komoditas=? WHERE kode_blok_lahan=?"
                        );
                        ps.setString(1, b.getNamaLahan());
                        ps.setInt(2, (int) Math.round(b.getLuasLahan()));
                        ps.setString(3, b.getKomoditas());
                        ps.setString(4, b.getKodeBlok());
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
    public synchronized boolean deleteBlok(String kode) {
        boolean removed = blokList.removeIf(b -> b.getKodeBlok().equalsIgnoreCase(kode));
        try {
            Connection c = Koneksi.getKoneksi();
            if (c != null) {
                PreparedStatement ps = c.prepareStatement("DELETE FROM blok_lahan WHERE kode_blok_lahan = ?");
                ps.setString(1, kode);
                ps.executeUpdate();
                ps.close();
            }
        } catch (Exception e) {
            System.err.println("Gagal hapus blok: " + e.getMessage());
        }
        return removed;
    }

    public synchronized List<Pegawai> getPegawaiList() { return new ArrayList<>(pegawaiList); }
    public synchronized void addPegawai(Pegawai p) {
        pegawaiList.add(p);
        try {
            Connection c = Koneksi.getKoneksi();
            if (c != null) {
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
                    }
                } catch (Exception e) {
                    System.err.println("Gagal update user: " + e.getMessage());
                }
                return true;
            }
        }
        return false;
    }
    public synchronized boolean deletePegawai(String idOrUser) {
        boolean removed = pegawaiList.removeIf(p -> p.getIdPegawai().equalsIgnoreCase(idOrUser) || p.getUsername().equalsIgnoreCase(idOrUser));
        try {
            Connection c = Koneksi.getKoneksi();
            if (c != null) {
                PreparedStatement ps = c.prepareStatement("DELETE FROM user WHERE id_user = ? OR username = ?");
                ps.setString(1, idOrUser);
                ps.setString(2, idOrUser);
                ps.executeUpdate();
                ps.close();
            }
        } catch (Exception e) {
            System.err.println("Gagal hapus user: " + e.getMessage());
        }
        return removed;
    }

    public synchronized List<Panen> getPanenList() { return new ArrayList<>(panenList); }
    public synchronized String generatePanenId() {
        return String.format("PN%03d", panenCounter++);
    }
    public synchronized void addPanen(Panen p) {
        panenList.add(p);
        logRiwayat("Tambah Data", p.getIdPanen(), "-", "Komoditas: " + p.getKomoditas() + ", Total: " + p.getTotalPanen() + " Kg");
        try {
            Connection c = Koneksi.getKoneksi();
            if (c != null) {
                String kodeBlok = p.getBlokLahan();
                if (kodeBlok.contains(" - ")) {
                    kodeBlok = kodeBlok.split(" - ")[0].trim();
                }
                PreparedStatement ps = c.prepareStatement(
                    "INSERT INTO aktivitas_panen (id_panen, tanggal_panen, total_jumlah_panen, hasil_panen_tidak_layak, kualitas_panen, kode_blok_lahan) " +
                    "VALUES (?, ?, ?, ?, ?, ?) " +
                    "ON DUPLICATE KEY UPDATE tanggal_panen=?, total_jumlah_panen=?, hasil_panen_tidak_layak=?, kualitas_panen=?, kode_blok_lahan=?"
                );
                ps.setString(1, p.getIdPanen());
                ps.setString(2, p.getTanggal());
                ps.setDouble(3, p.getTotalPanen());
                ps.setInt(4, (int) Math.round(p.getTidakLayak()));
                ps.setString(5, p.getKualitas());
                ps.setString(6, kodeBlok);
                ps.setString(7, p.getTanggal());
                ps.setDouble(8, p.getTotalPanen());
                ps.setInt(9, (int) Math.round(p.getTidakLayak()));
                ps.setString(10, p.getKualitas());
                ps.setString(11, kodeBlok);
                ps.executeUpdate();
                ps.close();
            }
        } catch (Exception e) {
            System.err.println("Gagal simpan panen: " + e.getMessage());
        }
    }
    public synchronized boolean updatePanen(Panen p) {
        for (int i = 0; i < panenList.size(); i++) {
            if (panenList.get(i).getIdPanen().equalsIgnoreCase(p.getIdPanen())) {
                Panen old = panenList.get(i);
                String before = "Total: " + old.getTotalPanen() + " Kg, Kualitas: " + old.getKualitas();
                String after = "Total: " + p.getTotalPanen() + " Kg, Kualitas: " + p.getKualitas();
                panenList.set(i, p);
                logRiwayat("Pembaruan Data", p.getIdPanen(), before, after);
                try {
                    Connection c = Koneksi.getKoneksi();
                    if (c != null) {
                        String kodeBlok = p.getBlokLahan();
                        if (kodeBlok.contains(" - ")) {
                            kodeBlok = kodeBlok.split(" - ")[0].trim();
                        }
                        PreparedStatement ps = c.prepareStatement(
                            "UPDATE aktivitas_panen SET tanggal_panen=?, total_jumlah_panen=?, hasil_panen_tidak_layak=?, kualitas_panen=?, kode_blok_lahan=? WHERE id_panen=?"
                        );
                        ps.setString(1, p.getTanggal());
                        ps.setDouble(2, p.getTotalPanen());
                        ps.setInt(3, (int) Math.round(p.getTidakLayak()));
                        ps.setString(4, p.getKualitas());
                        ps.setString(5, kodeBlok);
                        ps.setString(6, p.getIdPanen());
                        ps.executeUpdate();
                        ps.close();
                    }
                } catch (Exception e) {
                    System.err.println("Gagal update panen: " + e.getMessage());
                }
                return true;
            }
        }
        return false;
    }
    public synchronized boolean deletePanen(String idPanen) {
        for (int i = 0; i < panenList.size(); i++) {
            if (panenList.get(i).getIdPanen().equalsIgnoreCase(idPanen)) {
                Panen old = panenList.remove(i);
                logRiwayat("Hapus Data", idPanen, "Total: " + old.getTotalPanen() + " Kg", "Dihapus dari sistem");
                try {
                    Connection c = Koneksi.getKoneksi();
                    if (c != null) {
                        PreparedStatement ps = c.prepareStatement("DELETE FROM aktivitas_panen WHERE id_panen = ?");
                        ps.setString(1, idPanen);
                        ps.executeUpdate();
                        ps.close();
                    }
                } catch (Exception e) {
                    System.err.println("Gagal hapus panen: " + e.getMessage());
                }
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
                PreparedStatement ps = c.prepareStatement(
                    "INSERT INTO evaluasi_panen (id_evaluasi, tanggal_evaluasi, status, catatan_tindak_lanjut) VALUES (?, ?, ?, ?) " +
                    "ON DUPLICATE KEY UPDATE tanggal_evaluasi=?, status=?, catatan_tindak_lanjut=?"
                );
                ps.setString(1, ev.getIdEval());
                ps.setString(2, ev.getTglAkhir());
                ps.setString(3, ev.getStatus());
                ps.setString(4, ev.getCatatan());
                ps.setString(5, ev.getTglAkhir());
                ps.setString(6, ev.getStatus());
                ps.setString(7, ev.getCatatan());
                ps.executeUpdate();
                ps.close();
            }
        } catch (Exception e) {
            System.err.println("Gagal simpan evaluasi: " + e.getMessage());
        }
    }
    public synchronized boolean updateEvaluasi(Evaluasi ev) {
        for (int i = 0; i < evaluasiList.size(); i++) {
            if (evaluasiList.get(i).getIdEval().equalsIgnoreCase(ev.getIdEval())) {
                evaluasiList.set(i, ev);
                try {
                    Connection c = Koneksi.getKoneksi();
                    if (c != null) {
                        PreparedStatement ps = c.prepareStatement(
                            "UPDATE evaluasi_panen SET tanggal_evaluasi=?, status=?, catatan_tindak_lanjut=? WHERE id_evaluasi=?"
                        );
                        ps.setString(1, ev.getTglAkhir());
                        ps.setString(2, ev.getStatus());
                        ps.setString(3, ev.getCatatan());
                        ps.setString(4, ev.getIdEval());
                        ps.executeUpdate();
                        ps.close();
                    }
                } catch (Exception e) {
                    System.err.println("Gagal update evaluasi: " + e.getMessage());
                }
                return true;
            }
        }
        return false;
    }
    public synchronized boolean deleteEvaluasi(String idEval) {
        boolean removed = evaluasiList.removeIf(ev -> ev.getIdEval().equalsIgnoreCase(idEval));
        try {
            Connection c = Koneksi.getKoneksi();
            if (c != null) {
                PreparedStatement ps = c.prepareStatement("DELETE FROM evaluasi_panen WHERE id_evaluasi = ?");
                ps.setString(1, idEval);
                ps.executeUpdate();
                ps.close();
            }
        } catch (Exception e) {
            System.err.println("Gagal hapus evaluasi: " + e.getMessage());
        }
        return removed;
    }

    public synchronized List<Riwayat> getRiwayatList() { return new ArrayList<>(riwayatList); }
    public synchronized void logRiwayat(String action, String idPanen, String before, String after) {
        String id = String.format("RW%03d", riwayatCounter++);
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        riwayatList.add(0, new Riwayat(id, time, action, currentUser, idPanen, before, after));
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
        return new double[]{ totalKg, rejectPercent };
    }
}
