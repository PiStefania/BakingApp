package com.example.android.bakingrecipes.Objects;

import android.os.Parcel;
import android.os.Parcelable;

public class Recipe implements Parcelable {
    private String name;
    private String image;
    private String recipeIngredients;
    private String recipeSteps;
    private Integer servings;

    public Recipe(String name, String image, String recipeIngredients, String recipeSteps, Integer servings) {
        this.name = name;
        this.image = image;
        this.recipeIngredients = recipeIngredients;
        this.recipeSteps = recipeSteps;
        this.servings = servings;
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

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

    public String getRecipeIngredients() {
        return recipeIngredients;
    }

    public void setRecipeIngredients(String recipeIngredients) {
        this.recipeIngredients = recipeIngredients;
    }

    public String getRecipeSteps() {
        return recipeSteps;
    }

    public void setRecipeSteps(String recipeSteps) {
        this.recipeSteps = recipeSteps;
    }

    public Integer getServings() {
        return servings;
    }

    public void setServings(Integer servings) {
        this.servings = servings;
    }

    public Recipe(Parcel in) {
        name = in.readString();
        image = in.readString();
        recipeIngredients = in.readString();
        recipeSteps = in.readString();
        servings = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(image);
        parcel.writeString(recipeIngredients);
        parcel.writeString(recipeSteps);
        if (servings == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(servings);
        }
    }
}
