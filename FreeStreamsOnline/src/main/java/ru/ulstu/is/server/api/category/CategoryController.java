package ru.ulstu.is.server.api.category;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ru.ulstu.is.server.api.NotFoundException;
import ru.ulstu.is.server.configuration.Constants;

@RestController
@RequestMapping(Constants.API_URL + CategoryController.URL)
public class CategoryController {
    public static final String URL = "/category";
    private final List<CategoryDto> categories;

    public CategoryController() {
        this.categories = List.of(
                new CategoryDto(1, "jst vibing"),
                new CategoryDto(2, "letsplay"),
                new CategoryDto(3, "cooking"));
    }

    @GetMapping
    public List<CategoryDto> getAll() {
        return categories;
    }

    @GetMapping("/{id}")
    public CategoryDto get(@PathVariable int id) {
        return categories.stream()
                .filter(category -> category.getId() == id)
                .findAny()
                .orElseThrow(() -> new NotFoundException(CategoryDto.class, id));
    }
}
