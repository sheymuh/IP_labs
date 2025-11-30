package ru.ulstu.is.server.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

public class PlaylistDto {
    @JsonProperty(access = Access.READ_ONLY)
    private int id;
    private String name;

    public PlaylistDto() {
    }

    public PlaylistDto(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
}
