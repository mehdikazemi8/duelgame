package com.mehdiii.duelgame.models;

import com.google.gson.annotations.SerializedName;
import com.mehdiii.duelgame.models.base.BaseModel;

import java.util.List;

/**
 * Created by mehdiii on 9/29/15.
 */
public class OnlineUsersList extends BaseModel {
    @SerializedName("users")
    private List<Friend> users;

    public List<Friend> getUsers() {
        return users;
    }

    public void setUsers(List<Friend> users) {
        this.users = users;
    }
}
