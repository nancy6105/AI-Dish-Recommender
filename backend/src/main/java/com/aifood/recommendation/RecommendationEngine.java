package com.aifood.recommendation;

import org.springframework.stereotype.Component;

import com.aifood.dto.recommendation.RecommendationRequest;
import com.aifood.dto.recommendation.RecommendationResult;
import com.aifood.entity.dish.Dish;

@Component
public class RecommendationEngine {

    public RecommendationResult evaluate(Dish dish, RecommendationRequest request) {

        int totalScore = 0;
        StringBuilder reason = new StringBuilder();

        RecommendationResult cuisine = calculateCuisineScore(dish, request);
        totalScore += cuisine.getScore();
        reason.append(cuisine.getReason());

        RecommendationResult diet = calculateDietScore(dish, request);
        totalScore += diet.getScore();
        reason.append(diet.getReason());

        RecommendationResult meal = calculateMealScore(dish, request);
        totalScore += meal.getScore();
        reason.append(meal.getReason());

        RecommendationResult spice = calculateSpiceScore(dish, request);
        totalScore += spice.getScore();
        reason.append(spice.getReason());

        RecommendationResult budget = calculateBudgetScore(dish, request);
        totalScore += budget.getScore();
        reason.append(budget.getReason());

        return new RecommendationResult(totalScore, reason.toString().trim());
    }

    private RecommendationResult calculateCuisineScore(Dish dish, RecommendationRequest request) {

        int score = 0;
        StringBuilder reason = new StringBuilder();

        if (request.getCuisineId() != null &&
                dish.getCuisine().getId().equals(request.getCuisineId())) {

            score += 40;
            reason.append("Cuisine matched. ");
        }

        return new RecommendationResult(score, reason.toString());
    }

    private RecommendationResult calculateDietScore(Dish dish, RecommendationRequest request) {

        int score = 0;
        StringBuilder reason = new StringBuilder();

        if (request.getDietType() != null &&
                dish.getDietType() == request.getDietType()) {

            score += 25;
            reason.append("Diet matched. ");
        }

        return new RecommendationResult(score, reason.toString());
    }

    private RecommendationResult calculateMealScore(Dish dish, RecommendationRequest request) {

        int score = 0;
        StringBuilder reason = new StringBuilder();

        if (request.getMealType() != null &&
                dish.getMealType() == request.getMealType()) {

            score += 15;
            reason.append("Meal matched. ");
        }

        return new RecommendationResult(score, reason.toString());
    }

    private RecommendationResult calculateSpiceScore(Dish dish, RecommendationRequest request) {

        int score = 0;
        StringBuilder reason = new StringBuilder();

        if (request.getSpiceLevel() != null &&
                dish.getSpiceLevel() == request.getSpiceLevel()) {

            score += 10;
            reason.append("Spice level matched. ");
        }

        return new RecommendationResult(score, reason.toString());
    }

    private RecommendationResult calculateBudgetScore(Dish dish, RecommendationRequest request) {

        int score = 0;
        StringBuilder reason = new StringBuilder();

        if (request.getPriceCategory() != null &&
                dish.getPriceCategory() == request.getPriceCategory()) {

            score += 10;
            reason.append("Budget matched. ");
        }

        return new RecommendationResult(score, reason.toString());
    }
}