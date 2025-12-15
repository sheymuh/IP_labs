package ru.ulstu.is.server.service;

import java.util.List;
import org.springframework.stereotype.Service;

import ru.ulstu.is.server.api.category.CategoryRq;
import ru.ulstu.is.server.api.category.CategoryRs;
import ru.ulstu.is.server.entity.CategoryEntity;
import ru.ulstu.is.server.api.NotFoundException;
import ru.ulstu.is.server.mapper.CategoryMapper;
import ru.ulstu.is.server.repository.CategoryRepository;

@Service
public class CategoryService {
    private final CategoryRepository repository;
    private final CategoryMapper mapper;

    public CategoryService(CategoryRepository repository, CategoryMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public CategoryEntity getEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException(CategoryEntity.class, id));
    }

    public List<CategoryRs> getAll() {
        return mapper.toRsDtoList(repository.findAll());
    }

    public CategoryRs get(Long id) {
        final CategoryEntity entity = getEntity(id);
        return mapper.toRsDto(entity);
    }

    public CategoryRs create(CategoryRq dto) {
        CategoryEntity entity = new CategoryEntity(dto.getName(), dto.getAgeLimit());
        entity = repository.save(entity);
        return mapper.toRsDto(entity);
    }

    public CategoryRs update(Long id, CategoryRq dto) {
        CategoryEntity entity = getEntity(id);
        entity.setName(dto.getName());
        entity.setAgeLimit(dto.getAgeLimit());
        entity = repository.save(entity);
        return mapper.toRsDto(entity);
    }

    public CategoryRs delete(Long id) {
        final CategoryEntity entity = getEntity(id);
        repository.delete(entity);
        return mapper.toRsDto(entity);
    }
}
