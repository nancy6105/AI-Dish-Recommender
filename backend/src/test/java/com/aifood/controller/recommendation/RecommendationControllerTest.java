package com.aifood.controller.recommendation;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.aifood.dto.recommendation.RecommendationRequest;
import com.aifood.dto.recommendation.RecommendationResponse;
import com.aifood.security.JwtAuthenticationFilter;
import com.aifood.security.JwtService;
import com.aifood.service.recommendation.RecommendationService;

@WebMvcTest(RecommendationController.class)
@AutoConfigureMockMvc(addFilters = false)
class RecommendationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RecommendationService recommendationService;

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockitoBean
    private JwtService jwtService;

    @Test
    void shouldReturnRecommendations() throws Exception {

        RecommendationResponse response =
                new RecommendationResponse();

        response.setDishId(4L);
        response.setDishName("Paneer Butter Masala");
        response.setCuisine("Indian");
        response.setScore(135);

        response.setReasons(
                List.of(
                        "Diet matched. ",
                        "Contains preferred ingredient: Paneer"
                )
        );

        when(recommendationService.recommendDish(
                any(RecommendationRequest.class)
        )).thenReturn(List.of(response));

        mockMvc.perform(
                post("/api/recommendations")
                        .contentType("application/json")
                        .content("""
                            {
                                "dietType": "VEG",
                                "mealType": "DINNER"
                            }
                        """)
        )
        .andExpect(status().isOk())
        .andExpect(
                jsonPath("$[0].dishId")
                        .value(4)
        )
        .andExpect(
                jsonPath("$[0].dishName")
                        .value("Paneer Butter Masala")
        )
        .andExpect(
                jsonPath("$[0].cuisine")
                        .value("Indian")
        )
        .andExpect(
                jsonPath("$[0].score")
                        .value(135)
        )
        .andExpect(
                jsonPath("$[0].reasons[0]")
                        .value("Diet matched. ")
        );
    }

    @Test
    void shouldReturnMyRecommendations() throws Exception {

        RecommendationResponse response =
                new RecommendationResponse();

        response.setDishId(4L);
        response.setDishName("Paneer Butter Masala");
        response.setCuisine("Indian");
        response.setScore(135);

        response.setReasons(
                List.of(
                        "Diet matched. ",
                        "Contains preferred ingredient: Paneer"
                )
        );

        when(recommendationService.recommendMyDishes())
                .thenReturn(List.of(response));

        mockMvc.perform(
                get("/api/recommendations/me")
        )
        .andExpect(status().isOk())
        .andExpect(
                jsonPath("$[0].dishId")
                        .value(4)
        )
        .andExpect(
                jsonPath("$[0].dishName")
                        .value("Paneer Butter Masala")
        )
        .andExpect(
                jsonPath("$[0].cuisine")
                        .value("Indian")
        )
        .andExpect(
                jsonPath("$[0].score")
                        .value(135)
        )
        .andExpect(
                jsonPath("$[0].reasons[0]")
                        .value("Diet matched. ")
        );
    }
}