package com.mehdiii.duelgame.models.base;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Type;

/**
 * Created by omid on 4/8/2015.
 */
public class BaseModel {

    @SerializedName("code")
    String command;

    protected void setCommand(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }

    public String serialize() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public static <T> T deserialize(String json, Type type) {
        Gson gson = new Gson();
        return gson.fromJson(json, type);
    }
}
