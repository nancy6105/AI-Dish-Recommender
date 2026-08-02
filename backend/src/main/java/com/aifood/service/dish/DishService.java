package com.aifood.service.dish;

import java.util.List;

import com.aifood.dto.dish.CreateDishRequest;
import com.aifood.dto.dish.DishResponse;
import com.aifood.dto.ingredient.IngredientResponse;


public interface DishService {

    DishResponse createDish(CreateDishRequest request);

    List<DishResponse> getAllDishes();

    DishResponse getDishById(Long id);

    void addIngredientToDish(Long dishId, Long ingredientId);

    void removeIngredientFromDish(Long dishId, Long ingredientId);

    List<IngredientResponse> getIngredientsByDish(Long dishId);
    
}