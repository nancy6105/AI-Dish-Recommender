package com.aifood.mapper.dish;

import com.aifood.dto.dish.CreateDishRequest;
import com.aifood.dto.dish.DishResponse;
import com.aifood.entity.dish.Dish;

public class DishMapper {

    public static Dish toEntity(CreateDishRequest request) {

        Dish dish = new Dish();

        dish.setName(request.getName());
        dish.setDescription(request.getDescription());
        dish.setMealType(request.getMealType());
        dish.setDietType(request.getDietType());
        dish.setSpiceLevel(request.getSpiceLevel());
        dish.setPriceCategory(request.getPriceCategory());
        dish.setCalories(request.getCalories());
        dish.setProtein(request.getProtein());
        dish.setCarbs(request.getCarbs());
        dish.setFat(request.getFat());
        dish.setPrepTime(request.getPrepTime());
        dish.setServingSize(request.getServingSize());
        dish.setImageUrl(request.getImageUrl());
        dish.setIsAvailable(request.getIsAvailable());

        return dish;
    }

    public static DishResponse toResponse(Dish dish) {

        DishResponse response = new DishResponse();

        response.setId(dish.getId());
        response.setName(dish.getName());
        response.setDescription(dish.getDescription());
        response.setCuisineId(dish.getCuisine().getId());
        response.setCuisineName(dish.getCuisine().getName());
        response.setMealType(dish.getMealType());
        response.setDietType(dish.getDietType());
        response.setSpiceLevel(dish.getSpiceLevel());
        response.setPriceCategory(dish.getPriceCategory());
        response.setCalories(dish.getCalories());
        response.setProtein(dish.getProtein());
        response.setCarbs(dish.getCarbs());
        response.setFat(dish.getFat());
        response.setPrepTime(dish.getPrepTime());
        response.setServingSize(dish.getServingSize());
        response.setImageUrl(dish.getImageUrl());
        response.setIsAvailable(dish.getIsAvailable());

        return response;
    }
}