package com.mehdiii.duelgame.models.base;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

/**
 * Created by omid on 4/8/2015.
 */
public abstract class BaseModel {

    @SerializedName("code")
    private String command;

    protected String getCommand() {
        return command;
    }

    protected void setCommand(String command) {
        this.command = command;
    }

    public String serialize() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
