package com.mehdiii.duelgame.models;

import com.google.gson.annotations.SerializedName;
import com.mehdiii.duelgame.models.base.BaseModel;
import com.mehdiii.duelgame.models.base.CommandType;

import java.util.List;

/**
 * Created by mehdiii on 1/14/16.
 */
public class Quizzes extends BaseModel {
    @SerializedName("quizzes")
    List<Quiz> quizzes;

    public Quizzes(List<Quiz> quizzes) {
        this.quizzes = quizzes;
    }

    public Quizzes(CommandType command, List<Quiz> quizzes) {
        super(command);
        this.quizzes = quizzes;
    }

    public List<Quiz> getQuizzes() {
        return quizzes;
    }

    public void setQuizzes(List<Quiz> quizzes) {
        this.quizzes = quizzes;
    }
}
