package com.aifood.controller.ingredient;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.aifood.dto.ingredient.CreateIngredientRequest;
import com.aifood.dto.ingredient.IngredientResponse;
import com.aifood.service.ingredient.IngredientService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/ingredients")
public class IngredientController {

    private final IngredientService ingredientService;

    public IngredientController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    @PostMapping
    public IngredientResponse createIngredient(@Valid @RequestBody CreateIngredientRequest request) {

        return ingredientService.createIngredient(request);
    }

    @GetMapping
    public List<IngredientResponse> getAllIngredients() {
        return ingredientService.getAllIngredients();
    }

    @GetMapping("/{id}")
    public IngredientResponse getIngredientById(@PathVariable Long id) {
        return ingredientService.getIngredientById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteIngredient(@PathVariable Long id) {
        ingredientService.deleteIngredient(id);
    }
}