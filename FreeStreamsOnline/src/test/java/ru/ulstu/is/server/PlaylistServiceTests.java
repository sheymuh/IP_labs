package ru.ulstu.is.server;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

import ru.ulstu.is.server.api.playlist.PlaylistRq;
import ru.ulstu.is.server.api.playlist.PlaylistRs;
import ru.ulstu.is.server.error.AlreadyExistsException;
import ru.ulstu.is.server.error.NotFoundException;
import ru.ulstu.is.server.service.PlaylistService;

@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
@TestPropertySource(properties = { "spring.jpa.hibernate.ddl-auto=create-drop" })
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class PlaylistServiceTests {

    @Autowired
    private PlaylistService service;

    @Test
    void getTest() {
        Assertions.assertThrows(NotFoundException.class, () -> service.get(999999L));
    }

    @Test
    @Order(1)
    void createTest() {
        // Сначала очищаем контекст
        var allPlaylists = service.getAll();

        PlaylistRs playlist1 = service.create(new PlaylistRq("Лучшие видео"));
        PlaylistRs playlist2 = service.create(new PlaylistRq("Для настроения"));
        PlaylistRs playlist3 = service.create(new PlaylistRq("Обучение"));

        Assertions.assertEquals(6, service.getAll().size());

        PlaylistRs cmpEntity = service.get(playlist3.id());
        Assertions.assertEquals(playlist3.id(), cmpEntity.id());
        Assertions.assertEquals(playlist3.name(), cmpEntity.name());
    }

    @Test
    @Order(2)
    void createNullNameTest() {
        final var count = service.getAll().size();
        final var dto = new PlaylistRq(null);
        Assertions.assertThrows(DataIntegrityViolationException.class,
                () -> service.create(dto));
        // Количество не должно измениться
    }

    @Test
    @Order(3)
    void updateTest() {
        // Создаем тестовые данные, если их нет
        var allPlaylists = service.getAll();
        if (allPlaylists.isEmpty()) {
            service.create(new PlaylistRq("Тестовый плейлист"));
            allPlaylists = service.getAll();
        }

        final Long playlistId = allPlaylists.get(0).id();
        final PlaylistRs entity = service.get(playlistId);
        final String oldName = entity.name();

        final String newName = "Супер плейлист";
        final PlaylistRs updatedEntity = service.update(playlistId, new PlaylistRq(newName));

        Assertions.assertEquals(newName, updatedEntity.name());
        Assertions.assertNotEquals(oldName, updatedEntity.name());

        final PlaylistRs cmpEntity = service.get(playlistId);
        Assertions.assertEquals(updatedEntity.id(), cmpEntity.id());
        Assertions.assertEquals(updatedEntity.name(), cmpEntity.name());
    }

    @Test
    @Order(4)
    void deleteTest() {
        // Создаем тестовые данные
        PlaylistRs playlist = service.create(new PlaylistRq("Для удаления"));
        final int initialCount = service.getAll().size();

        final PlaylistRs deleted = service.delete(playlist.id());
        Assertions.assertEquals(initialCount - 1, service.getAll().size());
        Assertions.assertThrows(NotFoundException.class, () -> service.get(playlist.id()));
    }

    @Test
    @Order(5)
    void createDuplicateNameTest() {
        // Создаем первый плейлист
        service.create(new PlaylistRq("Уникальный плейлист"));

        // Попытка создать плейлист с таким же именем
        Assertions.assertThrows(AlreadyExistsException.class,
                () -> service.create(new PlaylistRq("Уникальный плейлист")));
    }

    @Test
    @Order(6)
    void getAllTest() {
        // Создаем несколько плейлистов
        service.create(new PlaylistRq("Плейлист 1"));
        service.create(new PlaylistRq("Плейлист 2"));

        final var playlists = service.getAll();
        Assertions.assertNotNull(playlists);
        Assertions.assertTrue(playlists.size() >= 2);
    }
}