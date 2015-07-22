package com.mehdiii.duelgame.models;

import com.google.gson.annotations.SerializedName;
import com.mehdiii.duelgame.models.base.BaseModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Omid on 7/22/2015.
 */
public class DeckSync extends BaseModel {
    public DeckSync () {
        this.seen = new ArrayList<>();
    }
    @SerializedName("_id")
    private String id;
    @SerializedName("seen")
    List<Pair<Integer, Integer>> seen;
    @SerializedName("to_ask")
    int[] toAsk;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Pair<Integer, Integer>> getSeen() {
        return seen;
    }

    public void setSeen(List<Pair<Integer, Integer>> seen) {
        this.seen = seen;
    }

    public int[] getToAsk() {
        return toAsk;
    }

    public void setToAsk(int[] toAsk) {
        this.toAsk = toAsk;
    }
}
