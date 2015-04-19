package com.mehdiii.duelgame.models;

/**
 * Created by omid on 4/19/2015.
 */
public class BuyCommand {
    String sku;

    public BuyCommand(String sku) {
        this.sku = sku;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }
}

