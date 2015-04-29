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

import com.mehdiii.duelgame.models.events.OnHeartChangeNotice;
import com.mehdiii.duelgame.models.HeartState;
import com.mehdiii.duelgame.receivers.OnHeartRefillTimeArrived;

import de.greenrobot.event.EventBus;

/**
 * Created by omid on 4/20/2015.
 */
public class HeartTracker {

    public static final String PREFERENCE_KEY_HEARTS = "heart_tracker";
    public static final String PREFERENCE_KEY_LAST_DECREMENT = "last_decrement_time";

    public static final int COUNT_HEARTS_MAX = 5;
    private static final int TIME_RECOVER_SINGLE_HEART_MILLS = 10000;

    private HeartState state;
    private boolean refillRunning = false;

    private Context context;
    private Intent intent;
    private PendingIntent pendingIntent;
    private AlarmManager alarmManager = null;

    private static HeartTracker instance;

    public static HeartTracker getInstance(Context ctx) {
        if (instance == null) {
            instance = new HeartTracker();
            instance.context = ctx;
        }

        return instance;
    }

    public void init() {

        state = new HeartState();
        intent = new Intent(context, OnHeartRefillTimeArrived.class);
        pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        state.setCurrent(GlobalPreferenceManager.readInteger(context, PREFERENCE_KEY_HEARTS, COUNT_HEARTS_MAX));
        state.setLastDecrementTime(GlobalPreferenceManager.readLong(context, PREFERENCE_KEY_LAST_DECREMENT, -1));

        startAlarm();
    }

    private void startAlarm() {
        if (refillRunning || (state != null && state.getCurrent() == COUNT_HEARTS_MAX))
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
        if ( state == null )
            init();

        if (state.getCurrent() <= 0)
            return;
//            throw new IllegalStateException("Hearts count is currently zero. It simply can't go down any further.");

        state.decrease();
        saveCheckpoint();
        notifyChange(OnHeartChangeNotice.ChangeMode.DECREASED);

        if (!refillRunning)
            startAlarm();
    }

    public void setLoginHearts(int khearts) {
        getState().setCurrent(khearts);
        persist();
        notifyChange(OnHeartChangeNotice.ChangeMode.REFRESH);
    }

    public void increaseHeart() {
        if ( state == null )
            init();

        state.increase();
        saveCheckpoint();
        notifyChange(OnHeartChangeNotice.ChangeMode.INCREASED);

        if (state.getCurrent() >= COUNT_HEARTS_MAX)
            stop();
    }

    private void persist() {
        GlobalPreferenceManager.writeInt(context, PREFERENCE_KEY_HEARTS, state.getCurrent());
        GlobalPreferenceManager.writeLong(context, PREFERENCE_KEY_LAST_DECREMENT, state.getLastDecrementTime());
    }

    private void saveCheckpoint() {
        state.setLastDecrementTime(SystemClock.elapsedRealtime());
        persist();
    }

    public int getCount() {
        return state.getCurrent();
    }

    public long getMinutesToNextRefill() {
        return TIME_RECOVER_SINGLE_HEART_MILLS - (SystemClock.elapsedRealtime() - state.getLastDecrementTime());
    }

    public void notifyChange(OnHeartChangeNotice.ChangeMode mode) {
        if (EventBus.getDefault() != null)
            EventBus.getDefault().post(new OnHeartChangeNotice(state, mode));
    }

    public HeartState getState() {
        return state;
    }
}
