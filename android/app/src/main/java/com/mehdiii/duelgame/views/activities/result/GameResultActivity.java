package com.mehdiii.duelgame.views.activities.result;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.mehdiii.duelgame.DuelApp;
import com.mehdiii.duelgame.R;
import com.mehdiii.duelgame.managers.AuthManager;
import com.mehdiii.duelgame.models.Category;
import com.mehdiii.duelgame.models.FriendList;
import com.mehdiii.duelgame.models.FriendRequest;
import com.mehdiii.duelgame.models.MutualCourseStat;
import com.mehdiii.duelgame.models.MutualStats;
import com.mehdiii.duelgame.models.PVsPStatRequest;
import com.mehdiii.duelgame.models.RemoveFriend;
import com.mehdiii.duelgame.models.User;
import com.mehdiii.duelgame.models.WannaChallenge;
import com.mehdiii.duelgame.models.base.BaseModel;
import com.mehdiii.duelgame.models.base.CommandType;
import com.mehdiii.duelgame.models.events.OnPurchaseResult;
import com.mehdiii.duelgame.utils.AvatarHelper;
import com.mehdiii.duelgame.utils.DuelBroadcastReceiver;
import com.mehdiii.duelgame.utils.FontHelper;
import com.mehdiii.duelgame.utils.OnMessageReceivedListener;
import com.mehdiii.duelgame.utils.ScoreHelper;
import com.mehdiii.duelgame.views.OnCompleteListener;
import com.mehdiii.duelgame.views.activities.ParentActivity;
import com.mehdiii.duelgame.views.activities.home.HomeActivity;
import com.mehdiii.duelgame.views.activities.waiting.WaitingActivity;
import com.mehdiii.duelgame.views.custom.AppRater;
import com.mehdiii.duelgame.views.custom.ProgressBarAnimation;
import com.mehdiii.duelgame.views.dialogs.ProfileDialog;
import com.mehdiii.duelgame.views.dialogs.ReviewQuestionsDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

public class GameResultActivity extends ParentActivity {
    public static final String ARGUMENT_OPPONENT = "argument_opponent";
    public static final String ARGUMENT_DIAMOND = "collected_diamond";
    public static final String ARGUMENT_RESULT_INFO = "argument_result_info";
    public static final String ARGUMENT_CORRECT_OPTIONS = "argument_correct_options";

    User opponentUser;

    String resultInfo;
    int gameStatus;
    int earnedDiamond;
    String gameVerdict;

    int WIN, LOSE;
    MediaPlayer myPlayer;
    ArrayList<ObjectAnimator> allAnimations;
    ArrayList<Integer> correctOptionsArray = new ArrayList<Integer>();

    private TextView gameResultStatus;
    private ImageView opponentAvatar;
    private TextView opponentName;
    private TextView opponentPointsTextView;
    private ImageView userAvatar;
    private TextView userName;
    private TextView userPointsTextView;
    private TextView gameResultT7;
    private TextView gameResultT8;
    private TextView gameResultT5;
    private TextView gameResultT6;
    private TextView gameResultT3;
    private TextView gameResultT4;
    private TextView gameResultT1;
    private TextView gameResultT2;
    private TextView gameResultTotalExperience;
    private TextView gameResultPointFactor;
    private TextView gameResultWinBonus;
    private TextView gameResultPositivePoints;
    private ImageView gameResultLevelStar;
    private ProgressBar gameResultLevelProgress;
    private TextView gameResultLevelText;
    private TextView gameResultDiamondCnt;
    private ImageView gameResultDiamondPicture;

    TextView winCounter, loseCounter, drawCounter;
    TextView winCaption, loseCaption, drawCaption;

    private Button gameResultDuelOthers;
    private Button gameResultReport;
    private Button gameResultHome;

    int positivePoints, winBonus, pointFactor, totalXP;
    User user = null;
    int collectedDiamond;

    private static <T> ObjectAnimator translateAnimation(final T imageView, String cmd, int duration, int startDelay, float... dx) {
        ObjectAnimator animation = ObjectAnimator.ofFloat(imageView, cmd, dx);
        animation.setDuration(duration);
        animation.setStartDelay(startDelay);
        animation.setInterpolator(new DecelerateInterpolator());

        animation.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                ((View) imageView).setVisibility(View.VISIBLE);
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
        return animation;
    }

