package com.mehdiii.duelgame.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by MeHdi on 3/2/2015.
 */
public class Question {
    @SerializedName("question_text")
    private String questionText;
    @SerializedName("options")
    private List<String> options;

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }
}
