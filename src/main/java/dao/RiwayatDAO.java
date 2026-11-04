package dao;

import model.Riwayat;
import java.sql.SQLException;
import java.util.List;

public interface RiwayatDAO {
    List<Riwayat> findAll() throws SQLException;
    Riwayat findById(String id) throws SQLException;
    void save(Riwayat r) throws SQLException;
    void update(Riwayat r) throws SQLException;
    void delete(String id) throws SQLException;
    List<Riwayat> findAll(String keyword) throws SQLException;
}

