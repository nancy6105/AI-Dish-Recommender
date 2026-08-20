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

        if(hasExcludedIngredient(dish, request)){
            List<String> reasons = new ArrayList<>();
            for(Ingredient ingredient : dish.getIngredients()){
                if(request.getExcludedIngredientIds().contains(ingredient.getId())){
                    reasons.add("Contains excluded ingredient: "+ ingredient.getName());
                }
            }

            return new RecommendationResult(Integer.MIN_VALUE, reasons);
        }


        if(isUnavailable(dish, request)){
            return new RecommendationResult(Integer.MIN_VALUE, List.of("Currently unavailable"));
        }

        int totalScore = 0;
        List<String> reasons = new ArrayList<>();

        RecommendationResult cuisine = calculateCuisineScore(dish, request);
        totalScore += cuisine.getScore();
        reasons.addAll(cuisine.getReasons());

        RecommendationResult diet = calculateDietScore(dish, request);
        totalScore += diet.getScore();
        reasons.addAll(diet.getReasons());

        RecommendationResult meal = calculateMealScore(dish, request);
        totalScore += meal.getScore();
        reasons.addAll(meal.getReasons());

        RecommendationResult spice = calculateSpiceScore(dish, request);
        totalScore += spice.getScore();
        reasons.addAll(spice.getReasons());

        RecommendationResult budget = calculateBudgetScore(dish, request);
        totalScore += budget.getScore();
        reasons.addAll(budget.getReasons());

        RecommendationResult preferred = scorePreferredIngredients(dish, request);

        totalScore += preferred.getScore();
        reasons.addAll(preferred.getReasons());
        
        RecommendationResult protein = scoreProtein(dish, request);

        totalScore += protein.getScore();
        reasons.addAll(protein.getReasons());

        RecommendationResult calories = scoreCalories(dish, request);

        totalScore += calories.getScore();
        reasons.addAll(calories.getReasons());

        RecommendationResult prepTime = scorePrepTime(dish, request);

        totalScore += prepTime.getScore();
        reasons.addAll(prepTime.getReasons());

        return new RecommendationResult(totalScore, reasons);
    }

    private RecommendationResult calculateCuisineScore(Dish dish, RecommendationRequest request) {

        int score = 0;
        List<String> reasons = new ArrayList<>();

        if (request.getCuisineId() != null &&
                dish.getCuisine().getId().equals(request.getCuisineId())) {

            score += 40;
            reasons.add("Cuisine matched. ");
        }

        return new RecommendationResult(score, reasons);
    }

    private RecommendationResult calculateDietScore(Dish dish, RecommendationRequest request) {

        int score = 0;
        List<String> reasons = new ArrayList<>();

        if (request.getDietType() != null &&
                dish.getDietType() == request.getDietType()) {

            score += 25;
            reasons.add("Diet matched. ");
        }

        return new RecommendationResult(score, reasons);
    }

    private RecommendationResult calculateMealScore(Dish dish, RecommendationRequest request) {

        int score = 0;
        List<String> reasons = new ArrayList<>();

        if (request.getMealType() != null &&
                dish.getMealType() == request.getMealType()) {

            score += 15;
            reasons.add("Meal matched. ");
        }

        return new RecommendationResult(score, reasons);
    }

    private RecommendationResult calculateSpiceScore(Dish dish, RecommendationRequest request) {

        int score = 0;
        List<String> reasons = new ArrayList<>();

        if (request.getSpiceLevel() != null &&
                dish.getSpiceLevel() == request.getSpiceLevel()) {

            score += 10;
            reasons.add("Spice level matched. ");
        }

        return new RecommendationResult(score, reasons);
    }

    private RecommendationResult calculateBudgetScore(Dish dish, RecommendationRequest request) {

        int score = 0;
        List<String> reasons = new ArrayList<>();

        if (request.getPriceCategory() != null &&
                dish.getPriceCategory() == request.getPriceCategory()) {

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

                score += 20;

                reasons.add("Contains preferred ingredient: " + ingredient.getName());
            }
        }

        return new RecommendationResult(score, reasons);
    }

    private RecommendationResult scoreProtein(Dish dish, RecommendationRequest request) {

        int score = 0;
        List<String> reasons = new ArrayList<>();

        if (request.getMinProtein() == null || dish.getProtein() == null) {
            return new RecommendationResult(0, new ArrayList<>());
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
            return new RecommendationResult(0, new ArrayList<>());
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
            return new RecommendationResult(0, new ArrayList<>());
        }

        if (dish.getPrepTime() <= request.getMaxPrepTime()) {
            score += 10;
            reasons.add("Quick to prepare. ");
        }

        return new RecommendationResult(score, reasons);
    }

    private boolean hasExcludedIngredient(Dish dish, RecommendationRequest request){

        if(request.getExcludedIngredientIds() == null || request.getExcludedIngredientIds().isEmpty()){
            return false;
        }

        for(Ingredient ingredient : dish.getIngredients()){
            if(request.getExcludedIngredientIds().contains(ingredient.getId())){
                return true;
            }
        }

        return false;
    }


    private boolean isUnavailable(Dish dish, RecommendationRequest request){

        return Boolean.TRUE.equals(request.getOnlyAvailable()) && !Boolean.TRUE.equals(dish.getIsAvailable());
    }
}