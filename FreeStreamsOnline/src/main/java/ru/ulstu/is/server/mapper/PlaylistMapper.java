package ru.ulstu.is.server.mapper;

import java.util.List;
import java.util.stream.StreamSupport;

import org.springframework.stereotype.Component;

import ru.ulstu.is.server.api.playlist.PlaylistRq;
import ru.ulstu.is.server.api.playlist.PlaylistRs;
import ru.ulstu.is.server.entity.PlaylistEntity;

@Component
public class PlaylistMapper {
    public PlaylistRq toRqDto(String name) {
        final PlaylistRq dto = new PlaylistRq();
        dto.setName(name);
        return dto;
    }

    public PlaylistRs toRsDto(PlaylistEntity entity) {
        final PlaylistRs dto = new PlaylistRs();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        return dto;
    }

    public List<PlaylistRs> toRsDtoList(Iterable<PlaylistEntity> entities) {
        return StreamSupport.stream(entities.spliterator(), false)
                .map(this::toRsDto)
                .toList();
    }
}
