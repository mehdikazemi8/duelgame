package com.mehdiii.duelgame.models;

import android.util.Log;

import com.google.gson.annotations.SerializedName;

/**
 * Created by frshd on 2/21/16.
 */
public class CourseRank {
    @SerializedName("rank")
    private int rank;
    @SerializedName("category")
    private String category;
    public String getCategory() {return category;}
    public void setCategory(String category) {
        this.category = category;
    }
    public int getRank() {
        return rank;
    }
    public void setRank(int rank) {this.rank = rank;}
    public CourseRank(String category, int rank){
        this.category = category;
        this.rank = rank;
    }
}
