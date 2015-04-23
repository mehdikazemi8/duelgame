package com.mehdiii.duelgame.managers;

import android.content.Context;

import com.mehdiii.duelgame.R;

import java.util.Arrays;
import java.util.List;

/**
 * Created by omid on 4/22/2015.
 */
public class ProvinceManager {
    public static String get(Context context, int idx) {
        List<String> provinces = Arrays.asList(context.getResources().getStringArray(R.array.province_array));
        try {
            return provinces.get(idx);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        return null;
    }
}
