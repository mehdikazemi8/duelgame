package com.mehdiii.duelgame;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.util.Random;

import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketException;
import de.tavendo.autobahn.WebSocketHandler;

public class MyBaseActivity extends Activity {

    static protected WebSocketConnection wsc = new WebSocketConnection();
    static protected String wsuri = "ws://192.168.128.216:9000";
//      static protected String wsuri = "ws://192.168.128.243:9000";
//    static protected String wsuri = "ws://52.16.134.157:9000";
//    static protected String wsuri = "ws://192.168.1.5:9000";

    static boolean DONE = false;
    static int NUMBER_OF_QUESTIONS = 6;
    static Question[] questionsToAsk = new Question[NUMBER_OF_QUESTIONS];

    static String oppName;
    static int myPoints;
    static int oppPoints;

    static int myAvatarIndex = 1;
    static int oppAvatarIndex = 1;
    static String myName;
    static String myOstanStr;
    static int myOstanInt;
    static String myEmail;
    static int myElo;
    static int myScore;
    static int myTime;
    static String myUserNumber;

    static String loginInfo;
    static String resultInfo;

    private String TAG = "---------------- ???";

    static final int NUMBER_OF_AVATARS = 24;
    static int[] avatarId = new int[NUMBER_OF_AVATARS + 1];

    public void initAvatarIds()
    {
        avatarId[1] = R.drawable.av1;
        avatarId[2] = R.drawable.av2;
        avatarId[3] = R.drawable.av3;
        avatarId[4] = R.drawable.av4;
        avatarId[5] = R.drawable.av5;
        avatarId[6] = R.drawable.av6;
        avatarId[7] = R.drawable.av7;
        avatarId[8] = R.drawable.av8;
        avatarId[9] = R.drawable.av9;
        avatarId[10] = R.drawable.av10;
        avatarId[11] = R.drawable.av11;
        avatarId[12] = R.drawable.av12;
        avatarId[13] = R.drawable.av13;
        avatarId[14] = R.drawable.av14;
        avatarId[15] = R.drawable.av15;
        avatarId[16] = R.drawable.av16;
        avatarId[17] = R.drawable.av17;
        avatarId[18] = R.drawable.av18;
        avatarId[19] = R.drawable.av19;
        avatarId[20] = R.drawable.av20;
        avatarId[21] = R.drawable.av21;
        avatarId[22] = R.drawable.av22;
        avatarId[23] = R.drawable.av23;
        avatarId[24] = R.drawable.av24;
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
        if (DONE == false) {
            doConnect();
            initAvatarIds();
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