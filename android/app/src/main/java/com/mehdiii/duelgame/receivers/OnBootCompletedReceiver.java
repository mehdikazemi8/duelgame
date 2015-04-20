package com.mehdiii.duelgame.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.mehdiii.duelgame.managers.HeartTracker;

/**
 * Created by omid on 4/20/2015.
 */
public class OnBootCompletedReceiver extends BroadcastReceiver {
    public static final String ACTION_NAME = "android.intent.action.BOOT_COMPLETED";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ACTION_NAME)) {
            HeartTracker.getInstance(context).init();
        }
    }
}
