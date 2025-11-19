package ru.ulstu.is.server.entity;

public abstract class BaseEntity {
    protected Long id;

    protected BaseEntity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
