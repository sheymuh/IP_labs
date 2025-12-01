package ru.ulstu.is.server;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ru.ulstu.is.server.api.stream.StreamRs;
import ru.ulstu.is.server.api.NotFoundException;
import ru.ulstu.is.server.mapper.CategoryMapper;
import ru.ulstu.is.server.mapper.PlaylistMapper;
import ru.ulstu.is.server.mapper.StreamMapper;
import ru.ulstu.is.server.service.CategoryService;
import ru.ulstu.is.server.service.PlaylistService;
import ru.ulstu.is.server.service.StreamService;

@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
class StreamServiceTests {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private PlaylistService playlistService;
    @Autowired
    private PlaylistMapper playlistMapper;
    @Autowired
    private StreamService streamService;
    @Autowired
    private StreamMapper streamMapper;

    @Test
    void getTest() {
        Assertions.assertThrows(NotFoundException.class, () -> streamService.get(0L));
    }

    @Test
    @Order(1)
    void createTest() {
        categoryService.create(categoryMapper.toRqDto("jst vibing"));
        categoryService.create(categoryMapper.toRqDto("letsplay"));
        categoryService.create(categoryMapper.toRqDto("cooking"));

        playlistService.create(playlistMapper.toRqDto("Very good videos"));
        playlistService.create(playlistMapper.toRqDto("baskemtbal"));
        playlistService.create(playlistMapper.toRqDto("How to make a nuclear bomb at home! Guide"));

        streamService.create(streamMapper.toRqDto("new_vid", "\\STREAM.jpg",
                "very new", 1L, 3L));
        streamService.create(streamMapper.toRqDto("marmok", "\\mmmMARMOK.jpg",
                "mmm MARMOK", 2L, 1L));
        final StreamRs last = streamService.create(streamMapper.toRqDto("KING", "\\king.jpg",
                "Bronny J", 3L, 2L));

        Assertions.assertEquals(3, streamService.getAll().size());

        final StreamRs cmpEntity = streamService.get(3L);
        Assertions.assertEquals(last.getId(), cmpEntity.getId());
        Assertions.assertEquals(last.getName(), cmpEntity.getName());
    }

    @Test
    @Order(2)
    void updateTest() {
        final String testName = "name";
        final String testImage = "img";
        final String testDescription = "descr";
        final Long testCategoryId = 1L;
        final Long testPlaylistId = 1L;
        final StreamRs entity = streamService.get(3L);
        final String oldName = entity.getName();
        final String oldImage = entity.getImage();
        final String oldDescription = entity.getDescription();
        final Long oldCategoryId = entity.getCategory().getId();
        final Long oldPlaylistId = entity.getPlaylist().getId();
        final StreamRs newEntity = streamService.update(3L,
                streamMapper.toRqDto(testName, testImage, testDescription, testCategoryId,
                        testPlaylistId));

        Assertions.assertEquals(3, streamService.getAll().size());
        Assertions.assertEquals(testName, newEntity.getName());
        Assertions.assertNotEquals(oldName, newEntity.getName());
        Assertions.assertEquals(testImage, newEntity.getImage());
        Assertions.assertNotEquals(oldImage, newEntity.getImage());
        Assertions.assertEquals(testDescription, newEntity.getDescription());
        Assertions.assertNotEquals(oldDescription, newEntity.getDescription());
        Assertions.assertEquals(testCategoryId, newEntity.getCategory().getId());
        Assertions.assertNotEquals(oldCategoryId, newEntity.getCategory().getId());
        Assertions.assertEquals(testPlaylistId, newEntity.getPlaylist().getId());
        Assertions.assertNotEquals(oldPlaylistId, newEntity.getPlaylist().getId());

        final StreamRs cmpEntity = streamService.get(3L);
        Assertions.assertEquals(newEntity.getId(), cmpEntity.getId());
        Assertions.assertEquals(newEntity.getName(), cmpEntity.getName());
        Assertions.assertEquals(newEntity.getImage(), cmpEntity.getImage());
        Assertions.assertEquals(newEntity.getDescription(), cmpEntity.getDescription());
        Assertions.assertEquals(newEntity.getCategory().getId(), cmpEntity.getCategory().getId());
        Assertions.assertEquals(newEntity.getPlaylist().getId(), cmpEntity.getPlaylist().getId());
    }

    @Test
    @Order(3)
    void deleteTest() {
        streamService.delete(3L);
        Assertions.assertEquals(2, streamService.getAll().size());

        final StreamRs last = streamService.get(2L);
        Assertions.assertEquals(2L, last.getId());

        final StreamRs newEntity = streamService.create(streamMapper.toRqDto("new_vid", "\\STREAM.jpg",
                "very new", 1L, 3L));
        Assertions.assertEquals(3, streamService.getAll().size());
        Assertions.assertEquals(4L, newEntity.getId());
    }
}
