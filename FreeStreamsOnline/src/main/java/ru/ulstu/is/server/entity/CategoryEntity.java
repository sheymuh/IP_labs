package ru.ulstu.is.server.entity;

public class CategoryEntity extends BaseEntity {
    private String name;

    public CategoryEntity() {
        super();
    }

    public CategoryEntity(String name) {
        this();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
