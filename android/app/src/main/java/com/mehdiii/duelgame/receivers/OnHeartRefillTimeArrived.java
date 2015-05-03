package com.mehdiii.duelgame.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.mehdiii.duelgame.managers.HeartTracker;

/**
 * Created by omid on 4/20/2015.
 */
public class OnHeartRefillTimeArrived extends BroadcastReceiver {
    private static final String TAG = "HEART_TRACKER";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "heart tracker increased by one.");
        HeartTracker.getInstance(context).increaseHeart();
    }
}
