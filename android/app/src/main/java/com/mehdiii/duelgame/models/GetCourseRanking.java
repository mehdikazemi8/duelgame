package com.mehdiii.duelgame.models;

import com.google.gson.annotations.SerializedName;
import com.mehdiii.duelgame.models.base.BaseModel;

/**
 * Created by mehdiii on 7/7/15.
 */
public class GetCourseRanking extends BaseModel {
    @SerializedName("course_id")
    private String courseId;
    @SerializedName("period")
    private String period;

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public GetCourseRanking(String courseId, String period) {
        this.courseId = courseId;
        this.period = period;
    }
}
