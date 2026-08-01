package com.aifood.service.dish;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


import org.springframework.stereotype.Service;

import com.aifood.dto.dish.CreateDishRequest;
import com.aifood.dto.dish.DishResponse;
import com.aifood.entity.cuisine.Cuisine;
import com.aifood.entity.dish.Dish;
import com.aifood.mapper.dish.DishMapper;
import com.aifood.repository.cuisine.CuisineRepository;
import com.aifood.repository.dish.DishRepository;

@Service
public class DishServiceImpl implements DishService {

    private final DishRepository dishRepository;

    private final CuisineRepository cuisineRepository;

    public DishServiceImpl(DishRepository dishRepository, CuisineRepository cuisineRepository) {
        this.dishRepository = dishRepository;
        this.cuisineRepository = cuisineRepository;
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

}
