package com.mehdiii.duelgame.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.mehdiii.duelgame.utils.NotificationHelper;

import java.util.Calendar;

/**
 * Created by frshd on 5/10/16.
 */
public class FlashCardReminder extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("TAG", "FlashCardReminder onReceive");

        String title = (String) intent.getExtras().get("flashCardName");
        String cardId = (String) intent.getExtras().get("flashCardId");
        NotificationHelper.setLEDNotificationForFlashCardReminder(context, cardId);
//        NotificationHelper.setVibrationForFlashCardReminder(context, cardId);
        NotificationHelper.setFlashCardStatusBarNotification(context, title, cardId);
    }

}


