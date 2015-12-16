package com.mehdiii.duelgame.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import com.mehdiii.duelgame.managers.HeartTracker;
import com.mehdiii.duelgame.views.activities.ParentActivity;

/**
 * Created by omid on 4/20/2015.
 */
public class OnBootCompletedReceiver extends WakefulBroadcastReceiver {
    public static final String ACTION_NAME = "android.intent.action.BOOT_COMPLETED";
    private static final String TAG = "HEART_TRACKER";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ACTION_NAME)) {
            Log.d(TAG, "heart tracker received boot complete intent.");
            ParentActivity.setAlarmForDuelHour(context);
//            HeartTracker.configure(context).show();
        }
    }
}
