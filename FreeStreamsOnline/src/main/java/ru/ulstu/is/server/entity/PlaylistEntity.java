package ru.ulstu.is.server.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "playlists")
public class PlaylistEntity extends BaseEntity {
    @Column(nullable = false)
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
