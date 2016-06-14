package com.mehdiii.duelgame.models;

import com.google.gson.annotations.SerializedName;
import com.mehdiii.duelgame.models.base.BaseModel;
import com.mehdiii.duelgame.models.base.CommandType;

/**
 * Created by mehdiii on 6/14/16.
 */
public class SendPhoneNumber extends BaseModel {
    @SerializedName("phone_number")
    private String phoneNumber;

    public SendPhoneNumber(CommandType command, String phoneNumber) {
        super(command);
        this.phoneNumber = phoneNumber;
    }

    public SendPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
