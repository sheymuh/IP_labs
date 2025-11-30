package ru.ulstu.is.server.api.stream;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ru.ulstu.is.server.api.category.CategoryController;
import ru.ulstu.is.server.api.playlist.PlaylistController;
import ru.ulstu.is.server.configuration.Constants;

@RestController
@RequestMapping(Constants.API_URL + StreamController.URL)
public class StreamController {
    public static final String URL = "/stream";
    private final Logger log = LoggerFactory.getLogger(StreamController.class);
    private final AtomicInteger idGenerator = new AtomicInteger();
    private final ConcurrentLinkedQueue<StreamDto> streams;
    private final PlaylistController playlistController;
    private final CategoryController categoryController;

    public StreamController(PlaylistController playlistController, CategoryController categoryController) {
        this.playlistController = playlistController;
        this.categoryController = categoryController;
        this.streams = new ConcurrentLinkedQueue<>(List.of(
                new StreamDto(idGenerator.incrementAndGet(), "new_vid", "\\STREAM.jpg",
                        "very new", 1, 1),
                new StreamDto(idGenerator.incrementAndGet(), "marmok", "\\mmmMARMOK.jpg",
                        "mmm MARMOK", 2, 1),
                new StreamDto(idGenerator.incrementAndGet(), "KING", "\\king.jpg",
                        "Bronny J", 2, 3)));
        this.streams.stream().forEach(stream -> {
            stream.setPlaylist(playlistController.get(stream.getPlaylistId()));
            stream.setCategory(categoryController.get(stream.getCategoryId()));
        });
    }

    @GetMapping
    public List<StreamDto> getMethodName() {
        log.debug("Get all streams");
        return streams.stream().toList();
    }

    @GetMapping("/{id}")
    public StreamDto get(@PathVariable("id") int id) {
        log.debug("Get stream with id {}", id);
        log.debug("Available streams IDs: {}", streams.stream().map(StreamDto::getId).toList());
        return streams.stream()
                .filter(stream -> stream.getId() == id)
                .findAny()
                .orElseThrow(() -> new NotFoundException(StreamDto.class, id));
    }

    @PostMapping
    public StreamDto create(@RequestBody StreamDto newStream) {
        log.debug("Create stream wtih data {}", newStream);
        newStream.setId(idGenerator.incrementAndGet());
        newStream.setPlaylist(playlistController.get(newStream.getPlaylistId()));
        newStream.setCategory(categoryController.get(newStream.getCategoryId()));
        streams.add(newStream);
        return newStream;
    }

    @PutMapping("/{id}")
    public StreamDto edit(@PathVariable("id") int id, @RequestBody StreamDto newStream) {
        log.debug("Edit stream wtih id {} and data {}", id, newStream);
        final StreamDto existsStream = get(id);
        existsStream.setName(newStream.getName());
        existsStream.setImage(newStream.getImage());
        existsStream.setDescription(newStream.getDescription());
        existsStream.setPlaylistId(newStream.getPlaylistId());
        existsStream.setPlaylist(playlistController.get(existsStream.getPlaylistId()));
        existsStream.setCategoryId(newStream.getCategoryId());
        existsStream.setCategory(categoryController.get(existsStream.getCategoryId()));
        return existsStream;
    }

    @DeleteMapping("/{id}")
    public StreamDto delete(@PathVariable("id") int id) {
        log.debug("Delete stream wtih id {}", id);
        final StreamDto stream = get(id);
        streams.remove(stream);
        return stream;
    }
}