    public ArrayList<ObjectAnimator> bothScaleAniamtion(final TextView textView, int duration, int startDelay, float... path) {
        ArrayList<ObjectAnimator> result = new ArrayList<ObjectAnimator>();
        result.add(scaleAnimation(textView, "scaleX", duration, startDelay, path));
        result.add(scaleAnimation(textView, "scaleY", duration, startDelay, path));
        return result;
    }

    public ObjectAnimator scaleAnimation(final TextView textView, String cmd, int duration, int startDelay, float... path) {
        ObjectAnimator animation = ObjectAnimator.ofFloat(textView, cmd, path);
        animation.setDuration(duration);
        animation.setStartDelay(startDelay);
        animation.setInterpolator(new DecelerateInterpolator());

        animation.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                textView.setVisibility(View.VISIBLE);
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

        return animation;
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    private void animateProgress() {
        int levelBefore = ScoreHelper.getLevel(user.getScore());
        int progressBefore = ScoreHelper.getThisLevelPercentage(user.getScore());

        final int levelAfter = ScoreHelper.getLevel(user.getScore() + totalXP);
        final int progressAfter = ScoreHelper.getThisLevelPercentage(user.getScore() + totalXP);

        Log.d("gameResult", "user.getScore() " + user.getScore());
        Log.d("gameResult", "totalXP " + totalXP);
        Log.d("gameResult", "levelBefore " + levelBefore);
        Log.d("gameResult", "progressBefore " + progressBefore);
        Log.d("gameResult", "levelAfter " + levelAfter);
        Log.d("gameResult", "progressAfter " + progressAfter);

        if (levelBefore == levelAfter) {
            ProgressBarAnimation anim = new ProgressBarAnimation(gameResultLevelProgress, progressBefore, progressAfter);
            anim.setDuration(1000);
            anim.setStartOffset(3600);
            anim.setInterpolator(new DecelerateInterpolator());
            anim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    prepareIncreaseInTextView();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
            gameResultLevelProgress.startAnimation(anim);
        } else {
            AnimationSet all = new AnimationSet(false);

            ProgressBarAnimation anim1 = new ProgressBarAnimation(gameResultLevelProgress, progressBefore, 100);
            anim1.setDuration(750);
            anim1.setStartOffset(3600);
            anim1.setInterpolator(new DecelerateInterpolator());
            anim1.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    gameResultLevelText.setText("" + levelAfter);

                    ProgressBarAnimation anim2 = new ProgressBarAnimation(gameResultLevelProgress, 0, progressAfter);
                    anim2.setDuration(750);
                    anim2.setStartOffset(250);
                    anim2.setInterpolator(new DecelerateInterpolator());
                    anim2.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                            gameResultLevelProgress.setProgress(0);
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            prepareIncreaseInTextView();
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {
                        }
                    });

                    gameResultLevelProgress.startAnimation(anim2);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
            all.addAnimation(anim1);

            gameResultLevelProgress.startAnimation(all);
        }
    }

    private void prepareIncreaseInTextView() {
        if (gameStatus == -1)
            return;

        for (int i = 1; i <= collectedDiamond; i++)
            if (collectedDiamond / i <= 20) {
                animateIncreaseInTextView(i, 2000 / collectedDiamond * i,
                        user.getDiamond() - collectedDiamond, user.getDiamond(), gameResultDiamondCnt);
                break;
            }
    }

    private void animateIncreaseInTextView(final int plusThis, final int duration, final int start, final int end, final TextView tv) {
        if (start == end) {
            AppRater.show(GameResultActivity.this, (gameStatus == 1));
        }

        Animation increase = new AlphaAnimation(0f, 1f);
        increase.setDuration(0);
        increase.setStartOffset(duration);
        increase.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                tv.setText("" + start);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (start == end)
                    return;

                if (start + plusThis > end)
                    animateIncreaseInTextView(plusThis, duration, end, end, tv);
                else
                    animateIncreaseInTextView(plusThis, duration, start + plusThis, end, tv);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        tv.startAnimation(increase);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_result);

        user = AuthManager.getCurrentUser();

        WIN = R.raw.win;
        LOSE = R.raw.lose;

        findControls();

        readArguments();

        configureControls();

        if (gameStatus >= 0)
            user.addDiamond(collectedDiamond);

        allAnimations = new ArrayList<ObjectAnimator>();

        allAnimations.add(translateAnimation(userAvatar, "translationX", 1000, 0, 500, 0));
        allAnimations.add(translateAnimation(userName, "translationX", 1000, 0, 500, 0));
        allAnimations.add(translateAnimation(userPointsTextView, "translationX", 1000, 0, 500, 0));
        allAnimations.add(translateAnimation(opponentAvatar, "translationX", 1000, 0, -500, 0));
        allAnimations.add(translateAnimation(opponentName, "translationX", 1000, 0, -500, 0));
        allAnimations.add(translateAnimation(opponentPointsTextView, "translationX", 1000, 0, -500, 0));

        allAnimations.addAll(bothScaleAniamtion(gameResultStatus, 1000, 1000, 0f, 1f));

        allAnimations.addAll(bothScaleAniamtion(gameResultT2, 100, 2000, 0f, 1.1f, 1f));
        allAnimations.addAll(bothScaleAniamtion(gameResultT1, 100, 2000, 0f, 1.1f, 1f));
        allAnimations.addAll(bothScaleAniamtion(gameResultPositivePoints, 300, 2100, 0f, 1.1f, 1f));

        allAnimations.addAll(bothScaleAniamtion(gameResultT4, 100, 2400, 0f, 1.1f, 1f));
        allAnimations.addAll(bothScaleAniamtion(gameResultT3, 100, 2400, 0f, 1.1f, 1f));
        allAnimations.addAll(bothScaleAniamtion(gameResultWinBonus, 300, 2500, 0f, 1.1f, 1f));

        allAnimations.addAll(bothScaleAniamtion(gameResultT6, 100, 2800, 0f, 1.1f, 1f));
        allAnimations.addAll(bothScaleAniamtion(gameResultT5, 100, 2800, 0f, 1.1f, 1f));
        allAnimations.addAll(bothScaleAniamtion(gameResultPointFactor, 300, 2900, 0f, 1.1f, 1f));

        allAnimations.addAll(bothScaleAniamtion(gameResultT8, 100, 3200, 0f, 1.1f, 1f));
        allAnimations.addAll(bothScaleAniamtion(gameResultT7, 100, 3200, 0f, 1.1f, 1f));
        allAnimations.addAll(bothScaleAniamtion(gameResultTotalExperience, 300, 3300, 0f, 1.1f, 1f));

        // TODO onResume? onWindowChangedFocus? onCreate?
        for (int i = 0; i < allAnimations.size(); i++)
            allAnimations.get(i).start();
        animateProgress();
        user.setScore(user.getScore() + totalXP);
        // score of this category is updated
        user.addScore(ParentActivity.category, totalXP);
        EventBus.getDefault().post(new OnPurchaseResult());
    }

    private void configureControls() {
        FontHelper.setKoodakFor(getApplicationContext(),
                opponentName, opponentPointsTextView, gameResultStatus,
                userName, userPointsTextView, gameResultT1,
                gameResultT2, gameResultT3, gameResultT4,
                gameResultT5, gameResultT6, gameResultT7, gameResultT8,
                gameResultPositivePoints, gameResultWinBonus, gameResultPointFactor,
                gameResultDiamondCnt, gameResultLevelText, gameResultTotalExperience,
                gameResultDuelOthers, gameResultReport, gameResultHome,
                winCounter, loseCounter, drawCounter,
                winCaption, loseCaption, drawCaption);

        userAvatar.setImageResource(AvatarHelper.getResourceId(GameResultActivity.this, user.getAvatar()));
        userName.setText(user.getName());
        userPointsTextView.setText("" + userPoints);

        opponentAvatar.setImageResource(AvatarHelper.getResourceId(GameResultActivity.this, opponentUser.getAvatar()));
        opponentName.setText(opponentUser.getName());
        opponentPointsTextView.setText("" + opponentPoints);

        gameStatus = 0;
        JSONObject resultParser = null;
        if (!resultInfo.isEmpty()) {
            try {
                resultParser = new JSONObject((new JSONObject(resultInfo)).getString("user"));

                positivePoints = resultParser.getInt("positive_points");
                winBonus = resultParser.getInt("win_bonus");
                pointFactor = user.getScoreFactor();
                totalXP = (positivePoints + winBonus) * pointFactor;
                gameStatus = resultParser.getInt("result");
                userPoints = resultParser.getInt("points");

                opponentPoints =
                        new JSONObject(
                                new JSONArray(
                                        (new JSONObject(resultInfo)).getString("opponents")
                                ).get(0).toString()
                        ).getInt("points");

                gameResultPositivePoints.setText("" + positivePoints);
                gameResultWinBonus.setText("" + winBonus);
                gameResultPointFactor.setText("" + pointFactor);
                gameResultTotalExperience.setText("" + totalXP);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        int bonus = 0, musicId;
        if (gameStatus == 0) {
            bonus = earnedDiamond;
            gameVerdict = "مساوی شد";
            musicId = LOSE;
        } else if (gameStatus == 1) {
            bonus = earnedDiamond;
            gameVerdict = "بردی";
            gameResultStatus.setTextColor(getResources().getColor(R.color.correct_answer));
            musicId = WIN;
        } else {
            // nothing
            gameVerdict = "باختی";
            gameResultStatus.setTextColor(getResources().getColor(R.color.wrong_answer));
            musicId = LOSE;
        }

        gameResultStatus.setText(gameVerdict);

        myPlayer = MediaPlayer.create(GameResultActivity.this, musicId);
        user.addDiamond(bonus);
        myPlayer.start();

        gameResultLevelText.setText("" + ScoreHelper.getLevel(user.getScore()));
        gameResultLevelProgress.setProgress(ScoreHelper.getThisLevelPercentage(user.getScore()));
        gameResultDiamondCnt.setText("" + user.getDiamond());
    }

    private void findControls() {
        gameResultStatus = (TextView) findViewById(R.id.game_result_status);
        opponentAvatar = (ImageView) findViewById(R.id.game_result_opponent_avatar);
        opponentName = (TextView) findViewById(R.id.game_result_opponent_name);
        opponentPointsTextView = (TextView) findViewById(R.id.game_result_opponent_points);
        userAvatar = (ImageView) findViewById(R.id.game_result_user_avatar);
        userName = (TextView) findViewById(R.id.game_result_user_name);
        userPointsTextView = (TextView) findViewById(R.id.game_result_player_points);
        gameResultT7 = (TextView) findViewById(R.id.game_result_t7);
        gameResultT8 = (TextView) findViewById(R.id.game_result_t8);
        gameResultT5 = (TextView) findViewById(R.id.game_result_t5);
        gameResultT6 = (TextView) findViewById(R.id.game_result_t6);
        gameResultT3 = (TextView) findViewById(R.id.game_result_t3);
        gameResultT4 = (TextView) findViewById(R.id.game_result_t4);
        gameResultT1 = (TextView) findViewById(R.id.game_result_t1);
        gameResultT2 = (TextView) findViewById(R.id.game_result_t2);
        gameResultTotalExperience = (TextView) findViewById(R.id.game_result_total_experience);
        gameResultPointFactor = (TextView) findViewById(R.id.game_result_point_factor);
        gameResultWinBonus = (TextView) findViewById(R.id.game_result_win_bonus);
        gameResultPositivePoints = (TextView) findViewById(R.id.game_result_positive_points);
        gameResultLevelStar = (ImageView) findViewById(R.id.game_result_level_star);
        gameResultLevelProgress = (ProgressBar) findViewById(R.id.game_result_level_progress);
        gameResultLevelText = (TextView) findViewById(R.id.game_result_level_text);
        gameResultDiamondCnt = (TextView) findViewById(R.id.game_result_diamond_cnt);
        gameResultDiamondPicture = (ImageView) findViewById(R.id.game_result_diamond_picture);
        gameResultHome = (Button) findViewById(R.id.game_result_home);
        gameResultDuelOthers = (Button) findViewById(R.id.game_result_duel_others);
        gameResultReport = (Button) findViewById(R.id.game_result_report);

        winCounter = (TextView) findViewById(R.id.win_counter);
        loseCounter = (TextView) findViewById(R.id.lose_counter);
        drawCounter = (TextView) findViewById(R.id.draw_counter);
        winCaption = (TextView) findViewById(R.id.win_caption);
        loseCaption = (TextView) findViewById(R.id.lose_caption);
        drawCaption = (TextView) findViewById(R.id.draw_caption);
    }

    private void readArguments() {
        Bundle params = getIntent().getExtras();
        if (params == null)
            return;

        String json = params.getString(ARGUMENT_OPPONENT, "");
        resultInfo = params.getString(ARGUMENT_RESULT_INFO, "");
        collectedDiamond = params.getInt(ARGUMENT_DIAMOND, 0);
        correctOptionsArray = params.getIntegerArrayList(ARGUMENT_CORRECT_OPTIONS);

        if (!json.isEmpty()) {
            opponentUser = BaseModel.deserialize(json, User.class);
        }
    }

    public void addToFriends(View v) {
        boolean everythingOK = true;

        FriendRequest request = new FriendRequest(opponentUser.getId());
        DuelApp.getInstance().sendMessage(request.serialize(CommandType.SEND_ADD_FRIEND));

        if (everythingOK == true) {
            String msg = "به لیست دوستان شما اضافه شد.";
            Toast toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP, 0, 10);
            toast.show();
        }
    }

    public void goToHome(View view) {
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }

    public void wannaChallenge(View view) {
//        Category cat = Category.newInstance(Category.CategoryType.WANNA_PLAY);
//        ParentActivity.category = //String.valueOf(Integer.parseInt(view.getContentDescription().toString()) + 10000 + 1);
//        cat.setCategory(ParentActivity.category);
//        DuelApp.getInstance().sendMessage(cat.serialize());
//        startActivity(new Intent(this, WaitingActivity.class));
//        WannaChallenge challenge = new WannaChallenge(opponentUser.getId(), Integer.valueOf(category), getResources().getString(R.string.message_duel_with_friends_default));
//        DuelApp.getInstance().sendMessage(challenge.serialize(CommandType.SEND_WANNA_CHALLENGE));

        Intent i = new Intent(this, WaitingActivity.class);
        i.putExtra("user_number", opponentUser.getId());
        i.putExtra("category", Integer.valueOf(category));
        i.putExtra("message", getResources().getString(R.string.message_duel_with_friends_default));
        i.putExtra("master", true);
        startActivity(i);
        this.finish();
    }

//    public void duelWithOthers(View v) {
//        int categoryId;
//
//        try {
//            categoryId = Integer.parseInt(category);
//        } catch (NumberFormatException ex) {
//            ex.printStackTrace();
//            return;
//        }
//
////        WannaChallenge challenge = new WannaChallenge(opponentUser.getId(), categoryId, getResources().getString(R.string.message_duel_with_friends_default));
////
////        DuelApp.getInstance().sendMessage(challenge.serialize(CommandType.SEND_WANNA_CHALLENGE));
//
//        Intent i = new Intent(this, WaitingActivity.class);
//        i.putExtra("user_number", opponentUser.getId());
//        i.putExtra("category", categoryId);
//        i.putExtra("master", true);
//
//        startActivity(i);
//        finish();
//    }

    public void reviewQuestions(View view) {
        Tracker tracker = DuelApp.getInstance().getTracker(DuelApp.TrackerName.GLOBAL_TRACKER);
        // Build and send an Event.
        tracker.send(new HitBuilders.EventBuilder()
                .setCategory("button_click")
                .setAction("report_button")
                .setLabel("review_questions")
                .build());

        ReviewQuestionsDialog dialog = new ReviewQuestionsDialog();

        Bundle bundle = new Bundle();
        bundle.putIntegerArrayList(ARGUMENT_CORRECT_OPTIONS, correctOptionsArray);
        dialog.setArguments(bundle);

        dialog.show(getSupportFragmentManager(), "DIALOG_REVIEW_QUESTIONS");
    }

    @Override
    public boolean canHandleChallengeRequest() {
        return true;
    }

    @Override
    public OnCompleteListener onDecisionMadeListener() {
        return new OnCompleteListener() {
            @Override
            public void onComplete(Object data) {
                finish();
            }
        };
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, DuelApp.getInstance().getIntentFilter());
        DuelApp.getInstance().sendMessage(new PVsPStatRequest(CommandType.GET_ONE_VS_ONE_RESULTS, opponentUser.getId()).serialize());
    }

    private BroadcastReceiver broadcastReceiver = new DuelBroadcastReceiver(new OnMessageReceivedListener() {
        @Override
        public void onReceive(String json, CommandType type) {
            if(json.isEmpty())
                return;

            if (type == CommandType.RECEIVE_ONE_VS_ONE_RESULTS) {
                MutualStats mutualStats = MutualStats.deserialize(json, MutualStats.class);
                if(mutualStats == null || mutualStats.getResults() == null) {
                    Log.d("TAG", "error RECEIVE_ONE_VS_ONE_RESULTS GameResultActivity");
                    return;
                }

                for(MutualCourseStat courseStat : mutualStats.getResults()) {
                    if(courseStat.getCategory() != null && courseStat.getCategory().equals(ParentActivity.category)) {
                        winCounter.setText(String.valueOf(courseStat.getWin()));
                        loseCounter.setText(String.valueOf(courseStat.getLose()));
                        drawCounter.setText(String.valueOf(courseStat.getDraw()));
                    }
                }
            }
        }
    });
}
