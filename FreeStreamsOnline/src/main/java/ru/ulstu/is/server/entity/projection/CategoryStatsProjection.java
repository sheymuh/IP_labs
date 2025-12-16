package ru.ulstu.is.server.entity.projection;

import ru.ulstu.is.server.entity.CategoryEntity;

public interface CategoryStatsProjection {
    CategoryEntity getCategory();

    Long getStreams();

    Long getTotalViews();

    Double getAvgViews();
}