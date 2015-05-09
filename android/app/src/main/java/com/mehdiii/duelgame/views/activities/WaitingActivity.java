package com.mehdiii.duelgame.views.activities;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mehdiii.duelgame.DuelApp;
import com.mehdiii.duelgame.R;
import com.mehdiii.duelgame.managers.AuthManager;
import com.mehdiii.duelgame.managers.HeartTracker;
import com.mehdiii.duelgame.managers.ProvinceManager;
import com.mehdiii.duelgame.models.OpponentCollection;
import com.mehdiii.duelgame.models.ProblemCollection;
import com.mehdiii.duelgame.models.Question;
import com.mehdiii.duelgame.models.User;
import com.mehdiii.duelgame.models.base.BaseModel;
import com.mehdiii.duelgame.models.base.CommandType;
import com.mehdiii.duelgame.utils.AvatarHelper;
import com.mehdiii.duelgame.utils.DuelBroadcastReceiver;
import com.mehdiii.duelgame.utils.DuelMusicPlayer;
import com.mehdiii.duelgame.utils.FontHelper;
import com.mehdiii.duelgame.utils.OnMessageReceivedListener;
import com.mehdiii.duelgame.utils.ScoreHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class WaitingActivity extends ParentActivity {
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

//    DuelMusicPlayer musicPlayer;
    User opponentUser = new User();

    BroadcastReceiver mListener = new DuelBroadcastReceiver(new OnMessageReceivedListener() {
        @Override
        public void onReceive(String json, CommandType type) {
            switch (type) {
                case RECEIVE_OPPONENT_DATA:
                    receiveOpponentDataListener(json);
                    break;
                case RECEIVE_START_PLAYING:
                    receiveStartPlayingListener();
                    break;
                case RECEIVE_GAME_DATA:
q                    receiveGameDataListener(json);
                    break;
            }
        }
    });

    private void receiveOpponentDataListener(String json) {
        OpponentCollection collection = BaseModel.deserialize(json, OpponentCollection.class);

        if (collection == null || collection.getOpponents() == null)
            return;

        opponentUser = collection.getOpponents().get(0);

        ((ImageView) findViewById(R.id.waiting_opponent_avatar)).setImageResource(AvatarHelper.getResourceId(WaitingActivity.this, opponentUser.getAvatar()));
        setTextView(opponentName, opponentUser.getName());
        setTextView(opponentProvince, ProvinceManager.get(WaitingActivity.this, opponentUser.getProvince()));
        setTextView(opponentTitle, ScoreHelper.getTitle(opponentUser.getScore()));

        translateAnimation(opponentLayout, "translationY", 500, 0, 1500);
    }

    private void receiveStartPlayingListener() {
        Intent i = new Intent(getApplicationContext(), PlayGameActivity.class);
        i.putExtra(PlayGameActivity.ARGUMENT_OPPONENT, opponentUser.serialize());
        if (HeartTracker.getInstance().useHeart()) {
            startActivity(i);
            finish();
        }
    }

    private void receiveGameDataListener(String json) {
        ProblemCollection collection = BaseModel.deserialize(json, ProblemCollection.class);
        if (collection == null)
            return;

        questionsToAsk = collection.getQuestions();

        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
        scheduledExecutorService.schedule(new Runnable() {
            @Override
            public void run() {
                if (hasLeft == true)
                    return;
                DuelApp.getInstance().sendMessage(new BaseModel(CommandType.SEND_READY_TO_PLAY).serialize());
            }
        }, 4, TimeUnit.SECONDS);
    }

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

        LocalBroadcastManager.getInstance(this).registerReceiver(mListener, new IntentFilter("MESSAGE"));

        User user = AuthManager.getCurrentUser();

        // TODO-2 DELETE THIS
//        user.setName("mehdiiiii");
//        user.setProvince(20);
//        user.setAvatar(15);
//        user.setScore(250);
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

//        musicPlayer = new DuelMusicPlayer(WaitingActivity.this, R.raw.waiting, true);
//        musicPlayer.execute();
    }

    public void setTextView(TextView tv, String str) {
        tv.setText(str);
    }

    @Override
    public void onPause() {
        super.onPause();

//        musicPlayer.pauseSound();
//        Intent svc = new Intent(this, MusicPlayer.class);
//        stopService(svc);
    }

    @Override
    public void onResume() {
        super.onResume();
//        setData();

//        musicPlayer.playSound();
//        Intent svc = new Intent(this, MusicPlayer.class);
//        startService(svc);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mListener);
    }

    @Override
    public void onBackPressed() {
        hasLeft = true;
        DuelApp.getInstance().sendMessage(new BaseModel(CommandType.SEND_USER_LEFT_GAME).serialize());
        finish();
    }
}
