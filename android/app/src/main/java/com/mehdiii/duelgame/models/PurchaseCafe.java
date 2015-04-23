package com.mehdiii.duelgame.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by omid on 4/23/2015.
 */
public class PurchaseCafe {
    @SerializedName("orderId")
    private String orderId;
    @SerializedName("packageName")
    private String packageName;
    @SerializedName("productId")
    private String productId;
    @SerializedName("developerPayload")
    private String developerPayload;
    @SerializedName("purchaseToken")
    private String purchaseToken;
    @SerializedName("purchaseTime")
    private double purchaseTime;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getDeveloperPayload() {
        return developerPayload;
    }

    public void setDeveloperPayload(String developerPayload) {
        this.developerPayload = developerPayload;
    }

    public String getPurchaseToken() {
        return purchaseToken;
    }

    public void setPurchaseToken(String purchaseToken) {
        this.purchaseToken = purchaseToken;
    }

    public double getPurchaseTime() {
        return purchaseTime;
    }

    public void setPurchaseTime(double purchaseTime) {
        this.purchaseTime = purchaseTime;
    }

    public PurchaseDone toPurchaseDone() {
        return new PurchaseDone(getDeveloperPayload());
    }
}
