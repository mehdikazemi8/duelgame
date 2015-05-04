package com.mehdiii.duelgame.models.responses;

import com.google.gson.annotations.SerializedName;
import com.mehdiii.duelgame.models.User;
import com.mehdiii.duelgame.models.base.BaseModel;

import java.util.List;

public class RankList extends BaseModel {
    @SerializedName("top")
    private List<User> top;

    @SerializedName("near")
    private List<User> near;

    public List<User> getTop() {
        return top;
    }

    public void setTop(List<User> top) {
        this.top = top;
    }

    public List<User> getNear() {
        return near;
    }

    public void setNear(List<User> near) {
        this.near = near;
    }


}
