package com.mehdiii.duelgame.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.mehdiii.duelgame.DuelApp;
import com.mehdiii.duelgame.R;
import com.mehdiii.duelgame.views.activities.ParentActivity;
import com.mehdiii.duelgame.views.activities.splash.StartActivity;

/**
 * Created by mehdiii on 12/16/15.
 */
public class NotificationHelper {
    private static final int LED_NOTIFICATION = 838;

    public static void setVibration(Context context) {
        if(!ParentActivity.isItNearDuelHour())
            return;

        Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(500);
    }

    public static void setLEDNotification(Context context) {
        if(!ParentActivity.isItNearDuelHour())
            return;

        Log.d("TAG", "setLEDNotification start");
        Notification notf = new NotificationCompat.Builder(context)
                .setAutoCancel(false)
                .setLights(0x00FF00, 3000, 500)
                .build();
        NotificationManager mNotificationManager =  (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(LED_NOTIFICATION, notf);
        Log.d("TAG", "setLEDNotification end");
    }

    public static void cancelLEDNotification(Context context) {
        NotificationManager mNotificationManager =  (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.cancel(LED_NOTIFICATION);
    }

    public static void setStatusBarNotification(Context context) {
        if(!ParentActivity.isItNearDuelHour())
            return;

        Intent openAppIntent = new Intent(context, StartActivity.class);
        openAppIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

        PendingIntent pendingIntent;
        if(DuelApp.getInstance().getResumedActivities() == 0)
            pendingIntent = PendingIntent.getActivity(context, 0, openAppIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        else
            pendingIntent = PendingIntent.getActivity(context, 0, new Intent(), PendingIntent.FLAG_CANCEL_CURRENT);

        android.support.v4.app.NotificationCompat.Builder mBuilder =
                new android.support.v4.app.NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.logo)
                        .setContentTitle(context.getResources().getString(R.string.app_name))
                        .setContentText(context.getResources().getString(R.string.message_duel_hour_started))
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true);

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(ParentActivity.DUEL_HOUR_NOTIFICATION_ID, mBuilder.build());
    }
}
