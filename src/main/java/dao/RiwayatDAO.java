package dao;

import model.Riwayat;
import java.sql.SQLException;
import java.util.List;

public interface RiwayatDAO {
    List<Riwayat> findAll() throws SQLException;
    void save(Riwayat r) throws SQLException;
}

