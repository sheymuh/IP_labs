package ru.ulstu.is.server;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import ru.ulstu.is.server.api.category.CategoryRq;
import ru.ulstu.is.server.api.playlist.PlaylistRq;
import ru.ulstu.is.server.api.stream.StreamRq;
import ru.ulstu.is.server.service.CategoryService;
import ru.ulstu.is.server.service.PlaylistService;
import ru.ulstu.is.server.service.StreamService;

@SpringBootApplication
public class ServerApplication implements CommandLineRunner {
    private final Logger log = LoggerFactory.getLogger(ServerApplication.class);

    private final CategoryService categoryService;
    private final PlaylistService playlistService;
    private final StreamService streamService;

    public ServerApplication(
            CategoryService categoryService,
            PlaylistService playlistService,
            StreamService streamService) {
        this.categoryService = categoryService;
        this.playlistService = playlistService;
        this.streamService = streamService;
    }

    private void populateData() {
        log.info("Create default categories");
        final var category1 = categoryService.create(new CategoryRq("jst vibing", 16));
        final var category2 = categoryService.create(new CategoryRq("letsplay", 12));
        final var category3 = categoryService.create(new CategoryRq("cooking", 10));

        log.info("Create default playlists");
        final var playlist1 = playlistService.create(new PlaylistRq("Very good videos"));
        final var playlist2 = playlistService.create(new PlaylistRq("baskemtbal"));
        final var playlist3 = playlistService
                .create(new PlaylistRq("How to make a nuclear bomb at home! Guide"));

        Random random = new Random();
        log.info("Create default streams");
        final var stream1 = streamService.create(new StreamRq(
                "new_vid",
                "\\STREAM.jpg",
                "very new",
                random.nextInt(10000000),
                LocalDate.now().toString(),
                playlist3.id(),
                List.of(category1.id(), category3.id())));

        final var stream2 = streamService.create(
                new StreamRq("marmok",
                        "\\mmmMARMOK.jpg",
                        "mmm MARMOK",
                        random.nextInt(10000000),
                        LocalDate.now().toString(),
                        playlist1.id(),
                        List.of(category2.id(), category3.id())));

        final var stream3 = streamService.create(
                new StreamRq("KING",
                        "\\king.jpg",
                        "Bronny J",
                        random.nextInt(10000000),
                        LocalDate.now().toString(),
                        playlist2.id(),
                        List.of(category1.id(), category2.id(), category3.id())));

        log.info("Data population completed");
    }

    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        populateData();
    }
}
