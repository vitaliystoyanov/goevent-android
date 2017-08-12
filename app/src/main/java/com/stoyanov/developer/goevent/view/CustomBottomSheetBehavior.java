package com.stoyanov.developer.goevent.view;

import android.content.Context;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;

public class CustomBottomSheetBehavior extends BottomSheetBehavior {

    public CustomBottomSheetBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, View child, View target, int dx, int dy, int[] consumed) {
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed);
        int currentState = getState();
        int newState = dy > 0 ? STATE_HIDDEN : STATE_EXPANDED;
        if (currentState != newState) {
            setState(newState);
        }
    }
}
