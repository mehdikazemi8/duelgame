package com.mehdiii.duelgame;

import android.app.Application;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketException;
import de.tavendo.autobahn.WebSocketHandler;

/**
 * Created by omid on 4/12/2015.
 */
public class DuelApp extends Application {
    private static DuelApp instance;
    static protected WebSocketConnection wsc = new WebSocketConnection();
    static boolean isConnected = false;
    private String TAG = "DUEL_APP";

    static protected String wsuri = "ws://188.166.118.149:9000";

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        if (!isConnected) {
            Intent svc = new Intent(this, MusicPlayer.class);
            startService(svc);

            doConnect();
            isConnected = true;
        }
    }

    protected boolean doConnect() {
        try {
            wsc.connect(wsuri, new WebSocketHandler() {
                @Override
                public void onOpen() {
                    Log.d(TAG, "Status: Connected to " + wsuri);
                }

                @Override
                public void onTextMessage(String payload) {
                    Log.d(TAG, "&&&&& Got echo: " + payload);
                    enqueueMessage(payload);
                }

                @Override
                public void onClose(int code, String reason) {
                    Log.d(TAG, "Connection lost.");
                }
            });
        } catch (WebSocketException e) {
            return false;
        }
        return true;
    }

    public void enqueueMessage(String json) {
        Intent i = new Intent();
        i.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        i.setAction("MESSAGE");
        i.putExtra("inputMessage", json);
        LocalBroadcastManager.getInstance(this).sendBroadcast(i);
    }

    public IntentFilter getIntentFilter() {
        return new IntentFilter("MESSAGE");
    }

    public void sendMessage(String json) {
        wsc.sendTextMessage(json);
    }

    public WebSocketConnection getSocket() {
        return wsc;
    }

    public static synchronized DuelApp getInstance() {
        return instance;
    }

    public void toast(int resourceId, int length) {
        Toast.makeText(getApplicationContext(), getResources().getString(resourceId), Toast.LENGTH_LONG).show();
    }

}
