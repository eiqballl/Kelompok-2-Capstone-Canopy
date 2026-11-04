package dao;

import model.Evaluasi;
import java.sql.SQLException;
import java.util.List;

public abstract class EvaluasiDAO extends BaseDAO<Evaluasi> {
    public abstract List<Evaluasi> findByStatus(String status) throws SQLException;

    public abstract List<Evaluasi> findAll(String keyword, String status) throws SQLException;
}
