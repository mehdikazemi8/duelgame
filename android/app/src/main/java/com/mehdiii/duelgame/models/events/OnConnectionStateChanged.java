package com.mehdiii.duelgame.models.events;

/**
 * Created by Omid on 5/9/2015.
 */
public class OnConnectionStateChanged {
    public OnConnectionStateChanged(ConnectionState state) {
        this.state = state;
    }

    public enum ConnectionState {
        CONNECTING,
        CONNECTED,
        DISCONNECTED
    }

    private ConnectionState state;

    public ConnectionState getState() {
        return state;
    }

    public void setState(ConnectionState state) {
        this.state = state;
    }
}
