package com.mehdiii.duelgame.models;

import com.google.gson.annotations.SerializedName;
import com.mehdiii.duelgame.models.base.BaseModel;

/**
 * Created by omid on 4/23/2015.
 */
public class PurchaseRequest extends BaseModel {
    public PurchaseRequest(int id, String cardId) {
        this.id = id;
        this.cardId = cardId;
    }

    public PurchaseRequest(int id) {
        this(id, "");
    }

    @SerializedName("card_id")
    private String cardId;
    @SerializedName("id")
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }
}
