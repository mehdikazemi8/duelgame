package com.mehdiii.duelgame.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by omid on 4/13/2015.
 */
public class Friend extends User {
    @SerializedName("status")
    private String status;
    @SerializedName("is_online")
    private boolean isOnline;
    @SerializedName("accepted")
    private boolean accepted;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean isOnline) {
        this.isOnline = isOnline;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public Friend getRespondFriendRequest(boolean decision) {
        setAccepted(decision);
        setCommand("AFR");
        return this;
    }
}
