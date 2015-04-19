package com.mehdiii.duelgame.views.activities;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mehdiii.duelgame.DuelApp;
import com.mehdiii.duelgame.R;
import com.mehdiii.duelgame.models.Question;
import com.mehdiii.duelgame.utils.AvatarHelper;
import com.mehdiii.duelgame.utils.FontHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class WaitingActivity extends MyBaseActivity {
    boolean hasLeft;

    private TextView waitingMyLevel;
    private TextView waitingMyName;
    private TextView waitingMyOstan;
    private ImageView waitingMyAvatar;
    private ImageView waitingOpponentAvatar;
    private TextView waitingOpponentLevel;
    private TextView waitingOpponentName;
    private TextView waitingOpponentOstan;
    private TextView waitingAgainst;

    private LinearLayout playerLayout, opponentLayout;

    private void translateAnimation(final LinearLayout layout, String cmd, int from, int to, int duration) {
        ObjectAnimator animation = ObjectAnimator.ofFloat(layout, cmd, from, to);
        animation.setDuration(duration);
        animation.setInterpolator(new BounceInterpolator());

        animation.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                layout.setVisibility(View.VISIBLE);
                Log.d("---- wating onAnimationStart", "");
            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        animation.start();
    }

    protected class TitleBarListener extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("MESSAGE")) {
                String inputMessage = intent.getExtras().getString("inputMessage");
                String messageCode;
                JSONObject parser = null;

                try {
                    parser = new JSONObject(inputMessage);
                    messageCode = parser.getString("code");
                    if (messageCode.compareTo("YOI") == 0) {

                        String allOpponentsStr = parser.getString("opponents");
                        Log.d("---- allOpponentsStr", allOpponentsStr);
                        JSONArray allOpponents = new JSONArray(allOpponentsStr);
                        JSONObject firstOpponent = new JSONObject(allOpponents.get(0).toString());
                        Log.d("------ firstOpponent", firstOpponent.toString());

                        oppName = firstOpponent.getString("name");
                        oppAvatarIndex = firstOpponent.getInt("avatar");

//                        int oppOstanInt = firstOpponent.getInt("ostan");
                        int oppElo = (int) firstOpponent.getDouble("elo");
                        oppUserNumber = firstOpponent.getString("user_number");

                        ((ImageView) findViewById(R.id.waiting_opponent_avatar)).setImageResource(AvatarHelper.getResourceId(WaitingActivity.this, oppAvatarIndex));
                        setTextView(waitingOpponentName, oppName);
//                        setTextView(waitingOpponentOstan, getOstanStr(oppOstanInt));

                        translateAnimation(opponentLayout, "translationY", 500, 0, 1500);
                    } else if (messageCode.compareTo("SP") == 0) {
                        myTime -= 120;

//                        android.os.Handler waitingHandler = new android.os.Handler();
//                        waitingHandler.postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                Log.d("-- runnable ", "start play Game");
//                                startActivity(new Intent(getApplicationContext(), PlayGameActivity.class));
//                                finish();
//                            }
//                        }, 2000);
                        startActivity(new Intent(getApplicationContext(), PlayGameActivity.class));
                        finish();
                    } else if (messageCode.compareTo("RGD") == 0) {

                        for (int problemIndex = 0; problemIndex < NUMBER_OF_QUESTIONS; problemIndex++) {
                            questionsToAsk[problemIndex] = new Question();

                            Log.d("parser.getString()", parser.toString());

                            JSONObject thisQuestion = new JSONObject(parser.getString("problem" + problemIndex));
                            questionsToAsk[problemIndex].questionText = thisQuestion.getString("question_text");
                            JSONArray options = thisQuestion.getJSONArray("options");
                            for (int op = 0; op < 4; op++) {
                                questionsToAsk[problemIndex].options[op] = "" + options.get(op);
                            }
                        }

                        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
                        ScheduledFuture scheduledFuture = scheduledExecutorService.schedule(new Runnable() {
                            @Override
                            public void run() {
                                if (hasLeft == true)
                                    return;

                                JSONObject query = new JSONObject();
                                try {
                                    query.put("code", "RTP");
                                    DuelApp.getInstance().sendMessage(query.toString());
                                } catch (JSONException e) {
                                    Log.d("---- StartActivity JSON", e.toString());
                                }
                                Log.d("-- runnable ", "RTP SEND runnable");
                            }
                        }, 4, TimeUnit.SECONDS);

                        Log.d("khoonddde shod", "ddd");
                    }
                } catch (JSONException e) {
                    Log.d("---------", "can not parse string Waiting Activity");
                }
            }
        }
    }

    TitleBarListener mListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting);

        waitingMyLevel = (TextView) findViewById(R.id.waiting_my_level);
        waitingMyName = (TextView) findViewById(R.id.waiting_my_name);
        waitingMyOstan = (TextView) findViewById(R.id.waiting_my_ostan);
        waitingMyAvatar = (ImageView) findViewById(R.id.waiting_my_avatar);
        waitingOpponentAvatar = (ImageView) findViewById(R.id.waiting_opponent_avatar);
        waitingOpponentLevel = (TextView) findViewById(R.id.waiting_opponent_level);
        waitingOpponentName = (TextView) findViewById(R.id.waiting_opponent_name);
        waitingOpponentOstan = (TextView) findViewById(R.id.waiting_opponent_ostan);
        waitingAgainst = (TextView) findViewById(R.id.waiting_against);

        playerLayout = (LinearLayout) findViewById(R.id.waiting_player_layout);
        opponentLayout = (LinearLayout) findViewById(R.id.waiting_opponent_layout);

        hasLeft = false;

        mListener = new TitleBarListener();
        registerReceiver(mListener, new IntentFilter("MESSAGE"));

        waitingMyAvatar.setImageResource(AvatarHelper.getResourceId(this, myAvatarIndex));
        setTextView(waitingMyName, myName);
        setTextView(waitingMyOstan, getOstanStr(myOstanInt));

        translateAnimation(playerLayout, "translationY", -500, 0, 1500);

        FontHelper.setKoodakFor(getApplicationContext(),
                waitingMyLevel, waitingMyName, waitingMyOstan,
                waitingOpponentLevel, waitingOpponentName, waitingOpponentOstan,
                waitingAgainst);
    }

    public void setTextView(TextView tv, String str) {
        tv.setText(str);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_start, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.about) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mListener);
    }

    @Override
    public void onBackPressed() {
        Log.d(" --- ", myName + " pressed onBack Waiting");

        hasLeft = true;

        JSONObject query = new JSONObject();
        try {
            query.put("code", "ULG");

            DuelApp.getInstance().sendMessage(query.toString());
        } catch (JSONException e) {
            Log.d("---- GQ GQ GQ", e.toString());
        }

        finish();
    }
}
