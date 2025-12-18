package ru.ulstu.is.server.entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;

@Entity
@Table(name = "categories")
public class CategoryEntity extends BaseEntity {
    @Column(length = 100, nullable = false, unique = true)
    private String name;

    @Column(name = "age_limit", nullable = false)
    private int ageLimit;

    @OneToMany(mappedBy = "category")
    @OrderBy("id ASC")
    private Set<CategoryStreamEntity> categoryStreams = new HashSet<>();

    public CategoryEntity() {
        super();
    }

    public CategoryEntity(String name, int ageLimit) {
        this();
        this.name = name;
        this.ageLimit = ageLimit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAgeLimit() {
        return ageLimit;
    }

    public void setAgeLimit(int ageLimit) {
        this.ageLimit = ageLimit;
    }

    public Set<CategoryStreamEntity> getCategoryStreams() {
        return categoryStreams;
    }

    public Set<StreamEntity> getStreams() {
        Set<StreamEntity> streams = new HashSet<>();
        for (CategoryStreamEntity cs : categoryStreams) {
            streams.add(cs.getStream());
        }
        return streams;
    }
}