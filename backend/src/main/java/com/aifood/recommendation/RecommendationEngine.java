package com.aifood.recommendation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.aifood.dto.recommendation.RecommendationRequest;
import com.aifood.dto.recommendation.RecommendationResult;
import com.aifood.entity.dish.Dish;
import com.aifood.entity.ingredient.Ingredient;

@Component
public class RecommendationEngine {

    public RecommendationResult evaluate(Dish dish, RecommendationRequest request) {

        if (hasExcludedIngredient(dish, request)) {
            List<String> reasons = new ArrayList<>();

            for (Ingredient ingredient : dish.getIngredients()) {
                if (request.getExcludedIngredientIds() .contains(ingredient.getId())) {
                    reasons.add("Contains excluded ingredient: " + ingredient.getName());
                }
            }

            return new RecommendationResult(Integer.MIN_VALUE, reasons);
        }

        if (isUnavailable(dish, request)) {
            return new RecommendationResult(Integer.MIN_VALUE, List.of("Currently unavailable"));
        }

        List<RecommendationResult> results = List.of(
                calculateCuisineScore(dish, request),
                calculateDietScore(dish, request),
                calculateMealScore(dish, request),
                calculateSpiceScore(dish, request),
                calculateBudgetScore(dish, request),
                scorePreferredIngredients(dish, request),
                scoreProtein(dish, request),
                scoreCalories(dish, request),
                scorePrepTime(dish, request)
        );

        int totalScore = 0;
        List<String> reasons = new ArrayList<>();

        for (RecommendationResult result : results) {
            totalScore += result.getScore();
            reasons.addAll(result.getReasons());
        }

        return new RecommendationResult(totalScore, reasons);
    }

    private RecommendationResult calculateCuisineScore(Dish dish, RecommendationRequest request) {

        int score = 0;
        List<String> reasons = new ArrayList<>();

        if (request.getCuisineId() != null && dish.getCuisine().getId().equals(request.getCuisineId())) {

            score += 40;
            reasons.add("Cuisine matched. ");
        }

        return new RecommendationResult(score, reasons);
    }

    private RecommendationResult calculateDietScore(Dish dish, RecommendationRequest request) {

        int score = 0;
        List<String> reasons = new ArrayList<>();

        if (request.getDietType() != null && dish.getDietType() == request.getDietType()) {

            score += 25;
            reasons.add("Diet matched. ");
        }

        return new RecommendationResult(score, reasons);
    }

    private RecommendationResult calculateMealScore(Dish dish, RecommendationRequest request) {

        int score = 0;
        List<String> reasons = new ArrayList<>();

        if (request.getMealType() != null && dish.getMealType() == request.getMealType()) {

            score += 15;
            reasons.add("Meal matched. ");
        }

        return new RecommendationResult(score, reasons);
    }

    private RecommendationResult calculateSpiceScore(Dish dish, RecommendationRequest request) {

        int score = 0;
        List<String> reasons = new ArrayList<>();

        if (request.getSpiceLevel() != null && dish.getSpiceLevel() == request.getSpiceLevel()) {

            score += 10;
            reasons.add("Spice level matched. ");
        }

        return new RecommendationResult(score, reasons);
    }

    private RecommendationResult calculateBudgetScore(Dish dish, RecommendationRequest request) {

        int score = 0;
        List<String> reasons = new ArrayList<>();

        if (request.getPriceCategory() != null && dish.getPriceCategory() == request.getPriceCategory()) {
            score += 10;
            reasons.add("Budget matched. ");
        }

        return new RecommendationResult(score, reasons);
    }

    private RecommendationResult scorePreferredIngredients(Dish dish, RecommendationRequest request) {

        int score = 0;
        List<String> reasons = new ArrayList<>();

        if (request.getPreferredIngredientIds() == null
                || request.getPreferredIngredientIds().isEmpty()) {

            return new RecommendationResult(0, new ArrayList<>());
        }

        for (Ingredient ingredient : dish.getIngredients()) {

            if (request.getPreferredIngredientIds()
                    .contains(ingredient.getId())) {

                if (score >= 40) {
                    break;
                }

                score += 20;

                reasons.add(
                        "Contains preferred ingredient: "
                        + ingredient.getName()
                );
            }
        }
        return new RecommendationResult(score, reasons);
    }

    private RecommendationResult scoreProtein(Dish dish, RecommendationRequest request) {

        int score = 0;
        List<String> reasons = new ArrayList<>();

        if (request.getMinProtein() == null || dish.getProtein() == null) {
            return new RecommendationResult(0, reasons);
        }

        if (dish.getProtein() >= request.getMinProtein()) {
            score += 15;
            reasons.add("High protein. ");
        }

        return new RecommendationResult(score, reasons);
    }

    private RecommendationResult scoreCalories(Dish dish, RecommendationRequest request) {

        int score = 0;
        List<String> reasons = new ArrayList<>();

        if (request.getMaxCalories() == null || dish.getCalories() == null) {
            return new RecommendationResult(0, reasons);
        }

        if (dish.getCalories() <= request.getMaxCalories()) {
            score += 10;
            reasons.add("Low calories. ");
        }

        return new RecommendationResult(score, reasons);
    }

    private RecommendationResult scorePrepTime(Dish dish, RecommendationRequest request) {

        int score = 0;
        List<String> reasons = new ArrayList<>();

        if (request.getMaxPrepTime() == null || dish.getPrepTime() == null) {
            return new RecommendationResult(0, reasons);
        }

        if (dish.getPrepTime() <= request.getMaxPrepTime()) {
            score += 10;
            reasons.add("Quick to prepare. ");
        }

        return new RecommendationResult(score, reasons);
    }

    private boolean hasExcludedIngredient(Dish dish, RecommendationRequest request) {

        if (request.getExcludedIngredientIds() == null || request.getExcludedIngredientIds().isEmpty()) {
            return false;
        }

        for (Ingredient ingredient : dish.getIngredients()) {
            if (request.getExcludedIngredientIds().contains(ingredient.getId())) {
                return true;
            }
        }

        return false;
    }

    private boolean isUnavailable(Dish dish, RecommendationRequest request) {
        return Boolean.TRUE.equals(request.getOnlyAvailable()) && !Boolean.TRUE.equals(dish.getIsAvailable());
    }
}