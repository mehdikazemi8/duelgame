package com.mehdiii.duelgame.models;

import android.util.Log;

import com.google.gson.annotations.SerializedName;
import com.mehdiii.duelgame.models.base.BaseModel;
import com.mehdiii.duelgame.models.base.CommandType;

/**
 * Created by mehdiii on 6/14/16.
 */
public class VerifyPhoneNumber extends BaseModel {
    @SerializedName("secret")
    private int number;

    public VerifyPhoneNumber(CommandType command, int number) {
        super(command);
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
