package ru.ulstu.is.server.api.playlist;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import ru.ulstu.is.server.configuration.Constants;
import ru.ulstu.is.server.service.PlaylistService;

@RestController
@RequestMapping(Constants.API_URL + PlaylistController.URL)
public class PlaylistController {
    public static final String URL = "/playlist";
    private final PlaylistService playlistService;

    public PlaylistController(PlaylistService playlistService) {
        this.playlistService = playlistService;
    }

    @GetMapping
    public List<PlaylistRs> getAll() {
        return playlistService.getAll();
    }

    @GetMapping("/{id}")
    public PlaylistRs get(@PathVariable("id") Long id) {
        return playlistService.get(id);
    }

    @PostMapping
    public PlaylistRs create(@RequestBody @Valid PlaylistRq dto) {
        return playlistService.create(dto);
    }

    @PutMapping("/{id}")
    public PlaylistRs update(@PathVariable("id") Long id, @RequestBody @Valid PlaylistRq dto) {
        return playlistService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public PlaylistRs delete(@PathVariable("id") Long id) {
        return playlistService.delete(id);
    }
}
