package com.aifood.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.aifood.dto.recommendation.RecommendationRequest;
import com.aifood.dto.recommendation.RecommendationResponse;
import com.aifood.dto.recommendation.RecommendationResult;
import com.aifood.entity.cuisine.Cuisine;
import com.aifood.entity.dish.Dish;
import com.aifood.entity.ingredient.Ingredient;
import com.aifood.entity.preference.UserPreference;
import com.aifood.enums.DietType;
import com.aifood.enums.MealType;
import com.aifood.enums.PriceCategory;
import com.aifood.enums.SpiceLevel;
import com.aifood.exception.ResourceNotFoundException;
import com.aifood.recommendation.RecommendationEngine;
import com.aifood.repository.dish.DishRepository;
import com.aifood.repository.preference.UserPreferenceRepository;
import com.aifood.repository.user.UserRepository;

import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.aifood.entity.user.User;

import com.aifood.service.recommendation.RecommendationServiceImpl;

class RecommendationServiceImplTest {

    @Mock
    private DishRepository dishRepository;

    @Mock
    private RecommendationEngine recommendationEngine;

    @Mock
    private UserPreferenceRepository userPreferenceRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private RecommendationServiceImpl recommendationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnEmptyListWhenNoDishesMatch() {

        when(dishRepository.findAll()).thenReturn(List.of());

        List<RecommendationResponse> result =
                recommendationService.recommendDish(
                        new com.aifood.dto.recommendation.RecommendationRequest()
                );

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(dishRepository).findAll();
    }

    @Test
    void shouldReturnOnlyPositiveScoreRecommendations() {

        RecommendationRequest request =
                new RecommendationRequest();

        Cuisine cuisine = new Cuisine();
        cuisine.setId(1L);
        cuisine.setName("Indian");

        Dish dish1 = new Dish();
        dish1.setId(1L);
        dish1.setName("Paneer Butter Masala");
        dish1.setCuisine(cuisine);

        Dish dish2 = new Dish();
        dish2.setId(2L);
        dish2.setName("Butter Chicken");
        dish2.setCuisine(cuisine);

        when(dishRepository.findAll())
                .thenReturn(List.of(dish1, dish2));

        when(recommendationEngine.evaluate(eq(dish1), any()))
                .thenReturn(new RecommendationResult(
                        40,
                        List.of("Contains preferred ingredient: Paneer")
                ));

        when(recommendationEngine.evaluate(eq(dish2), any()))
                .thenReturn(new RecommendationResult(
                        0,
                        List.of()
                ));

        List<RecommendationResponse> result =
                recommendationService.recommendDish(request);

        assertEquals(1, result.size());

        assertEquals(1L, result.get(0).getDishId());
        assertEquals("Paneer Butter Masala", result.get(0).getDishName());
        assertEquals(40, result.get(0).getScore());

        verify(recommendationEngine).evaluate(eq(dish1), any());
        verify(recommendationEngine).evaluate(eq(dish2), any());
    }


    @Test
    void shouldSortRecommendationsByScoreDescending() {

        RecommendationRequest request =
                new RecommendationRequest();

        Cuisine cuisine = new Cuisine();
        cuisine.setId(1L);
        cuisine.setName("Indian");

        Dish dish1 = new Dish();
        dish1.setId(1L);
        dish1.setName("Dish A");
        dish1.setCuisine(cuisine);

        Dish dish2 = new Dish();
        dish2.setId(2L);
        dish2.setName("Dish B");
        dish2.setCuisine(cuisine);

        Dish dish3 = new Dish();
        dish3.setId(3L);
        dish3.setName("Dish C");
        dish3.setCuisine(cuisine);

        when(dishRepository.findAll())
                .thenReturn(List.of(dish1, dish2, dish3));

        when(recommendationEngine.evaluate(eq(dish1), any()))
                .thenReturn(new RecommendationResult(
                        20,
                        List.of("Reason A")
                ));

        when(recommendationEngine.evaluate(eq(dish2), any()))
                .thenReturn(new RecommendationResult(
                        80,
                        List.of("Reason B")
                ));

        when(recommendationEngine.evaluate(eq(dish3), any()))
                .thenReturn(new RecommendationResult(
                        50,
                        List.of("Reason C")
                ));

        List<RecommendationResponse> result =
                recommendationService.recommendDish(request);

        assertEquals(3, result.size());

        assertEquals(2L, result.get(0).getDishId());
        assertEquals(80, result.get(0).getScore());

        assertEquals(3L, result.get(1).getDishId());
        assertEquals(50, result.get(1).getScore());

        assertEquals(1L, result.get(2).getDishId());
        assertEquals(20, result.get(2).getScore());
    }

