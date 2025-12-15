package ru.ulstu.is.server.entity;

public class StreamEntity extends BaseEntity {
    private String name;
    private String image;
    private String description;
    private int views;
    private String publicationDate;
    private PlaylistEntity playlist;
    private CategoryEntity category;

    public StreamEntity() {
        super();
    }

    public StreamEntity(String name, String image, String description, int views, String pubDate,
            PlaylistEntity playlist, CategoryEntity category) {
        this.name = name;
        this.image = image;
        this.description = description;
        this.views = views;
        this.publicationDate = pubDate;
        this.playlist = playlist;
        this.category = category;
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

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public String getPubDate() {
        return publicationDate;
    }

    public PlaylistEntity getPlaylist() {
        return playlist;
    }

    public void setPlaylist(PlaylistEntity newPlaylist) {
        playlist = newPlaylist;
    }

    public CategoryEntity getCategory() {
        return category;
    }

    public void setCategory(CategoryEntity newCategory) {
        category = newCategory;
    }
}
