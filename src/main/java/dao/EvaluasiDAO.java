package dao;

import model.Evaluasi;
import java.sql.SQLException;
import java.util.List;

public interface EvaluasiDAO {
    List<Evaluasi> findAll() throws SQLException;
    Evaluasi findById(String id) throws SQLException;
    void save(Evaluasi ev) throws SQLException;
    void update(Evaluasi ev) throws SQLException;
    void delete(String id) throws SQLException;
}