    @Test
    void shouldReturnOnlyTopFiveRecommendations() {

        RecommendationRequest request =
                new RecommendationRequest();

        Cuisine cuisine = new Cuisine();
        cuisine.setId(1L);
        cuisine.setName("Indian");

        Dish dish1 = createDish(1L, "Dish A", cuisine);
        Dish dish2 = createDish(2L, "Dish B", cuisine);
        Dish dish3 = createDish(3L, "Dish C", cuisine);
        Dish dish4 = createDish(4L, "Dish D", cuisine);
        Dish dish5 = createDish(5L, "Dish E", cuisine);
        Dish dish6 = createDish(6L, "Dish F", cuisine);
        Dish dish7 = createDish(7L, "Dish G", cuisine);

        when(dishRepository.findAll())
                .thenReturn(List.of(
                        dish1,
                        dish2,
                        dish3,
                        dish4,
                        dish5,
                        dish6,
                        dish7
                ));

        when(recommendationEngine.evaluate(eq(dish1), any()))
                .thenReturn(new RecommendationResult(20, List.of()));

        when(recommendationEngine.evaluate(eq(dish2), any()))
                .thenReturn(new RecommendationResult(70, List.of()));

        when(recommendationEngine.evaluate(eq(dish3), any()))
                .thenReturn(new RecommendationResult(50, List.of()));

        when(recommendationEngine.evaluate(eq(dish4), any()))
                .thenReturn(new RecommendationResult(90, List.of()));

        when(recommendationEngine.evaluate(eq(dish5), any()))
                .thenReturn(new RecommendationResult(40, List.of()));

        when(recommendationEngine.evaluate(eq(dish6), any()))
                .thenReturn(new RecommendationResult(80, List.of()));

        when(recommendationEngine.evaluate(eq(dish7), any()))
                .thenReturn(new RecommendationResult(30, List.of()));

        List<RecommendationResponse> result =
                recommendationService.recommendDish(request);

        assertEquals(5, result.size());

        assertEquals(4L, result.get(0).getDishId());
        assertEquals(90, result.get(0).getScore());

        assertEquals(6L, result.get(1).getDishId());
        assertEquals(80, result.get(1).getScore());

        assertEquals(2L, result.get(2).getDishId());
        assertEquals(70, result.get(2).getScore());

        assertEquals(3L, result.get(3).getDishId());
        assertEquals(50, result.get(3).getScore());

        assertEquals(5L, result.get(4).getDishId());
        assertEquals(40, result.get(4).getScore());
    }

    private Dish createDish(Long id, String name, Cuisine cuisine) {

        Dish dish = new Dish();

        dish.setId(id);
        dish.setName(name);
        dish.setCuisine(cuisine);
        dish.setIsAvailable(true);

        return dish;
    }

