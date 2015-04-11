package com.mehdiii.duelgame.models.base;

import com.google.gson.Gson;

/**
 * Created by omid on 4/8/2015.
 */
public abstract class SerializableModel {
    public String serialize() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
