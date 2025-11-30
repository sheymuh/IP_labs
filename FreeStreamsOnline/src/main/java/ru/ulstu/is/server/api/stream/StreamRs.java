package ru.ulstu.is.server.api.stream;

import ru.ulstu.is.server.api.category.CategoryRs;
import ru.ulstu.is.server.api.playlist.PlaylistRs;

public class StreamRs {
    private Long id;
    private String name;
    private String image;
    private String description;
    private PlaylistRs playlist;
    private CategoryRs category;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public PlaylistRs getPlaylist() {
        return playlist;
    }

    public void setPlaylist(PlaylistRs newPlaylist) {
        playlist = newPlaylist;
    }

    public CategoryRs getCategory() {
        return category;
    }

    public void setCategory(CategoryRs newCategory) {
        category = newCategory;
    }
}
