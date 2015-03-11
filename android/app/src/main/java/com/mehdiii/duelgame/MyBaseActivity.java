package com.mehdiii.duelgame;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.Random;

public class MyBaseActivity extends Activity {

    static protected WebSocketClient wsc = null;
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

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //8888888888888888888888888888888888888888888888
        myNameIs = "MEHDI";

        if (DONE == false) {
            wsc = new WebSocketClient(URI.create(wsuri)) {
                @Override
                public void onOpen(ServerHandshake serverHandshake) {
                    Log.d("-----", "onOpen " + serverHandshake.toString());
                }

                @Override
                public void onMessage(String inputMessage) {
                    Log.d("+++++++++++++++++", "onMessage " + inputMessage);


                    Intent i = new Intent();
                    i.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
                    i.setAction("MESSAGE");
                    i.putExtra("inputMessage", inputMessage);
                    sendBroadcast(i);

                }

                @Override
                public void onClose(int i, String s, boolean b) {
                    Log.d("-----", "onClose " + s);
                }

                @Override
                public void onError(Exception e) {
                    Log.d("-----", "onError " + e.toString());
                }
            };
            wsc.connect();

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
    static void shuffleArray(String[] ar)
    {
        for (int i = ar.length - 1; i > 0; i--)
        {
            int index = rnd.nextInt(i + 1);
            // Simple swap
            String a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
    }
}