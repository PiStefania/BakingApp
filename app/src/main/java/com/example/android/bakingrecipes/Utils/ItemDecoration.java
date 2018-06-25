package com.example.android.bakingrecipes.Utils;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class ItemDecoration extends RecyclerView.ItemDecoration{

    private int spaceColumn;

    public ItemDecoration(int space) {
        this.spaceColumn = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {
        outRect.left = spaceColumn;
        outRect.right = spaceColumn;
        outRect.bottom = spaceColumn;
        outRect.top = spaceColumn;
    }


}