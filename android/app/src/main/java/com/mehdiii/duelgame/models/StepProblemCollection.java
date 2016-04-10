package com.mehdiii.duelgame.models;

import com.google.gson.annotations.SerializedName;
import com.mehdiii.duelgame.models.base.BaseModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by frshd on 4/10/16.
 */
public class StepProblemCollection extends BaseModel {
    @SerializedName("problems")
    private ArrayList<QuestionForQuiz> questions;
    @SerializedName("course_id")
    private String course_id;
    @SerializedName("chapter_index")
    private Integer chapter_index;

    public ArrayList<QuestionForQuiz> getQuestions() {
        return questions;
    }

    public void setQuestions(ArrayList<QuestionForQuiz> questions) {
        this.questions = questions;
    }

    public String getCourse_id() {
        return course_id;
    }

    public void setCourse_id(String course_id) {
        this.course_id = course_id;
    }

    public Integer getChapter_index() {
        return chapter_index;
    }

    public void setChapter_index(Integer chapter_index) {
        this.chapter_index = chapter_index;
    }
}
