package com.mehdiii.duelgame.views.activities;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.ActionBarActivity;

import com.mehdiii.duelgame.models.Question;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.greenrobot.event.EventBus;

public class ParentActivity extends ActionBarActivity {

    static Random rand = new Random();
    static String category;
    static int NUMBER_OF_QUESTIONS = 6;
    static List<Question> questionsToAsk = new ArrayList<>();
    static int userPoints;
    static int opponentPoints;
    static final int NUMBER_OF_AVATARS = 6;

    static void shuffleArray(List<String> ar) {
        for (int i = ar.size() - 1; i > 0; i--) {
            int index = rand.nextInt(i + 1);
            // Simple swap
            String a = ar.get(index);
            ar.set(index, ar.get(i));
            ar.set(i, a);
        }
    }
}