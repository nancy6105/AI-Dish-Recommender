package com.aifood.service.preference;


import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.aifood.dto.preference.CreateUserPreferenceRequest;
import com.aifood.dto.preference.UserPreferenceResponse;
import com.aifood.entity.ingredient.Ingredient;
import com.aifood.entity.preference.UserPreference;
import com.aifood.entity.user.User;
import com.aifood.exception.ResourceNotFoundException;
import com.aifood.repository.ingredient.IngredientRepository;
import com.aifood.repository.preference.UserPreferenceRepository;
import com.aifood.repository.user.UserRepository;

@Service
public class UserPreferenceServiceImpl implements UserPreferenceService{

    private final UserPreferenceRepository userPreferenceRepository;
    private final UserRepository userRepository;
    private final IngredientRepository ingredientRepository;

    public UserPreferenceServiceImpl(UserPreferenceRepository userPreferenceRepository, UserRepository userRepository, IngredientRepository ingredientRepository) {
        this.userPreferenceRepository = userPreferenceRepository;
        this.userRepository = userRepository;
        this.ingredientRepository = ingredientRepository;
    }

    @Override
    public UserPreferenceResponse createOrUpdateUserPreference (CreateUserPreferenceRequest request){

        Long userId = getCurrentUserId();
        User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

        UserPreference preference = userPreferenceRepository.findByUserId(userId)
                                        .orElse(new UserPreference());

        preference.setUser(user);
        preference.setDietType(request.getDietType());
        preference.setMealType(request.getMealType());
        preference.setSpiceLevel(request.getSpiceLevel());
        preference.setPriceCategory(request.getPriceCategory());
        preference.setMaxCalories(request.getMaxCalories());
        preference.setMinProtein(request.getMinProtein());
        preference.setMaxPrepTime(request.getMaxPrepTime());
        preference.setOnlyAvailable(request.getOnlyAvailable());

        if (request.getPreferredIngredientIds() != null) {

            List<Ingredient> preferredIngredients = findIngredientsByIds(request.getPreferredIngredientIds());

            preference.setPreferredIngredients(preferredIngredients);
        }

        if (request.getExcludedIngredientIds() != null) {

            List<Ingredient> excludedIngredients = findIngredientsByIds(request.getExcludedIngredientIds());

            preference.setExcludedIngredients(excludedIngredients);
        }

        UserPreference savedPreference = userPreferenceRepository.save(preference);

        return toResponse(savedPreference);
    }

    @Override
    public UserPreferenceResponse getMyPreference(){
        Long userId = getCurrentUserId();

        UserPreference preference = userPreferenceRepository.findByUserId(userId)
                                    .orElseThrow(() -> new RuntimeException("Preference not found"));

        return toResponse(preference);
    }

    private UserPreferenceResponse toResponse(UserPreference preference){
        UserPreferenceResponse response =
                new UserPreferenceResponse();

        response.setId(preference.getId());
        response.setDietType(preference.getDietType());
        response.setMealType(preference.getMealType());
        response.setSpiceLevel(preference.getSpiceLevel());
        response.setPriceCategory(preference.getPriceCategory());
        response.setMaxCalories(preference.getMaxCalories());
        response.setMinProtein(preference.getMinProtein());
        response.setMaxPrepTime(preference.getMaxPrepTime());
        response.setOnlyAvailable(preference.getOnlyAvailable());

        response.setPreferredIngredientIds(preference.getPreferredIngredients()
                                                    .stream()
                                                    .map(Ingredient::getId)
                                                    .toList());

        response.setExcludedIngredientIds(preference.getExcludedIngredients()
                                                    .stream()
                                                    .map(Ingredient::getId)
                                                    .toList());

        return response;
    }


    private Long getCurrentUserId() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return user.getId();
    }

    private List<Ingredient> findIngredientsByIds(List<Long> ids) {

        List<Ingredient> ingredients = new ArrayList<>();

        for (Long id : ids) {

            Ingredient ingredient = ingredientRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Ingredient not found with id: " + id));

            ingredients.add(ingredient);
        }

        return ingredients;
    }
}
