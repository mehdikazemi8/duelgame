package com.mehdiii.duelgame.utils;

import com.mehdiii.duelgame.models.base.CommandType;

/**
 * Created by omid on 4/13/2015.
 */
public interface OnMessageReceivedListener {
    void onReceive(String json, CommandType type);
}
