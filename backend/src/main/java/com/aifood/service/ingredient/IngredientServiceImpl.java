package com.aifood.service.ingredient;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.aifood.dto.ingredient.CreateIngredientRequest;
import com.aifood.dto.ingredient.IngredientResponse;
import com.aifood.entity.ingredient.Ingredient;
import com.aifood.mapper.ingredient.IngredientMapper;
import com.aifood.repository.ingredient.IngredientRepository;

@Service
public class IngredientServiceImpl implements IngredientService {

    private final IngredientRepository ingredientRepository;

    public IngredientServiceImpl(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    @Override
    public IngredientResponse createIngredient(CreateIngredientRequest request) {

        if (ingredientRepository.existsByName(request.getName())) {
            throw new RuntimeException("Ingredient already exists.");
        }

        Ingredient ingredient = new Ingredient();
        ingredient.setName(request.getName());
        ingredient.setDescription(request.getDescription());
        ingredient.setCategory(request.getCategory());

        Ingredient savedIngredient = ingredientRepository.save(ingredient);

        return IngredientMapper.toResponse(savedIngredient);
    }

    @Override
    public List<IngredientResponse> getAllIngredients() {

        return ingredientRepository.findAll()
                .stream()
                .map(IngredientMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public IngredientResponse getIngredientById(Long id) {

        Ingredient ingredient = ingredientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ingredient not found."));

        return IngredientMapper.toResponse(ingredient);
    }

    @Override
    public void deleteIngredient(Long id) {

        Ingredient ingredient = ingredientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ingredient not found."));

        ingredientRepository.delete(ingredient);
    }
}