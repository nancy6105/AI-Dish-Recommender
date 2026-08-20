package com.aifood.entity.preference;

import java.util.ArrayList;
import java.util.List;

import com.aifood.entity.ingredient.Ingredient;
import com.aifood.entity.user.User;
import com.aifood.enums.DietType;
import com.aifood.enums.MealType;
import com.aifood.enums.PriceCategory;
import com.aifood.enums.SpiceLevel;

import jakarta.persistence.*;

@Entity
@Table(name = "user_preferences")
public class UserPreference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Enumerated(EnumType.STRING)
    private DietType dietType;

    @Enumerated(EnumType.STRING)
    private MealType mealType;

    @Enumerated(EnumType.STRING)
    private SpiceLevel spiceLevel;

    @Enumerated(EnumType.STRING)
    private PriceCategory priceCategory;

    private Integer maxCalories;

    private Double minProtein;

    private Integer maxPrepTime;

    private Boolean onlyAvailable;  

    public Boolean getOnlyAvailable() {
        return onlyAvailable;
    }

    public void setOnlyAvailable(Boolean onlyAvailable) {
        this.onlyAvailable = onlyAvailable;
    }

    @ManyToMany
    @JoinTable(
        name = "user_preferred_ingredients",
        joinColumns = @JoinColumn(name = "preference_id"),
        inverseJoinColumns = @JoinColumn(name = "ingredient_id")
    )
    private List<Ingredient> preferredIngredients = new ArrayList<>();

    @ManyToMany
    @JoinTable(
        name = "user_excluded_ingredients",
        joinColumns = @JoinColumn(name = "preference_id"),
        inverseJoinColumns = @JoinColumn(name = "ingredient_id")
    )
    private List<Ingredient> excludedIngredients = new ArrayList<>();

    public List<Ingredient> getPreferredIngredients() {
        return preferredIngredients;
    }

    public void setPreferredIngredients(List<Ingredient> preferredIngredients) {
        this.preferredIngredients = preferredIngredients;
    }

    public List<Ingredient> getExcludedIngredients() {
        return excludedIngredients;
    }

    public void setExcludedIngredients(List<Ingredient> excludedIngredients) {
        this.excludedIngredients = excludedIngredients;
    }

    public UserPreference() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public DietType getDietType() {
        return dietType;
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