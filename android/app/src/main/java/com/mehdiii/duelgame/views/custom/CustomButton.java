package com.mehdiii.duelgame.views.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

import com.mehdiii.duelgame.utils.FontHelper;

/**
 * Created by mehdiii on 1/14/16.
 */
public class CustomButton extends Button {
    public CustomButton(Context context) {
        super(context);
        fixFont();
    }

    public CustomButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        fixFont();
    }

    public CustomButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        fixFont();
    }

    private void fixFont() {
        setTypeface(FontHelper.getDefaultTypeface(getContext()));
    }
}
