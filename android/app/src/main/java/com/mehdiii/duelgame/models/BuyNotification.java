package com.mehdiii.duelgame.models;

/**
 * Created by omid on 4/19/2015.
 */
public class BuyNotification {
    String sku;
    int id;

    public BuyNotification(String sku, int id) {
        this.sku = sku;
        this.id = id;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

