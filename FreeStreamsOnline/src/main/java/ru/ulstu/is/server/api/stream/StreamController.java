package ru.ulstu.is.server.api.stream;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Pageable;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import ru.ulstu.is.server.api.PageHelper;
import ru.ulstu.is.server.api.PageRs;
import ru.ulstu.is.server.configuration.Constants;
import ru.ulstu.is.server.service.StreamService;

@RestController
@RequestMapping(Constants.API_URL + StreamController.URL)
public class StreamController {
    public static final String URL = "/stream";
    private final StreamService streamService;

    public StreamController(StreamService streamService) {
        this.streamService = streamService;
    }

    @GetMapping
    public PageRs<StreamRs> getAll(
            @RequestParam(defaultValue = "1") @Min(1) int page,
            @RequestParam(defaultValue = "6") @Min(1) int size,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long playlistId,
            @RequestParam(required = false) String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {

        Pageable pageable;

        if (sortBy != null && !sortBy.isEmpty()) {
            // Создаем Pageable с сортировкой
            Sort sort = sortDirection.equalsIgnoreCase("desc")
                    ? Sort.by(sortBy).descending()
                    : Sort.by(sortBy).ascending();
            pageable = PageRequest.of(page - 1, size, sort);
        } else {
            // Без сортировки
            pageable = PageHelper.toPageable(page, size);
        }

        if (categoryId != null || playlistId != null) {
            return streamService.getFiltered(pageable, categoryId, playlistId);
        }
        return streamService.getAll(pageable);
    }

    @GetMapping("/{id}")
    public StreamRs get(@PathVariable("id") Long id) {
        return streamService.get(id);
    }

    @PostMapping
    public StreamRs create(@RequestBody @Valid StreamRq dto) {
        System.out.println("Creating stream with data: " + dto);
        return streamService.create(dto);
    }

    @PutMapping("/{id}")
    public StreamRs update(@PathVariable("id") Long id, @RequestBody @Valid StreamRq dto) {
        System.out.println("Updating stream " + id + " with data: " + dto);
        return streamService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public StreamRs delete(@PathVariable("id") Long id) {
        return streamService.delete(id);
    }
}
