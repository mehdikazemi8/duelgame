package com.mehdiii.duelgame.models.events;

/**
 * Created by omid on 4/28/2015.
 */
public class OnPurchaseResult {
    private String status;

    public OnPurchaseResult() {
    }

    public OnPurchaseResult(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
