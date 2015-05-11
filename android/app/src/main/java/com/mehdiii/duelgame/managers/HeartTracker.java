package com.mehdiii.duelgame.managers;

import android.os.Handler;

import com.mehdiii.duelgame.models.events.OnHeartChangeNotice;

import de.greenrobot.event.EventBus;

/**
 * Created by omid on 4/20/2015.
 */
public class HeartTracker {
    private static final int TIME_RECOVER_SINGLE_HEART_SECONDS = 600;//60 * 10;
    public static final int COUNT_HEARTS_MAX = 5;

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
    public static HeartTracker configure(int heartsCount, boolean isExtremeHeart, int timeLeftToNextRefill) {
        if (instance == null)
            instance = new HeartTracker();

        instance.heartsCount = heartsCount;
        instance.isExtremeHeart = isExtremeHeart;

        // if hearts are less than max possible
        if (instance.heartsCount < COUNT_HEARTS_MAX && !isExtremeHeart)
            instance.resetCountdown(timeLeftToNextRefill == -1 ? TIME_RECOVER_SINGLE_HEART_SECONDS : timeLeftToNextRefill);
        else if (instance.heartsCount == COUNT_HEARTS_MAX || isExtremeHeart) {
            instance.stopCountdown();
        }

        instance.notifyChange(OnHeartChangeNotice.ChangeMode.REFRESH, heartsCount);
        return instance;
    }

    public static HeartTracker configure(int heartsCount, boolean isExtremeHeart) {
        return configure(heartsCount, isExtremeHeart, -1);
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
        running = true;
        scheduleNextTick();
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

    public boolean canUseHeart() {
        if (isExtremeHeart)
            return true;

        if (heartsCount <= 0)
            return false;
        return true;
    }

    /**
     * should be called when user starts a game and it is intended to use one of its hearts in return.
     *
     * @return true if it is valid to decrease hearts and false if it is not possible
     */
    public boolean useHeart() {
        if (isExtremeHeart)
            return true;

        if (heartsCount <= 0)
            return false;

        decreaseHeart();
        notifyChange(OnHeartChangeNotice.ChangeMode.DECREASED, heartsCount);

        if (!running)
            resetCountdown();

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

    private void decreaseHeart() {
        heartsCount--;
        if (AuthManager.getCurrentUser() != null)
            AuthManager.getCurrentUser().setHeart(heartsCount);
    }
}
