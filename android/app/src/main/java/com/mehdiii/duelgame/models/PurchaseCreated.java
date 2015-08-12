package com.mehdiii.duelgame.models;

import com.google.gson.annotations.SerializedName;
import com.mehdiii.duelgame.models.base.BaseModel;

/**
 * Created by omid on 4/23/2015.
 */
public class PurchaseCreated extends BaseModel {
    public PurchaseCreated(String purchaseId, String orderId) {
        this(purchaseId, orderId, "");
    }

    public PurchaseCreated(String purchaseId, String orderId, String cardId) {
        this.purchaseId = purchaseId;
        this.orderId = orderId;
        this.cardId = cardId;
    }

    @SerializedName("purchase_id")
    private String purchaseId;
    @SerializedName("order_id")
    private String orderId;
    @SerializedName("card_id")
    private String cardId;


    public String getPurchaseId() {
        return purchaseId;
    }

    public void setPurchaseId(String purchaseId) {
        this.purchaseId = purchaseId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }
}
