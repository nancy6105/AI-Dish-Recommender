package com.aifood.controller.dish;

import com.aifood.dto.dish.CreateDishRequest;
import com.aifood.dto.dish.DishResponse;
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
}   