package com.mehdiii.duelgame.views.custom;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;

import com.mehdiii.duelgame.utils.FontHelper;

public class CustomButton extends AppCompatButton {
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
