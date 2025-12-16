package ru.ulstu.is.server.entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;

@Entity
@Table(name = "streams")
public class StreamEntity extends BaseEntity {
    @Column(nullable = false)
    private String name;
    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(columnDefinition = "text")
    private String image;
    @Column(nullable = false)
    private String description;
    private int views;
    private String publicationDate;
    @OneToMany(mappedBy = "stream", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id ASC")
    private Set<CategoryStreamEntity> streamCategories = new HashSet<>();
    @JoinColumn(name = "category_id", nullable = false)
    @ManyToOne
    private PlaylistEntity playlist;

    public StreamEntity() {
        super();
    }

    public StreamEntity(String name, String image, String description, int views, String pubDate,
            PlaylistEntity playlist, CategoryEntity category) {

    public StreamEntity(String name, String image, String description,
            PlaylistEntity playlist) {
        this();
        this.name = name;
        this.image = image;
        this.description = description;
        this.views = views;
        this.publicationDate = pubDate;
        this.playlist = playlist;
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

    public Set<CategoryStreamEntity> getStreamCategories() {
        return streamCategories;
    }

    public void addCategory(CategoryStreamEntity streamCategory) {
        if (streamCategory.getStream() != this) {
            streamCategory.setStream(this);
        }
        streamCategories.add(streamCategory);
    }

    public void updateCategory(CategoryStreamEntity streamCategory) {
        if (streamCategory.getStream() != this) {
            return;
        }
        streamCategories.remove(streamCategory);
        streamCategories.add(streamCategory);
    }

    public void deleteCategory(CategoryStreamEntity streamCategory) {
        if (streamCategory.getStream() != this) {
            return;
        }
        streamCategories.remove(streamCategory);
    }

    public PlaylistEntity getPlaylist() {
        return playlist;
    }

    public void setPlaylist(PlaylistEntity newPlaylist) {
        playlist = newPlaylist;
    }
}
