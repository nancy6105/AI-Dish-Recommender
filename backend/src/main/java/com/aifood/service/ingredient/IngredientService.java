package com.aifood.service.ingredient;

import java.util.List;

import com.aifood.dto.ingredient.CreateIngredientRequest;
import com.aifood.dto.ingredient.IngredientResponse;

public interface IngredientService {

    IngredientResponse createIngredient(CreateIngredientRequest request);

    List<IngredientResponse> getAllIngredients();

    IngredientResponse getIngredientById(Long id);

    void deleteIngredient(Long id);
}