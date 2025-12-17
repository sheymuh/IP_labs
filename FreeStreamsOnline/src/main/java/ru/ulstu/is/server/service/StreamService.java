package ru.ulstu.is.server.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import ru.ulstu.is.server.api.PageRs;
import ru.ulstu.is.server.api.stream.StreamRq;
import ru.ulstu.is.server.api.stream.StreamRs;
import ru.ulstu.is.server.entity.CategoryEntity;
import ru.ulstu.is.server.entity.PlaylistEntity;
import ru.ulstu.is.server.entity.StreamEntity;
import ru.ulstu.is.server.error.NotFoundException;
import ru.ulstu.is.server.repository.StreamRepository;

@Service
public class StreamService {
    private final StreamRepository repository;
    private final PlaylistService playlistService;
    private final CategoryService categoryService;

    public StreamService(
            StreamRepository repository,
            PlaylistService playlistService,
            CategoryService categoryService) {
        this.repository = repository;
        this.playlistService = playlistService;
        this.categoryService = categoryService;
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public StreamEntity getEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException(StreamEntity.class, id));
    }

    @Transactional(readOnly = true)
    public List<StreamRs> getAll() {
        return StreamRs.fromList(repository.findAll());
    }

    @Transactional(readOnly = true)
    public PageRs<StreamRs> getAll(Pageable pageable) {
        return PageRs.from(repository.findAll(pageable), StreamRs::from);
    }

    @Transactional(readOnly = true)
    public StreamRs get(Long id) {
        final StreamEntity entity = getEntity(id);
        return StreamRs.from(entity);
    }

    @Transactional
    public StreamRs create(StreamRq dto) {
        final PlaylistEntity playlist = playlistService.getEntity(dto.playlistId());

        StreamEntity entity = new StreamEntity(
                dto.name(),
                dto.image(),
                dto.description(),
                dto.views(),
                LocalDate.parse(dto.publicationDate()),
                playlist);

        // Добавляем категории
        for (Long categoryId : dto.categoryIds()) {
            CategoryEntity category = categoryService.getEntity(categoryId);
            entity.addCategory(category);
        }

        entity = repository.save(entity);
        return StreamRs.from(entity);
    }

    @Transactional
    public StreamRs update(Long id, StreamRq dto) {
        StreamEntity entity = getEntity(id);
        entity.setName(dto.name());
        entity.setImage(dto.image());
        entity.setDescription(dto.description());
        entity.setViews(dto.views());
        entity.setPubDate(LocalDate.parse(dto.publicationDate()));
        entity.setPlaylist(playlistService.getEntity(dto.playlistId()));

        // Обновляем категории
        // 1. Удаляем старые связи
        entity.getCategories().forEach(entity::removeCategory);

        // 2. Добавляем новые
        for (Long categoryId : dto.categoryIds()) {
            CategoryEntity category = categoryService.getEntity(categoryId);
            entity.addCategory(category);
        }

        entity = repository.save(entity);
        return StreamRs.from(entity);
    }

    @Transactional
    public StreamRs delete(Long id) {
        final StreamEntity entity = getEntity(id);
        repository.delete(entity);
        return StreamRs.from(entity);
    }
}