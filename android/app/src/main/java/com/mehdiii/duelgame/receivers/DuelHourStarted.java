package com.mehdiii.duelgame.receivers;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.mehdiii.duelgame.DuelApp;
import com.mehdiii.duelgame.R;
import com.mehdiii.duelgame.utils.NotificationHelper;
import com.mehdiii.duelgame.views.activities.ParentActivity;
import com.mehdiii.duelgame.views.activities.splash.StartActivity;

/**
 * Created by mehdiii on 12/16/15.
 */
public class DuelHourStarted extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("TAG", "DuelHourStarted onReceive");

        NotificationHelper.setLEDNotification(context);
//        NotificationHelper.setVibration(context);
        NotificationHelper.setStatusBarNotification(context);
    }
}
