package com.mehdiii.duelgame.views.activities.splash;

import android.content.Context;
import android.graphics.Color;

import com.mehdiii.duelgame.R;

/**
 * Created by Omid on 5/10/2015.
 */
public class SplashColors {
    public static int[] getArray(Context context) {
        // Reads splash colors from resources
        String[] colors = context.getResources().getStringArray(R.array.splash_colors_array);
        int[] decimalColors = new int[colors.length];
        for (int i = 0; i < colors.length; i++)
            decimalColors[i] = Color.parseColor(colors[i]);

        return decimalColors;
    }
}
