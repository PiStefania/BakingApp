package com.example.android.bakingrecipes.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.android.bakingrecipes.Adapters.DetailRecipeAdapter;
import com.example.android.bakingrecipes.DetailsActivity;
import com.example.android.bakingrecipes.Objects.DetailRecipe;
import com.example.android.bakingrecipes.Objects.Recipe;
import com.example.android.bakingrecipes.R;
import com.example.android.bakingrecipes.Utils.ItemDecoration;
import com.example.android.bakingrecipes.Utils.VariousMethods;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailsFragment extends Fragment implements DetailRecipeAdapter.DetailRecipeAdapterOnClickHandler, LoaderManager.LoaderCallbacks<ArrayList<DetailRecipe>> {

    private DetailRecipeAdapter detailRecipeAdapter;
    @Nullable
    @BindView(R.id.content_details) RecyclerView mRecyclerView;
    @Nullable
    @BindView(R.id.content_details_two_pane) LinearLayout mLinearLayout;

    private Recipe recipe;
    private static final int DETAIL_RECIPE_LOADER_ID = 2;
    private static final String RECIPE_EXTRA = "Recipe";
    private static final String LOG_TAG = DetailsActivity.class.getSimpleName();
    private ArrayList<DetailRecipe> detailRecipes;
    private boolean twoPane = false;

    OnRecipeDetailSelected mCallback;

    public interface OnRecipeDetailSelected {
        public void onRecipeDetailSelected(int position,ArrayList<DetailRecipe> detailRecipes);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            recipe = bundle.getParcelable(RECIPE_EXTRA);
        }
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details, container, false);
        ButterKnife.bind(this, view);

        if(RecipeStepFragment.changed){
            int padding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics());
            mRecyclerView.setPadding(0, ((DetailsActivity)getActivity()).getActionBarHeight() + 3 * padding, 0, 0);
        }

        ArrayList<DetailRecipe> mDetailRecipe = new ArrayList<>();
        detailRecipeAdapter = new DetailRecipeAdapter(getContext(), mDetailRecipe, this);

        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.margin_recycler_view);
        mRecyclerView.addItemDecoration(new ItemDecoration(spacingInPixels));
        mRecyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(detailRecipeAdapter);
        LoaderManager.LoaderCallbacks<ArrayList<DetailRecipe>> callback = this;
        getActivity().getSupportLoaderManager().initLoader(DETAIL_RECIPE_LOADER_ID, null, callback);

        return view;
    }

    @NonNull
    @Override
    public Loader<ArrayList<DetailRecipe>> onCreateLoader(int id, @Nullable Bundle args) {
        return new AsyncTaskLoader<ArrayList<DetailRecipe>>(getContext()) {

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
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (OnRecipeDetailSelected) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnRecipeDetailSelected");
        }
    }

    @Override
    public void onClick(int position) {
        // Notify the parent activity of selected item
        mCallback.onRecipeDetailSelected(position,detailRecipes);
    }
}