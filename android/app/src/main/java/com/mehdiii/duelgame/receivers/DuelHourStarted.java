package com.mehdiii.duelgame.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.mehdiii.duelgame.utils.NotificationHelper;

/**
 * Created by mehdiii on 12/16/15.
 */
public class DuelHourStarted extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("TAG", "DuelHourStarted onReceive");

        NotificationHelper.setLEDNotification(context);
//        NotificationHelper.setVibration(context);
        NotificationHelper.setDuelHourStatusBarNotification(context);
    }
}
