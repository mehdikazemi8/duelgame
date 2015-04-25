package com.mehdiii.duelgame.views.activities;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
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
import com.mehdiii.duelgame.managers.AuthManager;
import com.mehdiii.duelgame.managers.ProvinceManager;
import com.mehdiii.duelgame.models.Question;
import com.mehdiii.duelgame.models.User;
import com.mehdiii.duelgame.utils.AvatarHelper;
import com.mehdiii.duelgame.utils.DuelMusicPlayer;
import com.mehdiii.duelgame.utils.FontHelper;
import com.mehdiii.duelgame.utils.ScoreHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class WaitingActivity extends MyBaseActivity {
    boolean hasLeft;

    private TextView userTitle;
    private TextView userName;
    private TextView userProvince;
    private ImageView userAvatar;
    private ImageView opponentAvatar;
    private TextView opponentTitle;
    private TextView opponentName;
    private TextView opponentProvince;
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

    DuelMusicPlayer musicPlayer;

    protected class TitleBarListener extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("MESSAGE")) {
                String inputMessage = intent.getExtras().getString("inputMessage");
                String messageCode;
                JSONObject parser = null;
                User opponentUser = new User();

                try {
                    parser = new JSONObject(inputMessage);
                    messageCode = parser.getString("code");
                    if (messageCode.compareTo("YOI") == 0) {

                        String allOpponentsStr = parser.getString("opponents");
                        JSONArray allOpponents = new JSONArray(allOpponentsStr);
                        JSONObject firstOpponent = new JSONObject(allOpponents.get(0).toString());

                        opponentUser.setName(firstOpponent.getString("name"));
                        opponentUser.setAvatar(firstOpponent.getInt("avatar"));
                        opponentUser.setProvince(firstOpponent.getInt("province"));
                        opponentUser.setId(firstOpponent.getString("user_number"));

                        ((ImageView) findViewById(R.id.waiting_opponent_avatar)).setImageResource(AvatarHelper.getResourceId(WaitingActivity.this, opponentUser.getAvatar()));
                        setTextView(opponentName, opponentUser.getName());
                        setTextView(opponentProvince, ProvinceManager.get(WaitingActivity.this, opponentUser.getProvince()));

                        translateAnimation(opponentLayout, "translationY", 500, 0, 1500);
                    } else if (messageCode.compareTo("SP") == 0) {
                        //AuthManager.getCurrentUser().decreaseDiamond(120);

                        Intent i = new Intent(getApplicationContext(), PlayGameActivity.class);
                        i.putExtra(PlayGameActivity.ARGUMENT_OPPONENT, opponentUser.serialize());
                        startActivity(i);
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

        userTitle = (TextView) findViewById(R.id.waiting_user_title);
        userName = (TextView) findViewById(R.id.waiting_user_name);
        userProvince = (TextView) findViewById(R.id.waiting_user_province);
        userAvatar = (ImageView) findViewById(R.id.waiting_user_avatar);
        opponentAvatar = (ImageView) findViewById(R.id.waiting_opponent_avatar);
        opponentTitle = (TextView) findViewById(R.id.waiting_opponent_title);
        opponentName = (TextView) findViewById(R.id.waiting_opponent_name);
        opponentProvince = (TextView) findViewById(R.id.waiting_opponent_province);
        waitingAgainst = (TextView) findViewById(R.id.waiting_against);

        playerLayout = (LinearLayout) findViewById(R.id.waiting_player_layout);
        opponentLayout = (LinearLayout) findViewById(R.id.waiting_opponent_layout);

        hasLeft = false;

        mListener = new TitleBarListener();
        LocalBroadcastManager.getInstance(this).registerReceiver(mListener, new IntentFilter("MESSAGE"));

        User user = AuthManager.getCurrentUser();

        // TODO-2 DELETE THIS
        user.setName("mehdiiiii");
        user.setProvince(20);
        user.setAvatar(15);
        user.setScore(250);
        // END OF TODO-2

        userAvatar.setImageResource(AvatarHelper.getResourceId(this, user.getAvatar()));
        setTextView(userName, user.getName());
        setTextView(userTitle, ScoreHelper.getTitle(user.getScore()));
        setTextView(userProvince, ProvinceManager.get(this, user.getProvince()));

        translateAnimation(playerLayout, "translationY", -500, 0, 1500);

        FontHelper.setKoodakFor(getApplicationContext(),
                userTitle, userName, userProvince,
                opponentTitle, opponentName, opponentProvince,
                waitingAgainst);

        musicPlayer = new DuelMusicPlayer(WaitingActivity.this, R.raw.waiting, true);
        musicPlayer.execute();
    }

    public void setTextView(TextView tv, String str) {
        tv.setText(str);
    }

    @Override
    public void onPause() {
        super.onPause();

        musicPlayer.pauseSound();
//        Intent svc = new Intent(this, MusicPlayer.class);
//        stopService(svc);
    }

    @Override
    public void onResume() {
        super.onResume();
//        setData();

        musicPlayer.playSound();
//        Intent svc = new Intent(this, MusicPlayer.class);
//        startService(svc);
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
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mListener);
    }

    @Override
    public void onBackPressed() {
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
