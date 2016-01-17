package com.mehdiii.duelgame.models;

import com.google.gson.annotations.SerializedName;
import com.mehdiii.duelgame.models.base.BaseModel;

/**
 * Created by mehdiii on 1/17/16.
 */
public class GetBuyQuizRequest extends BaseModel {
    @SerializedName("id")
    private String id;

    public GetBuyQuizRequest() {
    }

    public GetBuyQuizRequest(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
