package com.mehdiii.duelgame.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by MeHdi on 5/12/2015.
 */
public class UserForRanklist extends User {
    @SerializedName("rank")
    private int rank;
    @SerializedName("cup")
    private int cup;
    @SerializedName("cups")
    List<Integer> cups;

    public UserForRanklist(int rank, int cup) {
        this.rank = rank;
        this.cup = cup;
    }

    public UserForRanklist(List<Integer> cups) {
        this.cups = cups;
    }

    public List<Integer> getCups() {
        return cups;
    }

    public void setCups(List<Integer> cups) {
        this.cups = cups;
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
