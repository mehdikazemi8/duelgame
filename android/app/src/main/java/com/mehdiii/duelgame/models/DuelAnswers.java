package com.mehdiii.duelgame.models;

import com.google.gson.annotations.SerializedName;
import com.mehdiii.duelgame.models.base.BaseModel;

import java.util.ArrayList;

/**
 * Created by mehdiii on 2/2/16.
 */
public class DuelAnswers extends BaseModel {
    @SerializedName("answers")
    private ArrayList<QuestionAnswer> answers;

    public DuelAnswers() {
        answers = new ArrayList<>();
    }

    public DuelAnswers(ArrayList<QuestionAnswer> answers) {
        this.answers = answers;
    }

    public void addAnswer(QuestionAnswer answer) {
        answers.add(answer);
    }

    public ArrayList<QuestionAnswer> getAnswers() {
        return answers;
    }

    public void setAnswers(ArrayList<QuestionAnswer> answers) {
        this.answers = answers;
    }
}
