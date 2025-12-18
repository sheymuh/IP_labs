package ru.ulstu.is.server.api.category;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CategoryRq(@NotBlank String name, @NotNull @JsonProperty("age_limit") int ageLimit) {
}
