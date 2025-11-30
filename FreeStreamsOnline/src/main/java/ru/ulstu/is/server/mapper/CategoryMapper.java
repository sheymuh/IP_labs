package ru.ulstu.is.server.mapper;

import java.util.List;
import java.util.stream.StreamSupport;

import org.springframework.stereotype.Component;

import ru.ulstu.is.server.api.category.CategoryRq;
import ru.ulstu.is.server.api.category.CategoryRs;
import ru.ulstu.is.server.entity.CategoryEntity;

@Component
public class CategoryMapper {
    public CategoryRq toRqDto(String name) {
        final CategoryRq dto = new CategoryRq();
        dto.setName(name);
        return dto;
    }

    public CategoryRs toRsDto(CategoryEntity entity) {
        final CategoryRs dto = new CategoryRs();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        return dto;
    }

    public List<CategoryRs> toRsDtoList(Iterable<CategoryEntity> entities) {
        return StreamSupport.stream(entities.spliterator(), false)
                .map(this::toRsDto)
                .toList();
    }
}
