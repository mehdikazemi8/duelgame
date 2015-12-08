package com.mehdiii.duelgame.models;

import com.google.gson.annotations.SerializedName;
import com.mehdiii.duelgame.models.base.BaseModel;
import com.mehdiii.duelgame.models.base.CommandType;

import java.util.List;

/**
 * Created by mehdiii on 12/8/15.
 */
public class MutualStats extends BaseModel {
    @SerializedName("opponent")
    String opponentId;
    @SerializedName("results")
    List <MutualCourseStat> results;

    public String getOpponentId() {
        return opponentId;
    }

    public void setOpponentId(String opponentId) {
        this.opponentId = opponentId;
    }

    public List<MutualCourseStat> getResults() {
        return results;
    }

    public void setResults(List<MutualCourseStat> results) {
        this.results = results;
    }

    public MutualStats(String opponentId, List<MutualCourseStat> results) {
        this.opponentId = opponentId;
        this.results = results;
    }

    public MutualStats(CommandType command, String opponentId, List<MutualCourseStat> results) {
        super(command);
        this.opponentId = opponentId;
        this.results = results;
    }
}
