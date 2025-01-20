package hr.java.restaurant.repository;

import hr.java.restaurant.exception.RepositoryAccessException;
import hr.java.restaurant.model.Entity;

import java.util.Set;

public abstract class AbstractRepository <T extends Entity> {

    public abstract T findById(Long id) throws RepositoryAccessException;
    public abstract Set<T> findAll() throws RepositoryAccessException;
    public abstract void save(Set<T> entities) throws RepositoryAccessException;
    public abstract void save(T entity) throws RepositoryAccessException;
    public abstract Long findNextId() throws RepositoryAccessException;
}
