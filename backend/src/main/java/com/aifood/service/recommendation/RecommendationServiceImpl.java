package com.aifood.service.recommendation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.aifood.dto.recommendation.RecommendationRequest;
import com.aifood.dto.recommendation.RecommendationResponse;
import com.aifood.dto.recommendation.RecommendationResult;
import com.aifood.entity.dish.Dish;
import com.aifood.recommendation.RecommendationEngine;
import com.aifood.repository.dish.DishRepository;

@Service
public class RecommendationServiceImpl implements RecommendationService {
    private final DishRepository dishRepository;
    private final RecommendationEngine recommendationEngine;

    public RecommendationServiceImpl(DishRepository dishRepository, RecommendationEngine recommendationEngine) {
        this.dishRepository = dishRepository;
        this.recommendationEngine = recommendationEngine;
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
            response.setReason(result.getReason());

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
}
