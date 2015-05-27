package com.mehdiii.duelgame.models;

import com.google.gson.annotations.SerializedName;
import com.mehdiii.duelgame.models.base.BaseModel;

/**
 * Created by Omid on 5/26/2015.
 */
public class SendGcmCode extends BaseModel {
    @SerializedName("gcm_id")
    private String gcmId;

    public SendGcmCode(String gcmId) {
        this.gcmId = gcmId;
    }

    public String getGcmId() {
        return gcmId;
    }

    public void setGcmId(String gcmId) {
        this.gcmId = gcmId;
    }
}
