package dao;

import model.Panen;
import java.sql.SQLException;
import java.util.List;

/**
 * Kelas abstrak untuk akses data Panen.
 * Menerapkan pilar Abstraction dan Inheritance (turunan dari BaseDAO).
 */
public abstract class PanenDAO extends BaseDAO<Panen> {
    // Abstraksi method khusus untuk entitas panen
    public abstract List<Panen> findByBlok(String kodeBlok) throws SQLException;
    public abstract List<Panen> findByKomoditas(String komoditas) throws SQLException;

    // Overloading method pencarian data panen
    public abstract List<Panen> findAll(String keyword, String komoditas) throws SQLException;
}
