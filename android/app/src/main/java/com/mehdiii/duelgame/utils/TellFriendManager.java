package com.mehdiii.duelgame.utils;

import android.content.Context;
import android.content.Intent;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.mehdiii.duelgame.DuelApp;
import com.mehdiii.duelgame.R;
import com.mehdiii.duelgame.managers.AuthManager;

/**
 * Created by mehdiii on 1/18/16.
 */
public class TellFriendManager {
    public static void tellFriends(Context context) {
        Tracker tracker = DuelApp.getInstance().getTracker(DuelApp.TrackerName.GLOBAL_TRACKER);
        // Build and send an Event.
        tracker.send(new HitBuilders.EventBuilder()
                .setCategory("button_click")
                .setAction("report_button")
                .setLabel("tell_friend")
                .build());

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT,
                String.format(context.getResources().getString(R.string.message_share),
                        "http://cafebazaar.ir/app/" + context.getPackageName()) +
                        "\n\n" +
                        "موقع ثبت نام کد کاربری من رو بزن آزمون رایگان بگیریم " + AuthManager.getCurrentUser().getId()
        );
        sendIntent.setType("text/plain");
        context.startActivity(sendIntent);
    }
}
