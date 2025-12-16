package ru.ulstu.is.server.api.playlist;

import java.util.List;
import java.util.stream.StreamSupport;

import ru.ulstu.is.server.entity.PlaylistEntity;

public record PlaylistRs(Long id, String name) {

    public static PlaylistRs from(PlaylistEntity entity) {
        return new PlaylistRs(entity.getId(), entity.getName());
    }

    public static List<PlaylistRs> fromList(Iterable<PlaylistEntity> entities) {
        return StreamSupport.stream(entities.spliterator(), false)
                .map(PlaylistRs::from)
                .toList();
    }
}
