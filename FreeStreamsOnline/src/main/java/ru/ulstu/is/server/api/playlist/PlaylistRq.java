package ru.ulstu.is.server.api.playlist;

import jakarta.validation.constraints.NotBlank;

public record PlaylistRq(@NotBlank String name) {
}
