package com.mehdiii.duelgame.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by omid on 4/13/2015.
 */
public class Friend extends User {
    @SerializedName("status")
    private String status;
    @SerializedName("is_online")
    private boolean isOnline;
    @SerializedName("is_busy")
    private boolean isBusy;
    @SerializedName("accepted")
    private boolean accepted;

    @SerializedName("totalStars")
    private Integer totalStars;
    @SerializedName("lastStepBook")
    private Integer lastStepBook;
    @SerializedName("lastStepChapter")
    private Integer lastStepChapter;

    MutualStats statistics;

    public Integer getLastStepChapter() {
        return lastStepChapter;
    }

    public void setLastStepChapter(Integer lastStepChapter) {
        this.lastStepChapter = lastStepChapter;
    }

    public Integer getLastStepBook() {
        return lastStepBook;
    }

    public void setLastStepBook(Integer lastStepBook) {
        this.lastStepBook = lastStepBook;
    }

    public Integer getTotalStars() {
        return totalStars;
    }

    public void setTotalStars(Integer totalStars) {
        this.totalStars = totalStars;
    }

    public boolean isBusy() {
        return isBusy;
    }

    public void setIsBusy(boolean isBusy) {
        this.isBusy = isBusy;
    }

    public MutualStats getStatistics() {
        return statistics;
    }

    public void setStatistics(MutualStats statistics) {
        this.statistics = statistics;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean isOnline) {
        this.isOnline = isOnline;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }
}
