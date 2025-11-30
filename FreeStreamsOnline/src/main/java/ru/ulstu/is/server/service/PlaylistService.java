package ru.ulstu.is.server.service;

import java.util.List;

import org.springframework.stereotype.Service;

import ru.ulstu.is.server.api.playlist.PlaylistRq;
import ru.ulstu.is.server.api.playlist.PlaylistRs;
import ru.ulstu.is.server.entity.PlaylistEntity;
import ru.ulstu.is.server.api.NotFoundException;
import ru.ulstu.is.server.mapper.PlaylistMapper;
import ru.ulstu.is.server.repository.PlaylistRepository;

@Service
public class PlaylistService {
    private final PlaylistRepository repository;
    private final PlaylistMapper mapper;

    public PlaylistService(PlaylistRepository repository, PlaylistMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public PlaylistEntity getEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException(PlaylistEntity.class, id));
    }

    public List<PlaylistRs> getAll() {
        return mapper.toRsDtoList(repository.findAll());
    }

    public PlaylistRs get(Long id) {
        final PlaylistEntity entity = getEntity(id);
        return mapper.toRsDto(entity);
    }

    public PlaylistRs create(PlaylistRq dto) {
        PlaylistEntity entity = new PlaylistEntity(dto.getName());
        entity = repository.save(entity);
        return mapper.toRsDto(entity);
    }

    public PlaylistRs update(Long id, PlaylistRq dto) {
        PlaylistEntity entity = getEntity(id);
        entity.setName(dto.getName());
        entity = repository.save(entity);
        return mapper.toRsDto(entity);
    }

    public PlaylistRs delete(Long id) {
        final PlaylistEntity entity = getEntity(id);
        repository.delete(entity);
        return mapper.toRsDto(entity);
    }
}
