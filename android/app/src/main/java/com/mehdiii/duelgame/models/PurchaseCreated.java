package com.mehdiii.duelgame.models;

import com.google.gson.annotations.SerializedName;
import com.mehdiii.duelgame.models.base.BaseModel;

public class PurchaseCreated extends BaseModel {
    public PurchaseCreated(String purchaseId, String orderId) {
        this(purchaseId, orderId, "");
    }

    public PurchaseCreated(String purchaseId, String orderId, String cardId) {
        this.purchaseId = purchaseId;
        this.orderId = orderId;
        this.cardId = cardId;
    }

    public PurchaseCreated() {
    }

    @SerializedName("purchase_id")
    private String purchaseId;
    @SerializedName("order_id")
    private String orderId;
    @SerializedName("card_id")
    private String cardId;
    @SerializedName("quiz_id")
    private String quizId;
    @SerializedName("ok")
    private Boolean ok;
    @SerializedName("sku")
    private String sku;

    public Boolean getOk() {
        return ok;
    }

    public void setOk(Boolean ok) {
        this.ok = ok;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getQuizId() {
        return quizId;
    }

    public void setQuizId(String quizId) {
        this.quizId = quizId;
    }

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
