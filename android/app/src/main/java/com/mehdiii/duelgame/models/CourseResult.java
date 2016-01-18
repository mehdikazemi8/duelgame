package com.mehdiii.duelgame.models;

import com.google.gson.annotations.SerializedName;
import com.mehdiii.duelgame.models.base.BaseModel;

/**
 * Created by mehdiii on 1/18/16.
 */
public class CourseResult extends BaseModel {
    @SerializedName("course_name")
    private String courseName;
    @SerializedName("user_percent")
    private Double userPercent;
    @SerializedName("avg_percent")
    private Double avgPercent;
    @SerializedName("max_percent")
    private Double maxPercent;
    @SerializedName("median")
    private Double median;
    @SerializedName("rank")
    private Integer rank;

    public CourseResult() {
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public Double getUserPercent() {
        return userPercent;
    }

    public void setUserPercent(Double userPercent) {
        this.userPercent = userPercent;
    }

    public Double getAvgPercent() {
        return avgPercent;
    }

    public void setAvgPercent(Double avgPercent) {
        this.avgPercent = avgPercent;
    }

    public Double getMaxPercent() {
        return maxPercent;
    }

    public void setMaxPercent(Double maxPercent) {
        this.maxPercent = maxPercent;
    }

    public Double getMedian() {
        return median;
    }

    public void setMedian(Double median) {
        this.median = median;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }
}
