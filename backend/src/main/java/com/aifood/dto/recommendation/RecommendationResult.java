package com.aifood.dto.recommendation;

public class RecommendationResult {

    private int score;
    private String reason;

    public RecommendationResult() {
    }

    public RecommendationResult(int score, String reason) {
        this.score = score;
        this.reason = reason;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}