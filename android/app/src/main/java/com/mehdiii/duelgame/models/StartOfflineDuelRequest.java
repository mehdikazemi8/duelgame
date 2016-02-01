package com.mehdiii.duelgame.models;

import com.google.gson.annotations.SerializedName;
import com.mehdiii.duelgame.models.base.BaseModel;
import com.mehdiii.duelgame.models.base.CommandType;

/**
 * Created by mehdiii on 1/31/16.
 */
public class StartOfflineDuelRequest extends BaseModel {
    @SerializedName("opponent_user_number")
    private String opponentUserNumber;
    @SerializedName("category")
    private String category;

    public StartOfflineDuelRequest(CommandType command, String opponentUserNumber, String category) {
        super(command);
        this.opponentUserNumber = opponentUserNumber;
        this.category = category;
    }

    public StartOfflineDuelRequest() {
    }

    public StartOfflineDuelRequest(String opponentUserNumber, String category) {
        this.opponentUserNumber = opponentUserNumber;
        this.category = category;
    }

    public String getOpponentUserNumber() {
        return opponentUserNumber;
    }

    public void setOpponentUserNumber(String opponentUserNumber) {
        this.opponentUserNumber = opponentUserNumber;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
