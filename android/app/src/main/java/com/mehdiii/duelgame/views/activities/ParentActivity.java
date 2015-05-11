package com.mehdiii.duelgame.views.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.mehdiii.duelgame.models.Question;
import com.mehdiii.duelgame.models.events.OnConnectionLost;
import com.mehdiii.duelgame.views.dialogs.ConnectionLostDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.greenrobot.event.EventBus;

public class ParentActivity extends ActionBarActivity {

    static Random rand = new Random();
    static String category;
    static int NUMBER_OF_QUESTIONS = 6;
    protected static List<Question> questionsToAsk = new ArrayList<>();
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    public void onEvent(OnConnectionLost lost) {
        ConnectionLostDialog dialog = new ConnectionLostDialog(this);
        dialog.setCancelable(false);
        dialog.show();
    }
}