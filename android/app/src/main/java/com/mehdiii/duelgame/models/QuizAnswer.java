package com.mehdiii.duelgame.models;

import com.google.gson.annotations.SerializedName;
import com.mehdiii.duelgame.models.base.BaseModel;

import java.util.ArrayList;

/**
 * Created by mehdiii on 1/17/16.
 */
public class QuizAnswer extends BaseModel {
    @SerializedName("id")
    private String id;
    @SerializedName("comment")
    private String comment;
    @SerializedName("answers")
    private ArrayList<OneCourseAnswer> answers;

    public QuizAnswer(String id) {
        this.id = id;
    }

    public void addOneCourseAnswer(OneCourseAnswer oneCourseAnswer) {
        if(answers == null) {
            answers = new ArrayList<>();
        }
        answers.add(oneCourseAnswer);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public ArrayList<OneCourseAnswer> getAnswers() {
        return answers;
    }

    public void setAnswers(ArrayList<OneCourseAnswer> answers) {
        this.answers = answers;
    }
}
