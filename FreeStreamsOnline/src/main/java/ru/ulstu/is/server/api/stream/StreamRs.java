package ru.ulstu.is.server.api.stream;

import java.util.List;
import java.util.stream.StreamSupport;

import com.fasterxml.jackson.annotation.JsonProperty;

import ru.ulstu.is.server.api.category.CategoryRs;
import ru.ulstu.is.server.api.playlist.PlaylistRs;
import ru.ulstu.is.server.entity.StreamEntity;

public record StreamRs(
        Long id,
        String name,
        String image,
        String description,
        int views,
        @JsonProperty("publication_date") String publicationDate,
        PlaylistRs playlist,
        List<CategoryRs> categories) { // Список категорий

    public static StreamRs from(StreamEntity entity) {
        List<CategoryRs> categories = entity.getCategories().stream()
                .map(CategoryRs::from)
                .toList();

        return new StreamRs(
                entity.getId(),
                entity.getName(),
                entity.getImage(),
                entity.getDescription(),
                entity.getViews(),
                entity.getPubDate().toString(),
                PlaylistRs.from(entity.getPlaylist()),
                categories);
    }

    public static List<StreamRs> fromList(Iterable<StreamEntity> entities) {
        return StreamSupport.stream(entities.spliterator(), false)
                .map(StreamRs::from)
                .toList();
    }
}