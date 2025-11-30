package ru.ulstu.is.server.api.category;

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

import ru.ulstu.is.server.api.NotFoundException;
import ru.ulstu.is.server.configuration.Constants;

@RestController
@RequestMapping(Constants.API_URL + CategoryController.URL)
public class CategoryController {
    public static final String URL = "/category";
    private final ConcurrentLinkedQueue<CategoryDto> categories;
    private final Logger log = LoggerFactory.getLogger(CategoryController.class);
    private final AtomicInteger idGenerator = new AtomicInteger();

    public CategoryController() {
        this.categories = new ConcurrentLinkedQueue<>(List.of(
                new CategoryDto(idGenerator.incrementAndGet(), "jst vibing"),
                new CategoryDto(idGenerator.incrementAndGet(), "letsplay"),
                new CategoryDto(idGenerator.incrementAndGet(), "cooking")));
    }

    @GetMapping
    public List<CategoryDto> getAll() {
        log.debug("Get all categories");
        return categories.stream().toList();
    }

    @GetMapping("/{id}")
    public CategoryDto get(@PathVariable("id") int id) {
        log.debug("Get stream with id {}", id);
        log.debug("Available streams IDs: {}", categories.stream().map(CategoryDto::getId).toList());
        return categories.stream()
                .filter(category -> category.getId() == id)
                .findAny()
                .orElseThrow(() -> new NotFoundException(CategoryDto.class, id));
    }

    @PostMapping
    public CategoryDto create(@RequestBody CategoryDto newCategory) {
        log.debug("Create playlist with data {}", newCategory);
        newCategory.setId(idGenerator.incrementAndGet());
        categories.add(newCategory);
        return newCategory;
    }

    @DeleteMapping("/{id}")
    public CategoryDto delete(@PathVariable("id") int id) {
        log.debug("Delete playlist wtih id {}", id);
        final CategoryDto playlist = get(id);
        categories.remove(playlist);
        return playlist;
    }
}
