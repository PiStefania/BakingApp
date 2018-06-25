package com.example.android.bakingrecipes;

import org.json.JSONArray;

public class Recipe {
    private String name;
    private String image;
    private JSONArray recipeIngredients;
    private JSONArray recipeSteps;
    private Integer servings;

    public Recipe(String name, String image, JSONArray recipeIngredients, JSONArray recipeSteps, Integer servings) {
        this.name = name;
        this.image = image;
        this.recipeIngredients = recipeIngredients;
        this.recipeSteps = recipeSteps;
        this.servings = servings;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public JSONArray getRecipeIngredients() {
        return recipeIngredients;
    }

    public void setRecipeIngredients(JSONArray recipeIngredients) {
        this.recipeIngredients = recipeIngredients;
    }

    public JSONArray getRecipeSteps() {
        return recipeSteps;
    }

    public void setRecipeSteps(JSONArray recipeSteps) {
        this.recipeSteps = recipeSteps;
    }

    public Integer getServings() {
        return servings;
    }

    public void setServings(Integer servings) {
        this.servings = servings;
    }
}
