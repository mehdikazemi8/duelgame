package com.mehdiii.duelgame.models;

import com.google.gson.annotations.SerializedName;
import com.mehdiii.duelgame.models.base.BaseModel;

/**
 * Created by omid on 4/22/2015.
 */
public class PurchaseItem extends BaseModel {

    @SerializedName("mode")
    private int mode;
    @SerializedName("entity_type")
    private int entityType;
    @SerializedName("id")
    private int id;
    @SerializedName("title")
    private String title;
    @SerializedName("cost")
    private Cost cost;
    @SerializedName("sku")
    private String sku;

    public PurchaseItem(int mode, int entityType, int id, String title, Cost cost) {
        this.mode = mode;
        this.entityType = entityType;
        this.id = id;
        this.title = title;
        this.cost = cost;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public int getEntityType() {
        return entityType;
    }

    public void setEntityType(int entityType) {
        this.entityType = entityType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Cost getCost() {
        return cost;
    }

    public void setCost(Cost cost) {
        this.cost = cost;
    }

    public PurchaseRequest toPurchaseRequest() {
        return new PurchaseRequest(getId());
    }

    public PurchaseRequest toPurchaseRequest(String cardId) {
        return new PurchaseRequest(getId(), cardId);
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }
}
