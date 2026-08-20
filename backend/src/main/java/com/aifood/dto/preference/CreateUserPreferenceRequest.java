package com.aifood.dto.preference;

import java.util.List;

import com.aifood.enums.DietType;
import com.aifood.enums.MealType;
import com.aifood.enums.PriceCategory;
import com.aifood.enums.SpiceLevel;

public class CreateUserPreferenceRequest {
    
    private DietType dietType;

    private MealType mealType;

    private SpiceLevel spiceLevel;

    private PriceCategory priceCategory;

    private Integer maxCalories;

    private Double minProtein;

    private Integer maxPrepTime;

    private List<Long> preferredIngredientIds;
    
    private List<Long> excludedIngredientIds;

    private Boolean onlyAvailable;

    public Boolean getOnlyAvailable() {
        return onlyAvailable;
    }

    public void setOnlyAvailable(Boolean onlyAvailable) {
        this.onlyAvailable = onlyAvailable;
    }

    public DietType getDietType() {
        return dietType;
    }

    public List<Long> getPreferredIngredientIds() {
        return preferredIngredientIds;
    }

    public void setPreferredIngredientIds(List<Long> preferredIngredientIds) {
        this.preferredIngredientIds = preferredIngredientIds;
    }

    public List<Long> getExcludedIngredientIds() {
        return excludedIngredientIds;
    }

    public void setExcludedIngredientIds(List<Long> excludedIngredientIds) {
        this.excludedIngredientIds = excludedIngredientIds;
    }

    public void setDietType(DietType dietType) {
        this.dietType = dietType;
    }

    public MealType getMealType() {
        return mealType;
    }

    public void setMealType(MealType mealType) {
        this.mealType = mealType;
    }

    public SpiceLevel getSpiceLevel() {
        return spiceLevel;
    }

    public void setSpiceLevel(SpiceLevel spiceLevel) {
        this.spiceLevel = spiceLevel;
    }

    public PriceCategory getPriceCategory() {
        return priceCategory;
    }

    public void setPriceCategory(PriceCategory priceCategory) {
        this.priceCategory = priceCategory;
    }

    public Integer getMaxCalories() {
        return maxCalories;
    }

    public void setMaxCalories(Integer maxCalories) {
        this.maxCalories = maxCalories;
    }

    public Double getMinProtein() {
        return minProtein;
    }

    public void setMinProtein(Double minProtein) {
        this.minProtein = minProtein;
    }

    public Integer getMaxPrepTime() {
        return maxPrepTime;
    }

    public void setMaxPrepTime(Integer maxPrepTime) {
        this.maxPrepTime = maxPrepTime;
    }
}
