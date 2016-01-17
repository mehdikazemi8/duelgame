package com.mehdiii.duelgame.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by mehdiii on 1/17/16.
 */
public class OneCourseAnswer {
    @SerializedName("category")
    private String category;
    @SerializedName("data")
    private ArrayList<String> data;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public ArrayList<String> getData() {
        return data;
    }

    public void setData(ArrayList<String> data) {
        this.data = data;
    }
}
