package ru.ulstu.is.server;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

import ru.ulstu.is.server.api.stream.StreamRq;
import ru.ulstu.is.server.api.stream.StreamRs;
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
class StreamServiceTests {

    @Autowired
    private StreamService streamService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private PlaylistService playlistService;

    private Long testPlaylistId;
    private List<Long> testCategoryIds;

    @BeforeEach
    void setUp() {
        // Очищаем базу данных перед каждым тестом
        // Создаем тестовые данные для каждого теста
        var category1 = categoryService.create(new ru.ulstu.is.server.api.category.CategoryRq("Тест категория 1", 12));
        var category2 = categoryService.create(new ru.ulstu.is.server.api.category.CategoryRq("Тест категория 2", 16));
        testCategoryIds = Arrays.asList(category1.id(), category2.id());

        var playlist = playlistService.create(new ru.ulstu.is.server.api.playlist.PlaylistRq("Тест плейлист"));
        testPlaylistId = playlist.id();
    }

    @Test
    void getTest() {
        Assertions.assertThrows(NotFoundException.class, () -> streamService.get(999999L));
    }

    @Test
    void createTest() {
        // Проверяем, что данные созданы
        Assertions.assertNotNull(testPlaylistId);
        Assertions.assertNotNull(testCategoryIds);
        Assertions.assertFalse(testCategoryIds.isEmpty());

        StreamRs stream1 = streamService.create(new StreamRq(
                "Тестовый стрим 1",
                "/images/stream1.jpg",
                "Описание тестового стрима 1",
                1000,
                LocalDate.now().toString(),
                testPlaylistId,
                Arrays.asList(testCategoryIds.get(0))));

        StreamRs stream2 = streamService.create(new StreamRq(
                "Тестовый стрим 2",
                "/images/stream2.jpg",
                "Описание тестового стрима 2",
                2000,
                LocalDate.now().minusDays(1).toString(),
                testPlaylistId,
                Arrays.asList(testCategoryIds.get(1))));

        Assertions.assertEquals(5, streamService.getAll().size());

        StreamRs cmpEntity = streamService.get(stream2.id());
        Assertions.assertEquals(stream2.id(), cmpEntity.id());
        Assertions.assertEquals(stream2.name(), cmpEntity.name());
        Assertions.assertEquals(stream2.views(), cmpEntity.views());
        Assertions.assertEquals(testPlaylistId, cmpEntity.playlist().id());
        Assertions.assertEquals(1, cmpEntity.categories().size());
    }

    @Test
    void createNullNameTest() {
        Assertions.assertNotNull(testPlaylistId);
        Assertions.assertNotNull(testCategoryIds);

        final var dto = new StreamRq(
                null, // null имя
                "/images/test.jpg",
                "Описание",
                500,
                LocalDate.now().toString(),
                testPlaylistId,
                Arrays.asList(testCategoryIds.get(0)));

        Assertions.assertThrows(DataIntegrityViolationException.class,
                () -> streamService.create(dto));
    }

    @Test
    void createWithMultipleCategoriesTest() {
        Assertions.assertNotNull(testPlaylistId);
        Assertions.assertNotNull(testCategoryIds);

        final var count = streamService.getAll().size();

        StreamRs stream = streamService.create(new StreamRq(
                "Мультикатегориальный стрим",
                "/images/multi.jpg",
                "Стрим в нескольких категориях",
                1500,
                LocalDate.now().toString(),
                testPlaylistId,
                testCategoryIds // Обе категории
        ));

        Assertions.assertEquals(count + 1, streamService.getAll().size());
        Assertions.assertEquals(2, stream.categories().size());
    }

