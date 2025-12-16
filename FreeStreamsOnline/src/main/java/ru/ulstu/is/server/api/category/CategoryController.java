package ru.ulstu.is.server.api.category;

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
import ru.ulstu.is.server.service.CategoryService;

@RestController
@RequestMapping(Constants.API_URL + CategoryController.URL)
public class CategoryController {
    public static final String URL = "/category";

    public final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public List<CategoryRs> getAll() {
        return categoryService.getAll();
    }

    @GetMapping("/{id}")
    public CategoryRs get(@PathVariable("id") Long id) {
        return categoryService.get(id);
    }

    @PostMapping
    public CategoryRs create(@RequestBody @Valid CategoryRq dto) {
        return categoryService.create(dto);
    }

    @PutMapping("/{id}")
    public CategoryRs update(@PathVariable("id") Long id, @RequestBody @Valid CategoryRq dto) {
        return categoryService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public CategoryRs delete(@PathVariable("id") Long id) {
        return categoryService.delete(id);
    }

    @GetMapping("/stats")
    public List<CategoryStatsRs> getAllStats() {
        return categoryService.getAllCategoriesStats();
    }

    @GetMapping("/{id}/stats")
    public CategoryStatsRs getStats(@PathVariable("id") Long id) {
        return categoryService.getCategoryStats(id);
    }
}
