package ru.ulstu.is.server.api.playlist;

public class PlaylistDto {
    private final int id;
    private final String name;

    public PlaylistDto(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
