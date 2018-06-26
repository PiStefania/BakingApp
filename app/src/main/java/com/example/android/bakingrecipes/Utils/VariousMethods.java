package com.example.android.bakingrecipes.Utils;

import com.example.android.bakingrecipes.Objects.DetailRecipe;
import com.example.android.bakingrecipes.Objects.Recipe;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class VariousMethods {

    private static final String NAME_TAG = "name";
    private static final String IMAGE_TAG = "image";
    private static final String INGREDIENTS_TAG = "ingredients";
    private static final String STEPS_TAG = "steps";
    private static final String SERVINGS_TAG = "servings";

    public static ArrayList<Recipe> JSONtoArrayList(String recipesStr) throws JSONException {
        ArrayList<Recipe> recipes = new ArrayList<>();
        JSONArray mainObject = new JSONArray(recipesStr);
        for(int i=0;i<mainObject.length();i++){
            JSONObject recipeObject = mainObject.getJSONObject(i);
            String name = recipeObject.getString(NAME_TAG);
            String image = recipeObject.getString(IMAGE_TAG);
            JSONArray ingredients = recipeObject.getJSONArray(INGREDIENTS_TAG);
            JSONArray steps = recipeObject.getJSONArray(STEPS_TAG);
            Integer servings = recipeObject.getInt(SERVINGS_TAG);

            //new recipe
            Recipe recipe = new Recipe(name,image,ingredients.toString(),steps.toString(),servings);
            recipes.add(recipe);
        }
        return recipes;
    }

    private static final String STEP_TITLE = "shortDescription";
    private static final String STEP_DESCRIPTION = "description";
    private static final String STEP_VIDEO = "videoURL";

    public static ArrayList<DetailRecipe> getStepsRecipe(String stepsStr) throws JSONException {
        ArrayList<DetailRecipe> detailRecipes = new ArrayList<>();
        JSONArray mainObject = new JSONArray(stepsStr);
        for(int i=0;i<mainObject.length();i++){
            JSONObject recipeObject = mainObject.getJSONObject(i);
            String stepTitle = recipeObject.getString(STEP_TITLE);
            String stepInstructions = recipeObject.getString(STEP_DESCRIPTION);
            String stepVideo = recipeObject.getString(STEP_VIDEO);

            //new step detail
            DetailRecipe detail = new DetailRecipe(stepTitle,stepVideo,stepInstructions);
            detailRecipes.add(detail);
        }
        return detailRecipes;
    }
}
