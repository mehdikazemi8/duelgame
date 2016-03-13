package com.mehdiii.duelgame.managers;

import android.content.Context;

import com.mehdiii.duelgame.R;

import java.util.Arrays;
import java.util.List;

/**
 * Created by frshd on 3/13/16.
 */
public class MajorManager {
    public static String get(Context context, int idx) {
        List<String> majors = Arrays.asList(context.getResources().getStringArray(R.array.majors));
        try {
            return majors.get(idx);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        return null;
    }
}
