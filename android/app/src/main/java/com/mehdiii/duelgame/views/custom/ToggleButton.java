package com.mehdiii.duelgame.views.custom;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.Button;

import com.mehdiii.duelgame.R;
import com.mehdiii.duelgame.utils.FontHelper;

/**
 * Created by omid on 4/5/2015.
 */
public class ToggleButton extends Button {
    private boolean selected = false;

    public ToggleButton(Context context) {
        super(context);
    }

    public ToggleButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTypeface(FontHelper.getIcons(getContext()));
        unselect();
    }

    public ToggleButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setTypeface(FontHelper.getIcons(getContext()));
        unselect();
    }

    @Override
    public boolean isSelected() {
        return selected;
    }

    public ToggleButton toggle() {
        if (selected)
            unselect();
        else select();
        return this;
    }

    public void select() {
        Drawable drawable = getResources().getDrawable(R.drawable.selected_page_indicator);
        this.setCompoundDrawablesWithIntrinsicBounds(null, null, null, drawable);
        setSelected(true);
        selected = true;
    }

    public void unselect() {
        Drawable drawable = getResources().getDrawable(R.drawable.selected_page_indicator_empty);
        this.setCompoundDrawablesWithIntrinsicBounds(null, null, null, drawable);
        setSelected(false);
        selected = false;
    }
}
