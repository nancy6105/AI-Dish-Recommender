package com.aifood.service.dish;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


import org.springframework.stereotype.Service;

import com.aifood.dto.dish.CreateDishRequest;
import com.aifood.dto.dish.DishResponse;
import com.aifood.dto.ingredient.IngredientResponse;
import com.aifood.entity.cuisine.Cuisine;
import com.aifood.entity.dish.Dish;
import com.aifood.entity.ingredient.Ingredient;
import com.aifood.mapper.dish.DishMapper;
import com.aifood.mapper.ingredient.IngredientMapper;
import com.aifood.repository.cuisine.CuisineRepository;
import com.aifood.repository.dish.DishRepository;
import com.aifood.repository.ingredient.IngredientRepository;

@Service
public class DishServiceImpl implements DishService {

    private final DishRepository dishRepository;

    private final CuisineRepository cuisineRepository;

    private final IngredientRepository ingredientRepository;
    
    

    public DishServiceImpl(DishRepository dishRepository, CuisineRepository cuisineRepository, IngredientRepository ingredientRepository) {
        this.dishRepository = dishRepository;
        this.cuisineRepository = cuisineRepository;
        this.ingredientRepository = ingredientRepository;
    }

    @Override
    public DishResponse createDish(CreateDishRequest request) {

        Dish dish = DishMapper.toEntity(request);

        Cuisine cuisine = cuisineRepository.findById(request.getCuisineId())
                .orElseThrow(() -> new RuntimeException("Cuisine not found"));

        dish.setCuisine(cuisine);

        dish.setCreatedAt(LocalDateTime.now());
        dish.setUpdatedAt(LocalDateTime.now());

        Dish savedDish = dishRepository.save(dish);

        return DishMapper.toResponse(savedDish);
    }

    @Override
    public List<DishResponse> getAllDishes() {
        List<Dish> dishes = dishRepository.findAll();
        return dishes.stream()
                .map(DishMapper :: toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public DishResponse getDishById(Long id) {

        Dish dish = dishRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dish not found"));

        return DishMapper.toResponse(dish);
    }

    @Override
    public void addIngredientToDish(Long dishId, Long ingredientId) {

        Dish dish = dishRepository.findById(dishId)
                .orElseThrow(() -> new RuntimeException("Dish not found"));

        Ingredient ingredient = ingredientRepository.findById(ingredientId)
                .orElseThrow(() -> new RuntimeException("Ingredient not found"));

        dish.getIngredients().add(ingredient);

        dishRepository.save(dish);
    }

    @Override
    public void removeIngredientFromDish(Long dishId, Long ingredientId) {

        Dish dish = dishRepository.findById(dishId)
                .orElseThrow(() -> new RuntimeException("Dish not found"));

        Ingredient ingredient = ingredientRepository.findById(ingredientId)
                .orElseThrow(() -> new RuntimeException("Ingredient not found"));

        dish.getIngredients().remove(ingredient);

        dishRepository.save(dish);
    }

    @Override
    public List<IngredientResponse> getIngredientsByDish(Long dishId) {

        Dish dish = dishRepository.findById(dishId)
                .orElseThrow(() -> new RuntimeException("Dish not found"));

        return dish.getIngredients()
                .stream()
                .map(IngredientMapper::toResponse)
                .toList();
    }

}
