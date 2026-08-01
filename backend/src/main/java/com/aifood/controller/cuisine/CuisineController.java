package com.aifood.controller.cuisine;

import java.util.List;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.aifood.dto.cuisine.CreateCuisineRequest;
import com.aifood.dto.cuisine.CuisineResponse;
import com.aifood.service.cuisine.CuisineService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/cuisines")
@Validated
public class CuisineController {

    private final CuisineService cuisineService;

    public CuisineController(CuisineService cuisineService) {
        this.cuisineService = cuisineService;
    }

    @PostMapping
    public CuisineResponse createCuisine(
            @Valid @RequestBody CreateCuisineRequest request) {

        return cuisineService.createCuisine(request);
    }

    @GetMapping
    public List<CuisineResponse> getAllCuisines() {

        return cuisineService.getAllCuisines();
    }
}