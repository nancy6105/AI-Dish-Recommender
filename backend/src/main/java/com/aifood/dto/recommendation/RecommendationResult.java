package com.aifood.dto.recommendation;

import java.util.ArrayList;
import java.util.List;

public class RecommendationResult {

    private int score;
    private List<String> reasons = new ArrayList<>();

    public RecommendationResult() {
    }

    public RecommendationResult(int score, List<String> reasons) {
        this.score = score;
        this.reasons = reasons;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public List<String> getReasons() {
        return reasons;
    }

    public void setReasons(List<String> reasons) {
        this.reasons = reasons;
    }
}