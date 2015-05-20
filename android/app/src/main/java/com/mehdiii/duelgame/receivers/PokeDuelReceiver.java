package com.mehdiii.duelgame.receivers;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.mehdiii.duelgame.R;
import com.mehdiii.duelgame.managers.GlobalPreferenceManager;
import com.mehdiii.duelgame.utils.NetworkUtils;
import com.mehdiii.duelgame.views.activities.splash.StartActivity;

/**
 * Created by Omid on 5/19/2015.
 */
public class PokeDuelReceiver extends BroadcastReceiver {
    public static final String PREFERENCE_PREVIOUS_CHECK_IN = "preference_previous_check_in";
    public static final String PREFERENCE_POKE_SEED = "preference_poke_seed";
//    static int ONE_DAY_IN_MILLS = 50000;
    static int ONE_DAY_IN_MILLS = 86400000;

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean isConnected = NetworkUtils.isNetworkAvailable(context);
        if (!isConnected)
            return;

        long currentTime = System.currentTimeMillis();
        long previousTime = GlobalPreferenceManager.readLong(context, PREFERENCE_PREVIOUS_CHECK_IN, -1);
        int seed = GlobalPreferenceManager.readInteger(context, PREFERENCE_POKE_SEED, 2);

        if (previousTime == -1) {
            // save current time
            GlobalPreferenceManager.writeLong(context, PREFERENCE_PREVIOUS_CHECK_IN, currentTime);

            displayPokeNotification(context);
            return;
        }

        long nextDue = previousTime + seed * ONE_DAY_IN_MILLS;
        if (currentTime > nextDue) {
            // save current time
            GlobalPreferenceManager.writeLong(context, PREFERENCE_PREVIOUS_CHECK_IN, currentTime);
            GlobalPreferenceManager.writeInt(context, PREFERENCE_POKE_SEED, seed + 1);

            displayPokeNotification(context);
        }
    }

    private void displayPokeNotification(Context context) {
        Intent openAppIntent = new Intent(context, StartActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, openAppIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.logo)
                        .setContentTitle(context.getResources().getString(R.string.app_name))
                        .setContentText(context.getResources().getString(R.string.message_revisit_duel_konkoor))
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, mBuilder.build());
    }
}
