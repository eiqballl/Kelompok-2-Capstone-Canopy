package dao;

import config.Koneksi;
import model.Riwayat;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RiwayatDAOImpl extends BaseDAO<Riwayat> implements RiwayatDAO {
    private static final String SELECT_ALL = 
        "SELECT id_riwayat, waktu_perubahan, jenis_tindakan, id_user, id_panen, data_sebelum, data_setelah " +
        "FROM riwayat_perubahan ORDER BY waktu_perubahan DESC, id_riwayat DESC";

    private static final String INSERT = 
        "INSERT INTO riwayat_perubahan (id_riwayat, waktu_perubahan, jenis_tindakan, id_user, id_panen, data_sebelum, data_setelah) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?)";

    @Override
    public List<Riwayat> findAll() throws SQLException {
        List<Riwayat> list = new ArrayList<>();
        Connection c = Koneksi.getConnection();
        if (c == null) return list;
        try (PreparedStatement ps = c.prepareStatement(SELECT_ALL);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(RiwayatMapper.toModel(rs));
            }
        }
        return list;
    }

    @Override
    public List<Riwayat> findAll(String keyword) throws SQLException {
        if (keyword == null || keyword.trim().isEmpty()) return findAll();
        List<Riwayat> list = new ArrayList<>();
        Connection c = Koneksi.getConnection();
        if (c == null) return list;
        String query = "SELECT id_riwayat, waktu_perubahan, jenis_tindakan, id_user, id_panen, data_sebelum, data_setelah " +
                       "FROM riwayat_perubahan WHERE id_riwayat LIKE ? OR jenis_tindakan LIKE ? OR id_user LIKE ? OR id_panen LIKE ? " +
                       "ORDER BY waktu_perubahan DESC, id_riwayat DESC";
        try (PreparedStatement ps = c.prepareStatement(query)) {
            String kw = "%" + keyword.trim() + "%";
            for (int i = 1; i <= 4; i++) ps.setString(i, kw);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(RiwayatMapper.toModel(rs));
                }
            }
        }
        return list;
    }

    @Override
    public Riwayat findById(String id) throws SQLException {
        Connection c = Koneksi.getConnection();
        if (c == null) return null;
        try (PreparedStatement ps = c.prepareStatement("SELECT id_riwayat, waktu_perubahan, jenis_tindakan, id_user, id_panen, data_sebelum, data_setelah FROM riwayat_perubahan WHERE id_riwayat = ?")) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return RiwayatMapper.toModel(rs);
                }
            }
        }
        return null;
    }

    @Override
    public void save(Riwayat r) throws SQLException {
        Connection c = Koneksi.getConnection();
        if (c == null) return;
        try (PreparedStatement ps = c.prepareStatement(INSERT)) {
            ps.setString(1, r.getIdRiwayat());
            ps.setString(2, r.getWaktuPerubahan());
            ps.setString(3, r.getJenisTindakan());
            ps.setString(4, r.getUserId());
            ps.setString(5, r.getIdPanen());
            ps.setString(6, r.getDataSebelum());
            ps.setString(7, r.getDataSetelah());
            ps.executeUpdate();
        }
    }

    @Override
    public void update(Riwayat r) throws SQLException {
        Connection c = Koneksi.getConnection();
        if (c == null) return;
        String update = "UPDATE riwayat_perubahan SET waktu_perubahan=?, jenis_tindakan=?, id_user=?, id_panen=?, data_sebelum=?, data_setelah=? WHERE id_riwayat=?";
        try (PreparedStatement ps = c.prepareStatement(update)) {
            ps.setString(1, r.getWaktuPerubahan());
            ps.setString(2, r.getJenisTindakan());
            ps.setString(3, r.getUserId());
            ps.setString(4, r.getIdPanen());
            ps.setString(5, r.getDataSebelum());
            ps.setString(6, r.getDataSetelah());
            ps.setString(7, r.getIdRiwayat());
            ps.executeUpdate();
        }
    }

    @Override
    public void delete(String id) throws SQLException {
        Connection c = Koneksi.getConnection();
        if (c == null) return;
        try (PreparedStatement ps = c.prepareStatement("DELETE FROM riwayat_perubahan WHERE id_riwayat = ?")) {
            ps.setString(1, id);
            ps.executeUpdate();
        }
    }
}
