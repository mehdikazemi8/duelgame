package com.mehdiii.duelgame.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by MeHdi on 4/25/2015.
 */
public class ScoreHelper {
    static final int TITLE_SIZE = 8;
    static final int LEVEL_SIZE = 24;

    static Integer[] xpLevel = new Integer[]{
            50, 100, 150,             // diplom
            150, 250, 400,          // foghe diplom
            500, 800, 1200,          // lisans
            1500, 2000, 2500,         // foghe lisans
            3500, 4500, 5500,       // doctora
            7000, 8000, 9000,       // ostadyar
            10000, 11000, 12000,       // daneshyar
            15000, 20000, 40000        // ostad
            // full professor
    };

    static Integer[] xpTitle = null;
    static String[] strTitle = new String[]{
            "دیپلم",
            "فوق دیپلم",
            "لیسانس",
            "فوق لیسانس",
            "دکترا",
            "استادیار",
            "دانشیار",
            "استاد",
            "پروفسور"
    };

    private static void load() {
        xpTitle = new Integer[8];
        for (int i = 0; i < TITLE_SIZE; i++)
            xpTitle[i] = 0;

        for (int i = 0; i < LEVEL_SIZE; i++)
            xpTitle[i / 3] += xpLevel[i];
    }

    public static int getLevel(int score) {
        if (xpTitle == null)
            load();

        if (score == 0)
            return 1;

        for (int i = 0; i < LEVEL_SIZE; i++) {
            if (score - xpLevel[i] > 0)
                score -= xpLevel[i];
            else
                return i + 1;
        }
        return LEVEL_SIZE + 1;
    }

    public static int getThisLevelPercentage(int score) {
        if (xpTitle == null)
            load();

        if (score == 0)
            return 0;

        for (int i = 0; i < LEVEL_SIZE; i++) {
            if (score - xpLevel[i] > 0)
                score -= xpLevel[i];
            else
                return score * 100 / xpLevel[i];
        }

        // TODO last level it is always 50
        return 50;
    }

    public static String getTitle(int score) {
        if (xpTitle == null)
            load();

        if (score == 0)
            return strTitle[0];

        for (int i = 0; i < TITLE_SIZE; i++) {
            if (score - xpTitle[i] > 0)
                score -= xpTitle[i];
            else
                return strTitle[i];
        }
        return strTitle[8];
    }

    public static List<String> getTitles() {
        return new ArrayList<String>(Arrays.asList(strTitle));
    }

    public static List<Integer> getLevelScores() {
        return new ArrayList<Integer>(Arrays.asList(xpLevel));
    }

    public static List<Integer> getTitleScores() {
        if (xpTitle == null)
            load();
        return new ArrayList<Integer>(Arrays.asList(xpTitle));
    }
}
