package com.mehdiii.duelgame.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.TextView;

/**
 * Created by omid on 4/5/2015.
 */
public class FontHelper {
    private static final String FONT_APP_NAME = "roya_bold.ttf";
    private static final String FONT_AVATAR_NAME = "avatars.ttf";

    private static Typeface koodak = null;
    private static Typeface avatars = null;

    public static Typeface getKoodak(Context context) {
        if (koodak == null)
            koodak = Typeface.createFromAsset(context.getAssets(), FONT_APP_NAME);
        return koodak;
    }

    public static void setKoodakFor(Context context, TextView... views) {
        Typeface koodak = getKoodak(context);
        for (TextView view : views) {
            view.setTypeface(koodak);
        }
    }

    public static Typeface getAvatars(Context context) {
        if (avatars == null)
            avatars = Typeface.createFromAsset(context.getAssets(), FONT_AVATAR_NAME);
        return avatars;
    }
}