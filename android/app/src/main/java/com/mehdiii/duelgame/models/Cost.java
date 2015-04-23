package com.mehdiii.duelgame.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by omid on 4/22/2015.
 */
public class Cost {
    @SerializedName("value")
    private int value;
    @SerializedName("type")
    private int type;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        String str = String.valueOf(value) + " ";
        if (type == 1) {
            str += "تومان";
        } else if (type == 2) {
            str += "الماس";
        }
        return str;
    }
}
