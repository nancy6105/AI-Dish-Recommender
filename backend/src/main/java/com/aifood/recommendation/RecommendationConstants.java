package com.aifood.recommendation;

public final class RecommendationConstants {

    private RecommendationConstants() {}

    public static final int CUISINE_SCORE = 20;
    public static final int MEAL_SCORE = 15;
    public static final int DIET_SCORE = 15;
    public static final int SPICE_SCORE = 10;
    public static final int PRICE_SCORE = 10;

    public static final int PREFERRED_INGREDIENT_SCORE = 20;
    public static final int EXCLUDED_INGREDIENT_PENALTY = -30;

    public static final int PROTEIN_SCORE = 15;
    public static final int CALORIE_SCORE = 10;
    public static final int PREP_TIME_SCORE = 10;
    public static final int AVAILABLE_SCORE = 15;
    public static final int UNAVAILABLE_PENALTY = -100;
}