package com.mehdiii.duelgame.views.activities;

import android.support.v7.app.ActionBarActivity;

import com.mehdiii.duelgame.models.Question;

import java.util.Random;

public class MyBaseActivity extends ActionBarActivity {

    //    static protected WebSocketConnection wsc = new WebSocketConnection();
    //    static protected String wsuri = "ws://192.168.128.189:9000";
//    static protected String wsuri = "ws://192.168.128.222:9000";
//    static protected String wsuri = "ws://52.16.134.157:9000";
//    static protected String wsuri = "ws://192.168.1.103:9000";
    static protected String wsuri = "ws://192.168.128.145:9000";
    static Random rand = new Random();
    static String category;
    static int NUMBER_OF_QUESTIONS = 6;
    static Question[] questionsToAsk = new Question[NUMBER_OF_QUESTIONS];

    static int userPoints;
    static int opponentPoints;

    public static String myName;
    public static String myOstanStr;
    public static int myOstanInt;
    public static String myEmail;
    public static int myScore;
    public static int myTime;
    public static String myUserNumber;

    //    public static String loginInfo;
    static String resultInfo;

    private String TAG = "---------------- ???";

    static final int NUMBER_OF_AVATARS = 6;
//    public static int[] avatarId = new int[NUMBER_OF_AVATARS + 1];


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