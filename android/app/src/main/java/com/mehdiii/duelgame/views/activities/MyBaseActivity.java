package com.mehdiii.duelgame.views.activities;

import android.support.v7.app.ActionBarActivity;

import com.mehdiii.duelgame.DuelApp;
import com.mehdiii.duelgame.models.Question;

import java.util.Random;

public class MyBaseActivity extends ActionBarActivity {

    static Random rand = new Random();
    static String category;
    static int NUMBER_OF_QUESTIONS = 6;
    static Question[] questionsToAsk = new Question[NUMBER_OF_QUESTIONS];
    static int userPoints;
    static int opponentPoints;
    static final int NUMBER_OF_AVATARS = 6;

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