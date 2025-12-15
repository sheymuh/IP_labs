package ru.ulstu.is.server;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ru.ulstu.is.server.api.stream.StreamRs;
import ru.ulstu.is.server.api.NotFoundException;
import ru.ulstu.is.server.api.category.CategoryRs;
import ru.ulstu.is.server.api.playlist.PlaylistRs;
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

    @BeforeEach
    void setUp() {
        clearAllData();
    }

    private void clearAllData() {
        List<Long> streamIds = new ArrayList<>();
        streamService.getAll().forEach(stream -> streamIds.add(stream.getId()));
        streamIds.forEach(streamService::delete);

        List<Long> playlistIds = new ArrayList<>();
        playlistService.getAll().forEach(playlist -> playlistIds.add(playlist.getId()));
        playlistIds.forEach(playlistService::delete);

        List<Long> categoryIds = new ArrayList<>();
        categoryService.getAll().forEach(category -> categoryIds.add(category.getId()));
        categoryIds.forEach(categoryService::delete);
    }

    @Test
    void getTest() {
        Assertions.assertThrows(NotFoundException.class, () -> streamService.get(0L));
    }

    @Test
    @Order(1)
    void createTest() {
        final CategoryRs category1 = categoryService.create(categoryMapper.toRqDto("jst vibing", 16));
        final CategoryRs category2 = categoryService.create(categoryMapper.toRqDto("letsplay", 12));
        final CategoryRs category3 = categoryService.create(categoryMapper.toRqDto("cooking", 10));

        final PlaylistRs playlist1 = playlistService.create(playlistMapper.toRqDto("Very good videos"));
        final PlaylistRs playlist2 = playlistService.create(playlistMapper.toRqDto("baskemtbal"));
        final PlaylistRs playlist3 = playlistService
                .create(playlistMapper.toRqDto("How to make a nuclear bomb at home! Guide"));

        Random random = new Random();
        final StreamRs stream1 = streamService.create(streamMapper.toRqDto("new_vid", "\\STREAM.jpg",
                "very new", random.nextInt(10000000), LocalDate.now().toString(), category1.getId(),
                playlist3.getId()));
        final StreamRs stream2 = streamService.create(streamMapper.toRqDto("marmok", "\\mmmMARMOK.jpg",
                "mmm MARMOK", random.nextInt(10000000), LocalDate.now().toString(), category2.getId(),
                playlist1.getId()));
        final StreamRs last = streamService.create(streamMapper.toRqDto("KING", "\\king.jpg",
                "Bronny J", random.nextInt(10000000), LocalDate.now().toString(), category3.getId(),
                playlist2.getId()));

        Assertions.assertEquals(3, streamService.getAll().size());

        final StreamRs cmpEntity = streamService.get(last.getId());
        Assertions.assertEquals(last.getId(), cmpEntity.getId());
        Assertions.assertEquals(last.getName(), cmpEntity.getName());
    }

    @Test
    @Order(2)
    void updateTest() {
        final CategoryRs category1 = categoryService.create(categoryMapper.toRqDto("jst vibing", 16));
        final PlaylistRs playlist1 = playlistService.create(playlistMapper.toRqDto("Very good videos"));
        final String testName = "name";
        final String testImage = "img";
        final String testDescription = "descr";
        final Long testCategoryId = category1.getId();
        final Long testPlaylistId = playlist1.getId();
        final CategoryRs category2 = categoryService.create(categoryMapper.toRqDto("letsplay", 12));
        final PlaylistRs playlist2 = playlistService.create(playlistMapper.toRqDto("baskemtbal"));
        Random random = new Random();
        final StreamRs entity = streamService.create(streamMapper.toRqDto("new_vid", "\\STREAM.jpg",
                "very new", random.nextInt(10000000), LocalDate.now().toString(), category2.getId(),
                playlist2.getId()));
        final String oldName = entity.getName();
        final String oldImage = entity.getImage();
        final String oldDescription = entity.getDescription();
        final Long oldCategoryId = entity.getCategory().getId();
        final Long oldPlaylistId = entity.getPlaylist().getId();
        final StreamRs newEntity = streamService.update(entity.getId(),
                streamMapper.toRqDto(testName, testImage, testDescription, random.nextInt(10000000),
                        LocalDate.now().toString(), testCategoryId,
                        testPlaylistId));

        Assertions.assertEquals(1, streamService.getAll().size());
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

        final StreamRs cmpEntity = streamService.get(newEntity.getId());
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
        final CategoryRs category = categoryService.create(categoryMapper.toRqDto("jst vibing", 16));
        final PlaylistRs playlist = playlistService.create(playlistMapper.toRqDto("Very good videos"));
        Random random = new Random();
        final StreamRs stream1 = streamService.create(streamMapper.toRqDto("new_vid", "\\STREAM.jpg",
                "very new", random.nextInt(10000000), LocalDate.now().toString(), category.getId(), playlist.getId()));
        final StreamRs stream2 = streamService.create(streamMapper.toRqDto("marmok", "\\mmmMARMOK.jpg",
                "mmm MARMOK", random.nextInt(10000000), LocalDate.now().toString(), category.getId(),
                playlist.getId()));
        final StreamRs stream3 = streamService.create(streamMapper.toRqDto("KING", "\\king.jpg",
                "Bronny J", random.nextInt(10000000), LocalDate.now().toString(), category.getId(), playlist.getId()));
        streamService.delete(stream3.getId());
        Assertions.assertEquals(2, streamService.getAll().size());

        final StreamRs newEntity = streamService.create(streamMapper.toRqDto("new_vid", "\\STREAM.jpg",
                "very new", random.nextInt(10000000), LocalDate.now().toString(), category.getId(), playlist.getId()));
        Assertions.assertEquals(3, streamService.getAll().size());
    }
}
