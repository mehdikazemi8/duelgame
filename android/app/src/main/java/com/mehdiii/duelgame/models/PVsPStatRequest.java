package com.mehdiii.duelgame.models;

import com.google.gson.annotations.SerializedName;
import com.mehdiii.duelgame.models.base.BaseModel;
import com.mehdiii.duelgame.models.base.CommandType;

/**
 * Created by mehdiii on 12/7/15.
 */
public class PVsPStatRequest extends BaseModel {
    @SerializedName("opponent")
    String opponent;

    public PVsPStatRequest(String opponent) {
        this.opponent = opponent;
    }

    public PVsPStatRequest(CommandType command, String opponent) {
        super(command);
        this.opponent = opponent;
    }
}
