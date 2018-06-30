package com.example.android.bakingrecipes.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.bakingrecipes.Adapters.IngredientsAdapter;
import com.example.android.bakingrecipes.DetailsActivity;
import com.example.android.bakingrecipes.Objects.Ingredient;
import com.example.android.bakingrecipes.R;
import com.example.android.bakingrecipes.Utils.ItemDecoration;
import com.example.android.bakingrecipes.Utils.RecipesJsonUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IngredientsFragment extends Fragment implements LoaderManager.LoaderCallbacks<ArrayList<Ingredient>> {

    private IngredientsAdapter ingredientsAdapter;
    @BindView(R.id.content_ingredients) RecyclerView mRecyclerView;

    private static final int INGREDIENTS_LOADER_ID = 3;
    private String ingredientsJSON;
    private static final String RECIPE_INGREDIENTS_EXTRA = "RecipeIngredients";
    private static final String LOG_TAG = IngredientsFragment.class.getSimpleName();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        ((DetailsActivity) getActivity()).setActionBarTitle(getContext().getResources().getString(R.string.recipe_ingredients_activity));
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            ingredientsJSON = bundle.getString(RECIPE_INGREDIENTS_EXTRA);
        }
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ingredients, container, false);
        ButterKnife.bind(this, view);

        ArrayList<Ingredient> mIngredients = new ArrayList<>();
        ingredientsAdapter = new IngredientsAdapter(getContext(), mIngredients);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.margin_recycler_view);
        mRecyclerView.addItemDecoration(new ItemDecoration(spacingInPixels));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(ingredientsAdapter);
        LoaderManager.LoaderCallbacks<ArrayList<Ingredient>> callback = this;
        getActivity().getSupportLoaderManager().initLoader(INGREDIENTS_LOADER_ID, null, callback);

        return view;
    }

    @NonNull
    @Override
    public Loader<ArrayList<Ingredient>> onCreateLoader(int id, @Nullable Bundle args) {
        return new AsyncTaskLoader<ArrayList<Ingredient>>(getContext()) {

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
                    return RecipesJsonUtils.getIngredientsRecipe(ingredientsJSON);
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

    @Override
    public void onDestroy() {
        ((DetailsActivity) getActivity()).setActionBarTitle(getContext().getResources().getString(R.string.details_activity));
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((DetailsActivity) getActivity()).setActionBarTitle(getContext().getResources().getString(R.string.recipe_ingredients_activity));
    }
}
