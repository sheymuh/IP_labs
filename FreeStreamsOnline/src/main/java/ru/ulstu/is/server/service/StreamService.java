package ru.ulstu.is.server.service;

import java.util.List;

import org.springframework.stereotype.Service;

import ru.ulstu.is.server.api.stream.StreamRq;
import ru.ulstu.is.server.api.stream.StreamRs;
import ru.ulstu.is.server.entity.CategoryEntity;
import ru.ulstu.is.server.entity.PlaylistEntity;
import ru.ulstu.is.server.entity.StreamEntity;
import ru.ulstu.is.server.api.NotFoundException;
import ru.ulstu.is.server.mapper.StreamMapper;
import ru.ulstu.is.server.repository.StreamRepository;

@Service
public class StreamService {
    private final StreamRepository repository;
    private final CategoryService categoryService;
    private final PlaylistService playlistService;
    private final StreamMapper mapper;

    public StreamService(StreamRepository repository, CategoryService categoryService, PlaylistService playlistService,
            StreamMapper mapper) {
        this.repository = repository;
        this.categoryService = categoryService;
        this.playlistService = playlistService;
        this.mapper = mapper;
    }

    public StreamEntity getEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException(StreamEntity.class, id));
    }

    public List<StreamRs> getAll() {
        return mapper.toRsListDto(repository.findAll());
    }

    public StreamRs get(Long id) {
        final StreamEntity entity = getEntity(id);
        return mapper.toRsDto(entity);
    }

    public StreamRs create(StreamRq dto) {
        final CategoryEntity category = categoryService.getEntity(dto.getCategoryId());
        final PlaylistEntity playlist = playlistService.getEntity(dto.getPlaylistId());
        StreamEntity entity = new StreamEntity(
                dto.getName(),
                dto.getImage(),
                dto.getDescription(),
                playlist,
                category);
        entity = repository.save(entity);
        return mapper.toRsDto(entity);
    }

    public StreamRs update(Long id, StreamRq dto) {
        StreamEntity entity = getEntity(id);
        entity.setName(dto.getName());
        entity.setImage(dto.getImage());
        entity.setDescription(dto.getDescription());
        entity.setPlaylist(categoryService.getEntity(dto.getCategoryId()));
        entity.setPlaylist(playlistService.getEntity(dto.getPlaylistId()));
        entity = repository.save(entity);
        return mapper.toRsDto(entity);
    }

    public StreamRs delete(Long id) {
        final StreamEntity entity = getEntity(id);
        repository.delete(entity);
        return mapper.toRsDto(entity);
    }
}
