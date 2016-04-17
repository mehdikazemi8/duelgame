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

    public ArrayList<StepCourse> getStepByCategory(int category){
        ArrayList<StepCourse> stepCourses = new ArrayList<>();
        for (StepCourse stepCourse: this.stepCourses ){
            if (stepCourse.getCategory() == category )
                stepCourses.add(stepCourse);
        }
        return stepCourses;
    }
    public StepCourse getStepByCategoryAndBook(int category, int book){
        for (StepCourse stepCourse: this.stepCourses ){
            if (stepCourse.getCategory() == category && stepCourse.getBook() == book )
                return stepCourse;
        }
        return null;
    }
}
