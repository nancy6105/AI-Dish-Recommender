package com.aifood.service.recommendation;

import java.util.List;

import com.aifood.dto.recommendation.RecommendationRequest;
import com.aifood.dto.recommendation.RecommendationResponse;

public interface RecommendationService {
    
    List<RecommendationResponse> recommendDish(RecommendationRequest request);
}
