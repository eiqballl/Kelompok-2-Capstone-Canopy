package dao;

import model.Pegawai;
import java.sql.ResultSet;
import java.sql.SQLException;

final class PegawaiMapper {
    private PegawaiMapper() { }

    static Pegawai toModel(ResultSet rs) throws SQLException {
        String id = rs.getString("id_user");
        String nama = rs.getString("nama");
        String username = rs.getString("username");
        String password = rs.getString("password");
        String noTelp = rs.getString("no_telepon");
        String role = "Petugas";
        try {
            role = rs.getString("role");
            if (role == null || role.isEmpty()) role = "Petugas";
        } catch (SQLException ignored) {}
        return new Pegawai(id, nama, username, password, noTelp, role);
    }
}

