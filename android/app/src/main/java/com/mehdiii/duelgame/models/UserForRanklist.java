package com.mehdiii.duelgame.models;

/**
 * Created by MeHdi on 5/12/2015.
 */
public class UserForRanklist extends User {
    private int placeInRank;

    public UserForRanklist(int placeInRank) {
        this.placeInRank = placeInRank;
    }

    public int getPlaceInRank() {
        return placeInRank;
    }

    public void setPlaceInRank(int placeInRank) {
        this.placeInRank = placeInRank;
    }
}
