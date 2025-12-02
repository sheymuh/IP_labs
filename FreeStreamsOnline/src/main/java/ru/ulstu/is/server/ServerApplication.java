package ru.ulstu.is.server;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import ru.ulstu.is.server.mapper.CategoryMapper;
import ru.ulstu.is.server.mapper.PlaylistMapper;
import ru.ulstu.is.server.mapper.StreamMapper;
import ru.ulstu.is.server.service.CategoryService;
import ru.ulstu.is.server.service.PlaylistService;
import ru.ulstu.is.server.service.StreamService;

@SpringBootApplication
public class ServerApplication implements CommandLineRunner {

    private final Logger log = LoggerFactory.getLogger(ServerApplication.class);

    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;
    private final PlaylistService playlistService;
    private final PlaylistMapper playlistMapper;
    private final StreamService streamService;
    private final StreamMapper streamMapper;

    public ServerApplication(
            CategoryService categoryService, CategoryMapper categoryMapper,
            PlaylistService playlistService, PlaylistMapper playlistMapper,
            StreamService streamService, StreamMapper streamMapper) {
        this.categoryService = categoryService;
        this.categoryMapper = categoryMapper;
        this.playlistService = playlistService;
        this.playlistMapper = playlistMapper;
        this.streamService = streamService;
        this.streamMapper = streamMapper;
    }

    private void populateData() {
        log.info("Create default categories");
        final var category1 = categoryService.create(categoryMapper.toRqDto("jst vibing"));
        final var category2 = categoryService.create(categoryMapper.toRqDto("letsplay"));
        final var category3 = categoryService.create(categoryMapper.toRqDto("cooking"));

        log.info("Create default playlists");
        final var playlist1 = playlistService.create(playlistMapper.toRqDto("Very good videos"));
        final var playlist2 = playlistService.create(playlistMapper.toRqDto("baskemtbal"));
        final var playlist3 = playlistService
                .create(playlistMapper.toRqDto("How to make a nuclear bomb at home! Guide"));

        log.info("Create default streams");
        streamService.create(streamMapper.toRqDto("new_vid", "\\STREAM.jpg",
                "very new", category1.getId(), playlist3.getId()));
        streamService.create(streamMapper.toRqDto("marmok", "\\mmmMARMOK.jpg",
                "mmm MARMOK", category2.getId(), playlist1.getId()));
        streamService.create(streamMapper.toRqDto("KING", "\\king.jpg",
                "Bronny J", category3.getId(), playlist2.getId()));
    }

    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        populateData();
    }
}
