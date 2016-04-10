package com.mehdiii.duelgame.models;

import com.google.gson.annotations.SerializedName;
import com.mehdiii.duelgame.models.base.BaseModel;

import java.util.ArrayList;

/**
 * Created by frshd on 4/5/16.
 */
public class CourseMap extends BaseModel {
    @SerializedName("map")
    private ArrayList<StepCourse> stepCourses;

    public ArrayList<StepCourse> getStepCourses() {
        return stepCourses;
    }

    public void setStepCourses(ArrayList<StepCourse> stepCourses) {
        this.stepCourses = stepCourses;
    }
}
