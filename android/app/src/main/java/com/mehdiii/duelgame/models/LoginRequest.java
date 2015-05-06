package com.mehdiii.duelgame.models;

import com.google.gson.annotations.SerializedName;
import com.mehdiii.duelgame.models.base.BaseModel;
import com.mehdiii.duelgame.models.base.CommandType;

/**
 * Created by Omid on 5/6/2015.
 */
public class LoginRequest extends BaseModel {
    @SerializedName("user_id")
    private String userId;

    public LoginRequest(String userId) {
        this.userId = userId;
    }

    public LoginRequest(CommandType command, String userId) {
        super(command);
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
