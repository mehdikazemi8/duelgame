package com.mehdiii.duelgame.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

/**
 * Created by mehdiii on 12/16/15.
 */
public class NotificationHelper {
    private static final int LED_NOTIFICATION = 838;

    public static void setLEDNotification(Context context) {
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
}
