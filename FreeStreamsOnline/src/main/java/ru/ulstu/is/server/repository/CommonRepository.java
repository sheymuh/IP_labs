package ru.ulstu.is.server.repository;

import java.util.Optional;

public interface CommonRepository<E, T> {
    Iterable<E> findAll();

    Optional<E> findById(T id);

    E save(E entity);

    void delete(E entity);

    void deleteAll();
}
