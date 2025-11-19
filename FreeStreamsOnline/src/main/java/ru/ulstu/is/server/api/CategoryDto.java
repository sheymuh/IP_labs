package ru.ulstu.is.server.api;

public class CategoryDto {
    private final int id;
    private final String name;

    public CategoryDto(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
