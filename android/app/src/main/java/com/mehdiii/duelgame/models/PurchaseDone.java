package com.mehdiii.duelgame.models;

import com.google.gson.annotations.SerializedName;
import com.mehdiii.duelgame.models.base.BaseModel;

/**
 * Created by omid on 4/23/2015.
 */
public class PurchaseDone extends BaseModel {
    public PurchaseDone(String purchaseId) {
        this.purchaseId = purchaseId;
    }

    @SerializedName("purchase_id")
    private String purchaseId;

    public String getPurchaseId() {
        return purchaseId;
    }

    public void setPurchaseId(String purchaseId) {
        this.purchaseId = purchaseId;
    }
}
