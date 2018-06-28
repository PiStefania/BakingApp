package com.example.android.bakingrecipes;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.android.bakingrecipes.Adapters.DetailRecipeAdapter;
import com.example.android.bakingrecipes.Objects.DetailRecipe;
import com.example.android.bakingrecipes.Objects.Recipe;
import com.example.android.bakingrecipes.Utils.ItemDecoration;
import com.example.android.bakingrecipes.Utils.VariousMethods;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailsActivity extends AppCompatActivity implements DetailRecipeAdapter.DetailRecipeAdapterOnClickHandler, LoaderManager.LoaderCallbacks<ArrayList<DetailRecipe>> {

    private DetailRecipeAdapter detailRecipeAdapter;
    @BindView(R.id.content_details) RecyclerView mRecyclerView;

    private Recipe recipe;
    private static final int DETAIL_RECIPE_LOADER_ID = 2;
    private static final String RECIPE_EXTRA = "Recipe";
    private static final String RECIPE_STEPS_EXTRA = "RecipeSteps";
    private static final String RECIPE_STEPS_POSITION_EXTRA = "RecipeStepPosition";
    private static final String RECIPE_STEPS_ALL_STEPS_EXTRA = "RecipeStepAllSteps";
    private static final String RECIPE_STEPS_NEXT_STEP_EXTRA = "RecipeStepNext";
    private static final String RECIPE_STEPS_PREVIOUS_STEP_EXTRA = "RecipeStepPrevious";
    private static final String LOG_TAG = DetailsActivity.class.getSimpleName();
    private ArrayList<DetailRecipe> detailRecipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);
        //get recipe's info
        recipe = (Recipe) getIntent().getParcelableExtra(RECIPE_EXTRA);

        ArrayList<DetailRecipe> mDetailRecipe = new ArrayList<>();
        detailRecipeAdapter = new DetailRecipeAdapter(this, mDetailRecipe, this,recipe.getRecipeIngredients());

        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.margin_recycler_view);
        mRecyclerView.addItemDecoration(new ItemDecoration(spacingInPixels));
        mRecyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(detailRecipeAdapter);
        LoaderManager.LoaderCallbacks<ArrayList<DetailRecipe>> callback = this;
        getSupportLoaderManager().initLoader(DETAIL_RECIPE_LOADER_ID, null, callback);
    }

    @NonNull
    @Override
    public Loader<ArrayList<DetailRecipe>> onCreateLoader(int id, @Nullable Bundle args) {
        return new AsyncTaskLoader<ArrayList<DetailRecipe>>(this) {

            ArrayList<DetailRecipe> detailRecipes = null;

            @Override
            protected void onStartLoading() {
                if (detailRecipes != null) {
                    detailRecipes = null;
                } else {
                    forceLoad();
                }
            }

            @Override
            public ArrayList<DetailRecipe> loadInBackground() {
                try {
                    return VariousMethods.getStepsRecipe(recipe.getRecipeSteps());
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
        };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<ArrayList<DetailRecipe>> loader, ArrayList<DetailRecipe> data) {
        detailRecipeAdapter.setDetailRecipeData(data);
        detailRecipes = data;
    }

    @Override
    public void onLoaderReset(@NonNull Loader<ArrayList<DetailRecipe>> loader) {
        detailRecipeAdapter.setDetailRecipeData(null);
    }

    @Override
    public void onClick(DetailRecipe detailRecipe) {
        int positionStep = VariousMethods.findPositionClickedStep(detailRecipe,detailRecipes);
        Intent recipeStep = new Intent(this,RecipeStepActivity.class);
        recipeStep.putExtra(RECIPE_STEPS_EXTRA,detailRecipe);
        recipeStep.putExtra(RECIPE_STEPS_POSITION_EXTRA,positionStep);
        recipeStep.putExtra(RECIPE_STEPS_ALL_STEPS_EXTRA,detailRecipes);
        if(positionStep == 0){
            recipeStep.putExtra(RECIPE_STEPS_NEXT_STEP_EXTRA,detailRecipes.get(positionStep+1));
        }else if(positionStep == detailRecipes.size()-1){
            recipeStep.putExtra(RECIPE_STEPS_PREVIOUS_STEP_EXTRA,detailRecipes.get(positionStep-1));
        }else{
            recipeStep.putExtra(RECIPE_STEPS_PREVIOUS_STEP_EXTRA,detailRecipes.get(positionStep-1));
            recipeStep.putExtra(RECIPE_STEPS_NEXT_STEP_EXTRA,detailRecipes.get(positionStep+1));
        }
        startActivity(recipeStep);
    }
}