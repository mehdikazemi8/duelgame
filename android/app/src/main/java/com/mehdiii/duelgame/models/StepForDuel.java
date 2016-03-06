package com.mehdiii.duelgame.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by frshd on 3/6/16.
 */
public class StepForDuel {
    @SerializedName("stepId")
    private long stepId;
    @SerializedName("name")
    private String name;
    @SerializedName("stars")
    private int stars;

    public long getStepId() {
        return stepId;
    }

    public void setStepId(long stepId) {
        this.stepId = stepId;
    }

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
}
