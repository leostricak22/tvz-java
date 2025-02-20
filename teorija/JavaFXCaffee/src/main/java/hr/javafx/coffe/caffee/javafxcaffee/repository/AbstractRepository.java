package hr.javafx.coffe.caffee.javafxcaffee.repository;

import hr.javafx.coffe.caffee.javafxcaffee.exception.RepositoryAccessException;
import hr.javafx.coffe.caffee.javafxcaffee.model.Entity;

import java.util.List;

public abstract class AbstractRepository<T extends Entity> {

    public abstract T findById(Long id) throws RepositoryAccessException;
    public abstract List<T> findAll() throws RepositoryAccessException;
    public abstract void save(List<T> entities) throws RepositoryAccessException;
    public abstract void save(T entity) throws RepositoryAccessException;
}
