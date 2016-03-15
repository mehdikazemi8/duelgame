package com.mehdiii.duelgame.utils;

import android.content.Context;

import com.mehdiii.duelgame.R;

import java.util.HashMap;
import java.util.Map;

public class CategoryManager {

    //    private static String[] values = null;
//    private static String[] keys = null;
    static Map<Integer, String> map = new HashMap<>();
    static boolean loaded = false;

    private static void load(Context context) {
        String[] values = context.getResources().getStringArray(R.array.categories_values);
        int[] keys = context.getResources().getIntArray(R.array.categories_keys);

        int counter = 0;
        for (int key : keys) {
            map.put(key, values[counter++]);
        }
        loaded = true;
    }

    public static String getCategory(Context context, int id) {
        if (!loaded)
            load(context);
        return map.get(id);
    }
}
