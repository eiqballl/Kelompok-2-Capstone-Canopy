package dao;

import config.Koneksi;
import model.Panen;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PanenDAOImpl extends PanenDAO {
    private static boolean hasSawitBeratCol(Connection c) {
        try (ResultSet rs = c.getMetaData().getColumns(null, null, "sawit", "jumlah_berat_tandan")) {
            return rs.next();
        } catch (Exception e) {
            return false;
        }
    }

    private static String getSelectSql(Connection c) {
        boolean hasBerat = hasSawitBeratCol(c);
        String col1 = hasBerat ? "sw.jumlah_berat_tandan" : "sw.jumlah_tandan";
        String col2 = hasBerat ? "sw.jumlah_berat_brondolan" : "sw.jumlah_brondolan";
        return "SELECT ap.id_panen, ap.tanggal_panen, ap.total_jumlah_panen, ap.hasil_panen_tidak_layak, ap.kualitas_panen, " +
               "       ap.kode_blok_lahan, bl.nama_blok_lahan, bl.komoditas, " +
               "       pp.id_produk, pp.nama_produk, pp.tahun_tanam, " +
               "       " + col1 + " AS jumlah_berat_tandan, " + col2 + " AS jumlah_berat_brondolan, " +
               "       kr.kadar_air_karet, kr.berat_kotor_karet " +
               "FROM aktivitas_panen ap " +
               "LEFT JOIN blok_lahan bl ON ap.kode_blok_lahan = bl.kode_blok_lahan " +
               "LEFT JOIN produk_panen pp ON pp.aktivitas_panen_id_panen = ap.id_panen " +
               "LEFT JOIN sawit sw ON sw.id_produk = pp.id_produk " +
               "LEFT JOIN karet kr ON kr.id_produk = pp.id_produk ";
    }

    @Override
    public List<Panen> findAll() throws SQLException {
        List<Panen> list = new ArrayList<>();
        Connection c = Koneksi.getConnection();
        if (c == null) return list;
        try (PreparedStatement ps = c.prepareStatement(getSelectSql(c) + "ORDER BY ap.tanggal_panen DESC, ap.id_panen DESC");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(PanenMapper.toModel(rs));
            }
        }
        return list;
    }

    @Override
    public List<Panen> findAll(String keyword) throws SQLException {
        if (keyword == null || keyword.trim().isEmpty()) return findAll();
        List<Panen> list = new ArrayList<>();
        Connection c = Koneksi.getConnection();
        if (c == null) return list;
        String query = getSelectSql(c) +
            "WHERE ap.id_panen LIKE ? OR ap.kode_blok_lahan LIKE ? OR bl.nama_blok_lahan LIKE ? " +
            "   OR bl.komoditas LIKE ? OR ap.kualitas_panen LIKE ? " +
            "ORDER BY ap.tanggal_panen DESC, ap.id_panen DESC";
        try (PreparedStatement ps = c.prepareStatement(query)) {
            String kw = "%" + keyword.trim() + "%";
            for (int i = 1; i <= 5; i++) ps.setString(i, kw);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(PanenMapper.toModel(rs));
                }
            }
        }
        return list;
    }

    @Override
    public Panen findById(String id) throws SQLException {
        Connection c = Koneksi.getConnection();
        if (c == null) return null;
        try (PreparedStatement ps = c.prepareStatement(getSelectSql(c) + "WHERE ap.id_panen = ?")) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return PanenMapper.toModel(rs);
                }
            }
        }
        return null;
    }

    @Override
    public void save(Panen p) throws SQLException {
        Connection c = Koneksi.getConnection();
        if (c == null) return;
        String kodeBlok = kodeBlok(p.getBlokLahan());

        String insert =
            "INSERT INTO aktivitas_panen (id_panen, tanggal_panen, total_jumlah_panen, hasil_panen_tidak_layak, kualitas_panen, kode_blok_lahan) " +
            "VALUES (?, ?, ?, ?, ?, ?) " +
            "ON DUPLICATE KEY UPDATE tanggal_panen=?, total_jumlah_panen=?, hasil_panen_tidak_layak=?, kualitas_panen=?, kode_blok_lahan=?";

        try (PreparedStatement ps = c.prepareStatement(insert)) {
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
        }

        saveProduk(c, p);
    }

    @Override
    public void update(Panen p) throws SQLException {
        Connection c = Koneksi.getConnection();
        if (c == null) return;
        String kodeBlok = kodeBlok(p.getBlokLahan());

        String update =
            "UPDATE aktivitas_panen SET tanggal_panen=?, total_jumlah_panen=?, hasil_panen_tidak_layak=?, kualitas_panen=?, kode_blok_lahan=? WHERE id_panen=?";

        try (PreparedStatement ps = c.prepareStatement(update)) {
            ps.setString(1, p.getTanggal());
            ps.setDouble(2, p.getTotalPanen());
            ps.setInt(3, (int) Math.round(p.getTidakLayak()));
            ps.setString(4, p.getKualitas());
            ps.setString(5, kodeBlok);
            ps.setString(6, p.getIdPanen());
            ps.executeUpdate();
        }

        saveProduk(c, p);
    }

    @Override
    public void delete(String id) throws SQLException {
        Connection c = Koneksi.getConnection();
        if (c == null) return;
        try (PreparedStatement ps = c.prepareStatement("DELETE FROM aktivitas_panen WHERE id_panen = ?")) {
            ps.setString(1, id);
            ps.executeUpdate();
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

    private void saveProduk(Connection c, Panen p) throws SQLException {
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
            String insSawit = "INSERT INTO sawit (id_produk, " + col1 + ", " + col2 + ") VALUES (?, ?, ?)";
            try (PreparedStatement ps = c.prepareStatement(insSawit)) {
                ps.setString(1, idProduk);
                ps.setInt(2, (int) Math.round(p.getInput1()));
                ps.setInt(3, (int) Math.round(p.getInput2()));
                ps.executeUpdate();
            }
        } else {
            try (PreparedStatement ps = c.prepareStatement("INSERT INTO karet (id_produk, kadar_air_karet, berat_kotor_karet) VALUES (?, ?, ?)")) {
                ps.setString(1, idProduk);
                ps.setInt(2, (int) Math.round(p.getInput2()));
                ps.setInt(3, (int) Math.round(p.getInput1()));
                ps.executeUpdate();
            }
        }
    }

    @Override
    public List<Panen> findByBlok(String kodeBlok) throws SQLException {
        List<Panen> list = new ArrayList<>();
        Connection c = Koneksi.getConnection();
        if (c == null) return list;
        try (PreparedStatement ps = c.prepareStatement(getSelectSql(c) + "WHERE ap.kode_blok_lahan = ? ORDER BY ap.tanggal_panen DESC")) {
            ps.setString(1, kodeBlok);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(PanenMapper.toModel(rs));
                }
            }
        }
        return list;
    }

    @Override
    public List<Panen> findByKomoditas(String komoditas) throws SQLException {
        List<Panen> list = new ArrayList<>();
        Connection c = Koneksi.getConnection();
        if (c == null) return list;
        try (PreparedStatement ps = c.prepareStatement(getSelectSql(c) + "WHERE bl.komoditas = ? ORDER BY ap.tanggal_panen DESC")) {
            ps.setString(1, komoditas);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(PanenMapper.toModel(rs));
                }
            }
        }
        return list;
    }

    @Override
    public List<Panen> findAll(String keyword, String komoditas) throws SQLException {
        if (komoditas == null || "Semua".equalsIgnoreCase(komoditas)) {
            return findAll(keyword);
        }
        List<Panen> list = new ArrayList<>();
        for (Panen p : findAll(keyword)) {
            if (komoditas.equalsIgnoreCase(p.getKomoditas())) {
                list.add(p);
            }
        }
        return list;
    }
}
