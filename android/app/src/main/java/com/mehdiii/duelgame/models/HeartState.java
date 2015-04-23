package com.mehdiii.duelgame.models;

/**
 * Created by omid on 4/20/2015.
 */
public class HeartState {
    private int current;
    private long lastDecrementTime;

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public long getLastDecrementTime() {
        return lastDecrementTime;
    }

    public void setLastDecrementTime(long lastDecrementTime) {
        this.lastDecrementTime = lastDecrementTime;
    }
    public void decrease() {
        current--;
    }
    public void increase() {
        current++;
    }
}
