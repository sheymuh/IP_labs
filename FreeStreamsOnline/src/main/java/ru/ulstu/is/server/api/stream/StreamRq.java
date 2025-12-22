package ru.ulstu.is.server.api.stream;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import ru.ulstu.is.server.validation.PublicationDate;

public record StreamRq(
        @NotBlank(message = "Название не может быть пустым") String name,

        String image,

        @NotBlank(message = "Описание не может быть пустым") String description,

        @NotNull(message = "Количество просмотров обязательно") Integer views,

        @NotNull(message = "Дата публикации обязательна") @PublicationDate(minYear = 2020, allowFuture = false, message = "Дата публикации должна быть в формате YYYY-MM-DD, не в будущем и не раньше 2020 года") @JsonProperty("publication_date") String publicationDate,

        @NotNull(message = "Плейлист обязателен") Long playlistId,

        @NotNull(message = "Необходимо указать хотя бы одну категорию") List<Long> categoryIds) {
}