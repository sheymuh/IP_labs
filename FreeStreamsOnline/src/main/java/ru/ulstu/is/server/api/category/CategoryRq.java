package ru.ulstu.is.server.api.category;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;

public record CategoryRq(@NotBlank String name, @JsonProperty("age_limit") int ageLimit) {
}
