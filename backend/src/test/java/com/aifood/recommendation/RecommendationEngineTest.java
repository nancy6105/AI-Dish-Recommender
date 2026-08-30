package com.aifood.recommendation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.aifood.dto.recommendation.RecommendationRequest;
import com.aifood.dto.recommendation.RecommendationResult;
import com.aifood.entity.dish.Dish;
import com.aifood.entity.ingredient.Ingredient;
import com.aifood.enums.DietType;
import com.aifood.enums.MealType;
import com.aifood.enums.PriceCategory;
import com.aifood.enums.SpiceLevel;

class RecommendationEngineTest {

    private RecommendationEngine recommendationEngine;

    @BeforeEach
    void setUp() {
        recommendationEngine = new RecommendationEngine();
    }

    @Test
    void shouldGive20PointsForOnePreferredIngredient() {

        Ingredient paneer =
                createIngredient(4L, "Paneer");

        Dish dish = createDish();

        dish.setIngredients(Set.of(paneer));

        RecommendationRequest request =
                new RecommendationRequest();

        request.setPreferredIngredientIds(
                List.of(4L)
        );

        RecommendationResult result =
                recommendationEngine.evaluate(
                        dish,
                        request
                );

        assertEquals(20, result.getScore());

        assertEquals(
                1,
                result.getReasons().size()
        );

        assertTrue(
                result.getReasons().contains(
                        "Contains preferred ingredient: Paneer"
                )
        );
    }

    @Test
    void shouldGive40PointsForTwoPreferredIngredients() {

        Ingredient paneer =
                createIngredient(4L, "Paneer");

        Ingredient tomato =
                createIngredient(5L, "Tomato");

        Dish dish = createDish();

        dish.setIngredients(
                Set.of(paneer, tomato)
        );

        RecommendationRequest request =
                new RecommendationRequest();

        request.setPreferredIngredientIds(
                List.of(4L, 5L)
        );

        RecommendationResult result =
                recommendationEngine.evaluate(
                        dish,
                        request
                );

        assertEquals(40, result.getScore());

        assertEquals(
                2,
                result.getReasons().size()
        );
    }

    @Test
    void shouldCapPreferredIngredientScoreAt40() {

        Ingredient paneer =
                createIngredient(4L, "Paneer");

        Ingredient tomato =
                createIngredient(5L, "Tomato");

        Ingredient onion =
                createIngredient(6L, "Onion");

        Dish dish = createDish();

        dish.setIngredients(
                Set.of(
                        paneer,
                        tomato,
                        onion
                )
        );

        RecommendationRequest request =
                new RecommendationRequest();

        request.setPreferredIngredientIds(
                List.of(4L, 5L, 6L)
        );

        RecommendationResult result =
                recommendationEngine.evaluate(
                        dish,
                        request
                );

        assertEquals(40, result.getScore());

        assertEquals(
                2,
                result.getReasons().size()
        );
    }

    @Test
    void shouldRejectDishWithExcludedIngredient() {

        Ingredient butter =
                createIngredient(3L, "Butter");

        Ingredient paneer =
                createIngredient(4L, "Paneer");

        Dish dish = createDish();

        dish.setIngredients(
                Set.of(
                        butter,
                        paneer
                )
        );

        RecommendationRequest request =
                new RecommendationRequest();

        request.setExcludedIngredientIds(
                List.of(3L)
        );

        RecommendationResult result =
                recommendationEngine.evaluate(
                        dish,
                        request
                );

        assertEquals(
                Integer.MIN_VALUE,
                result.getScore()
        );

        assertTrue(
                result.getReasons().contains(
                        "Contains excluded ingredient: Butter"
                )
        );
    }

    @Test
    void shouldRejectUnavailableDishWhenOnlyAvailableIsTrue() {

        Dish dish = createDish();

        dish.setIsAvailable(false);

        RecommendationRequest request =
                new RecommendationRequest();

        request.setOnlyAvailable(true);

        RecommendationResult result =
                recommendationEngine.evaluate(
                        dish,
                        request
                );

        assertEquals(
                Integer.MIN_VALUE,
                result.getScore()
        );

        assertTrue(
                result.getReasons().contains(
                        "Currently unavailable"
                )
        );
    }

    @Test
    void shouldAllowUnavailableDishWhenOnlyAvailableIsFalse() {

        Dish dish = createDish();

        dish.setIsAvailable(false);

        RecommendationRequest request =
                new RecommendationRequest();

        request.setOnlyAvailable(false);

        RecommendationResult result =
                recommendationEngine.evaluate(
                        dish,
                        request
                );

        assertEquals(
                0,
                result.getScore()
        );

        assertTrue(
                result.getReasons().isEmpty()
        );
    }

    @Test
    void shouldGive25PointsForMatchingDiet() {

        Dish dish = createDish();

        dish.setDietType(
                DietType.VEG
        );

        RecommendationRequest request =
                new RecommendationRequest();

        request.setDietType(
                DietType.VEG
        );

        RecommendationResult result =
                recommendationEngine.evaluate(
                        dish,
                        request
                );

        assertEquals(
                25,
                result.getScore()
        );

        assertTrue(
                result.getReasons().contains(
                        "Diet matched. "
                )
        );
    }

