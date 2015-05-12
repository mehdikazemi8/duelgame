package com.mehdiii.duelgame.models.responses;

import com.google.gson.annotations.SerializedName;
import com.mehdiii.duelgame.models.UserForRanklist;
import com.mehdiii.duelgame.models.base.BaseModel;

import java.util.List;

public class RankList extends BaseModel {
    @SerializedName("top")
    private List<UserForRanklist> top;

    @SerializedName("near")
    private List<UserForRanklist> near;

    public List<UserForRanklist> getTop() {
        if (top.size() != 0) {
            top.get(0).setPlaceInRank(1);
            for (int i = 1; i < top.size(); i++)
                if (top.get(i).getScore() == top.get(i - 1).getScore())
                    top.get(i).setPlaceInRank(top.get((i - 1)).getPlaceInRank());
                else
                    top.get(i).setPlaceInRank(i + 1);
        }
        return top;
    }

    public void setTop(List<UserForRanklist> top) {
        this.top = top;
    }

    public List<UserForRanklist> getNear() {
        return near;
    }

    public void setNear(List<UserForRanklist> near) {
        this.near = near;
    }


}
