package com.mehdiii.duelgame.models;

import com.google.gson.annotations.SerializedName;
import com.mehdiii.duelgame.models.base.BaseModel;
import com.mehdiii.duelgame.models.base.CommandType;

import java.util.List;

/**
 * Created by mehdiii on 1/14/16.
 */
public class Quizzes extends BaseModel {
    @SerializedName("offset")
    private Integer offset;
    @SerializedName("limit")
    private Integer limit;
    @SerializedName("status")
    private String status;
    @SerializedName("quizzes")
    List<Quiz> quizzes;

    public Integer getOffset() {
        return offset;
    }

    public Quizzes(Integer offset, Integer limit, String status) {
        this.offset = offset;
        this.limit = limit;
        this.status = status;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

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
