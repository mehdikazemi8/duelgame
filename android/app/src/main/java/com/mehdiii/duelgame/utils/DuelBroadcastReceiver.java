package com.mehdiii.duelgame.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.mehdiii.duelgame.models.base.BaseModel;

/**
 * Created by omid on 4/13/2015.
 */
public class DuelBroadcastReceiver extends BroadcastReceiver {
    public static final String BUNDLE_JSON_KEY = "inputMessage";
    public static final String ACTION_NAME = "MESSAGE";
    OnMessageReceivedListener onMessageReceived;

    public DuelBroadcastReceiver(OnMessageReceivedListener onMessageReceived) {
        this.onMessageReceived = onMessageReceived;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String json = intent.getExtras().getString(BUNDLE_JSON_KEY);
        BaseModel baseModel = BaseModel.deserialize(json, BaseModel.class);
        if (onMessageReceived != null)
            onMessageReceived.onReceive(json, baseModel.getCommand());
    }
}
