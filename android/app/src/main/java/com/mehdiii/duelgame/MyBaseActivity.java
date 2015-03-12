package com.mehdiii.duelgame;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.util.Random;

import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketException;
import de.tavendo.autobahn.WebSocketHandler;

public class MyBaseActivity extends Activity {

    static protected WebSocketConnection wsc = new WebSocketConnection();
    static protected String wsuri = "ws://192.168.128.168:9000";
//    static protected String wsuri = "ws://52.16.134.157:9000";
//    static protected String wsuri = "ws://192.168.1.5:9000";

    static boolean DONE = false;
    static int NUMBER_OF_QUESTIONS = 6;
    static Question[] questionsToAsk = new Question[NUMBER_OF_QUESTIONS];

    static String myNameIs;
    static String oppNameIs;
    static int myPoints;

    static int oppPoints;

    private String TAG = "---------------- ???";

    protected boolean doConnect() {
        try {
            wsc.connect(wsuri, new WebSocketHandler() {
                @Override
                public void onOpen() {
                    Log.d(TAG, "Status: Connected to " + wsuri);
                }

                @Override
                public void onTextMessage(String payload) {
                    Log.d(TAG, "Got echo: " + payload);

                    Intent i = new Intent();
                    i.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
                    i.setAction("MESSAGE");
                    i.putExtra("inputMessage", payload);
                    sendBroadcast(i);
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

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //8888888888888888888888888888888888888888888888
        myNameIs = "MEHDI";

        if (DONE == false) {
            doConnect();
            DONE = true;
        }
    }

    protected void onResume() {
        super.onResume();
    }

    protected void onPause() {
        super.onPause();
    }

    protected void onDestroy() {
        super.onDestroy();
    }

    static Random rnd = new Random();

    static void shuffleArray(String[] ar) {
        for (int i = ar.length - 1; i > 0; i--) {
            int index = rnd.nextInt(i + 1);
            // Simple swap
            String a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
    }
}