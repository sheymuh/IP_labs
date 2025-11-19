package ru.ulstu.is.server.entity;

public class PlaylistEntity extends BaseEntity {
    private String name;

    public PlaylistEntity() {
        super();
    }

    public PlaylistEntity(String name) {
        this();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
