package com.mehdiii.duelgame.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.mehdiii.duelgame.models.base.BaseModel;

/**
 * Created by omid on 4/13/2015.
 */
public class DuelBroadcastReceiver extends BroadcastReceiver {
    private static final String KEY_JSON = "inputMessage";
    OnMessageReceived onMessageReceived;

    public DuelBroadcastReceiver(OnMessageReceived onMessageReceived) {
        this.onMessageReceived = onMessageReceived;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String json = intent.getExtras().getString(KEY_JSON);
        BaseModel baseModel = BaseModel.deserialize(json, BaseModel.class);
        if (onMessageReceived != null)
            onMessageReceived.onReceive(json, baseModel.getCommand());
    }
}
