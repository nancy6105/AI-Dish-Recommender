package com.aifood.entity.ingredient;

import com.aifood.enums.IngredientCategory;

import jakarta.persistence.*;

@Entity
@Table(name = "ingredients")
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(length = 500)
    private String description;

    @Enumerated(EnumType.STRING)
    private IngredientCategory category;

    public void setId(Long id) {
        this.id = id;
    }

    public IngredientCategory getCategory() {
        return category;
    }
    
    public Ingredient() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCategory(IngredientCategory category) {
        this.category = category;
    }
}