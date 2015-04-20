package com.mehdiii.duelgame.models;

/**
 * Created by omid on 4/20/2015.
 */
public class HeartChangeNotice {
    HeartState state;
    ChangeMode mode;

    public HeartChangeNotice(HeartState state, ChangeMode mode) {
        this.state = state;
        this.mode = mode;
    }

    public HeartState getState() {
        return state;
    }

    public void setState(HeartState state) {
        this.state = state;
    }

    public ChangeMode getMode() {
        return mode;
    }

    public void setMode(ChangeMode mode) {
        this.mode = mode;
    }

    public enum ChangeMode {
        INCREASED,
        DECREASED
    }
}
