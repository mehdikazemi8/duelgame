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
    private int category;
    @SerializedName("book")
    private String book;
    @SerializedName("chapter")
    private String chapter;

    public StartOfflineDuelRequest(CommandType command, String opponentUserNumber, int category) {
        super(command);
        this.opponentUserNumber = opponentUserNumber;
        this.category = category;
    }

    public StartOfflineDuelRequest(CommandType command, String opponentUserNumber, int category, String book, String chapter) {
        super(command);
        this.opponentUserNumber = opponentUserNumber;
        this.category = category;
        this.book = book;
        this.chapter = chapter;
    }

    public StartOfflineDuelRequest(String opponentUserNumber, int category) {
        this.opponentUserNumber = opponentUserNumber;
        this.category = category;
    }

    public String getOpponentUserNumber() {
        return opponentUserNumber;
    }

    public void setOpponentUserNumber(String opponentUserNumber) {
        this.opponentUserNumber = opponentUserNumber;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }
}
