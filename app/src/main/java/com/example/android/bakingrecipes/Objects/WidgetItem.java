package com.example.android.bakingrecipes.Objects;

public class WidgetItem {
    private String recipeName;

    public WidgetItem(String recipeName) {
        this.recipeName = recipeName;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }
}
