package com.mehdiii.duelgame.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by frshd on 4/7/16.
 */
public class ProgressForStep {
    @SerializedName("name")
    private String name;
    @SerializedName("stars")
    private int stars;
    @SerializedName("totalStars")
    private int totalStars;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public int getTotalStars() {
        return totalStars;
    }

    public void setTotalStars(int totalStars) {
        this.totalStars = totalStars;
    }
}
