package com.mehdiii.duelgame.models.events;

/**
 * Created by omid on 4/20/2015.
 */
public class OnSoundStateChanged {
    private boolean playSound;

    public OnSoundStateChanged(boolean _playSound) {
        this.playSound = _playSound;
    }

    public boolean getState() {
        return playSound;
    }

    public void setState(boolean _playSound) {
        this.playSound = _playSound;
    }
}
