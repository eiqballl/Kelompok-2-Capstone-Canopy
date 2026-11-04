package dao;

import model.BlokLahan;
import java.sql.SQLException;
import java.util.List;

public interface BlokLahanDAO {
    List<BlokLahan> findAll() throws SQLException;
    BlokLahan findByKode(String kode) throws SQLException;
    BlokLahan findById(String id) throws SQLException;
    void save(BlokLahan b) throws SQLException;
    void update(BlokLahan b) throws SQLException;
    void delete(String kode) throws SQLException;
    List<BlokLahan> findAll(String keyword) throws SQLException;
}

