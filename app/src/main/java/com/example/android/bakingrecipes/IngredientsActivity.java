package com.example.android.bakingrecipes;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.android.bakingrecipes.Adapters.IngredientsAdapter;
import com.example.android.bakingrecipes.Objects.Ingredient;
import com.example.android.bakingrecipes.Utils.ItemDecoration;
import com.example.android.bakingrecipes.Utils.VariousMethods;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IngredientsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<Ingredient>> {

    private IngredientsAdapter ingredientsAdapter;
    @BindView(R.id.content)
    RecyclerView mRecyclerView;

    private static final int INGREDIENTS_LOADER_ID = 3;
    private String ingredientsJSON;
    private static final String RECIPE_INGREDIENTS_EXTRA = "RecipeIngredients";
    private static final String LOG_TAG = IngredientsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        ArrayList<Ingredient> mIngredients = new ArrayList<>();
        ingredientsAdapter = new IngredientsAdapter(this, mIngredients);

        //get ingredients json
        ingredientsJSON =  getIntent().getStringExtra(RECIPE_INGREDIENTS_EXTRA);

        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.margin_recycler_view);
        mRecyclerView.addItemDecoration(new ItemDecoration(spacingInPixels));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(ingredientsAdapter);
        LoaderManager.LoaderCallbacks<ArrayList<Ingredient>> callback = this;
        getSupportLoaderManager().initLoader(INGREDIENTS_LOADER_ID, null, callback);
    }


    @NonNull
    @Override
    public Loader<ArrayList<Ingredient>> onCreateLoader(int id, @Nullable Bundle args) {
        return new AsyncTaskLoader<ArrayList<Ingredient>>(this) {

            ArrayList<Ingredient> ingredients = null;

            @Override
            protected void onStartLoading() {
                if (ingredients != null) {
                    ingredients = null;
                } else {
                    forceLoad();
                }
            }

            @Override
            public ArrayList<Ingredient> loadInBackground() {
                try {
                    return VariousMethods.getIngredientsRecipe(ingredientsJSON);
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
        };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<ArrayList<Ingredient>> loader, ArrayList<Ingredient> data) {
        ingredientsAdapter.setIngredientData(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<ArrayList<Ingredient>> loader) {
        ingredientsAdapter.setIngredientData(null);
    }
}
