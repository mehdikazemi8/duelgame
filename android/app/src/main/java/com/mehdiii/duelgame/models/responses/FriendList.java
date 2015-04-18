package com.mehdiii.duelgame.models.responses;

import com.google.gson.annotations.SerializedName;
import com.mehdiii.duelgame.models.Friend;

import java.util.List;

/**
 * Created by omid on 4/15/2015.
 */
public class FriendList {
    @SerializedName("friends")
    private List<Friend> friends;

    public List<Friend> getFriends() {
        return friends;
    }

    public void setFriends(List<Friend> friends) {
        this.friends = friends;
    }
}
