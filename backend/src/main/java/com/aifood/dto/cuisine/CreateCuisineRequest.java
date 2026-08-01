package com.aifood.dto.cuisine;

import jakarta.validation.constraints.NotBlank;

public class CreateCuisineRequest {
    @NotBlank(message = "Cuisine name is required")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
