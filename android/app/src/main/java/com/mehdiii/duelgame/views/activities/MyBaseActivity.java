package com.mehdiii.duelgame.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import com.mehdiii.duelgame.MusicPlayer;
import com.mehdiii.duelgame.R;
import com.mehdiii.duelgame.models.Question;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketException;
import de.tavendo.autobahn.WebSocketHandler;

public class MyBaseActivity extends ActionBarActivity {

    static protected WebSocketConnection wsc = new WebSocketConnection();
    //    static protected String wsuri = "ws://192.168.128.189:9000";
//    static protected String wsuri = "ws://192.168.128.222:9000";
//    static protected String wsuri = "ws://52.16.134.157:9000";
//    static protected String wsuri = "ws://192.168.1.103:9000";
    static protected String wsuri = "ws://192.168.128.174:9000";

    static Random rand = new Random();

    static String category;

    static boolean DONE = false;
    static int NUMBER_OF_QUESTIONS = 6;
    static Question[] questionsToAsk = new Question[NUMBER_OF_QUESTIONS];

    static String oppName;
    static int myPoints;
    static int oppPoints;
    static String oppUserNumber;

    public static int myAvatarIndex = 1;
    public static int oppAvatarIndex = 1;
    public static String myName;
    public static String myOstanStr;
    public static int myOstanInt;
    public static String myEmail;
    public static int myElo;
    public static int myScore;
    public static int myTime;
    public static String myUserNumber;

    public static String loginInfo;
    static String resultInfo;

    private String TAG = "---------------- ???";

    static final int NUMBER_OF_AVATARS = 6;
    public static int[] avatarId = new int[NUMBER_OF_AVATARS + 1];

    public String getOstanStr(int idx) {
        List<String> ostans = Arrays.asList(getResources().getStringArray(R.array.ostan_array));
        try {
            return ostans.get(idx);
        } catch (IndexOutOfBoundsException e) {
            Log.d("-- getOstanStr", e.toString());
        }
        return "errorOstan";
    }

    public void initAvatarIds() {
        avatarId[1] = R.drawable.av1;
        avatarId[2] = R.drawable.av2;
        avatarId[3] = R.drawable.av3;
        avatarId[4] = R.drawable.av4;
        avatarId[5] = R.drawable.av5;
        avatarId[6] = R.drawable.av6;
//        avatarId[7] = R.drawable.av7;
//        avatarId[8] = R.drawable.av8;
//        avatarId[9] = R.drawable.av9;
//        avatarId[10] = R.drawable.av10;
//        avatarId[11] = R.drawable.av11;
//        avatarId[12] = R.drawable.av12;
//        avatarId[13] = R.drawable.av13;
//        avatarId[14] = R.drawable.av14;
//        avatarId[15] = R.drawable.av15;
//        avatarId[16] = R.drawable.av16;
//        avatarId[17] = R.drawable.av17;
//        avatarId[18] = R.drawable.av18;
//        avatarId[19] = R.drawable.av19;
//        avatarId[20] = R.drawable.av20;
//        avatarId[21] = R.drawable.av21;
//        avatarId[22] = R.drawable.av22;
//        avatarId[23] = R.drawable.av23;
//        avatarId[24] = R.drawable.av24;
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


            Intent svc = new Intent(this, MusicPlayer.class);
            startService(svc);

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


    static void shuffleArray(String[] ar) {
        for (int i = ar.length - 1; i > 0; i--) {
            int index = rand.nextInt(i + 1);
            // Simple swap
            String a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
    }
}