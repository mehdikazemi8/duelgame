package com.mehdiii.duelgame.models.events;

/**
 * Created by omid on 4/28/2015.
 */
public class OnDiamondChangeNotice {
    private int newValue;

    public OnDiamondChangeNotice(int newValue) {
        this.newValue = newValue;
    }

    public int getNewValue() {
        return newValue;
    }

    public void setNewValue(int newValue) {
        this.newValue = newValue;
    }
}
