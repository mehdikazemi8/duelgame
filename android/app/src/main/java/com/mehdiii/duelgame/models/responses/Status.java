package com.mehdiii.duelgame.models.responses;

import com.google.gson.annotations.SerializedName;
import com.mehdiii.duelgame.models.base.BaseModel;
import com.mehdiii.duelgame.models.base.CommandType;

/**
 * Created by mehdiii on 6/14/16.
 */
public class Status extends BaseModel {
    @SerializedName("status")
    private boolean successful;

    public Status(CommandType command, boolean successful) {
        super(command);
        this.successful = successful;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public void setSuccessful(boolean successful) {
        this.successful = successful;
    }
}
