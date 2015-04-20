/**
 * To-Do list (future work)
 * 1. post update notif
 */
package com.mehdiii.duelgame.managers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

import com.mehdiii.duelgame.receivers.OnHeartRefillTimeArrived;

import de.greenrobot.event.EventBus;

/**
 * Created by omid on 4/20/2015.
 */
public class HeartTracker {

    public static final String PREFERENCE_KEY_HEARTS = "heart_tracker";
    public static final String PREFERENCE_KEY_LAST_DECREMENT = "last_decrement_time";

    private static final int COUNT_HEARTS_MAX = 5;
    private static final int TIME_RECOVER_SINGLE_HEART_MILLS = 10000;

    private int current;
    private long lastDecrementTime;
    private boolean refillRunning = false;

    private Context context;
    Intent intent;
    PendingIntent pendingIntent;
    AlarmManager alarmManager = null;

    private static HeartTracker instance;

    public static HeartTracker getInstance(Context ctx) {
        if (instance == null)
            instance = new HeartTracker();

        instance.context = ctx;
        instance.init();

        return instance;
    }

    public void init() {

        intent = new Intent(context, OnHeartRefillTimeArrived.class);
        pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);


        current = GlobalPreferenceManager.readInteger(context, PREFERENCE_KEY_HEARTS, COUNT_HEARTS_MAX);
        lastDecrementTime = GlobalPreferenceManager.readLong(context, PREFERENCE_KEY_LAST_DECREMENT, 5);

        startAlarm();
    }

    private void startAlarm() {
        if (refillRunning || current == COUNT_HEARTS_MAX)
            return;

        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + TIME_RECOVER_SINGLE_HEART_MILLS,
                TIME_RECOVER_SINGLE_HEART_MILLS,
                pendingIntent);

        refillRunning = true;
    }

    public void stop() {
        refillRunning = false;
        if (alarmManager != null)
            alarmManager.cancel(pendingIntent);
    }

    public void useHeart() {

        if (current <= 0)
            throw new IllegalStateException("Hearts count is currently zero. It simply can't go down any further.");

        lastDecrementTime = SystemClock.elapsedRealtime();
        current--;

        persist();

        if (!refillRunning)
            startAlarm();
    }

    public void increaseHeart() {
        lastDecrementTime = SystemClock.elapsedRealtime();
        current++;
        persist();
        if (current >= COUNT_HEARTS_MAX)
            stop();
    }

    private void persist() {
        GlobalPreferenceManager.writeInt(context, PREFERENCE_KEY_HEARTS, current);
        GlobalPreferenceManager.writeLong(context, PREFERENCE_KEY_LAST_DECREMENT, lastDecrementTime);
    }

    public int getCount() {
        return current;
    }

    public long getMinutesToNextRefill() {
        return TIME_RECOVER_SINGLE_HEART_MILLS - (SystemClock.elapsedRealtime() - lastDecrementTime);
    }
}
