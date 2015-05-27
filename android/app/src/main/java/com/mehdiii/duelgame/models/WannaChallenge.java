package com.mehdiii.duelgame.models;

import com.google.gson.annotations.SerializedName;
import com.mehdiii.duelgame.models.base.BaseModel;

/**
 * Created by Omid on 5/23/2015.
 */
public class WannaChallenge extends BaseModel {

    @SerializedName("user_number")
    private String userNumber;
    @SerializedName("category")
    private int category;
    @SerializedName("message")
    private String message;

    public WannaChallenge(String userNumber, int category, String message) {
        this.userNumber = userNumber;
        this.category = category;
        this.message = message;
    }

    public String getUserNumber() {
        return userNumber;
    }

    public void setUserNumber(String userNumber) {
        this.userNumber = userNumber;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