    @Test
    void shouldRecommendMyDishesUsingUserPreferences() {

        User user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");

        UserPreference preference = new UserPreference();
        preference.setUser(user);
        preference.setDietType(DietType.VEG);
        preference.setMealType(MealType.DINNER);
        preference.setSpiceLevel(SpiceLevel.MEDIUM);
        preference.setPriceCategory(PriceCategory.MEDIUM);
        preference.setMaxCalories(500);
        preference.setMinProtein(20.0);
        preference.setMaxPrepTime(30);
        preference.setOnlyAvailable(true);

        Ingredient paneer =
                createIngredient(4L, "Paneer");

        Ingredient tomato =
                createIngredient(5L, "Tomato");

        preference.setPreferredIngredients(
                List.of(paneer)
        );

        preference.setExcludedIngredients(
                List.of(tomato)
        );

        Dish dish = createDish(
                1L,
                "Paneer Butter Masala",
                createCuisine()
        );

        RecommendationResult recommendationResult =
                new RecommendationResult(
                        100,
                        List.of("Good match")
                );

        when(userRepository.findByEmail("test@example.com"))
                .thenReturn(Optional.of(user));

        when(userPreferenceRepository.findByUserId(1L))
                .thenReturn(Optional.of(preference));

        when(dishRepository.findAll())
                .thenReturn(List.of(dish));

        when(recommendationEngine.evaluate(
                eq(dish),
                any(RecommendationRequest.class)
        )).thenReturn(recommendationResult);

        mockAuthentication("test@example.com");

        List<RecommendationResponse> result =
                recommendationService.recommendMyDishes();

        assertEquals(1, result.size());

        assertEquals(
                1L,
                result.get(0).getDishId()
        );

        assertEquals(
                100,
                result.get(0).getScore()
        );

        verify(recommendationEngine).evaluate(
                eq(dish),
                argThat(request ->
                        request.getDietType() == DietType.VEG
                        && request.getMealType() == MealType.DINNER
                        && request.getSpiceLevel() == SpiceLevel.MEDIUM
                        && request.getPriceCategory() == PriceCategory.MEDIUM
                        && request.getMaxCalories() == 500
                        && request.getMinProtein() == 20.0
                        && request.getMaxPrepTime() == 30
                        && Boolean.TRUE.equals(
                                request.getOnlyAvailable()
                        )
                        && request.getPreferredIngredientIds()
                                .contains(4L)
                        && request.getExcludedIngredientIds()
                                .contains(5L)
                )
        );
    }

    private Cuisine createCuisine() {

        Cuisine cuisine = new Cuisine();

        cuisine.setId(1L);
        cuisine.setName("Indian");

        return cuisine;
    }

    private void mockAuthentication(String email) {

        Authentication authentication = mock(Authentication.class);

        when(authentication.getName()).thenReturn(email);

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }


    private Ingredient createIngredient(Long id, String name) {

        Ingredient ingredient = new Ingredient();

        ingredient.setId(id);
        ingredient.setName(name);

        return ingredient;
    }

    @Test
    void shouldThrowExceptionWhenUserPreferencesDoNotExist() {

        User user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");

        when(userRepository.findByEmail("test@example.com"))
                .thenReturn(Optional.of(user));

        when(userPreferenceRepository.findByUserId(1L))
                .thenReturn(Optional.empty());

        mockAuthentication("test@example.com");

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> recommendationService.recommendMyDishes()
        );

        assertEquals(
                "User preferences not found",
                exception.getMessage()
        );

        verify(userRepository)
                .findByEmail("test@example.com");

        verify(userPreferenceRepository)
                .findByUserId(1L);

        verifyNoInteractions(
                dishRepository,
                recommendationEngine
        );
    }

    @Test
    void shouldThrowExceptionWhenUserDoesNotExist() {

        when(userRepository.findByEmail("unknown@example.com"))
                .thenReturn(Optional.empty());

        mockAuthentication("unknown@example.com");

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> recommendationService.recommendMyDishes()
        );

        assertEquals(
                "User not found",
                exception.getMessage()
        );

        verify(userRepository)
                .findByEmail("unknown@example.com");

        verifyNoInteractions(
                userPreferenceRepository,
                dishRepository,
                recommendationEngine
        );
    }
}