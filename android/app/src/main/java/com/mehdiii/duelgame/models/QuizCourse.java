package com.mehdiii.duelgame.models;

import com.google.gson.annotations.SerializedName;
import com.mehdiii.duelgame.models.base.BaseModel;
import com.mehdiii.duelgame.models.base.CommandType;

/**
 * Created by mehdiii on 1/14/16.
 */
public class QuizCourse extends BaseModel {
    @SerializedName("course_name")
    private String courseName;
    @SerializedName("count")
    private Integer count;

    public QuizCourse(String courseName, Integer count) {
        this.courseName = courseName;
        this.count = count;
    }

    public QuizCourse(CommandType command, String courseName, Integer count) {
        super(command);
        this.courseName = courseName;
        this.count = count;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
}
