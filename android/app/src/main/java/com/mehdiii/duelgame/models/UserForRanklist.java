package com.mehdiii.duelgame.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by MeHdi on 5/12/2015.
 */
public class UserForRanklist extends User {
    @SerializedName("rank")
    private int rank;
    @SerializedName("cup")
    private int cup;

    public UserForRanklist(int rank, int cup) {
        this.rank = rank;
        this.cup = cup;
    }

    public int getCup() {
        return cup;
    }

    public void setCup(int cup) {
        this.cup = cup;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }
}
