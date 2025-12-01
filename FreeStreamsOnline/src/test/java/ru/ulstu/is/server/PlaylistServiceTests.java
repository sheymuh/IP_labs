package ru.ulstu.is.server;

import org.junit.jupiter.api.Assertions;
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

    @Test
    void getTest() {
        Assertions.assertThrows(NotFoundException.class, () -> service.get(0L));
    }

    @Test
    @Order(1)
    void createTest() {
        service.create(mapper.toRqDto("Very good videos"));
        service.create(mapper.toRqDto("baskemtbal"));
        final PlaylistRs last = service.create(mapper.toRqDto("How to make a nuclear bomb at home! Guide"));

        Assertions.assertEquals(3, service.getAll().size());

        final PlaylistRs cmpEntity = service.get(3L);
        Assertions.assertEquals(last.getId(), cmpEntity.getId());
        Assertions.assertEquals(last.getName(), cmpEntity.getName());
    }

    @Test
    @Order(2)
    void updateTest() {
        final String test = "TEST";
        final PlaylistRs entity = service.get(3L);
        final String oldName = entity.getName();
        final PlaylistRs newEntity = service.update(3L, mapper.toRqDto(test));

        Assertions.assertEquals(3, service.getAll().size());
        Assertions.assertEquals(test, newEntity.getName());
        Assertions.assertNotEquals(oldName, newEntity.getName());

        final PlaylistRs cmpEntity = service.get(3L);
        Assertions.assertEquals(newEntity.getId(), cmpEntity.getId());
        Assertions.assertEquals(newEntity.getName(), cmpEntity.getName());
    }

    @Test
    @Order(3)
    void deleteTest() {
        service.delete(3L);
        Assertions.assertEquals(2, service.getAll().size());

        final PlaylistRs last = service.get(2L);
        Assertions.assertEquals(2L, last.getId());

        final PlaylistRs newEntity = service.create(mapper.toRqDto("Very good videos"));
        Assertions.assertEquals(3, service.getAll().size());
        Assertions.assertEquals(4L, newEntity.getId());
    }
}
