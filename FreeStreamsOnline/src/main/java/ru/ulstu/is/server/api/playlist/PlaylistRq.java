package ru.ulstu.is.server.api.playlist;

import jakarta.validation.constraints.NotBlank;

public class PlaylistRq {
    @NotBlank
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
