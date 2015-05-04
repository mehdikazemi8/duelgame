package com.mehdiii.duelgame.views.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.TextView;

/**
 * Created by Omid on 5/4/2015.
 */
public class SquareTextView extends TextView {
    public static final int MAX_WIDTH = 200;

    public SquareTextView(Context context) {
        super(context);
    }

    int width;

    public SquareTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        width = context.getResources().getDisplayMetrics().widthPixels / 3;
        if (width > MAX_WIDTH)
            width = MAX_WIDTH;
    }

    public SquareTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(width, width);
    }
}
