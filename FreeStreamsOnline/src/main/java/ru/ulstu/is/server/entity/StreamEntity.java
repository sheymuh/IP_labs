package ru.ulstu.is.server.entity;

import java.time.LocalDate;
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

    @Column(nullable = false)
    private int views;

    @Column(name = "publication_date", nullable = false)
    private LocalDate publicationDate;

    @JoinColumn(name = "playlist_id", nullable = false)
    @ManyToOne
    private PlaylistEntity playlist;

    @OneToMany(mappedBy = "stream", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id ASC")
    private Set<CategoryStreamEntity> streamCategories = new HashSet<>();

    public StreamEntity() {
        super();
    }

    public StreamEntity(String name, String image, String description, int views, LocalDate pubDate,
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

    public LocalDate getPubDate() {
        return publicationDate;
    }

    public void setPubDate(LocalDate pubDate) {
        this.publicationDate = pubDate;
    }

    public PlaylistEntity getPlaylist() {
        return playlist;
    }

    public void setPlaylist(PlaylistEntity newPlaylist) {
        playlist = newPlaylist;
    }

    public Set<CategoryStreamEntity> getStreamCategories() {
        return streamCategories;
    }

    public void addCategory(CategoryEntity category) {
        CategoryStreamEntity categoryStream = new CategoryStreamEntity(category, this);
        streamCategories.add(categoryStream);
        category.getCategoryStreams().add(categoryStream);
    }

    public void removeCategory(CategoryEntity category) {
        CategoryStreamEntity categoryStream = streamCategories.stream()
                .filter(cs -> cs.getCategory().equals(category))
                .findFirst()
                .orElse(null);

        if (categoryStream != null) {
            streamCategories.remove(categoryStream);
            category.getCategoryStreams().remove(categoryStream);
            categoryStream.setCategory(null);
            categoryStream.setStream(null);
        }
    }

    public Set<CategoryEntity> getCategories() {
        Set<CategoryEntity> categories = new HashSet<>();
        for (CategoryStreamEntity cs : streamCategories) {
            categories.add(cs.getCategory());
        }
        return categories;
    }
}
