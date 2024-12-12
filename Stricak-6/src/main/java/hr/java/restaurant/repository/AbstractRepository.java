package hr.java.restaurant.repository;

import hr.java.restaurant.model.Entity;

import java.util.List;
import java.util.Set;

public abstract class AbstractRepository <T extends Entity> {

    abstract T findById(Long id);
    abstract Set<T> findAll();
    abstract void save(Set<T> entity);
}
