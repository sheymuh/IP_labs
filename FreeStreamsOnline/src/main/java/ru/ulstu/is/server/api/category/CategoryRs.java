package ru.ulstu.is.server.api.category;

import java.util.List;
import java.util.stream.StreamSupport;

import ru.ulstu.is.server.entity.CategoryEntity;

public record CategoryRs(Long id, String name, int ageLimit) {

    public static CategoryRs from(CategoryEntity category) {
        return new CategoryRs(category.getId(), category.getName(), category.getAgeLimit());
    }

    public static List<CategoryRs> fromList(Iterable<CategoryEntity> entities) {
        return StreamSupport.stream(entities.spliterator(), false)
                .map(CategoryRs::from)
                .toList();
    }
}