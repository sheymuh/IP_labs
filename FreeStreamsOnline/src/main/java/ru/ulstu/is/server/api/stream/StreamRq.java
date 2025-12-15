package ru.ulstu.is.server.api.stream;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class StreamRq {
    @NotBlank
    private String name;
    private String image;
    private String description;
    private int views;
    @JsonProperty("publication_date")
    private String publicationDate;
    @NotNull
    private Long categoryId;
    @NotNull
    private Long playlistId;

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

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public String getPubDate() {
        return publicationDate;
    }

    public void setPubDate(String pubDate) {
        this.publicationDate = pubDate;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getPlaylistId() {
        return playlistId;
    }

    public void setPlaylistId(Long playlistId) {
        this.playlistId = playlistId;
    }
}
