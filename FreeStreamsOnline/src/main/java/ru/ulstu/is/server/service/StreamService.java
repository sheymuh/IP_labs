package ru.ulstu.is.server.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Join;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import ru.ulstu.is.server.api.PageRs;
import ru.ulstu.is.server.api.stream.StreamRq;
import ru.ulstu.is.server.api.stream.StreamRs;
import ru.ulstu.is.server.entity.CategoryEntity;
import ru.ulstu.is.server.entity.CategoryStreamEntity;
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

    @Transactional(readOnly = true)
    public PageRs<StreamRs> getFiltered(Pageable pageable, Long categoryId, Long playlistId) {
        Specification<StreamEntity> spec = Specification.where(null);

        if (categoryId != null) {
            spec = spec.and((root, query, cb) -> {
                Join<StreamEntity, CategoryStreamEntity> categoryJoin = root.join("streamCategories");
                Join<CategoryStreamEntity, CategoryEntity> category = categoryJoin.join("category");
                return cb.equal(category.get("id"), categoryId);
            });
        }

        if (playlistId != null) {
            spec = spec.and((root, query, cb) -> {
                Join<StreamEntity, PlaylistEntity> playlistJoin = root.join("playlist");
                return cb.equal(playlistJoin.get("id"), playlistId);
            });
        }

        Page<StreamEntity> page = repository.findAll(spec, pageable);
        return PageRs.from(page, StreamRs::from);
    }

    @Transactional
    public StreamRs create(StreamRq dto) {
        final PlaylistEntity playlist = playlistService.getEntity(dto.playlistId());

        int randomViews = 100 + (int) (Math.random() * 999901); // 100 - 1,000,000
        LocalDate currentDate = LocalDate.now();

        StreamEntity entity = new StreamEntity(
                dto.name(),
                dto.image(),
                dto.description(),
                randomViews,
                currentDate,
                playlist);

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
        entity.setPlaylist(playlistService.getEntity(dto.playlistId()));

        entity.getCategories().forEach(entity::removeCategory);

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