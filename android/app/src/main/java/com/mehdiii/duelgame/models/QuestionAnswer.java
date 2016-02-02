package com.mehdiii.duelgame.models;

import com.google.gson.annotations.SerializedName;
import com.mehdiii.duelgame.models.base.BaseModel;

/**
 * Created by mehdiii on 2/2/16.
 */
public class QuestionAnswer extends BaseModel {
    @SerializedName("ok")
    private Boolean ok;
    @SerializedName("time")
    private Integer time;
    @SerializedName("hint_remove")
    private Integer hintRemove;

    public QuestionAnswer(Boolean ok, Integer time, Integer hintRemove) {
        this.ok = ok;
        this.time = time;
        this.hintRemove = hintRemove;
    }

    public Boolean getOk() {
        return ok;
    }

    public void setOk(Boolean ok) {
        this.ok = ok;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public Integer getHintRemove() {
        return hintRemove;
    }

    public void setHintRemove(Integer hintRemove) {
        this.hintRemove = hintRemove;
    }
}