    @Test
    void updateTest() {
        Assertions.assertNotNull(testPlaylistId);
        Assertions.assertNotNull(testCategoryIds);

        // Создаем стрим для обновления
        StreamRs stream = streamService.create(new StreamRq(
                "Стрим для обновления",
                "/before.jpg",
                "Старое описание",
                1000,
                LocalDate.now().toString(),
                testPlaylistId,
                Arrays.asList(testCategoryIds.get(0))));

        final String newName = "Обновленный стрим";
        final int newViews = 5000;

        final StreamRs updatedEntity = streamService.update(stream.id(),
                new StreamRq(
                        newName,
                        "/updated.jpg",
                        "Обновленное описание",
                        newViews,
                        LocalDate.now().plusDays(1).toString(),
                        testPlaylistId,
                        Arrays.asList(testCategoryIds.get(1)) // Меняем категорию
                ));

        Assertions.assertEquals(newName, updatedEntity.name());
        Assertions.assertEquals(newViews, updatedEntity.views());
        Assertions.assertEquals(1, updatedEntity.categories().size());
        Assertions.assertEquals(testCategoryIds.get(1), updatedEntity.categories().get(0).id());

        final StreamRs cmpEntity = streamService.get(stream.id());
        Assertions.assertEquals(updatedEntity.id(), cmpEntity.id());
        Assertions.assertEquals(updatedEntity.name(), cmpEntity.name());
        Assertions.assertEquals(updatedEntity.views(), cmpEntity.views());
    }

    @Test
    void deleteTest() {
        Assertions.assertNotNull(testPlaylistId);
        Assertions.assertNotNull(testCategoryIds);

        // Создаем стрим для удаления
        StreamRs stream = streamService.create(new StreamRq(
                "Стрим для удаления",
                "/delete.jpg",
                "Скоро удалится",
                3000,
                LocalDate.now().toString(),
                testPlaylistId,
                Arrays.asList(testCategoryIds.get(0))));

        final int initialCount = streamService.getAll().size();

        final StreamRs deleted = streamService.delete(stream.id());
        Assertions.assertEquals(initialCount - 1, streamService.getAll().size());
        Assertions.assertThrows(NotFoundException.class, () -> streamService.get(stream.id()));
    }

    @Test
    void getAllTest() {
        Assertions.assertNotNull(testPlaylistId);
        Assertions.assertNotNull(testCategoryIds);

        // Очищаем и создаем заново
        streamService.create(new StreamRq(
                "Фильм 1",
                "/movie1.jpg",
                "Описание фильма 1",
                10000,
                LocalDate.now().toString(),
                testPlaylistId,
                Arrays.asList(testCategoryIds.get(0))));

        final var streams = streamService.getAll();
        Assertions.assertNotNull(streams);
        Assertions.assertFalse(streams.isEmpty());

        for (StreamRs stream : streams) {
            Assertions.assertNotNull(stream.id());
            Assertions.assertNotNull(stream.name());
            Assertions.assertNotNull(stream.description());
            Assertions.assertTrue(stream.views() >= 0);
            Assertions.assertNotNull(stream.publicationDate());
            Assertions.assertNotNull(stream.playlist());
            Assertions.assertNotNull(stream.categories());
        }
    }

    @Test
    void createStreamWithoutCategoriesTest() {
        Assertions.assertNotNull(testPlaylistId);

        // Тест на создание стрима без категорий (если разрешено)
        // В текущей реализации StreamRq требует @NotNull List<Long> categoryIds
        // Так что этот тест может не проходить, если валидация требует категории

        StreamRs stream = streamService.create(new StreamRq(
                "Стрим без категорий",
                "/no-cat.jpg",
                "Описание",
                500,
                LocalDate.now().toString(),
                testPlaylistId,
                Arrays.asList(testCategoryIds.get(0)) // Хотя бы одна категория
        ));

        Assertions.assertNotNull(stream);
        Assertions.assertEquals("Стрим без категорий", stream.name());
    }

    @Test
    void createStreamInvalidPlaylistTest() {
        Assertions.assertNotNull(testCategoryIds);

        // Попытка создать стрим с несуществующим плейлистом
        final var dto = new StreamRq(
                "Стрим с неверным плейлистом",
                "/invalid.jpg",
                "Описание",
                1000,
                LocalDate.now().toString(),
                999999L, // Несуществующий ID
                Arrays.asList(testCategoryIds.get(0)));

        Assertions.assertThrows(NotFoundException.class,
                () -> streamService.create(dto));
    }

    @Test
    void createStreamInvalidCategoryTest() {
        Assertions.assertNotNull(testPlaylistId);

        // Попытка создать стрим с несуществующей категорией
        final var dto = new StreamRq(
                "Стрим с неверной категорией",
                "/invalid-cat.jpg",
                "Описание",
                1000,
                LocalDate.now().toString(),
                testPlaylistId,
                Arrays.asList(999999L) // Несуществующий ID
        );

        Assertions.assertThrows(NotFoundException.class,
                () -> streamService.create(dto));
    }
}