package com.aifood.controller.dish;

import com.aifood.dto.dish.CreateDishRequest;
import com.aifood.dto.dish.DishResponse;
import com.aifood.dto.ingredient.IngredientResponse;
import com.aifood.service.dish.DishService;

import jakarta.validation.Valid;

import java.util.List;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dishes")
public class DishController {

    private final DishService dishService;

    public DishController(DishService dishService) {
        this.dishService = dishService;
    }

    @PostMapping
    public DishResponse createDish(
            @Valid @RequestBody CreateDishRequest request) {

        return dishService.createDish(request);
    }

    @GetMapping
    public List<DishResponse> getAllDishes() {
        return dishService.getAllDishes();
    }

    @GetMapping("/{id}")
    public DishResponse getDishById(@PathVariable Long id) {
        return dishService.getDishById(id);
    }


    @PostMapping("/{dishId}/ingredients/{ingredientId}")
    public void addIngredientToDish(
            @PathVariable Long dishId,
            @PathVariable Long ingredientId) {

        dishService.addIngredientToDish(dishId, ingredientId);
    }

    @DeleteMapping("/{dishId}/ingredients/{ingredientId}")
    public void removeIngredientFromDish(
            @PathVariable Long dishId,
            @PathVariable Long ingredientId) {

        dishService.removeIngredientFromDish(dishId, ingredientId);
    }

    @GetMapping("/{dishId}/ingredients")
    public List<IngredientResponse> getIngredientsByDish(
            @PathVariable Long dishId) {

        return dishService.getIngredientsByDish(dishId);
    }
}   