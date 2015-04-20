package com.mehdiii.duelgame.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.mehdiii.duelgame.managers.HeartTracker;

/**
 * Created by omid on 4/20/2015.
 */
public class OnHeartRefillTimeArrived extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        HeartTracker.getInstance(context).increaseHeart();
    }
}
