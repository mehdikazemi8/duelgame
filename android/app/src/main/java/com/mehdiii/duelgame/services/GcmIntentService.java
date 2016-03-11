package com.mehdiii.duelgame.services;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.mehdiii.duelgame.DuelApp;
import com.mehdiii.duelgame.R;
import com.mehdiii.duelgame.receivers.GcmBroadcastReceiver;
import com.mehdiii.duelgame.views.activities.ParentActivity;
import com.mehdiii.duelgame.views.activities.splash.StartActivity;

/**
 * Created by Omid on 5/26/2015.
 */
public class GcmIntentService extends IntentService {
    public static final String TAG = "TAG_GCM";
    private final static String NEW_APP_INTRODUCTION = "new_app_introduction";
    private final static String OPEN_DUEL = "open_duel";

    public GcmIntentService() {
        super("GcmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) {  // has effect of unparcelling Bundle
            /*
             * Filter messages based on message type. Since it is likely that GCM
             * will be extended in the future with new message types, just ignore
             * any message types you're not interested in, or that you don't
             * recognize.
             */
            if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                String type = extras.getString("type");

                if (type.equals(NEW_APP_INTRODUCTION)) {
                    try {
                        String url = extras.getString("url");
                        String message = extras.getString("message");
                        String title = extras.getString("title");

                        if (url != null && !url.isEmpty()) {

                            NotificationManager notificationManager =
                                    (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                            int icon = R.drawable.ic_launcher;
                            CharSequence notiText = title;
                            long meow = System.currentTimeMillis();

                            Notification notification = new Notification(icon, notiText, meow);

                            Context context = getApplicationContext();
                            Intent notificationIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                            PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
                            notification.setLatestEventInfo(context, title, message, contentIntent);

                            int SERVER_DATA_RECEIVED = 1;
                            notificationManager.notify(SERVER_DATA_RECEIVED, notification);

                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } else if(type.equals(OPEN_DUEL)) {
                    try {
                        String message = extras.getString("message");
                        String title = extras.getString("title");

                        Intent openAppIntent = new Intent(getApplicationContext(), StartActivity.class);
                        openAppIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

                        PendingIntent pendingIntent;
                        boolean autoCancel = false;
                        if(DuelApp.getInstance().getResumedActivities() == 0) {
                            pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, openAppIntent, PendingIntent.FLAG_CANCEL_CURRENT);
                        }
                        else {
                            pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, new Intent(), PendingIntent.FLAG_CANCEL_CURRENT);
                            autoCancel = true;
                        }

                        android.support.v4.app.NotificationCompat.Builder mBuilder =
                                new android.support.v4.app.NotificationCompat.Builder(getApplicationContext())
                                        .setSmallIcon(R.drawable.ic_launcher)
                                        .setContentTitle(title)
                                        .setContentText(message)
                                        .setContentIntent(pendingIntent)
                                        .setAutoCancel(autoCancel);

                        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                        notificationManager.notify(ParentActivity.OPEN_DUEL_ID, mBuilder.build());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }

        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }
}
