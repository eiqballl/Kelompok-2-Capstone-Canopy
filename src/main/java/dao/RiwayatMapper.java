package dao;

import model.Riwayat;
import java.sql.ResultSet;
import java.sql.SQLException;

final class RiwayatMapper {
    private RiwayatMapper() { }

    static Riwayat toModel(ResultSet rs) throws SQLException {
        String idRiwayat = rs.getString("id_riwayat");
        String waktu = rs.getString("waktu_perubahan");
        String jenis = rs.getString("jenis_tindakan");
        String user = rs.getString("id_user");
        String idPanen = rs.getString("id_panen");
        String sebelum = rs.getString("data_sebelum");
        String setelah = rs.getString("data_setelah");
        return new Riwayat(idRiwayat, waktu, jenis, user, idPanen, sebelum, setelah);
    }
}

