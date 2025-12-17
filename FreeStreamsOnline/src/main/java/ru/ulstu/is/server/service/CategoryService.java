package ru.ulstu.is.server.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import ru.ulstu.is.server.api.PageRs;
import ru.ulstu.is.server.api.category.CategoryRq;
import ru.ulstu.is.server.api.category.CategoryRs;
import ru.ulstu.is.server.api.category.CategoryStatsRs;
import ru.ulstu.is.server.entity.CategoryEntity;
import ru.ulstu.is.server.error.AlreadyExistsException;
import ru.ulstu.is.server.error.NotFoundException;
import ru.ulstu.is.server.repository.CategoryRepository;

@Service
public class CategoryService {
    private final CategoryRepository repository;

    public CategoryService(CategoryRepository repository) {
        this.repository = repository;
    }

    private void checkName(String name) {
        repository.findOneByNameIgnoreCase(name).ifPresent(val -> {
            throw new AlreadyExistsException(CategoryEntity.class, name);
        });
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public CategoryEntity getEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException(CategoryEntity.class, id));
    }

    @Transactional(readOnly = true)
    public List<CategoryRs> getAll() {
        return CategoryRs.fromList(repository.findAll());
    }

    @Transactional(readOnly = true)
    public PageRs<CategoryRs> getAll(Pageable pageable) {
        return PageRs.from(repository.findAll(pageable), CategoryRs::from);
    }

    @Transactional(readOnly = true)
    public CategoryRs get(Long id) {
        final CategoryEntity entity = getEntity(id);
        return CategoryRs.from(entity);
    }

    @Transactional
    public CategoryRs create(CategoryRq dto) {
        checkName(dto.name());
        CategoryEntity entity = new CategoryEntity(dto.name(), dto.ageLimit());
        entity = repository.save(entity);
        return CategoryRs.from(entity);
    }

    @Transactional
    public CategoryRs update(Long id, CategoryRq dto) {
        checkName(dto.name());
        CategoryEntity entity = getEntity(id);
        entity.setName(dto.name());
        entity.setAgeLimit(dto.ageLimit());
        entity = repository.save(entity);
        return CategoryRs.from(entity);
    }

    @Transactional
    public CategoryRs delete(Long id) {
        final CategoryEntity entity = getEntity(id);
        repository.delete(entity);
        return CategoryRs.from(entity);
    }

    @Transactional(readOnly = true)
    public List<CategoryStatsRs> getAllCategoriesStats() {
        return CategoryStatsRs.fromList(repository.getAllCategoriesStatistics());
    }

    @Transactional(readOnly = true)
    public CategoryStatsRs getCategoryStats(Long id) {
        final CategoryEntity categoryEntity = getEntity(id);
        return CategoryStatsRs.from(repository.getCategoryStatistics(categoryEntity.getId()));
    }
}
