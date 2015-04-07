package com.mehdiii.duelgame.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.mehdiii.duelgame.R;

/**
 * Created by omid on 4/6/2015.
 */
public class AvatarHelper {
    static int[] avatars = null;

    public static String getSampleChar() {
        return String.valueOf((char) 0xe998);
    }


    public static Bitmap getAvatarBitmap(Context context, int position) {
        return BitmapFactory.decodeResource(context.getResources(), getAvatarResource(context, position));
    }

    private static void loadAvatars(Context context) {
        TypedArray imgs = context.getResources().obtainTypedArray(R.array.avatars);
        int length = imgs.length();
        avatars = new int[length];
        for (int i = 0; i < length; i++)
            avatars[i] = imgs.getResourceId(i, 0);
        imgs.recycle();
    }

    public static int getAvatarResource(Context context, int position) {
        if (avatars == null)
            loadAvatars(context);

        if (avatars.length > position)
            return avatars[position - 1];
        else
            return avatars[avatars.length - 1];
    }
}
