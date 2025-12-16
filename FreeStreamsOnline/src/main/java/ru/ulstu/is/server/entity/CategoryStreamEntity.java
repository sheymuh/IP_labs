package ru.ulstu.is.server.entity;

import java.time.LocalDate;
import java.util.Objects;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

@Entity
@Table(name = "category_stream")
public class CategoryStreamEntity {
    @EmbeddedId
    private CategoryStreamId id = new CategoryStreamId();

    @ManyToOne
    @MapsId("categoryId")
    @JoinColumn(name = "category_id", nullable = false)
    private CategoryEntity category;

    @ManyToOne
    @MapsId("streamId")
    @JoinColumn(name = "stream_id", nullable = false)
    private StreamEntity stream;

    public CategoryStreamEntity() {
    }

    public CategoryStreamEntity(CategoryEntity category, StreamEntity stream) {
        this.category = category;
        this.stream = stream;
        this.id = new CategoryStreamId(category.getId(), stream.getId());
    }

    public CategoryStreamId getId() {
        return id;
    }

    public void setId(CategoryStreamId id) {
        this.id = id;
    }

    public CategoryEntity getCategory() {
        return category;
    }

    public void setCategory(CategoryEntity category) {
        this.category = category;
    }

    public StreamEntity getStream() {
        return stream;
    }

    public void setStream(StreamEntity stream) {
        this.stream = stream;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        CategoryStreamEntity other = (CategoryStreamEntity) obj;
        return Objects.equals(id, other.id);
    }
}
