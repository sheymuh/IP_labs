package ru.ulstu.is.server.api.category;

import jakarta.validation.constraints.NotBlank;

public class CategoryRq {
    @NotBlank
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
