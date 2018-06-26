package com.example.android.bakingrecipes;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.android.bakingrecipes.Adapters.RecipeAdapter;
import com.example.android.bakingrecipes.Objects.Recipe;
import com.example.android.bakingrecipes.Utils.ItemDecoration;
import com.example.android.bakingrecipes.Utils.VariousMethods;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements RecipeAdapter.RecipeAdapterOnClickHandler, LoaderManager.LoaderCallbacks<ArrayList<Recipe>> {

    private RecipeAdapter recipeAdapter;
    @BindView(R.id.content)
    RecyclerView mRecyclerView;

    private static final int RECIPES_LOADER_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        ArrayList<Recipe> mRecipes = new ArrayList<>();
        recipeAdapter = new RecipeAdapter(this, mRecipes, this);

        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.margin_recycler_view);
        mRecyclerView.addItemDecoration(new ItemDecoration(spacingInPixels));
        mRecyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(recipeAdapter);
        LoaderManager.LoaderCallbacks<ArrayList<Recipe>> callback = this;
        getSupportLoaderManager().initLoader(RECIPES_LOADER_ID, null, callback);
    }

    @Override
    public void onClick(Recipe recipe) {
        Intent detailsIntent = new Intent(this,DetailsActivity.class);
        detailsIntent.putExtra("Recipe",recipe);
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
                    forceLoad();
                }
            }

            @Override
            public ArrayList<Recipe> loadInBackground() {
                try {
                    return VariousMethods.JSONtoArrayList(getResources().getString(R.string.recipes));
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Recipe>> loader, ArrayList<Recipe> recipes) {
        recipeAdapter.setRecipeData(recipes);
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Recipe>> loader) {
        recipeAdapter.setRecipeData(null);
    }
}
