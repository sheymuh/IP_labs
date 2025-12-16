package ru.ulstu.is.server;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

import ru.ulstu.is.server.api.category.CategoryRq;
import ru.ulstu.is.server.api.category.CategoryRs;
import ru.ulstu.is.server.error.AlreadyExistsException;
import ru.ulstu.is.server.error.NotFoundException;
import ru.ulstu.is.server.service.CategoryService;

@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
@TestPropertySource(properties = { "spring.jpa.hibernate.ddl-auto=create-drop" })
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class CategoryServiceTests {

    @Autowired
    private CategoryService service;

    @Test
    void getTest() {
        Assertions.assertThrows(NotFoundException.class, () -> service.get(999999L));
    }

    @Test
    @Order(1)
    void createTest() {
        CategoryRs category1 = service.create(new CategoryRq("Игры", 12));
        CategoryRs category2 = service.create(new CategoryRq("Музыка", 0));
        CategoryRs category3 = service.create(new CategoryRq("Спорт", 16));

        Assertions.assertEquals(6, service.getAll().size());

        CategoryRs cmpEntity = service.get(category3.id());
        Assertions.assertEquals(category3.id(), cmpEntity.id());
        Assertions.assertEquals(category3.name(), cmpEntity.name());
        Assertions.assertEquals(category3.ageLimit(), cmpEntity.ageLimit());
    }

    @Test
    @Order(2)
    void createDuplicateNameTest() {
        service.create(new CategoryRq("Уникальная категория", 12));

        Assertions.assertThrows(AlreadyExistsException.class,
                () -> service.create(new CategoryRq("Уникальная категория", 18)));
    }

    @Test
    @Order(3)
    void updateTest() {
        // Создаем тестовую категорию
        CategoryRs category = service.create(new CategoryRq("Для обновления", 12));

        final String newName = "Обновленная категория";
        final int newAgeLimit = 18;
        final CategoryRs updatedEntity = service.update(category.id(),
                new CategoryRq(newName, newAgeLimit));

        Assertions.assertEquals(newName, updatedEntity.name());
        Assertions.assertEquals(newAgeLimit, updatedEntity.ageLimit());

        // Проверяем, что обновление сохранилось
        final CategoryRs cmpEntity = service.get(category.id());
        Assertions.assertEquals(updatedEntity.id(), cmpEntity.id());
        Assertions.assertEquals(updatedEntity.name(), cmpEntity.name());
        Assertions.assertEquals(updatedEntity.ageLimit(), cmpEntity.ageLimit());
    }

    @Test
    @Order(4)
    void deleteTest() {
        CategoryRs category = service.create(new CategoryRq("Для удаления", 12));
        final int initialCount = service.getAll().size();

        final CategoryRs deleted = service.delete(category.id());
        Assertions.assertEquals(initialCount - 1, service.getAll().size());
        Assertions.assertThrows(NotFoundException.class, () -> service.get(category.id()));
    }

    @Test
    @Order(5)
    void getAllTest() {
        // Очищаем и создаем заново
        service.create(new CategoryRq("Категория 1", 12));
        service.create(new CategoryRq("Категория 2", 16));

        final var categories = service.getAll();
        Assertions.assertNotNull(categories);
        Assertions.assertTrue(categories.size() >= 2);
    }
}