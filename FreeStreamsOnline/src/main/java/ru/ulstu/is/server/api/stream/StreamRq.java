package ru.ulstu.is.server.api.stream;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record StreamRq(
        @NotBlank String name,
        String image,
        String description,
        @NotNull int views,
        @NotNull @JsonProperty("publication_date") String publicationDate,
        @NotNull Long playlistId,
        @NotNull List<Long> categoryIds) { // Список ID категорий
}