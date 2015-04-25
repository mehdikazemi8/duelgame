package com.mehdiii.duelgame.utils;

/**
 * Created by MeHdi on 4/25/2015.
 */
public class ScoreHelper {
    static final int TITLE_SIZE = 8;
    static final int LEVEL_SIZE = 24;

    static Integer[] xpLevel = new Integer[]{
            30, 60, 90,             // diplom
            120, 150, 300,          // foghe diplom
            375, 525, 600,          // lisans
            750, 900, 1000,         // foghe lisans
            1100, 1500, 1500,       // doctora
            1500, 1500, 1500,       // ostadyar
            1500, 2000, 2000,       // daneshyar
            3500, 3500, 4000        // ostad
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
            "پروفسور تمام",
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
        return 25;
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
                return score*100/xpLevel[i];
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
}
