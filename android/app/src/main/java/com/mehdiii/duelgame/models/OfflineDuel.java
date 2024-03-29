package com.mehdiii.duelgame.models;

import com.google.gson.annotations.SerializedName;
import com.mehdiii.duelgame.models.base.BaseModel;

/**
 * Created by mehdiii on 2/4/16.
 */
public class OfflineDuel extends BaseModel {
    @SerializedName("course_name")
    private String courseName;
    @SerializedName("game_score")
    private Integer userDuelScore;
    @SerializedName("id")
    private String duelId;
    @SerializedName("category")
    private String category;
    @SerializedName("opponent")
    private User opponent;
    @SerializedName("diamonds_collected")
    private Integer diamondsCollected;

    public Integer getDiamondsCollected() {
        return diamondsCollected;
    }

    public void setDiamondsCollected(Integer diamondsCollected) {
        this.diamondsCollected = diamondsCollected;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public OfflineDuel() {
    }

    public OfflineDuel(Integer userDuelScore, String duelId, User opponent) {
        this.userDuelScore = userDuelScore;
        this.duelId = duelId;
        this.opponent = opponent;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public Integer getUserDuelScore() {
        return userDuelScore;
    }

    public void setUserDuelScore(Integer userDuelScore) {
        this.userDuelScore = userDuelScore;
    }

    public String getDuelId() {
        return duelId;
    }

    public void setDuelId(String duelId) {
        this.duelId = duelId;
    }

    public User getOpponent() {
        return opponent;
    }

    public void setOpponent(User opponent) {
        this.opponent = opponent;
    }
}
