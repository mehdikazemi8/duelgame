package com.mehdiii.duelgame.models;

import com.google.gson.annotations.SerializedName;
import com.mehdiii.duelgame.models.base.SerializableModel;

/**
 * Created by omid on 4/8/2015.
 */
public class RegisterUser extends SerializableModel {

    @SerializedName("code")
    private String command = "RU";
    @SerializedName("email")
    private String email;
    @SerializedName("user_id")
    private String userId;
    @SerializedName("name")
    private String username;
    @SerializedName("ostan")
    private int ostan;
    @SerializedName("avatar")
    private int avatar;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getOstan() {
        return ostan;
    }

    public void setOstan(int ostan) {
        this.ostan = ostan;
    }

    public int getAvatar() {
        return avatar;
    }

    public void setAvatar(int avatar) {
        this.avatar = avatar;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
