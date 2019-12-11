package com.example.voicepaper.adapter.Decoration;

import android.app.Activity;
import android.graphics.Rect;
import android.util.TypedValue;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

public class RoomMemberRecyclerViewDecoration extends RecyclerView.ItemDecoration {
    private int spanCount;
    private int spacing;
    private int outerMargin;

    public RoomMemberRecyclerViewDecoration(Fragment mFragment) {
        spanCount = 1;
        spacing = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                20, mFragment.getResources().getDisplayMetrics());
        outerMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                20, mFragment.getResources().getDisplayMetrics());
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int maxCount = parent.getAdapter().getItemCount();
        int position = parent.getChildAdapterPosition(view);
        int column = position % spanCount;
        int row = position / spanCount;
        int lastRow = (maxCount - 1) / spanCount;

        //outRect.left = spacing - column * spacing / spanCount;
        //outRect.right = spacing - (1 - column) * spacing / spanCount;
        outRect.top = spacing;
        outRect.bottom = spacing / 3;

        if (row == lastRow) {
            outRect.bottom = outerMargin;
        }
    }
}
