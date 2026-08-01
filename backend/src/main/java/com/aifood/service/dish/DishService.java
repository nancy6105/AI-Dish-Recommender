package com.aifood.service.dish;

import java.util.List;

import com.aifood.dto.dish.CreateDishRequest;
import com.aifood.dto.dish.DishResponse;


public interface DishService {

    DishResponse createDish(CreateDishRequest request);

    List<DishResponse> getAllDishes();

    DishResponse getDishById(Long id);
    
}