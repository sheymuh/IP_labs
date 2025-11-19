package ru.ulstu.is.server.repository;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.AtomicLong;

import ru.ulstu.is.server.entity.BaseEntity;

public abstract class MapRepository<E extends BaseEntity> implements CommonRepository<E, Long> {
    private final ConcurrentNavigableMap<Long, E> entities = new ConcurrentSkipListMap<>();
    private final AtomicLong idGenerator = new AtomicLong(0L);

    protected MapRepository() {
    }

    private boolean isNew(E entity) {
        return Objects.isNull(entity.getId());
    }

    private E create(E entity) {
        final Long lastId = idGenerator.incrementAndGet();
        entity.setId(lastId);
        entities.put(lastId, entity);
        return entity;
    }

    private E update(E entity) {
        if (findById(entity.getId()).isEmpty()) {
            return null;
        }
        entities.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public Iterable<E> findAll() {
        return entities.values();
    }

    @Override
    public Optional<E> findById(Long id) {
        return Optional.ofNullable(entities.get(id));
    }

    @Override
    public E save(E entity) {
        if (isNew(entity)) {
            return create(entity);
        }
        return update(entity);
    }

    @Override
    public void delete(E entity) {
        if (findById(entity.getId()).isEmpty()) {
            return;
        }
        entities.remove(entity.getId());
    }

    @Override
    public void deleteAll() {
        entities.clear();
        idGenerator.set(0L);
    }
}
