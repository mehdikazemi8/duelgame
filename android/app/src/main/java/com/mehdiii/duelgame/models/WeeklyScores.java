package com.mehdiii.duelgame.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by frshd on 2/21/16.
 */
public class WeeklyScores {
    @SerializedName("category")
    private String category;
    @SerializedName("score")
    private int score;
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
