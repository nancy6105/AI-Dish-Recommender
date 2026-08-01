package com.aifood.controller.recommendation;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import com.aifood.dto.recommendation.RecommendationRequest;
import com.aifood.dto.recommendation.RecommendationResponse;
import com.aifood.service.recommendation.RecommendationService;

@RestController
@RequestMapping("/api/recommendations")
public class RecommendationController {
    
    private final RecommendationService recommendationService;

    public RecommendationController (RecommendationService recommendationService){
        this.recommendationService = recommendationService;
    }

    @PostMapping
    public List<RecommendationResponse> recommend(@RequestBody RecommendationRequest request){
        return recommendationService.recommendDish(request);
    }
}
