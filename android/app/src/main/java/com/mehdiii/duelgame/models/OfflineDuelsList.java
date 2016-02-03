package com.mehdiii.duelgame.models;

import com.google.gson.annotations.SerializedName;
import com.mehdiii.duelgame.models.base.BaseModel;

/**
 * Created by mehdiii on 2/2/16.
 */
public class OfflineDuelsList extends BaseModel {
    @SerializedName("turn")
    private String turn;
    @SerializedName("offset")
    private Integer offset;
    @SerializedName("limit")
    private Integer limit;

    public OfflineDuelsList(String turn, Integer offset, Integer limit) {
        this.turn = turn;
        this.offset = offset;
        this.limit = limit;
    }

    public String getTurn() {
        return turn;
    }

    public void setTurn(String turn) {
        this.turn = turn;
    }

    public Integer getOffset() {
        return offset;
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
}
