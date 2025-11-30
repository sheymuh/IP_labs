package ru.ulstu.is.server.api;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ru.ulstu.is.server.configuration.Constants;

@RestController
@RequestMapping(Constants.API_URL + PlaylistController.URL)
public class PlaylistController {
    public static final String URL = "/playlist";
    private final ConcurrentLinkedQueue<PlaylistDto> playlists;
    private final Logger log = LoggerFactory.getLogger(PlaylistController.class);
    private final AtomicInteger idGenerator = new AtomicInteger();

    public PlaylistController() {
        this.playlists = new ConcurrentLinkedQueue<>(List.of(
            new PlaylistDto(idGenerator.incrementAndGet(), "Very good videos"),
            new PlaylistDto(idGenerator.incrementAndGet(), "baskemtbal"),
            new PlaylistDto(idGenerator.incrementAndGet(), "How to make a nuclear bomb at home! Guide")));
    }

    @GetMapping
    public List<PlaylistDto> getAll() {
        log.debug("Get all playlists");
        return playlists.stream().toList();
    }

    @GetMapping("/{id}")
    public PlaylistDto get(@PathVariable("id") int id) {
        log.debug("Get stream with id {}", id);
        log.debug("Available streams IDs: {}", playlists.stream().map(PlaylistDto::getId).toList());
    
        return playlists.stream()
            .filter(playlist -> playlist.getId() == id)
            .findAny()
            .orElseThrow(() -> new NotFoundException(PlaylistDto.class, id));
    }
    
    @PostMapping
    public PlaylistDto create(@RequestBody PlaylistDto newPlaylist) {
        log.debug("Create playlist with data {}", newPlaylist);
        newPlaylist.setId(idGenerator.incrementAndGet());
        playlists.add(newPlaylist);
        return newPlaylist;
    }
    
    @DeleteMapping("/{id}")
    public PlaylistDto delete(@PathVariable("id") int id) {
        log.debug("Delete playlist wtih id {}", id);
        final PlaylistDto playlist = get(id);
        playlists.remove(playlist);
        return playlist;
    }
}
