package com.mehdiii.duelgame.models;

import android.util.Log;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mehdiii on 8/22/15.
 */
public class CourseScore {
    @SerializedName("overall")
    private int overall;
    @SerializedName("week")
    private int week;

    public int getScore(String period) {
        if (period.isEmpty())
            return 0;
        if (period.equals("week"))
            return getWeek();
        if (period.equals("overall"))
            return getOverall();

        Log.d("DUEL_APP", "CourseScore : not week, not overall");
        return 0;
    }

    public CourseScore(int overall, int week) {
        this.overall = overall;
        this.week = week;
    }

    public int getOverall() {
        return overall;
    }

    public void setOverall(int overall) {
        this.overall = overall;
    }

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }
}
