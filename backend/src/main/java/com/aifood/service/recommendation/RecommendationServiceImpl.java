package com.aifood.service.recommendation;


import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


import com.aifood.dto.recommendation.RecommendationRequest;
import com.aifood.dto.recommendation.RecommendationResponse;
import com.aifood.dto.recommendation.RecommendationResult;
import com.aifood.entity.dish.Dish;
import com.aifood.entity.ingredient.Ingredient;
import com.aifood.entity.preference.UserPreference;
import com.aifood.entity.user.User;
import com.aifood.recommendation.RecommendationEngine;
import com.aifood.repository.dish.DishRepository;
import com.aifood.repository.preference.UserPreferenceRepository;
import com.aifood.repository.user.UserRepository;

@Service
public class RecommendationServiceImpl implements RecommendationService {
    private final DishRepository dishRepository;
    private final RecommendationEngine recommendationEngine;
    private final UserPreferenceRepository userPreferenceRepository;
    private final UserRepository userRepository;

    public RecommendationServiceImpl(DishRepository dishRepository, RecommendationEngine recommendationEngine,UserPreferenceRepository userPreferenceRepository, UserRepository userRepository) {
        this.dishRepository = dishRepository;
        this.recommendationEngine = recommendationEngine;
        this.userPreferenceRepository = userPreferenceRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<RecommendationResponse> recommendDish(RecommendationRequest request) {

        List<Dish> dishes = dishRepository.findAll();

        List<RecommendationResponse> recommendations = new ArrayList<>();

        for (Dish dish : dishes) {

            RecommendationResult result =
                    recommendationEngine.evaluate(dish, request);

            RecommendationResponse response =
                    new RecommendationResponse();

            response.setDishId(dish.getId());
            response.setDishName(dish.getName());
            response.setCuisine(dish.getCuisine().getName());

            response.setScore(result.getScore());
            response.setReasons(result.getReasons());

            if (result.getScore() > 0) {
                recommendations.add(response);
            }
        }

        recommendations.sort(
                (a, b) -> Integer.compare(b.getScore(), a.getScore()));

        if (recommendations.size() > 5) {
            return recommendations.subList(0, 5);
        }

        return recommendations;
    }

    @Override
    public List<RecommendationResponse> recommendMyDishes() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        UserPreference preference = userPreferenceRepository.findByUserId(getUserIdByEmail(email)).orElseThrow(() -> new RuntimeException("User preferences not found"));

        RecommendationRequest request = new RecommendationRequest();

        request.setDietType(preference.getDietType());
        request.setMealType(preference.getMealType());
        request.setSpiceLevel(preference.getSpiceLevel());
        request.setPriceCategory(preference.getPriceCategory());
        request.setMaxCalories(preference.getMaxCalories());
        request.setMinProtein(preference.getMinProtein());
        request.setMaxPrepTime(preference.getMaxPrepTime());
        request.setOnlyAvailable(preference.getOnlyAvailable());
        

        request.setPreferredIngredientIds(preference.getPreferredIngredients()
                                            .stream()
                                            .map(Ingredient::getId)
                                            .toList());

        request.setExcludedIngredientIds(preference.getExcludedIngredients()
                                            .stream()
                                            .map(Ingredient::getId)
                                            .toList());

        System.out.println("Preferred IDs: " + request.getPreferredIngredientIds());
        System.out.println("Excluded IDs: " + request.getExcludedIngredientIds());
        System.out.println("Only Available: " + request.getOnlyAvailable());

        return recommendDish(request);
    }

    private Long getUserIdByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

        return user.getId();
    }
}
