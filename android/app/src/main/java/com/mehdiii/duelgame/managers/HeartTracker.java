/**
 * To-Do list (future work)
 * 1. post update notif
 */
package com.mehdiii.duelgame.managers;

import android.app.AlarmManager;
import android.app.Fragment;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;

import com.mehdiii.duelgame.models.events.OnHeartChangeNotice;
import com.mehdiii.duelgame.receivers.OnHeartRefillTimeArrived;

import de.greenrobot.event.EventBus;

/**
 * Created by omid on 4/20/2015.
 */
public class HeartTracker {
    private static final int TIME_RECOVER_SINGLE_HEART_SECONDS = 10;//60 * 10;
    public static final int COUNT_HEARTS_MAX = 5;

    private int heartsCount;
    int timeLeft;
    boolean running = false;
    private static HeartTracker instance;

    public static HeartTracker configure(int heartsCount) {
        if (instance == null) {
            instance = new HeartTracker();
            instance.heartsCount = heartsCount;
        }

        // if hearts are less than max possible
        if (instance.heartsCount < COUNT_HEARTS_MAX)
            instance.resetCountdown(TIME_RECOVER_SINGLE_HEART_SECONDS);

        return instance;
    }

    public static HeartTracker getInstance() {
        return instance;
    }

    private void resetCountdown() {
        resetCountdown(TIME_RECOVER_SINGLE_HEART_SECONDS);
    }

    private void resetCountdown(int timeLeft) {
        this.timeLeft = timeLeft;
        running = true;
        scheduleNextTick();
    }

    private void scheduleNextTick() {
        if (!running)
            return;

        // check if it is time to recover one heart
        if (timeLeft == 0) {
            // increase hearts count
            heartsCount++;
            running = false;
            notifyChange(OnHeartChangeNotice.ChangeMode.INCREASED, heartsCount);
            if (heartsCount < COUNT_HEARTS_MAX)
                resetCountdown();
            return;
        } else
            notifyChange(OnHeartChangeNotice.ChangeMode.TICK, timeLeft);

        // schedule next tick
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                HeartTracker.this.timeLeft--;
                scheduleNextTick();
            }
        }, 1000);
    }

    public int getHeartsCount() {
        return heartsCount;
    }

    public boolean useHeart() {
        if (heartsCount <= 0)
            return false;

        heartsCount--;
        notifyChange(OnHeartChangeNotice.ChangeMode.DECREASED, heartsCount);

        if (!running)
            resetCountdown();

        return true;
    }

    /**
     * sends a notify signal to ui to update hearts, if mode is INCREASED or DECREASED value is indicating the number of remaining hearts.
     * if mode equals TICK then value is returning the number of seconds left to the next refill.
     *
     * @param mode  the type of notification
     * @param value the number associated with the notification method description for more detail.
     */
    public void notifyChange(OnHeartChangeNotice.ChangeMode mode, int value) {
        if (EventBus.getDefault() != null)
            EventBus.getDefault().post(new OnHeartChangeNotice(mode, value));
    }
}
