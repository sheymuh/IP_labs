package ru.ulstu.is.server.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.ulstu.is.server.entity.CategoryStreamEntity;
import ru.ulstu.is.server.entity.CategoryStreamId;

public interface CategoryStreamRepository extends JpaRepository<CategoryStreamEntity, CategoryStreamId> {
    Optional<CategoryStreamEntity> findOneByStreamIdAndCategoryId(Long streamId, Long categoryId);
}
