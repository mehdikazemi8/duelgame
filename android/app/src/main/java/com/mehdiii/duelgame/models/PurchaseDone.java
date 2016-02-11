package com.mehdiii.duelgame.models;

import com.google.gson.annotations.SerializedName;
import com.mehdiii.duelgame.models.base.BaseModel;

public class PurchaseDone extends BaseModel {
    public enum PurchaseResult {
        COMPLETED,
        FAILED,
        DUPLICATE,
        NOT_ENOUGH,
        UNKNOWN
    }

    PurchaseItem purchaseItem;

    @SerializedName("purchase_id")
    private String purchaseId;
    @SerializedName("status")
    private String status;
    @SerializedName("diamond")
    private int diamond;
    @SerializedName("heart")
    private int heart;
    @SerializedName("extreme_heart")
    private boolean extremeHeart;
    @SerializedName("score_factor")
    private int scoreFactor;

    public String getPurchaseId() {
        return purchaseId;
    }

    public void setPurchaseId(String purchaseId) {
        this.purchaseId = purchaseId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public PurchaseItem getPurchaseItem() {
        return purchaseItem;
    }

    public int getDiamond() {
        return diamond;
    }

    public void setDiamond(int diamond) {
        this.diamond = diamond;
    }

    public void setPurchaseItem(PurchaseItem purchaseItem) {
        this.purchaseItem = purchaseItem;
    }

    public int getHeart() {
        return heart;
    }

    public void setHeart(int heart) {
        this.heart = heart;
    }

    public boolean isExtremeHeart() {
        return extremeHeart;
    }

    public void setExtremeHeart(boolean extremeHeart) {
        this.extremeHeart = extremeHeart;
    }

    public int getScoreFactor() {
        return scoreFactor;
    }

    public void setScoreFactor(int scoreFactor) {
        this.scoreFactor = scoreFactor;
    }

    public PurchaseResult getPurchaseResult() {
        if (status.equals("complete"))
            return PurchaseResult.COMPLETED;
        else if (status.equals("duplicate"))
            return PurchaseResult.DUPLICATE;
        else if (status.equals("failed"))
            return PurchaseResult.FAILED;
        else if (status.equals("not_enough"))
            return PurchaseResult.NOT_ENOUGH;
        else
            return PurchaseResult.UNKNOWN;
    }
}
