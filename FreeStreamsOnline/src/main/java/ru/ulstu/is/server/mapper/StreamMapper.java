package ru.ulstu.is.server.mapper;

import java.util.List;
import java.util.stream.StreamSupport;

import org.springframework.stereotype.Component;

import ru.ulstu.is.server.api.stream.StreamRq;
import ru.ulstu.is.server.api.stream.StreamRs;
import ru.ulstu.is.server.entity.StreamEntity;

@Component
public class StreamMapper {
    private final CategoryMapper categoryMapper;
    private final PlaylistMapper playlistMapper;

    public StreamMapper(CategoryMapper categoryMapper, PlaylistMapper playlistMapper) {
        this.categoryMapper = categoryMapper;
        this.playlistMapper = playlistMapper;
    }

    public StreamRq toRqDto(
            String name, String image, String description, int views, String pubDate, long categoryId,
            long playlistId) {
        final StreamRq dto = new StreamRq();
        dto.setName(name);
        dto.setImage(image);
        dto.setDescription(description);
        dto.setViews(views);
        dto.setPubDate(pubDate);
        dto.setCategoryId(categoryId);
        dto.setPlaylistId(playlistId);
        return dto;
    }

    public StreamRs toRsDto(StreamEntity entity) {
        final StreamRs dto = new StreamRs();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setImage(entity.getImage());
        dto.setDescription(entity.getDescription());
        dto.setViews(entity.getViews());
        dto.setPubDate(entity.getPubDate());
        dto.setCategory(categoryMapper.toRsDto(entity.getCategory()));
        dto.setPlaylist(categoryMapper.toRsDto(entity.getPlaylist()));
        dto.setPlaylist(playlistMapper.toRsDto(entity.getPlaylist()));
        return dto;
    }

    public List<StreamRs> toRsListDto(Iterable<StreamEntity> entities) {
        return StreamSupport.stream(entities.spliterator(), false)
                .map(this::toRsDto)
                .toList();
    }
}
