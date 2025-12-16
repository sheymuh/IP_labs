package ru.ulstu.is.server.api.category;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;

public class CategoryRq {
    @NotBlank
    private String name;
    @NotBlank
    @JsonProperty("age_limit")
    private int ageLimit;

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
