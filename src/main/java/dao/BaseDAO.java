package dao;

import java.sql.SQLException;
import java.util.List;

public abstract class BaseDAO<T> {

    public abstract List<T> findAll() throws SQLException;

    public abstract T findById(String id) throws SQLException;

    public abstract void save(T entity) throws SQLException;

    public abstract void update(T entity) throws SQLException;

    public abstract void delete(String id) throws SQLException;

    public abstract List<T> findAll(String keyword) throws SQLException;
}

