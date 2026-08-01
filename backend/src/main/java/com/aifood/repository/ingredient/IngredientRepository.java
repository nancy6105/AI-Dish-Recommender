package com.aifood.repository.ingredient;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aifood.entity.ingredient.Ingredient;

public interface IngredientRepository
        extends JpaRepository<Ingredient, Long> {

    Optional<Ingredient> findByName(String name);

    boolean existsByName(String name);
}