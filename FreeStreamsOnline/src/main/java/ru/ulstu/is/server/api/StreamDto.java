package ru.ulstu.is.server.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

public class StreamDto {
    @JsonProperty(access = Access.READ_ONLY)
    private int id;
    private String name;
    private String image;
    private String description;
    private int playlistId;
    private int categoryId;
    @JsonProperty(access = Access.READ_ONLY)
    private PlaylistDto playlist;
    @JsonProperty(access = Access.READ_ONLY)
    private CategoryDto category;

    public StreamDto() {
    }

    public StreamDto(
            int id,
            String name,
            String image,
            String description,
            int playlistId,
            int categoryId) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.description = description;
        this.playlistId = playlistId;
        this.categoryId = categoryId;
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

    public int getPlaylistId() {
        return playlistId;
    }

    public void setPlaylistId(int playlistId) {
        this.playlistId = playlistId;
    }

    public PlaylistDto getPlaylist() {
        return playlist;
    }

    public void setPlaylist(PlaylistDto newPlaylist) {
        playlist = newPlaylist;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public CategoryDto getCategory() {
        return category;
    }

    public void setCategory(CategoryDto newCategory) {
        category = newCategory;
    }

    @Override
    public String toString() {
        return "StreamDto [name" + name + ", image=" +
                image + ", description=" + description + ", playlistId=" + playlistId + ", categoryId=" + categoryId
                + "]";
    }
}
