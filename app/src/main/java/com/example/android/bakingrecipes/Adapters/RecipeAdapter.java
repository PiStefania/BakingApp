package com.example.android.bakingrecipes.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.bakingrecipes.R;
import com.example.android.bakingrecipes.Objects.Recipe;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<Recipe> mRecipes;
    private RecipeAdapterOnClickHandler mClickHandler;

    private static final String LOG_TAG = RecipeAdapter.class.getSimpleName();

    public RecipeAdapter(Context context, ArrayList<Recipe> recipes,RecipeAdapterOnClickHandler clickHandler){
        mContext = context;
        mRecipes = recipes;
        mClickHandler = clickHandler;
    }

    public interface RecipeAdapterOnClickHandler {
        void onClick(Recipe recipe);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.recipe_image_view) ImageView imageRecipe;
        @BindView(R.id.recipe_text_view) TextView recipeName;

        public ViewHolder(View v){
            super(v);
            ButterKnife.bind(this, v);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            mClickHandler.onClick(mRecipes.get(position));
        }
    }


    @Override
    public RecipeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        // Create a new View
        View v = LayoutInflater.from(mContext).inflate(R.layout.list_item,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        String name = mRecipes.get(position).getName();
        holder.recipeName.setText(name);
        String imageUrl = mRecipes.get(position).getImage();
        if(imageUrl != null) {
            if(!imageUrl.isEmpty()) {
                Picasso.get()
                        .load(imageUrl)
                        .placeholder(R.drawable.no_image_found)
                        .error(R.drawable.no_image_found)
                        .into(holder.imageRecipe);
            }
            else{
                holder.imageRecipe.setImageDrawable(mContext.getResources().getDrawable(R.drawable.no_image_found));
            }
        }else{
            holder.imageRecipe.setImageDrawable(mContext.getResources().getDrawable(R.drawable.no_image_found));
        }
    }

    @Override
    public int getItemCount() {
        if(mRecipes == null)
            return 0;
        return mRecipes.size();
    }

    public void setRecipeData(ArrayList<Recipe> recipes) {
        mRecipes = recipes;
        notifyDataSetChanged();
    }
}