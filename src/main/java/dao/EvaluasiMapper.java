package dao;

import model.Evaluasi;
import java.sql.ResultSet;
import java.sql.SQLException;

final class EvaluasiMapper {
    private EvaluasiMapper() { }

    static Evaluasi toModel(ResultSet rs) throws SQLException {
        String idEval = rs.getString("id_evaluasi");
        String tgl = rs.getString("tanggal_evaluasi");
        String status = rs.getString("status");
        String catatan = rs.getString("catatan_tindak_lanjut");
        if (catatan == null) catatan = "-";

        double total = 0;
        double tidakLayak = 0;
        try {
            total = rs.getDouble("total_jumlah_panen");
            tidakLayak = rs.getDouble("hasil_panen_tidak_layak");
        } catch (SQLException ignored) {}

        double persen = total > 0 ? (tidakLayak / total) * 100 : 0.0;
        return new Evaluasi(idEval, tgl, tgl, total, persen, status, catatan);
    }
}

