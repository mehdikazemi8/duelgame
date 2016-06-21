package com.mehdiii.duelgame.models;

import com.google.gson.annotations.SerializedName;
import com.mehdiii.duelgame.models.base.BaseModel;
import com.mehdiii.duelgame.models.base.CommandType;

/**
 * Created by mehdiii on 6/21/16.
 */
public class BlockRequest extends BaseModel {
    @SerializedName("user_number")
    private String userNumber;

    public BlockRequest(CommandType command, String userNumber) {
        super(command);
        this.userNumber = userNumber;
    }
}
