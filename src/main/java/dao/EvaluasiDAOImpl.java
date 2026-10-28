package dao;

import config.Koneksi;
import model.Evaluasi;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EvaluasiDAOImpl implements EvaluasiDAO {
    private static final String SELECT_BASE = 
        "SELECT ep.id_evaluasi, ep.tanggal_evaluasi, ep.status, ep.catatan_tindak_lanjut, " +
        "       ap.total_jumlah_panen, ap.hasil_panen_tidak_layak " +
        "FROM evaluasi_panen ep " +
        "LEFT JOIN aktivitas_panen ap ON ep.aktivitas_panen_id_panen = ap.id_panen ";

    @Override
    public List<Evaluasi> findAll() throws SQLException {
        List<Evaluasi> list = new ArrayList<>();
        Connection c = Koneksi.getConnection();
        if (c == null) return list;
        try (PreparedStatement ps = c.prepareStatement(SELECT_BASE + "ORDER BY ep.tanggal_evaluasi DESC, ep.id_evaluasi DESC");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(EvaluasiMapper.toModel(rs));
            }
        }
        return list;
    }

    @Override
    public Evaluasi findById(String id) throws SQLException {
        Connection c = Koneksi.getConnection();
        if (c == null) return null;
        try (PreparedStatement ps = c.prepareStatement(SELECT_BASE + "WHERE ep.id_evaluasi = ?")) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return EvaluasiMapper.toModel(rs);
                }
            }
        }
        return null;
    }

    @Override
    public void save(Evaluasi ev) throws SQLException {
        Connection c = Koneksi.getConnection();
        if (c == null) return;
        String insert = 
            "INSERT INTO evaluasi_panen (id_evaluasi, tanggal_evaluasi, status, catatan_tindak_lanjut) VALUES (?, ?, ?, ?) " +
            "ON DUPLICATE KEY UPDATE tanggal_evaluasi=?, status=?, catatan_tindak_lanjut=?";

        try (PreparedStatement ps = c.prepareStatement(insert)) {
            ps.setString(1, ev.getIdEval());
            ps.setString(2, ev.getTglAkhir());
            ps.setString(3, ev.getStatus());
            ps.setString(4, ev.getCatatan());

            ps.setString(5, ev.getTglAkhir());
            ps.setString(6, ev.getStatus());
            ps.setString(7, ev.getCatatan());
            ps.executeUpdate();
        }
    }

    @Override
    public void update(Evaluasi ev) throws SQLException {
        Connection c = Koneksi.getConnection();
        if (c == null) return;
        String update = "UPDATE evaluasi_panen SET tanggal_evaluasi=?, status=?, catatan_tindak_lanjut=? WHERE id_evaluasi=?";
        try (PreparedStatement ps = c.prepareStatement(update)) {
            ps.setString(1, ev.getTglAkhir());
            ps.setString(2, ev.getStatus());
            ps.setString(3, ev.getCatatan());
            ps.setString(4, ev.getIdEval());
            ps.executeUpdate();
        }
    }

    @Override
    public void delete(String id) throws SQLException {
        Connection c = Koneksi.getConnection();
        if (c == null) return;
        try (PreparedStatement ps = c.prepareStatement("DELETE FROM evaluasi_panen WHERE id_evaluasi = ?")) {
            ps.setString(1, id);
            ps.executeUpdate();
        }
    }
}

