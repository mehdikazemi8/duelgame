package com.mehdiii.duelgame;

import android.app.Application;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.mehdiii.duelgame.managers.HeartTracker;
import com.mehdiii.duelgame.models.base.BaseModel;
import com.mehdiii.duelgame.utils.DuelBroadcastReceiver;
import com.mehdiii.duelgame.utils.OnMessageReceivedListener;

import java.util.HashMap;
import java.util.Map;

import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketException;
import de.tavendo.autobahn.WebSocketHandler;

/**
 * Created by omid on 4/12/2015.
 */
public class DuelApp extends Application {
    public static final String PROPERTY_ID = "UA-62041991-1";
    private static DuelApp instance;
    static protected WebSocketConnection wsc = new WebSocketConnection();
    static boolean isConnected = false;
    private String TAG = "DUEL_APP";
    Map<Integer, BaseModel> pendingMessages = new HashMap<>();

//    static protected String wsuri = "ws://188.166.118.149:9000";
    static protected String wsuri = "ws://192.168.128.217:9000";

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        initGA();
        HeartTracker.getInstance(getApplicationContext()).init();
        if (!isConnected) {
            Intent svc = new Intent(this, MusicPlayer.class);
            startService(svc);

            doConnect();
        }
    }

    protected void doConnect() {
        try {
            wsc.connect(wsuri, new WebSocketHandler() {
                @Override
                public void onOpen() {
                    isConnected = true;
                    Log.d(TAG, "Status: Connected to " + wsuri);
                }

                @Override
                public void onTextMessage(String payload) {
                    Log.d(TAG, "&&&&& Got echo: " + payload);

                    dispatchMessage(payload);
                }

                @Override
                public void onClose(int code, String reason) {
                    isConnected = false;
                    Log.d(TAG, "Connection lost.");
                }
            });
        } catch (WebSocketException e) {
            e.printStackTrace();
        }
    }

    /**
     * sends off messages delivered to wsc from server to all of the app.
     *
     * @param json the message received from server
     */
    public void dispatchMessage(String json) {
        Intent i = new Intent();
        i.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        i.setAction(DuelBroadcastReceiver.ACTION_NAME);
        i.putExtra(DuelBroadcastReceiver.BUNDLE_JSON_KEY, json);

        // detect ke in message delivery
        // detect konam ke in message vase kie?
        // call konam ino

        // use local broadcast manager to avoid unnecessary calls to other apps
        LocalBroadcastManager.getInstance(this).sendBroadcast(i);
    }

    public IntentFilter getIntentFilter() {
        return new IntentFilter(DuelBroadcastReceiver.ACTION_NAME);
    }

    public void sendMessage(String json) {
        wsc.sendTextMessage(json);
    }

    /**
     * used to send a message and receive delivery message so that you can check whether request is reached/processed successfully on server or not?
     *
     * @param model                     the model that is used to be sent off to the server
     * @param onMessageReceivedListener this listens for delivery message of current request and calls onDelivered when delivery message is received.
     */
    public void sendMessageWithDelivery(BaseModel model, OnMessageReceivedListener onMessageReceivedListener) {
        pendingMessages.put(model.hashCode(), model);
        sendMessage(model.serialize());
    }

    public WebSocketConnection getSocket() {
        return wsc;
    }

    public static synchronized DuelApp getInstance() {
        return instance;
    }

    /**
     * used as tell android display a global toast messages in screen.
     *
     * @param resourceId reference to the string resource in which toast is going to display
     * @param length     length of the toast, i.e. typically TOAST.LENGTH_SHORT or TOAST.LENGTH_LONG
     */
    public void toast(int resourceId, int length) {
        Toast.makeText(getApplicationContext(), getResources().getString(resourceId), length).show();
    }

    /**
     * Enum used to identify the tracker that needs to be used for tracking.
     * <p/>
     * A single tracker is usually enough for most purposes. In case you do need multiple trackers,
     * storing them all in Application object helps ensure that they are created only once per
     * application instance.
     */
    public enum TrackerName {
        APP_TRACKER, // Tracker used only in this app.
        GLOBAL_TRACKER, // Tracker used by all the apps from a company. eg: roll-up tracking.
        ECOMMERCE_TRACKER, // Tracker used by all ecommerce transactions from a company.
    }

    HashMap<TrackerName, Tracker> mTrackers = new HashMap<TrackerName, Tracker>();

    public synchronized Tracker getTracker(TrackerName trackerId) {
        if (!mTrackers.containsKey(trackerId)) {

            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            Tracker t = (trackerId == TrackerName.APP_TRACKER) ? analytics.newTracker(PROPERTY_ID)
                    : analytics.newTracker(R.xml.global_tracker);

            mTrackers.put(trackerId, t);
        }
        return mTrackers.get(trackerId);
    }

    private void initGA() {
        Tracker mGATracker = getTracker(TrackerName.GLOBAL_TRACKER);
        mGATracker.setSessionTimeout(300);
        mGATracker.enableAutoActivityTracking(true);
        mGATracker.enableExceptionReporting(true);
    }
}
