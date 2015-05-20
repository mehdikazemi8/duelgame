package com.mehdiii.duelgame.managers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;

import com.mehdiii.duelgame.DuelApp;
import com.mehdiii.duelgame.models.events.OnHeartChangeNotice;
import com.mehdiii.duelgame.receivers.HeartFullyRecovered;

import de.greenrobot.event.EventBus;

/**
 * Created by omid on 4/20/2015.
 */
public class HeartTracker {
    private static final int TIME_RECOVER_SINGLE_HEART_SECONDS = 600;//60 * 10;
    //    private static final int TIME_RECOVER_SINGLE_HEART_SECONDS = 5;//60 * 10;
    public static final int COUNT_HEARTS_MAX = 5;
    private static final int DEFAULT_RESET_TIME_FLAG = 0;

    private int heartsCount;
    boolean isExtremeHeart;
    int timeLeft;
    boolean running = false;
    private static HeartTracker instance;

    public static HeartTracker getInstance() {
        return instance;
    }

    /**
     * used to configure heart tracker engine.
     *
     * @param heartsCount    number of current hearts
     * @param isExtremeHeart true if extreme heart is true for logged-in user
     * @return the current working HeartTracker instance.
     */
    public static HeartTracker configure(Context context, int heartsCount, boolean isExtremeHeart, int timeLeftToNextRefill) {
        if (instance == null)
            instance = new HeartTracker();

        instance.heartsCount = heartsCount;
        instance.isExtremeHeart = isExtremeHeart;

        // if hearts are less than max possible
        if (instance.heartsCount < COUNT_HEARTS_MAX && !isExtremeHeart)
            instance.resetCountdown(timeLeftToNextRefill == DEFAULT_RESET_TIME_FLAG ? TIME_RECOVER_SINGLE_HEART_SECONDS : timeLeftToNextRefill);
        else if (instance.heartsCount == COUNT_HEARTS_MAX || isExtremeHeart) {
            instance.stopCountdown();
        }

        instance.notifyChange(OnHeartChangeNotice.ChangeMode.REFRESH, heartsCount);
        instance.persist(context);
        return instance;
    }

    public static HeartTracker configure(Context context, int heartsCount, boolean isExtremeHeart) {
        return configure(context, heartsCount, isExtremeHeart, DEFAULT_RESET_TIME_FLAG);
    }

    /**
     * this method is used to (re)start the timer. if no argument is passed, it is assumed that timer is going
     * to have a refill after TIME_RECOVER_SINGLE_HEART_SECONDS, otherwise next heart refill will be scheduled to the provided argument
     */

    private void resetCountdown() {
        resetCountdown(TIME_RECOVER_SINGLE_HEART_SECONDS);
    }

    private void resetCountdown(int timeLeft) {
        this.timeLeft = timeLeft;
        if (!running) {
            running = true;
            scheduleNextTick();
        }
    }

    /**
     * stop the countdown
     */
    private void stopCountdown() {
        running = false;
    }

    /**
     * schedule next tick of heart refiller
     */
    private void scheduleNextTick() {
        if (!running)
            return;

        // check if it is time to recover one heart
        if (timeLeft == 0) {
            // increase hearts count
            increaseHeart();
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

    /**
     * should be called when user starts a game and it is intended to use one of its hearts in return.
     *
     * @return true if it is valid to decrease hearts and false if it is not possible
     */
    public boolean useHeart(Context context) {
        if (isExtremeHeart)
            return true;

        if (heartsCount <= 0)
            return false;

        decreaseHeart(context);
        notifyChange(OnHeartChangeNotice.ChangeMode.DECREASED, heartsCount);

        if (!running)
            resetCountdown();

        return true;
    }

    public boolean canUseHeart() {
        if (isExtremeHeart)
            return true;

        if (heartsCount <= 0)
            return false;
        return true;
    }

    /**
     * sends a notify signal to ui to update hearts, if mode is INCREASED or DECREASED value is indicating
     * the number of remaining hearts. if mode equals TICK then value is returning the number of seconds
     * left to the next refill.
     *
     * @param mode  the type of notification
     * @param value the number associated with the notification method description for more detail.
     */
    public void notifyChange(OnHeartChangeNotice.ChangeMode mode, int value) {
        if (EventBus.getDefault() != null)
            EventBus.getDefault().post(new OnHeartChangeNotice(mode, value));
    }

    /**
     * (in/de)crease hearts and set the new value to the user profile if accessible.
     */
    private void increaseHeart() {
        heartsCount++;
        if (AuthManager.getCurrentUser() != null)
            AuthManager.getCurrentUser().setHeart(heartsCount);
    }

    private void decreaseHeart(Context context) {
        heartsCount--;
        if (AuthManager.getCurrentUser() != null)
            AuthManager.getCurrentUser().setHeart(heartsCount);
        persist(context);
    }

    private static final String PREFERENCE_USER_HEART = "preference_user_heart";
    private static final String PREFERENCE_EXTREME_HEART = "preference_extreme_heart";

    private void persist(Context context) {
        GlobalPreferenceManager.writeInt(context, PREFERENCE_USER_HEART, heartsCount);
        GlobalPreferenceManager.writeBoolean(context, PREFERENCE_EXTREME_HEART, isExtremeHeart);
        scheduleRefillTime(context, computeRefillTime(heartsCount, timeLeft));
    }

    public static long computeRefillTime(int khearts, long timeLeft) {
        int diff = (COUNT_HEARTS_MAX - khearts);
        int fullUnites = diff > 1 ? diff - 1 : 0;

        long time = fullUnites * TIME_RECOVER_SINGLE_HEART_SECONDS * 1000;
        time += timeLeft * 1000;

        return time;
    }

    public static void scheduleRefillTime(Context context, long time) {
        if (time > 0) {
            Intent intent = new Intent(context, HeartFullyRecovered.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC, System.currentTimeMillis() + time, pendingIntent);
        }
    }
}
