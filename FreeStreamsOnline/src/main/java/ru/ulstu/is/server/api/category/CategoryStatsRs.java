package ru.ulstu.is.server.api.category;

import java.util.List;
import java.util.stream.StreamSupport;

import ru.ulstu.is.server.entity.projection.CategoryStatsProjection;

public record CategoryStatsRs(
        CategoryRs category,
        Long streams,
        Long totalViews,
        Double avgViews) {

    public static CategoryStatsRs from(CategoryStatsProjection projection) {
        if (projection == null) {
            return new CategoryStatsRs(null, 0L, 0L, 0.0);
        }
        return new CategoryStatsRs(
                CategoryRs.from(projection.getCategory()),
                projection.getStreams() != null ? projection.getStreams() : 0L,
                projection.getTotalViews() != null ? projection.getTotalViews() : 0L,
                projection.getAvgViews() != null ? projection.getAvgViews() : 0.0);
    }

    public static List<CategoryStatsRs> fromList(Iterable<CategoryStatsProjection> projections) {
        return StreamSupport.stream(projections.spliterator(), false)
                .map(CategoryStatsRs::from)
                .toList();
    }
}