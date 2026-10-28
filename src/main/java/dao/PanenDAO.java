package dao;

import model.Panen;
import java.sql.SQLException;
import java.util.List;

public interface PanenDAO {
    List<Panen> findAll() throws SQLException;
    Panen findById(String id) throws SQLException;
    void save(Panen p) throws SQLException;
    void update(Panen p) throws SQLException;
    void delete(String id) throws SQLException;
}

