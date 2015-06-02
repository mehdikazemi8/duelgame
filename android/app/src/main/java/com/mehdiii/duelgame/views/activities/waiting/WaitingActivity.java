package com.mehdiii.duelgame.views.activities.waiting;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mehdiii.duelgame.DuelApp;
import com.mehdiii.duelgame.R;
import com.mehdiii.duelgame.managers.AuthManager;
import com.mehdiii.duelgame.managers.HeartTracker;
import com.mehdiii.duelgame.models.Category;
import com.mehdiii.duelgame.models.ChallengeRequestDecision;
import com.mehdiii.duelgame.models.OpponentCollection;
import com.mehdiii.duelgame.models.ProblemCollection;
import com.mehdiii.duelgame.models.User;
import com.mehdiii.duelgame.models.WannaChallenge;
import com.mehdiii.duelgame.models.base.BaseModel;
import com.mehdiii.duelgame.models.base.CommandType;
import com.mehdiii.duelgame.utils.DuelBroadcastReceiver;
import com.mehdiii.duelgame.utils.FontHelper;
import com.mehdiii.duelgame.utils.OnMessageReceivedListener;
import com.mehdiii.duelgame.views.OnCompleteListener;
import com.mehdiii.duelgame.views.activities.ParentActivity;
import com.mehdiii.duelgame.views.activities.PlayGameActivity;
import com.mehdiii.duelgame.views.activities.waiting.fragments.FindingOpponentFragment;
import com.mehdiii.duelgame.views.activities.waiting.fragments.UserFragment;
import com.mehdiii.duelgame.views.dialogs.AlertDialog;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class WaitingActivity extends ParentActivity {
    boolean hasLeft;

    private final String USER_FRAGMENT_TAG = "user_fragment";
    private final String OPPONENT_FRAGMENT_TAG = "opponent_fragment";
    private final String FINDING_OPPONENT_FRAGMENT_TAG = "finding_opponent_fragment";

    private TextView waitingAgainst;

    private Fragment userFragment;
    private Fragment opponentFragment;
    private Fragment findingOpponentFragment;

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
                    receiveGameDataListener(json);
                    break;
                case RECEIVE_CHALLENGE_REQUEST_DECISION:
                    receiveChallengeRequestDecisionListener(json);
                    break;
            }
        }
    });

    private void receiveChallengeRequestDecisionListener(String json) {
        ChallengeRequestDecision decision = BaseModel.deserialize(json, ChallengeRequestDecision.class);
        String message = "";

        if (decision == null)
            return;

        switch (decision.getDecision()) {
            case 0:
                message = getResources().getString(R.string.messege_duel_request_denied);
                break;
            case 1:
                break;
            case 2:
                message = getResources().getString(R.string.message_duel_request_playing);
                break;
            case 3:
                message = getResources().getString(R.string.message_duel_request_offline);
                break;
        }
        if (decision.getDecision() != 1) {
            AlertDialog dialog = new AlertDialog(WaitingActivity.this, message);
            dialog.setCancelable(false);

            dialog.setOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(Object data) {
                    finish();
                }
            });

            dialog.show();
        }
    }

    private void receiveOpponentDataListener(String json) {
        OpponentCollection collection = BaseModel.deserialize(json, OpponentCollection.class);

        if (collection == null || collection.getOpponents() == null)
            return;

        opponentUser = collection.getOpponents().get(0);

        opponentFragment = Fragment.instantiate(this, UserFragment.class.getName(), null);
        Bundle opponentBundle = new Bundle();
        opponentBundle.putInt("layout", R.layout.fragment_waiting_opponent);
        opponentBundle.putString("user", opponentUser.serialize());
        opponentFragment.setArguments(opponentBundle);

        getFragmentManager().popBackStack();

        getFragmentManager().beginTransaction().setCustomAnimations(
                R.animator.slide_up_from_bottom, R.animator.slide_down_to_bottom, R.animator.slide_up_from_bottom, R.animator.slide_down_to_bottom
        ).add(R.id.opponent_fragment_holder, opponentFragment, OPPONENT_FRAGMENT_TAG).addToBackStack(null).commit();

//        ((ImageView) findViewById(R.id.waiting_opponent_avatar)).setImageResource(AvatarHelper.getResourceId(WaitingActivity.this, opponentUser.getAvatar()));
//        setTextView(opponentName, opponentUser.getName());
//        setTextView(opponentProvince, ProvinceManager.get(WaitingActivity.this, opponentUser.getProvince()));
//        setTextView(opponentTitle, ScoreHelper.getTitle(opponentUser.getScore()));
//        translateAnimation(opponentLayout, "translationY", 500, 0, 1500);

    }

    private void receiveStartPlayingListener() {
        Intent i = new Intent(getApplicationContext(), PlayGameActivity.class);
        i.putExtra(PlayGameActivity.ARGUMENT_OPPONENT, opponentUser.serialize());
        if (HeartTracker.getInstance().useHeart(WaitingActivity.this)) {
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

        if (getIntent().getExtras() != null) {
            String userNumber = getIntent().getExtras().getString("user_number", null);
            int category = getIntent().getExtras().getInt("category", -1);
            String message = getIntent().getExtras().getString("message", getResources().getString(R.string.message_duel_with_friends_default));

            if (userNumber != null) {
                WannaChallenge challenge = new WannaChallenge(userNumber, category, message);
                DuelApp.getInstance().sendMessage(challenge.serialize(CommandType.SEND_WANNA_CHALLENGE));
            }
        }

        waitingAgainst = (TextView) findViewById(R.id.waiting_against);

        hasLeft = false;

        LocalBroadcastManager.getInstance(this).registerReceiver(mListener, new IntentFilter("MESSAGE"));

//        User user = AuthManager.getCurrentUser();
//        userAvatar.setImageResource(AvatarHelper.getResourceId(this, user.getAvatar()));
//        setTextView(userName, user.getName());
//        setTextView(userTitle, ScoreHelper.getTitle(user.getScore()));
//        setTextView(userProvince, ProvinceManager.get(this, user.getProvince()));
//        translateAnimation(playerLayout, "translationY", -500, 0, 1500);

        FontHelper.setKoodakFor(getApplicationContext(), waitingAgainst);

//        musicPlayer = new DuelMusicPlayer(WaitingActivity.this, R.raw.waiting, true);
//        musicPlayer.execute();

        userFragment = Fragment.instantiate(this, UserFragment.class.getName(), null);
        Bundle userBundle = new Bundle();
        userBundle.putInt("layout", R.layout.fragment_waiting_user);
        userBundle.putString("user", AuthManager.getCurrentUser().serialize());
        userFragment.setArguments(userBundle);
//        getFragmentManager().beginTransaction().add(R.id.user_fragment_holder, userFragment).commit();
        getFragmentManager().beginTransaction().setCustomAnimations(R.animator.slide_down_from_top, R.animator.slide_up_to_top, R.animator.slide_down_from_top, R.animator.slide_up_to_top)
                .add(R.id.user_fragment_holder, userFragment, USER_FRAGMENT_TAG)
                .addToBackStack(null).commit();

        findingOpponentFragment = Fragment.instantiate(this, FindingOpponentFragment.class.getName(), null);
//        getFragmentManager().beginTransaction().add(R.id.finding_opponent_fragment_holder, findingOpponentFragment).commit();
        getFragmentManager().beginTransaction().setCustomAnimations(
                R.animator.slide_up_from_bottom,
                R.animator.slide_up_to_top,
                R.animator.slide_up_from_bottom,
                R.animator.slide_up_to_top)
                .add(R.id.finding_opponent_fragment_holder, findingOpponentFragment, FINDING_OPPONENT_FRAGMENT_TAG)
                .addToBackStack(null).commit();
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
