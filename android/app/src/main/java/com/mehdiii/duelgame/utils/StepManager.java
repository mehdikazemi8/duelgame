package com.mehdiii.duelgame.utils;

import android.content.Context;

import com.mehdiii.duelgame.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by frshd on 3/1/16.
 */
public class StepManager {
    static Map<String, String> map = new HashMap<>();
    static Map<String,Integer> positionMap = new HashMap<>();
    static boolean loaded = false;

    private static void load(Context context) {
        String[] values = context.getResources().getStringArray(R.array.step_values);
        String[] keys = context.getResources().getStringArray(R.array.step_keys);

        int counter = 0;
        for (String key : keys) {
            String k = key;
            map.put(k, values[counter++]);
            positionMap.put(k, counter);
        }
        loaded = true;
    }

    public static String getStep(Context context, String id) {
        if (!loaded)
            load(context);
        return map.get(id);
    }

    public static int getStepPosition(Context context, String id){
        if (!loaded)
            load(context);
        return positionMap.get(id);
    }
}

