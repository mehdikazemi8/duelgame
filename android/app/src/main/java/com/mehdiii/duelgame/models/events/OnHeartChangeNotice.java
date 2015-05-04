package com.mehdiii.duelgame.models.events;

import com.mehdiii.duelgame.models.HeartState;

/**
 * Created by omid on 4/20/2015.
 */
public class OnHeartChangeNotice {
    private ChangeMode mode;
    private int value;

    public OnHeartChangeNotice(ChangeMode mode, int value) {
        this.mode = mode;
        this.value = value;
    }

    public ChangeMode getMode() {
        return mode;
    }

    public void setMode(ChangeMode mode) {
        this.mode = mode;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public enum ChangeMode {
        INCREASED,
        DECREASED,
        REFRESH,
        TICK
    }
}
