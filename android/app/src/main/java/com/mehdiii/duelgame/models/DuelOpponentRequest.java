package com.mehdiii.duelgame.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Omid on 5/23/2015.
 */
public class DuelOpponentRequest extends User {
    @SerializedName("message")
    private String message;
    @SerializedName("category")
    private int category;

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
