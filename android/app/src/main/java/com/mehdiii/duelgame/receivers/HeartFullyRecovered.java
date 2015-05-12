package com.mehdiii.duelgame.receivers;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.mehdiii.duelgame.R;
import com.mehdiii.duelgame.managers.HeartTracker;
import com.mehdiii.duelgame.views.activities.splash.StartActivity;

import java.util.List;

/**
 * Created by omid on 4/20/2015.
 */
public class HeartFullyRecovered extends BroadcastReceiver {
    private static final String TAG = "HEART_TRACKER";

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent openAppIntent = new Intent(context, StartActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, openAppIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.heart_full)
                        .setContentTitle(context.getResources().getString(R.string.app_name))
                        .setContentText(context.getResources().getString(R.string.message_hearts_fully_refilled))
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, mBuilder.build());
//                    .addAction(R.drawable.icon, "Call", pIntent)
//                    .addAction(R.drawable.icon, "More", pIntent)
//                    .addAction(R.drawable.icon, "And more", pIntent).build();
//        }

    }
}
