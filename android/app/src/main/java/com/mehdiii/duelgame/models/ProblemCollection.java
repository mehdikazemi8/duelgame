package com.mehdiii.duelgame.models;

import com.google.android.gms.games.quest.Quest;
import com.google.gson.annotations.SerializedName;
import com.mehdiii.duelgame.models.base.BaseModel;

import java.util.List;

/**
 * Created by Omid on 5/6/2015.
 */
public class ProblemCollection extends BaseModel {
    @SerializedName("problems")
    private List<Question> questions;

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }
}
