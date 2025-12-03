package ru.ulstu.is.server.entity;

import java.util.Objects;

import jakarta.persistence.Embeddable;

@Embeddable
public class CategoryStreamId {
    private Long categoryId;
    private Long streamId;

    public CategoryStreamId() {
    }

    public CategoryStreamId(Long categoryId, Long streamId) {
        this.categoryId = categoryId;
        this.streamId = streamId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getStreamId() {
        return streamId;
    }

    public void setStreamId(Long streamId) {
        this.streamId = streamId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(categoryId, streamId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        CategoryStreamId other = (CategoryStreamId) obj;
        return Objects.equals(categoryId, other.categoryId) && Objects.equals(streamId, other.streamId);
    }
}
