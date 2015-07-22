package com.mehdiii.duelgame.models;

import com.google.gson.annotations.SerializedName;
import com.mehdiii.duelgame.models.base.BaseModel;

/**
 * Created by Omid on 7/22/2015.
 */
public class Pair<T, U> extends BaseModel {

    public Pair(T key, U value) {
        setKey(key);
        setValue(value);
    }
    @SerializedName("key")
    private T key;
    @SerializedName("value")
    private U value;

    public T getKey() {
        return key;
    }

    public void setKey(T key) {
        this.key = key;
    }

    public U getValue() {
        return value;
    }

    public void setValue(U value) {
        this.value = value;
    }
}
