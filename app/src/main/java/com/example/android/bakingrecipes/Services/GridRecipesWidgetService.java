package com.example.android.bakingrecipes.Services;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.android.bakingrecipes.AppWidgetProvider.RecipesWidgetProvider;
import com.example.android.bakingrecipes.Objects.Recipe;
import com.example.android.bakingrecipes.Objects.WidgetItem;
import com.example.android.bakingrecipes.R;
import com.example.android.bakingrecipes.Utils.NetworkUtils;
import com.example.android.bakingrecipes.Utils.RecipesJsonUtils;

import java.util.ArrayList;


public class GridRecipesWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent){
        return new RecipesRemoteViewsFactory(this.getApplicationContext(), intent);
    }
}

class RecipesRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory{

    private ArrayList<Recipe> recipes;
    private ArrayList<WidgetItem> items = new ArrayList<WidgetItem>();;
    private static final String LOG_TAG = RecipesRemoteViewsFactory.class.getSimpleName();
    private Context mContext;
    private int appWidgetId;

    public RecipesRemoteViewsFactory(Context context, Intent intent){
        mContext = context;
        appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    @Override
    public void onCreate() {
        try {
            getRecipes();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (int i=0;i<recipes.size();i++){
            items.add(new WidgetItem(recipes.get(i).getName()));
        }
    }

    @Override
    public void onDataSetChanged() {
    }

    @Override
    public void onDestroy() {
        items.clear();
    }

    @Override
    public int getCount() {
        return recipes.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_recipe_grid_item);
        rv.setTextViewText(R.id.widget_item, items.get(position).getRecipeName());
        Bundle extras = new Bundle();
        extras.putString(RecipesWidgetProvider.PRESS_ITEM,recipes.get(position).getRecipeIngredients());
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);
        rv.setOnClickFillInIntent(R.id.widget_item, fillInIntent);
        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    private void getRecipes() throws InterruptedException {
        Thread th = new Thread(new Runnable() {
            public void run() {
                try {
                    final String jsonResponse = NetworkUtils.getResponseFromHttpUrl(NetworkUtils.buildUrl());
                    recipes = RecipesJsonUtils.JSONtoArrayList(jsonResponse);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        th.start();
        th.join();
    }
}