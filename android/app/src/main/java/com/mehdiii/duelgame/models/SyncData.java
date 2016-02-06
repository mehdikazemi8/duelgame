package com.mehdiii.duelgame.models;

import com.google.gson.annotations.SerializedName;
import com.mehdiii.duelgame.models.base.BaseModel;

/**
 * Created by mehdiii on 2/5/16.
 */
public class SyncData extends BaseModel {
    @SerializedName("diamond")
    private Integer diamond;

    public SyncData(Integer diamond) {
        this.diamond = diamond;
    }

    public Integer getDiamond() {
        return diamond;
    }

    public void setDiamond(Integer diamond) {
        this.diamond = diamond;
    }
}
