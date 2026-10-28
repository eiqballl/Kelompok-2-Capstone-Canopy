package dao;

import config.Koneksi;
import model.Panen;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PanenDAOImpl implements PanenDAO {
    private static final String SELECT_BASE = 
        "SELECT ap.id_panen, ap.tanggal_panen, ap.total_jumlah_panen, ap.hasil_panen_tidak_layak, ap.kualitas_panen, " +
        "       ap.kode_blok_lahan, bl.nama_blok_lahan, bl.komoditas, " +
        "       ap.jumlah_tandan, ap.jumlah_brondolan, ap.kadar_air_karet, ap.berat_kotor_karet " +
        "FROM aktivitas_panen ap " +
        "LEFT JOIN blok_lahan bl ON ap.kode_blok_lahan = bl.kode_blok_lahan ";

    @Override
    public List<Panen> findAll() throws SQLException {
        List<Panen> list = new ArrayList<>();
        Connection c = Koneksi.getConnection();
        if (c == null) return list;
        try (PreparedStatement ps = c.prepareStatement(SELECT_BASE + "ORDER BY ap.tanggal_panen DESC, ap.id_panen DESC");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(PanenMapper.toModel(rs));
            }
        }
        return list;
    }

    @Override
    public Panen findById(String id) throws SQLException {
        Connection c = Koneksi.getConnection();
        if (c == null) return null;
        try (PreparedStatement ps = c.prepareStatement(SELECT_BASE + "WHERE ap.id_panen = ?")) {
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
        String kodeBlok = p.getBlokLahan();
        if (kodeBlok != null && kodeBlok.contains(" - ")) {
            kodeBlok = kodeBlok.split(" - ")[0].trim();
        }

        String insert = 
            "INSERT INTO aktivitas_panen (id_panen, tanggal_panen, total_jumlah_panen, hasil_panen_tidak_layak, kualitas_panen, kode_blok_lahan, " +
            "jumlah_tandan, jumlah_brondolan, kadar_air_karet, berat_kotor_karet) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) " +
            "ON DUPLICATE KEY UPDATE tanggal_panen=?, total_jumlah_panen=?, hasil_panen_tidak_layak=?, kualitas_panen=?, kode_blok_lahan=?, " +
            "jumlah_tandan=?, jumlah_brondolan=?, kadar_air_karet=?, berat_kotor_karet=?";

        try (PreparedStatement ps = c.prepareStatement(insert)) {
            ps.setString(1, p.getIdPanen());
            ps.setString(2, p.getTanggal());
            ps.setDouble(3, p.getTotalPanen());
            ps.setDouble(4, p.getTidakLayak());
            ps.setString(5, p.getKualitas());
            ps.setString(6, kodeBlok);
            ps.setInt(7, "Sawit".equalsIgnoreCase(p.getKomoditas()) ? (int) Math.round(p.getInput1()) : 0);
            ps.setInt(8, "Sawit".equalsIgnoreCase(p.getKomoditas()) ? (int) Math.round(p.getInput2()) : 0);
            ps.setDouble(9, "Karet".equalsIgnoreCase(p.getKomoditas()) ? p.getInput1() : 0);
            ps.setDouble(10, "Karet".equalsIgnoreCase(p.getKomoditas()) ? p.getInput2() : 0);

            ps.setString(11, p.getTanggal());
            ps.setDouble(12, p.getTotalPanen());
            ps.setDouble(13, p.getTidakLayak());
            ps.setString(14, p.getKualitas());
            ps.setString(15, kodeBlok);
            ps.setInt(16, "Sawit".equalsIgnoreCase(p.getKomoditas()) ? (int) Math.round(p.getInput1()) : 0);
            ps.setInt(17, "Sawit".equalsIgnoreCase(p.getKomoditas()) ? (int) Math.round(p.getInput2()) : 0);
            ps.setDouble(18, "Karet".equalsIgnoreCase(p.getKomoditas()) ? p.getInput1() : 0);
            ps.setDouble(19, "Karet".equalsIgnoreCase(p.getKomoditas()) ? p.getInput2() : 0);
            ps.executeUpdate();
        }
    }

    @Override
    public void update(Panen p) throws SQLException {
        Connection c = Koneksi.getConnection();
        if (c == null) return;
        String kodeBlok = p.getBlokLahan();
        if (kodeBlok != null && kodeBlok.contains(" - ")) {
            kodeBlok = kodeBlok.split(" - ")[0].trim();
        }

        String update = 
            "UPDATE aktivitas_panen SET tanggal_panen=?, total_jumlah_panen=?, hasil_panen_tidak_layak=?, kualitas_panen=?, kode_blok_lahan=?, " +
            "jumlah_tandan=?, jumlah_brondolan=?, kadar_air_karet=?, berat_kotor_karet=? WHERE id_panen=?";

        try (PreparedStatement ps = c.prepareStatement(update)) {
            ps.setString(1, p.getTanggal());
            ps.setDouble(2, p.getTotalPanen());
            ps.setDouble(3, p.getTidakLayak());
            ps.setString(4, p.getKualitas());
            ps.setString(5, kodeBlok);
            ps.setInt(6, "Sawit".equalsIgnoreCase(p.getKomoditas()) ? (int) Math.round(p.getInput1()) : 0);
            ps.setInt(7, "Sawit".equalsIgnoreCase(p.getKomoditas()) ? (int) Math.round(p.getInput2()) : 0);
            ps.setDouble(8, "Karet".equalsIgnoreCase(p.getKomoditas()) ? p.getInput1() : 0);
            ps.setDouble(9, "Karet".equalsIgnoreCase(p.getKomoditas()) ? p.getInput2() : 0);
            ps.setString(10, p.getIdPanen());
            ps.executeUpdate();
        }
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
}

