package ru.ulstu.is.server.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import ru.ulstu.is.server.api.playlist.PlaylistRq;
import ru.ulstu.is.server.api.playlist.PlaylistRs;
import ru.ulstu.is.server.entity.PlaylistEntity;
import ru.ulstu.is.server.error.AlreadyExistsException;
import ru.ulstu.is.server.error.NotFoundException;
import ru.ulstu.is.server.repository.PlaylistRepository;

@Service
public class PlaylistService {
    private final PlaylistRepository repository;

    public PlaylistService(PlaylistRepository repository) {
        this.repository = repository;
    }

    private void checkName(String name) {
        repository.findOneByNameIgnoreCase(name).ifPresent(val -> {
            throw new AlreadyExistsException(PlaylistEntity.class, name);
        });
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public PlaylistEntity getEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException(PlaylistEntity.class, id));
    }

    @Transactional(readOnly = true)
    public List<PlaylistRs> getAll() {
        return PlaylistRs.fromList(repository.findAll());
    }

    @Transactional(readOnly = true)
    public PlaylistRs get(Long id) {
        final PlaylistEntity entity = getEntity(id);
        return PlaylistRs.from(entity);
    }

    @Transactional
    public PlaylistRs create(PlaylistRq dto) {
        checkName(dto.name()); // Проверка уникальности
        PlaylistEntity entity = new PlaylistEntity(dto.name());
        entity = repository.save(entity);
        return PlaylistRs.from(entity);
    }

    @Transactional
    public PlaylistRs update(Long id, PlaylistRq dto) {
        checkName(dto.name()); // Проверка уникальности
        PlaylistEntity entity = getEntity(id);
        entity.setName(dto.name());
        entity = repository.save(entity);
        return PlaylistRs.from(entity);
    }

    @Transactional
    public PlaylistRs delete(Long id) {
        final PlaylistEntity entity = getEntity(id);
        repository.delete(entity);
        return PlaylistRs.from(entity);
    }
}