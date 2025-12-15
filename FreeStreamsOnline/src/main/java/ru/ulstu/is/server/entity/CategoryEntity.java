package ru.ulstu.is.server.entity;

public class CategoryEntity extends BaseEntity {
    private String name;
    private int ageLimit;

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
}
