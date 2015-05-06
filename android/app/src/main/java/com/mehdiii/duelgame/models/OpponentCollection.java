package com.mehdiii.duelgame.models;

import com.google.gson.annotations.SerializedName;
import com.mehdiii.duelgame.models.base.BaseModel;

import java.util.List;

/**
 * Created by Omid on 5/6/2015.
 */
public class OpponentCollection extends BaseModel {
    @SerializedName("opponents")
    private List<User> opponents;

    public List<User> getOpponents() {
        return opponents;
    }

    public void setOpponents(List<User> opponents) {
        this.opponents = opponents;
    }
}
