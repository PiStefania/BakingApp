package com.example.android.bakingrecipes;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.ScrollView;

import com.example.android.bakingrecipes.Adapters.RecipeAdapter;
import com.example.android.bakingrecipes.Objects.Recipe;
import com.example.android.bakingrecipes.Utils.ItemDecoration;
import com.example.android.bakingrecipes.Utils.NetworkUtils;
import com.example.android.bakingrecipes.Utils.RecipesJsonUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements RecipeAdapter.RecipeAdapterOnClickHandler, LoaderManager.LoaderCallbacks<ArrayList<Recipe>> {

    private RecipeAdapter recipeAdapter;
    @Nullable
    @BindView(R.id.content) RecyclerView mRecyclerView;
    @Nullable
    @BindView(R.id.content_two_pane) RecyclerView mRecyclerViewTwoPane;
    @BindView(R.id.loading_indicator)
    ProgressBar loadingIndicator;
    @BindView(R.id.error_view)
    ScrollView errorView;

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final int RECIPES_LOADER_ID = 1;
    private boolean twoPane = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        ArrayList<Recipe> mRecipes = new ArrayList<>();
        recipeAdapter = new RecipeAdapter(this, mRecipes, this);

        twoPane = mRecyclerViewTwoPane != null && mRecyclerViewTwoPane.getVisibility() == View.VISIBLE;
        if(twoPane) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        if(twoPane){
            int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.margin_recycler_view);
            mRecyclerViewTwoPane.addItemDecoration(new ItemDecoration(spacingInPixels));
            mRecyclerViewTwoPane.setHasFixedSize(true);
            mRecyclerViewTwoPane.setLayoutManager(new GridLayoutManager(this, 3));
            mRecyclerViewTwoPane.setAdapter(recipeAdapter);
            mRecyclerViewTwoPane.setVisibility(View.VISIBLE);

        }else{
            int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.margin_recycler_view);
            mRecyclerView.addItemDecoration(new ItemDecoration(spacingInPixels));
            mRecyclerView.setHasFixedSize(true);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            mRecyclerView.setLayoutManager(linearLayoutManager);
            mRecyclerView.setAdapter(recipeAdapter);
            mRecyclerView.setVisibility(View.VISIBLE);

        }

        errorView.setVisibility(View.INVISIBLE);

        LoaderManager.LoaderCallbacks<ArrayList<Recipe>> callback = this;
        getSupportLoaderManager().initLoader(RECIPES_LOADER_ID, null, callback);
    }

    @Override
    public void onClick(Recipe recipe) {
        Intent detailsIntent = new Intent(this, DetailsActivity.class);
        detailsIntent.putExtra("Recipe", recipe);
        startActivity(detailsIntent);
    }

    @Override
    public Loader<ArrayList<Recipe>> onCreateLoader(int i, Bundle bundle) {
        return new AsyncTaskLoader<ArrayList<Recipe>>(this) {

            ArrayList<Recipe> recipes = null;

            @Override
            protected void onStartLoading() {
                if (recipes != null) {
                    recipes = null;
                } else {
                    loadingIndicator.setVisibility(View.VISIBLE);
                    forceLoad();
                }
            }

            @Override
            public ArrayList<Recipe> loadInBackground() {
                try {
                    String jsonResponse = NetworkUtils.getResponseFromHttpUrl(NetworkUtils.buildUrl());
                    return RecipesJsonUtils.JSONtoArrayList(jsonResponse);
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Recipe>> loader, ArrayList<Recipe> recipes) {
        loadingIndicator.setVisibility(View.INVISIBLE);
        recipeAdapter.setRecipeData(recipes);
        if(recipes == null || recipes.size()==0){
            if(twoPane) {
                mRecyclerViewTwoPane.setVisibility(View.INVISIBLE);
            }else{
                mRecyclerView.setVisibility(View.INVISIBLE);
            }
            errorView.setVisibility(View.VISIBLE);
        }else{
            errorView.setVisibility(View.INVISIBLE);
            if(twoPane) {
                mRecyclerViewTwoPane.setVisibility(View.VISIBLE);
            }else{
                mRecyclerView.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Recipe>> loader) {
        recipeAdapter.setRecipeData(null);
    }
}
