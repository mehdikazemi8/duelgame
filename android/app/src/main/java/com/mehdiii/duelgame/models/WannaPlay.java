package com.mehdiii.duelgame.models;

import com.google.gson.annotations.SerializedName;
import com.mehdiii.duelgame.models.base.BaseModel;
import com.mehdiii.duelgame.models.base.CommandType;

/**
 * Created by Omid on 5/6/2015.
 */
public class WannaPlay extends BaseModel {
    @SerializedName("category")
    private String category;

    public WannaPlay(String category) {
        this.category = category;
    }

    public WannaPlay(CommandType command, String category) {
        super(command);
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