    @Test
    void shouldGive15PointsForMatchingMeal() {

        Dish dish = createDish();

        dish.setMealType(
                MealType.DINNER
        );

        RecommendationRequest request =
                new RecommendationRequest();

        request.setMealType(
                MealType.DINNER
        );

        RecommendationResult result =
                recommendationEngine.evaluate(
                        dish,
                        request
                );

        assertEquals(
                15,
                result.getScore()
        );

        assertTrue(
                result.getReasons().contains(
                        "Meal matched. "
                )
        );
    }

    @Test
    void shouldGive10PointsForMatchingSpiceLevel() {

        Dish dish = createDish();

        dish.setSpiceLevel(
                SpiceLevel.MEDIUM
        );

        RecommendationRequest request =
                new RecommendationRequest();

        request.setSpiceLevel(
                SpiceLevel.MEDIUM
        );

        RecommendationResult result =
                recommendationEngine.evaluate(
                        dish,
                        request
                );

        assertEquals(
                10,
                result.getScore()
        );

        assertTrue(
                result.getReasons().contains(
                        "Spice level matched. "
                )
        );
    }

    @Test
    void shouldGive10PointsForMatchingBudget() {

        Dish dish = createDish();

        dish.setPriceCategory(
                PriceCategory.MEDIUM
        );

        RecommendationRequest request =
                new RecommendationRequest();

        request.setPriceCategory(
                PriceCategory.MEDIUM
        );

        RecommendationResult result =
                recommendationEngine.evaluate(
                        dish,
                        request
                );

        assertEquals(
                10,
                result.getScore()
        );

        assertTrue(
                result.getReasons().contains(
                        "Budget matched. "
                )
        );
    }

    @Test
    void shouldGive15PointsForHighProtein() {

        Dish dish = createDish();

        dish.setProtein(30.0);

        RecommendationRequest request =
                new RecommendationRequest();

        request.setMinProtein(20.0);

        RecommendationResult result =
                recommendationEngine.evaluate(
                        dish,
                        request
                );

        assertEquals(
                15,
                result.getScore()
        );

        assertTrue(
                result.getReasons().contains(
                        "High protein. "
                )
        );
    }

    @Test
    void shouldGive10PointsForLowCalories() {

        Dish dish = createDish();

        dish.setCalories(400);

        RecommendationRequest request =
                new RecommendationRequest();

        request.setMaxCalories(500);

        RecommendationResult result =
                recommendationEngine.evaluate(
                        dish,
                        request
                );

        assertEquals(
                10,
                result.getScore()
        );

        assertTrue(
                result.getReasons().contains(
                        "Low calories. "
                )
        );
    }

    @Test
    void shouldGive10PointsForQuickPreparation() {

        Dish dish = createDish();

        dish.setPrepTime(20);

        RecommendationRequest request =
                new RecommendationRequest();

        request.setMaxPrepTime(30);

        RecommendationResult result =
                recommendationEngine.evaluate(
                        dish,
                        request
                );

        assertEquals(
                10,
                result.getScore()
        );

        assertTrue(
                result.getReasons().contains(
                        "Quick to prepare. "
                )
        );
    }

    @Test
    void shouldCalculateCompleteScoreWithoutCuisine() {

        Ingredient paneer =
                createIngredient(4L, "Paneer");

        Ingredient tomato =
                createIngredient(5L, "Tomato");

        Dish dish = createDish();

        dish.setDietType(
                DietType.VEG
        );

        dish.setMealType(
                MealType.DINNER
        );

        dish.setSpiceLevel(
                SpiceLevel.MEDIUM
        );

        dish.setPriceCategory(
                PriceCategory.MEDIUM
        );

        dish.setProtein(30.0);

        dish.setCalories(400);

        dish.setPrepTime(20);

        dish.setIngredients(
                Set.of(
                        paneer,
                        tomato
                )
        );

        RecommendationRequest request =
                new RecommendationRequest();

        request.setDietType(
                DietType.VEG
        );

        request.setMealType(
                MealType.DINNER
        );

        request.setSpiceLevel(
                SpiceLevel.MEDIUM
        );

        request.setPriceCategory(
                PriceCategory.MEDIUM
        );

        request.setMinProtein(20.0);

        request.setMaxCalories(500);

        request.setMaxPrepTime(30);

        request.setPreferredIngredientIds(
                List.of(4L, 5L)
        );

        RecommendationResult result =
                recommendationEngine.evaluate(
                        dish,
                        request
                );

        assertEquals(
                135,
                result.getScore()
        );

        assertEquals(
                9,
                result.getReasons().size()
        );
    }

    private Ingredient createIngredient(
            Long id,
            String name) {

        Ingredient ingredient =
                new Ingredient();

        ingredient.setId(id);

        ingredient.setName(name);

        return ingredient;
    }

    private Dish createDish() {

        Dish dish =
                new Dish();

        dish.setName(
                "Test Dish"
        );

        dish.setIsAvailable(
                true
        );

        return dish;
    }
}