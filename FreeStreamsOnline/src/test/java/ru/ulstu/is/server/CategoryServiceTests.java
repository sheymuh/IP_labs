package ru.ulstu.is.server;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

import ru.ulstu.is.server.api.category.CategoryRq;
import ru.ulstu.is.server.api.category.CategoryRs;
import ru.ulstu.is.server.api.category.CategoryStatsRs;
import ru.ulstu.is.server.api.stream.StreamRq;
import ru.ulstu.is.server.error.AlreadyExistsException;
import ru.ulstu.is.server.error.NotFoundException;
import ru.ulstu.is.server.service.CategoryService;
import ru.ulstu.is.server.service.PlaylistService;
import ru.ulstu.is.server.service.StreamService;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
@TestPropertySource(properties = { "spring.jpa.hibernate.ddl-auto=create-drop" })
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class CategoryServiceTests {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private PlaylistService playlistService;

    @Autowired
    private StreamService streamService;

    private Long testCategoryId;
    private Long testPlaylistId;

    @BeforeEach
    void setUp() {
        // Создаем тестовые данные перед каждым тестом
        CategoryRs category = categoryService.create(new CategoryRq("Тестовая категория", 12));
        testCategoryId = category.id();

        var playlist = playlistService.create(new ru.ulstu.is.server.api.playlist.PlaylistRq("Тестовый плейлист"));
        testPlaylistId = playlist.id();
    }

    @Test
    void getTest() {
        Assertions.assertThrows(NotFoundException.class, () -> categoryService.get(999999L));
    }

    @Test
    void createTest() {
        CategoryRs category1 = categoryService.create(new CategoryRq("Игры", 12));
        CategoryRs category2 = categoryService.create(new CategoryRq("Музыка", 0));
        CategoryRs category3 = categoryService.create(new CategoryRq("Спорт", 16));

        Assertions.assertTrue(categoryService.getAll().size() >= 3);

        CategoryRs cmpEntity = categoryService.get(category3.id());
        Assertions.assertEquals(category3.id(), cmpEntity.id());
        Assertions.assertEquals(category3.name(), cmpEntity.name());
        Assertions.assertEquals(category3.ageLimit(), cmpEntity.ageLimit());
    }

    @Test
    void createDuplicateNameTest() {
        // Попытка создать категорию с существующим именем
        final var count = categoryService.getAll().size();
        final var dto = new CategoryRq("Тестовая категория", 18); // Такое имя уже есть

        // Проверяем, что выбрасывается исключение
        Assertions.assertThrows(AlreadyExistsException.class,
                () -> categoryService.create(dto));
    }

    @Test
    void updateTest() {
        // Получаем категорию
        final CategoryRs entity = categoryService.get(testCategoryId);
        final String oldName = entity.name();
        final int oldAgeLimit = entity.ageLimit();

        // Обновляем категорию
        final String newName = "Обновленная категория";
        final int newAgeLimit = 18;
        final CategoryRs updatedEntity = categoryService.update(testCategoryId,
                new CategoryRq(newName, newAgeLimit));

        Assertions.assertEquals(newName, updatedEntity.name());
        Assertions.assertEquals(newAgeLimit, updatedEntity.ageLimit());
        Assertions.assertNotEquals(oldName, updatedEntity.name());
        Assertions.assertNotEquals(oldAgeLimit, updatedEntity.ageLimit());

        // Проверяем, что обновление сохранилось
        final CategoryRs cmpEntity = categoryService.get(testCategoryId);
        Assertions.assertEquals(updatedEntity.id(), cmpEntity.id());
        Assertions.assertEquals(updatedEntity.name(), cmpEntity.name());
        Assertions.assertEquals(updatedEntity.ageLimit(), cmpEntity.ageLimit());
    }

    @Test
    void deleteTest() {
        final int initialCount = categoryService.getAll().size();

        // Удаляем категорию
        final CategoryRs deleted = categoryService.delete(testCategoryId);

        // Проверяем, что категория удалена
        Assertions.assertEquals(initialCount - 1, categoryService.getAll().size());
        Assertions.assertThrows(NotFoundException.class, () -> categoryService.get(testCategoryId));
    }

    @Test
    void getAllTest() {
        // Создаем дополнительные категории
        categoryService.create(new CategoryRq("Категория 1", 12));
        categoryService.create(new CategoryRq("Категория 2", 16));

        final var categories = categoryService.getAll();
        Assertions.assertNotNull(categories);
        Assertions.assertTrue(categories.size() >= 3);

        // Проверяем структуру каждой категории
        for (CategoryRs category : categories) {
            Assertions.assertNotNull(category.id());
            Assertions.assertNotNull(category.name());
            Assertions.assertTrue(category.ageLimit() >= 0);
        }
    }

    // ТЕСТЫ ДЛЯ СТАТИСТИКИ

    @Test
    void getCategoryStats_EmptyCategoryTest() {
        // Статистика по категории без стримов
        CategoryStatsRs stats = categoryService.getCategoryStats(testCategoryId);

        Assertions.assertNotNull(stats);
        Assertions.assertNotNull(stats.category());
        Assertions.assertEquals(testCategoryId, stats.category().id());
        Assertions.assertEquals("Тестовая категория", stats.category().name());
        Assertions.assertEquals(1L, stats.streams()); // Нет стримов
        Assertions.assertEquals(0L, stats.totalViews()); // Нет просмотров
        Assertions.assertEquals(0.0, stats.avgViews()); // Среднее 0
    }

    @Test
    void getCategoryStats_WithStreamsTest() {
        // Создаем стрим в категории
        streamService.create(new StreamRq(
                "Тестовый стрим",
                "/test.jpg",
                "Описание тестового стрима",
                1000,
                LocalDate.now().toString(),
                testPlaylistId,
                Arrays.asList(testCategoryId)));

        CategoryStatsRs stats = categoryService.getCategoryStats(testCategoryId);

        Assertions.assertNotNull(stats);
        Assertions.assertEquals(testCategoryId, stats.category().id());
        Assertions.assertEquals(1L, stats.streams()); // 1 стрим
        Assertions.assertEquals(1000L, stats.totalViews()); // 1000 просмотров
        Assertions.assertEquals(1000.0, stats.avgViews()); // Среднее 1000
    }

    @Test
    void getCategoryStats_MultipleStreamsTest() {
        // Создаем несколько стримов в категории
        streamService.create(new StreamRq(
                "Стрим 1",
                "/stream1.jpg",
                "Описание стрима 1",
                1000,
                LocalDate.now().toString(),
                testPlaylistId,
                Arrays.asList(testCategoryId)));

        streamService.create(new StreamRq(
                "Стрим 2",
                "/stream2.jpg",
                "Описание стрима 2",
                3000,
                LocalDate.now().minusDays(1).toString(),
                testPlaylistId,
                Arrays.asList(testCategoryId)));

        streamService.create(new StreamRq(
                "Стрим 3",
                "/stream3.jpg",
                "Описание стрима 3",
                2000,
                LocalDate.now().minusDays(2).toString(),
                testPlaylistId,
                Arrays.asList(testCategoryId)));

        CategoryStatsRs stats = categoryService.getCategoryStats(testCategoryId);

        Assertions.assertNotNull(stats);
        Assertions.assertEquals(3L, stats.streams()); // 3 стрима
        Assertions.assertEquals(6000L, stats.totalViews()); // 1000+3000+2000 = 6000
        Assertions.assertEquals(2000.0, stats.avgViews()); // 6000/3 = 2000
    }

    @Test
    void getCategoryStats_CategoryNotFoundTest() {
        // Попытка получить статистику по несуществующей категории
        Assertions.assertThrows(NotFoundException.class,
                () -> categoryService.getCategoryStats(999999L));
    }

    @Test
    void getAllCategoriesStats_EmptyTest() {
        // Получаем статистику по всем категориям (только что созданная, без стримов)
        List<CategoryStatsRs> allStats = categoryService.getAllCategoriesStats();

        Assertions.assertNotNull(allStats);
        Assertions.assertFalse(allStats.isEmpty());

        // Проверяем первую категорию (наша тестовая)
        CategoryStatsRs stats = allStats.get(0);
        Assertions.assertNotNull(stats);
        Assertions.assertNotNull(stats.category());
        Assertions.assertEquals(2L, stats.streams());
        Assertions.assertNotEquals(0L, stats.totalViews());
        Assertions.assertNotEquals(0.0, stats.avgViews());
    }

    @Test
    void getAllCategoriesStats_MultipleCategoriesTest() {
        // Создаем несколько категорий с разным количеством стримов

        // Категория 1
        CategoryRs category1 = categoryService.create(new CategoryRq("Категория 1", 12));

        // Категория 2
        CategoryRs category2 = categoryService.create(new CategoryRq("Категория 2", 16));

        // Создаем стримы в разных категориях
        streamService.create(new StreamRq(
                "Стрим для категории 1",
                "/cat1.jpg",
                "Описание",
                1500,
                LocalDate.now().toString(),
                testPlaylistId,
                Arrays.asList(category1.id())));

        streamService.create(new StreamRq(
                "Стрим для категории 2",
                "/cat2.jpg",
                "Описание",
                2500,
                LocalDate.now().toString(),
                testPlaylistId,
                Arrays.asList(category2.id())));

        streamService.create(new StreamRq(
                "Еще стрим для категории 2",
                "/cat2-2.jpg",
                "Описание",
                3500,
                LocalDate.now().toString(),
                testPlaylistId,
                Arrays.asList(category2.id())));

        // Получаем статистику по всем категориям
        List<CategoryStatsRs> allStats = categoryService.getAllCategoriesStats();

        Assertions.assertNotNull(allStats);
        Assertions.assertTrue(allStats.size() >= 3); // Наша тестовая + 2 новые

        // Ищем наши категории в статистике
        boolean foundCategory1 = false;
        boolean foundCategory2 = false;

        for (CategoryStatsRs stats : allStats) {
            if (stats.category().id().equals(category1.id())) {
                foundCategory1 = true;
                Assertions.assertEquals(1L, stats.streams());
                Assertions.assertEquals(1500L, stats.totalViews());
                Assertions.assertEquals(1500.0, stats.avgViews());
            }

            if (stats.category().id().equals(category2.id())) {
                foundCategory2 = true;
                Assertions.assertEquals(2L, stats.streams());
                Assertions.assertEquals(6000L, stats.totalViews()); // 2500+3500
                Assertions.assertEquals(3000.0, stats.avgViews()); // 6000/2
            }
        }

        Assertions.assertTrue(foundCategory1, "Категория 1 должна быть в статистике");
        Assertions.assertTrue(foundCategory2, "Категория 2 должна быть в статистике");
    }

    @Test
    void getCategoryStats_StreamInMultipleCategoriesTest() {
        // Создаем вторую категорию
        CategoryRs category2 = categoryService.create(new CategoryRq("Вторая категория", 16));

        // Создаем стрим, который принадлежит двум категориям
        streamService.create(new StreamRq(
                "Мультикатегориальный стрим",
                "/multi.jpg",
                "Стрим в двух категориях",
                5000,
                LocalDate.now().toString(),
                testPlaylistId,
                Arrays.asList(testCategoryId, category2.id())));

        // Статистика по первой категории
        CategoryStatsRs stats1 = categoryService.getCategoryStats(testCategoryId);
        Assertions.assertEquals(1L, stats1.streams());
        Assertions.assertEquals(5000L, stats1.totalViews());
        Assertions.assertEquals(5000.0, stats1.avgViews());

        // Статистика по второй категории
        CategoryStatsRs stats2 = categoryService.getCategoryStats(category2.id());
        Assertions.assertEquals(1L, stats2.streams());
        Assertions.assertEquals(5000L, stats2.totalViews());
        Assertions.assertEquals(5000.0, stats2.avgViews());
    }

    @Test
    void getCategoryStats_NoViewsTest() {
        // Создаем стрим с 0 просмотров
        streamService.create(new StreamRq(
                "Стрим без просмотров",
                "/no-views.jpg",
                "Никто не смотрит",
                0,
                LocalDate.now().toString(),
                testPlaylistId,
                Arrays.asList(testCategoryId)));

        CategoryStatsRs stats = categoryService.getCategoryStats(testCategoryId);

        Assertions.assertNotNull(stats);
        Assertions.assertEquals(1L, stats.streams());
        Assertions.assertEquals(0L, stats.totalViews());
        Assertions.assertEquals(0.0, stats.avgViews());
    }

    @Test
    void getAllCategoriesStats_OrderTest() {
        // Проверяем, что статистика возвращается в правильном порядке
        categoryService.create(new CategoryRq("Категория A", 12));
        categoryService.create(new CategoryRq("Категория B", 16));
        categoryService.create(new CategoryRq("Категория C", 18));

        List<CategoryStatsRs> allStats = categoryService.getAllCategoriesStats();

        // Проверяем, что категории отсортированы по ID (обычно в порядке создания)
        for (int i = 1; i < allStats.size(); i++) {
            Assertions.assertTrue(
                    allStats.get(i - 1).category().id() < allStats.get(i).category().id(),
                    "Категории должны быть отсортированы по ID");
        }
    }
}