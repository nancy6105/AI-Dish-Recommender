package com.aifood.mapper.ingredient;

import com.aifood.dto.ingredient.IngredientResponse;
import com.aifood.entity.ingredient.Ingredient;

public class IngredientMapper {

    private IngredientMapper() {
    }

    public static IngredientResponse toResponse(Ingredient ingredient) {

        IngredientResponse response = new IngredientResponse();

        response.setId(ingredient.getId());
        response.setName(ingredient.getName());
        response.setDescription(ingredient.getDescription());
        response.setCategory(ingredient.getCategory());

        return response;
    }
}