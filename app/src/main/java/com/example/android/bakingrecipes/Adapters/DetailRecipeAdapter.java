package com.example.android.bakingrecipes.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.android.bakingrecipes.Objects.DetailRecipe;
import com.example.android.bakingrecipes.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class DetailRecipeAdapter extends RecyclerView.Adapter<DetailRecipeAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<DetailRecipe> mDetailRecipes;
    private DetailRecipeAdapterOnClickHandler mClickHandler;

    private static final String LOG_TAG = RecipeAdapter.class.getSimpleName();

    public DetailRecipeAdapter(Context context, ArrayList<DetailRecipe> detailRecipes,DetailRecipeAdapterOnClickHandler clickHandler){
        mContext = context;
        mDetailRecipes = detailRecipes;
        mClickHandler = clickHandler;
    }

    public interface DetailRecipeAdapterOnClickHandler {
        void onClick(DetailRecipe detailRecipe);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.recipe_detail_item) Button stepRecipe;

        public ViewHolder(View v){
            super(v);
            ButterKnife.bind(this, v);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            mClickHandler.onClick(mDetailRecipes.get(position));
        }
    }


    @Override
    public DetailRecipeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        // Create a new View
        View v = LayoutInflater.from(mContext).inflate(R.layout.list_item_details,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        String recipeStepTitle = mDetailRecipes.get(position).getDetailTitle();
        holder.stepRecipe.setText(recipeStepTitle);
    }

    @Override
    public int getItemCount() {
        if(mDetailRecipes == null)
            return 0;
        return mDetailRecipes.size();
    }

    public void setDetailRecipeData(ArrayList<DetailRecipe> detailRecipeData) {
        mDetailRecipes = detailRecipeData;
        notifyDataSetChanged();
    }
}