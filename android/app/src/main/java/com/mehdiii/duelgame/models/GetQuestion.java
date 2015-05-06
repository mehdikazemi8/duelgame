package com.mehdiii.duelgame.models;

import com.google.gson.annotations.SerializedName;
import com.mehdiii.duelgame.models.base.BaseModel;
import com.mehdiii.duelgame.models.base.CommandType;

/**
 * Created by Omid on 5/6/2015.
 */
public class GetQuestion extends BaseModel {
    @SerializedName("hint_again")
    private int hintAgain;
    @SerializedName("hint_remove")
    private int hintRemove;
    @SerializedName("time")
    private long time;
    @SerializedName("ok")
    private int ok;

    public GetQuestion() {}
    public GetQuestion(int hintAgain, int hintRemove, long time, int ok) {
        this.hintAgain = hintAgain;
        this.hintRemove = hintRemove;
        this.time = time;
        this.ok = ok;
    }

    public GetQuestion(CommandType command, int hintAgain, int hintRemove, long time, int ok) {
        super(command);
        this.hintAgain = hintAgain;
        this.hintRemove = hintRemove;
        this.time = time;
        this.ok = ok;
    }

    public int getHintAgain() {
        return hintAgain;
    }

    public void setHintAgain(int hintAgain) {
        this.hintAgain = hintAgain;
    }

    public int getHintRemove() {
        return hintRemove;
    }

    public void setHintRemove(int hintRemove) {
        this.hintRemove = hintRemove;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getOk() {
        return ok;
    }

    public void setOk(int ok) {
        this.ok = ok;
    }
}
