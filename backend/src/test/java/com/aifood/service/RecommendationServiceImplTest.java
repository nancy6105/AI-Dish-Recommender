package com.aifood.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.aifood.dto.recommendation.RecommendationResponse;
import com.aifood.entity.dish.Dish;
import com.aifood.entity.preference.UserPreference;
import com.aifood.entity.user.User;
import com.aifood.recommendation.RecommendationEngine;
import com.aifood.repository.dish.DishRepository;
import com.aifood.repository.preference.UserPreferenceRepository;
import com.aifood.repository.user.UserRepository;
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
}