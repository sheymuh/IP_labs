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

    @BeforeEach
    void setUp() {
        clearAllData();
    }

    private void clearAllData() {
        List<Long> categoryIds = new ArrayList<>();
        service.getAll().forEach(category -> categoryIds.add(category.getId()));
        categoryIds.forEach(service::delete);
    }

    @Test
    void getTest() {
        Assertions.assertThrows(NotFoundException.class, () -> service.get(0L));
    }

    @Test
    @Order(1)
    void createTest() {
        final CategoryRs category1 = service.create(mapper.toRqDto("jst vibing", 16));
        final CategoryRs category2 = service.create(mapper.toRqDto("letsplay", 12));
        final CategoryRs last = service.create(mapper.toRqDto("cooking", 10));

        Assertions.assertEquals(3, service.getAll().size());

        final CategoryRs cmpEntity = service.get(last.getId());
        Assertions.assertEquals(last.getId(), cmpEntity.getId());
        Assertions.assertEquals(last.getName(), cmpEntity.getName());
    }

    @Test
    @Order(2)
    void updateTest() {
        final CategoryRs category = service.create(mapper.toRqDto("jst vibing", 16));
        final String testName = "TEST";
        final int testAgeLim = 18;
        final String oldName = category.getName();
        final int oldAgeLim = category.getAgeLimit();
        final CategoryRs newEntity = service.update(category.getId(), mapper.toRqDto(testName, testAgeLim));

        Assertions.assertEquals(1, service.getAll().size());
        Assertions.assertEquals(testName, newEntity.getName());
        Assertions.assertNotEquals(oldName, newEntity.getName());
        Assertions.assertEquals(testAgeLim, newEntity.getAgeLimit());
        Assertions.assertNotEquals(oldAgeLim, newEntity.getAgeLimit());

        final CategoryRs cmpEntity = service.get(category.getId());
        Assertions.assertEquals(newEntity.getId(), cmpEntity.getId());
        Assertions.assertEquals(newEntity.getName(), cmpEntity.getName());
    }

    @Test
    @Order(3)
    void deleteTest() {
        final CategoryRs category1 = service.create(mapper.toRqDto("jst vibing", 16));
        final CategoryRs category2 = service.create(mapper.toRqDto("letsplay", 12));
        final CategoryRs category3 = service.create(mapper.toRqDto("cooking", 10));
        service.delete(category3.getId());
        Assertions.assertEquals(2, service.getAll().size());

        final CategoryRs newEntity = service.create(mapper.toRqDto("cooking", 10));
        Assertions.assertEquals(3, service.getAll().size());
    }
}
