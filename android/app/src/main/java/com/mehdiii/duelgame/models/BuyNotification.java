package com.mehdiii.duelgame.models;

/**
 * Created by omid on 4/19/2015.
 */
public class BuyNotification {
    int id;
    int type;

    public BuyNotification(int id, int type) {
        this.id = id;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}

