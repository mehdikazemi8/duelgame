package com.mehdiii.duelgame.models;

import com.google.gson.annotations.SerializedName;
import com.mehdiii.duelgame.models.base.BaseModel;

import java.util.ArrayList;

/**
 * Created by mehdiii on 1/17/16.
 */
public class QuestionForQuiz extends BaseModel {
    @SerializedName("question_text")
    private String questionText;
    @SerializedName("options")
    private ArrayList<String> options;
    @SerializedName("category")
    private String category;
    @SerializedName("course_name")
    private String courseName;
    @SerializedName("description")
    private String description;
    @SerializedName("answer")
    private String answer;

    public QuestionForQuiz() {
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public ArrayList<String> getOptions() {
        return options;
    }

    public void setOptions(ArrayList<String> options) {
        this.options = options;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
