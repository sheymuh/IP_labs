package ru.ulstu.is.server;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ru.ulstu.is.server.api.category.CategoryRs;
import ru.ulstu.is.server.api.NotFoundException;
import ru.ulstu.is.server.mapper.CategoryMapper;
import ru.ulstu.is.server.service.CategoryService;

@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
class CategoryServiceTests {
    @Autowired
    private CategoryService service;
    @Autowired
    private CategoryMapper mapper;

    @Test
    void getTest() {
        Assertions.assertThrows(NotFoundException.class, () -> service.get(0L));
    }

    @Test
    @Order(1)
    void createTest() {
        service.create(mapper.toRqDto("jst vibing"));
        service.create(mapper.toRqDto("letsplay"));
        final CategoryRs last = service.create(mapper.toRqDto("cooking"));

        Assertions.assertEquals(3, service.getAll().size());

        final CategoryRs cmpEntity = service.get(3L);
        Assertions.assertEquals(last.getId(), cmpEntity.getId());
        Assertions.assertEquals(last.getName(), cmpEntity.getName());
    }

    @Test
    @Order(2)
    void updateTest() {
        final String test = "TEST";
        final CategoryRs entity = service.get(3L);
        final String oldName = entity.getName();
        final CategoryRs newEntity = service.update(3L, mapper.toRqDto(test));

        Assertions.assertEquals(3, service.getAll().size());
        Assertions.assertEquals(test, newEntity.getName());
        Assertions.assertNotEquals(oldName, newEntity.getName());

        final CategoryRs cmpEntity = service.get(3L);
        Assertions.assertEquals(newEntity.getId(), cmpEntity.getId());
        Assertions.assertEquals(newEntity.getName(), cmpEntity.getName());
    }

    @Test
    @Order(3)
    void deleteTest() {
        service.delete(3L);
        Assertions.assertEquals(2, service.getAll().size());

        final CategoryRs last = service.get(2L);
        Assertions.assertEquals(2L, last.getId());

        final CategoryRs newEntity = service.create(mapper.toRqDto("jst vibing"));
        Assertions.assertEquals(3, service.getAll().size());
        Assertions.assertEquals(4L, newEntity.getId());
    }
}
