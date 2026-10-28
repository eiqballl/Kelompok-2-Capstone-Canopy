package dao;

import config.Koneksi;
import model.Riwayat;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RiwayatDAOImpl implements RiwayatDAO {
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
}

