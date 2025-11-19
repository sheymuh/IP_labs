package ru.ulstu.is.server.api.playlist;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ru.ulstu.is.server.api.NotFoundException;
import ru.ulstu.is.server.configuration.Constants;

@RestController
@RequestMapping(Constants.API_URL + PlaylistController.URL)
public class PlaylistController {
    public static final String URL = "/playlist";
    private final List<PlaylistDto> playlists;

    public PlaylistController() {
        this.playlists = List.of(
                new PlaylistDto(1, "Very good videos"),
                new PlaylistDto(2, "baskemtbal"),
                new PlaylistDto(3, "How to make a nuclear bomb at home! Guide"));
    }

    @GetMapping
    public List<PlaylistDto> getAll() {
        return playlists;
    }

    @GetMapping("/{id}")
    public PlaylistDto get(@PathVariable int id) {
        return playlists.stream()
                .filter(playlist -> playlist.getId() == id)
                .findAny()
                .orElseThrow(() -> new NotFoundException(PlaylistDto.class, id));
    }
}
