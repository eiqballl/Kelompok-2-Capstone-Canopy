package dao;

import model.Pegawai;
import java.sql.SQLException;
import java.util.List;

public interface PegawaiDAO {
    List<Pegawai> findAll() throws SQLException;
    Pegawai findById(String id) throws SQLException;
    Pegawai findByUsername(String username) throws SQLException;
    void save(Pegawai p) throws SQLException;
    void update(Pegawai p) throws SQLException;
    void delete(String idOrUsername) throws SQLException;
}

