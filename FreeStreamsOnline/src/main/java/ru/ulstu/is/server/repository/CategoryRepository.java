package ru.ulstu.is.server.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ru.ulstu.is.server.entity.CategoryEntity;
import ru.ulstu.is.server.entity.projection.CategoryStatsProjection;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {
    Optional<CategoryEntity> findOneByNameIgnoreCase(String name);

    @Query("select cs.category as category, " +
            "count(cs) as streams, " +
            "avg(cs.grade) as avgGrade, " +
            "min(cs.grade) as minGrade, " +
            "max(cs.grade) as maxGrade " +
            "from CategoryEntity c inner join c.categoryStreams cs " +
            "group by cs.category having count(cs) > 0 " +
            "order by cs.category.id")
    List<CategoryStatsProjection> getAllCategorysStatistics();

    @Query("select cs.category as category, " +
            "count(cs) as streams, " +
            "avg(cs.grade) as avgGrade, " +
            "min(cs.grade) as minGrade, " +
            "max(cs.grade) as maxGrade " +
            "from CategoryEntity c inner join c.categoryStreams cs " +
            "where cs.category.id = :categoryId group by cs.category " +
            "having count(cs) > 0")
    CategoryStatsProjection getCategoryStatistics(@Param("categoryId") Long categoryId);
}
