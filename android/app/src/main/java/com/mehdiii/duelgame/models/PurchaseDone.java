package com.mehdiii.duelgame.models;

import com.google.gson.annotations.SerializedName;
import com.mehdiii.duelgame.models.base.BaseModel;

/**
 * Created by omid on 4/27/2015.
 */
public class PurchaseDone extends BaseModel {
    public enum PurchaseResult {
        COMPLETED,
        FAILED,
        DUPLICATE,
        UNKNOWN
    }

    PurchaseItem purchaseItem;

    @SerializedName("purchase_id")
    private String purchaseId;
    @SerializedName("status")
    private String status;
    @SerializedName("diamond")
    private int diamond;

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

    public PurchaseResult getPurchaseResult() {
        if (status.equals("complete"))
            return PurchaseResult.COMPLETED;
        else if (status.equals("duplicate"))
            return PurchaseResult.DUPLICATE;
        else if (status.equals("failed"))
            return PurchaseResult.FAILED;
        else
            return PurchaseResult.UNKNOWN;
    }
}
