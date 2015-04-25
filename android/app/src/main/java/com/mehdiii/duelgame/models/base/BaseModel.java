package com.mehdiii.duelgame.models.base;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Type;

/**
 * Created by omid on 4/8/2015.
 */
public class BaseModel {

    @SerializedName("code")
    String command;

    public void setCommand(CommandType type) {
        setCommand(ActionEmitter.getInstance().getCommandCode(type));
    }

    protected void setCommand(String command) {
        this.command = command;
    }

    public CommandType getCommand() {
        return ActionEmitter.getInstance().getCommandType(command);
    }

    public String serialize(CommandType type) {
        setCommand(type);
        return serialize();
    }

    public String serialize() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public static <T> T deserialize(String json, Type type) {
        Gson gson = new Gson();
        try {
            return gson.fromJson(json, type);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }
}