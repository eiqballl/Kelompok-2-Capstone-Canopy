package dao;

import model.Evaluasi;
import java.sql.SQLException;
import java.util.List;

/**
 * Kelas abstrak untuk akses data Evaluasi Hasil Panen.
 * Menerapkan pilar Abstraction dan Inheritance (turunan dari BaseDAO).
 */
public abstract class EvaluasiDAO extends BaseDAO<Evaluasi> {
    // Abstraksi method khusus untuk entitas evaluasi
    public abstract List<Evaluasi> findByStatus(String status) throws SQLException;

    // Overloading method pencarian evaluasi
    public abstract List<Evaluasi> findAll(String keyword, String status) throws SQLException;
}
