package com.mehdiii.duelgame.models;

import com.google.gson.annotations.SerializedName;
import com.mehdiii.duelgame.models.base.BaseModel;
import com.mehdiii.duelgame.models.base.CommandType;

public class ChallengeRequestDecision extends BaseModel {
    @SerializedName("decision")
    private int decision;
    @SerializedName("user_number")
    private String userNumber;
    @SerializedName("category")
    private int category;

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public ChallengeRequestDecision(CommandType command) {
        super(command);
    }

    public ChallengeRequestDecision(CommandType command, int decision, String userNumber) {
        super(command);
        this.decision = decision;
        this.userNumber = userNumber;
    }

    public int getDecision() {
        return decision;
    }

    public void setDecision(int decision) {
        this.decision = decision;
    }

    public String getUserNumber() {
        return userNumber;
    }

    public void setUserNumber(String userNumber) {
        this.userNumber = userNumber;
    }
}
