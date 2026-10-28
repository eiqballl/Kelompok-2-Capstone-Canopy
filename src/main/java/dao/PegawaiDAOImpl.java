package dao;

import config.Koneksi;
import model.Pegawai;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PegawaiDAOImpl implements PegawaiDAO {
    private static final String SELECT_BASE = 
        "SELECT u.id_user, u.nama, u.username, u.password, u.no_telepon, " +
        "CASE WHEN a.id_user IS NOT NULL THEN 'Admin' " +
        "     WHEN d.id_user IS NOT NULL THEN 'Direksi' " +
        "     ELSE 'Petugas' END AS role " +
        "FROM user u " +
        "LEFT JOIN admin a ON u.id_user = a.id_user " +
        "LEFT JOIN direksi d ON u.id_user = d.id_user " +
        "LEFT JOIN petugas p ON u.id_user = p.id_user ";

    @Override
    public List<Pegawai> findAll() throws SQLException {
        List<Pegawai> list = new ArrayList<>();
        Connection c = Koneksi.getConnection();
        if (c == null) return list;
        try (PreparedStatement ps = c.prepareStatement(SELECT_BASE + "ORDER BY u.id_user");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(PegawaiMapper.toModel(rs));
            }
        }
        return list;
    }

    @Override
    public Pegawai findById(String id) throws SQLException {
        Connection c = Koneksi.getConnection();
        if (c == null) return null;
        try (PreparedStatement ps = c.prepareStatement(SELECT_BASE + "WHERE u.id_user = ?")) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return PegawaiMapper.toModel(rs);
                }
            }
        }
        return null;
    }

    @Override
    public Pegawai findByUsername(String username) throws SQLException {
        Connection c = Koneksi.getConnection();
        if (c == null) return null;
        try (PreparedStatement ps = c.prepareStatement(SELECT_BASE + "WHERE u.username = ?")) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return PegawaiMapper.toModel(rs);
                }
            }
        }
        return null;
    }

    @Override
    public void save(Pegawai p) throws SQLException {
        Connection c = Koneksi.getConnection();
        if (c == null) return;
        String insertUser = "INSERT INTO user (id_user, nama, username, password, no_telepon) VALUES (?, ?, ?, ?, ?) " +
                            "ON DUPLICATE KEY UPDATE nama=?, username=?, password=?, no_telepon=?";
        try (PreparedStatement ps = c.prepareStatement(insertUser)) {
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
        }

        syncRoleTable(c, p.getIdPegawai(), p.getRole());
    }

    @Override
    public void update(Pegawai p) throws SQLException {
        Connection c = Koneksi.getConnection();
        if (c == null) return;
        String update = "UPDATE user SET nama=?, username=?, password=?, no_telepon=? WHERE id_user=?";
        try (PreparedStatement ps = c.prepareStatement(update)) {
            ps.setString(1, p.getNamaPegawai());
            ps.setString(2, p.getUsername());
            ps.setString(3, p.getPassword());
            ps.setString(4, p.getNoTelp());
            ps.setString(5, p.getIdPegawai());
            ps.executeUpdate();
        }

        syncRoleTable(c, p.getIdPegawai(), p.getRole());
    }

    @Override
    public void delete(String idOrUsername) throws SQLException {
        Connection c = Koneksi.getConnection();
        if (c == null) return;
        String deleteRole = "DELETE FROM admin WHERE id_user=?; DELETE FROM direksi WHERE id_user=?; DELETE FROM petugas WHERE id_user=?;";
        try (PreparedStatement ps = c.prepareStatement("DELETE FROM user WHERE id_user = ? OR username = ?")) {
            ps.setString(1, idOrUsername);
            ps.setString(2, idOrUsername);
            ps.executeUpdate();
        }
    }

    private void syncRoleTable(Connection c, String userId, String role) {
        try {
            try (PreparedStatement ps = c.prepareStatement("DELETE FROM admin WHERE id_user = ?")) {
                ps.setString(1, userId);
                ps.executeUpdate();
            }
            try (PreparedStatement ps = c.prepareStatement("DELETE FROM direksi WHERE id_user = ?")) {
                ps.setString(1, userId);
                ps.executeUpdate();
            }
            try (PreparedStatement ps = c.prepareStatement("DELETE FROM petugas WHERE id_user = ?")) {
                ps.setString(1, userId);
                ps.executeUpdate();
            }

            String targetTable = "petugas";
            if ("Admin".equalsIgnoreCase(role)) targetTable = "admin";
            else if ("Direksi".equalsIgnoreCase(role)) targetTable = "direksi";

            try (PreparedStatement ps = c.prepareStatement("INSERT INTO " + targetTable + " (id_user) VALUES (?)")) {
                ps.setString(1, userId);
                ps.executeUpdate();
            }
        } catch (SQLException ignored) {}
    }
}

