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

    @Query("select c as category, " +
            "count(cs) as streams, " +
            "sum(s.views) as totalViews, " +
            "avg(s.views) as avgViews " +
            "from CategoryEntity c " +
            "left join c.categoryStreams cs " +
            "left join cs.stream s " +
            "group by c " +
            "order by c.id")
    List<CategoryStatsProjection> getAllCategoriesStatistics();

    @Query("select c as category, " +
            "count(cs) as streams, " +
            "sum(s.views) as totalViews, " +
            "avg(s.views) as avgViews " +
            "from CategoryEntity c " +
            "left join c.categoryStreams cs " +
            "left join cs.stream s " +
            "where c.id = :categoryId " +
            "group by c")
    CategoryStatsProjection getCategoryStatistics(@Param("categoryId") Long categoryId);
}