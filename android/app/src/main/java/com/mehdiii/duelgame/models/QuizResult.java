package com.mehdiii.duelgame.models;

import com.google.gson.annotations.SerializedName;
import com.mehdiii.duelgame.models.base.BaseModel;
import com.mehdiii.duelgame.models.base.CommandType;

import java.util.ArrayList;

/**
 * Created by mehdiii on 1/18/16.
 */
public class QuizResult extends BaseModel {
    @SerializedName("id")
    private String id;
    @SerializedName("num_participants")
    private Integer numOfParticipants;
    @SerializedName("results")
    private ArrayList<CourseResult> results;

    public QuizResult() {
    }

    public QuizResult(CommandType command, String id) {
        super(command);
        setCommand(command);
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getNumOfParticipants() {
        return numOfParticipants;
    }

    public void setNumOfParticipants(Integer numOfParticipants) {
        this.numOfParticipants = numOfParticipants;
    }

    public ArrayList<CourseResult> getResults() {
        return results;
    }

    public void setResults(ArrayList<CourseResult> results) {
        this.results = results;
    }
}
