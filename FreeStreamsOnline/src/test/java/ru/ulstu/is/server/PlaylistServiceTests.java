package ru.ulstu.is.server;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ru.ulstu.is.server.api.playlist.PlaylistRs;
import ru.ulstu.is.server.api.NotFoundException;
import ru.ulstu.is.server.mapper.PlaylistMapper;
import ru.ulstu.is.server.service.PlaylistService;

@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
class PlaylistServiceTests {
    @Autowired
    private PlaylistService service;
    @Autowired
    private PlaylistMapper mapper;

    @BeforeEach
    void setUp() {
        clearAllData();
    }

    private void clearAllData() {
        List<Long> playlistIds = new ArrayList<>();
        service.getAll().forEach(playlist -> playlistIds.add(playlist.getId()));
        playlistIds.forEach(service::delete);
    }

    @Test
    void getTest() {
        Assertions.assertThrows(NotFoundException.class, () -> service.get(0L));
    }

    @Test
    @Order(1)
    void createTest() {
        final PlaylistRs playlist1 = service.create(mapper.toRqDto("Very good videos"));
        final PlaylistRs playlist2 = service.create(mapper.toRqDto("baskemtbal"));
        final PlaylistRs last = service.create(mapper.toRqDto("How to make a nuclear bomb at home! Guide"));

        Assertions.assertEquals(3, service.getAll().size());

        final PlaylistRs cmpEntity = service.get(last.getId());
        Assertions.assertEquals(last.getId(), cmpEntity.getId());
        Assertions.assertEquals(last.getName(), cmpEntity.getName());
    }

    @Test
    @Order(2)
    void updateTest() {
        final PlaylistRs playlist = service.create(mapper.toRqDto("Very good videos"));
        final String test = "TEST";
        final String oldName = playlist.getName();
        final PlaylistRs newEntity = service.update(playlist.getId(), mapper.toRqDto(test));

        Assertions.assertEquals(1, service.getAll().size());
        Assertions.assertEquals(test, newEntity.getName());
        Assertions.assertNotEquals(oldName, newEntity.getName());

        final PlaylistRs cmpEntity = service.get(playlist.getId());
        Assertions.assertEquals(newEntity.getId(), cmpEntity.getId());
        Assertions.assertEquals(newEntity.getName(), cmpEntity.getName());
    }

    @Test
    @Order(3)
    void deleteTest() {
        final PlaylistRs playlist1 = service.create(mapper.toRqDto("Very good videos"));
        final PlaylistRs playlist2 = service.create(mapper.toRqDto("baskemtbal"));
        final PlaylistRs playlist3 = service.create(mapper.toRqDto("cooking"));
        service.delete(playlist3.getId());
        Assertions.assertEquals(2, service.getAll().size());

        final PlaylistRs newEntity = service.create(mapper.toRqDto("newplaylist"));
        Assertions.assertEquals(3, service.getAll().size());
    }
}
