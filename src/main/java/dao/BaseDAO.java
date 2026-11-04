package dao;

import java.sql.SQLException;
import java.util.List;

/**
 * Abstract class BaseDAO mewujudkan pilar Abstraksi dan Pewarisan (Inheritance)
 * dalam arsitektur Data Access Object (DAO) CanopyONE.
 *
 * Kelas abstrak ini menjadi superclass dari seluruh DAO implementasi,
 * menyediakan kontrak dasar CRUD serta method overloading untuk pencarian.
 *
 * @param <T> Tipe model entitas data
 */
public abstract class BaseDAO<T> {

    /**
     * Mengambil seluruh data entitas dari database.
     *
     * @return List seluruh entitas
     * @throws SQLException jika terjadi error pada database
     */
    public abstract List<T> findAll() throws SQLException;

    /**
     * Mencari satu entitas berdasarkan ID uniknya.
     *
     * @param id ID entitas yang dicari
     * @return Objek entitas jika ditemukan, null jika tidak ada
     * @throws SQLException jika terjadi error pada database
     */
    public abstract T findById(String id) throws SQLException;

    /**
     * Menyimpan data entitas baru ke dalam database.
     *
     * @param entity Objek entitas yang akan disimpan
     * @throws SQLException jika terjadi error pada database
     */
    public abstract void save(T entity) throws SQLException;

    /**
     * Memperbarui data entitas yang sudah ada di database.
     *
     * @param entity Objek entitas dengan data terbaru
     * @throws SQLException jika terjadi error pada database
     */
    public abstract void update(T entity) throws SQLException;

    /**
     * Menghapus data entitas dari database berdasarkan ID.
     *
     * @param id ID entitas yang akan dihapus
     * @throws SQLException jika terjadi error pada database
     */
    public abstract void delete(String id) throws SQLException;

    /**
     * Method Overloading: Mencari entitas berdasarkan kata kunci (keyword).
     *
     * @param keyword Kata kunci pencarian (nama, kode, status, dsb.)
     * @return List entitas yang cocok dengan keyword
     * @throws SQLException jika terjadi error pada database
     */
    public abstract List<T> findAll(String keyword) throws SQLException;
}

