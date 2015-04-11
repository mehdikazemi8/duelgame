package com.mehdiii.duelgame.models;

import com.google.gson.annotations.SerializedName;
import com.mehdiii.duelgame.models.base.SerializableModel;

/**
 * Created by omid on 4/8/2015.
 */
public class User extends SerializableModel {

    @SerializedName("code")
    private String command = "RU";
    @SerializedName("email")
    private String email;
    @SerializedName("user_id")
    private String userId;
    @SerializedName("name")
    private String username;
    @SerializedName("ostan")
    private int province;
    @SerializedName("avatar")
    private int avatar;
    @SerializedName("level")
    private int level;

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

    public int getProvince() {
        return province;
    }

    public void setProvince(int province) {
        this.province = province;
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

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
