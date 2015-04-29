package com.mehdiii.duelgame.views.activities;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
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

import com.mehdiii.duelgame.DuelApp;
import com.mehdiii.duelgame.R;
import com.mehdiii.duelgame.managers.AuthManager;
import com.mehdiii.duelgame.models.User;
import com.mehdiii.duelgame.models.base.BaseModel;
import com.mehdiii.duelgame.utils.AvatarHelper;
import com.mehdiii.duelgame.utils.FontHelper;
import com.mehdiii.duelgame.utils.ScoreHelper;
import com.mehdiii.duelgame.views.custom.AppRater;
import com.mehdiii.duelgame.views.custom.ProgressBarAnimation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class GameResultActivity extends MyBaseActivity {
    public static final String ARGUMENT_OPPONENT = "argument_opponent";
    public static final String ARGUMENT_RESULT_INFO = "argument_result_info";

    User opponentUser;

    String resultInfo;
    int gameStatus;
    int earnedDiamond;
    String gameVerdict;

    int WIN, LOSE;
    MediaPlayer myPlayer;

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

    private Button gameResultDuelOthers;
    private Button gameResultAddFriend;
    private Button gameResultReport;

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

    ArrayList<ObjectAnimator> allAnimations;

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void animateProgress() {
        int levelBefore = ScoreHelper.getLevel(user.getScore());
        int progressBefore = ScoreHelper.getThisLevelPercentage(user.getScore());

        final int levelAfter = ScoreHelper.getLevel(user.getScore() + totalXP);
        final int progressAfter = ScoreHelper.getThisLevelPercentage(user.getScore() + totalXP);

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
        if(gameStatus == -1)
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
            AppRater ar = new AppRater();
            ar.init(GameResultActivity.this, (gameStatus == 1));
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
                if(start == end)
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

        user.setScore(user.getScore() + userPoints);

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


        // TODO onResum? onWindowChangedFocus? onCreate?
        for (int i = 0; i < allAnimations.size(); i++)
            allAnimations.get(i).start();
        animateProgress();
    }

    private void configureControls() {
        FontHelper.setKoodakFor(getApplicationContext(),
                gameResultStatus,
                userName, userPointsTextView,
                opponentName, opponentPointsTextView,
                gameResultT1, gameResultT2, gameResultT3, gameResultT4,
                gameResultT5, gameResultT6, gameResultT7, gameResultT8,
                gameResultPositivePoints, gameResultWinBonus, gameResultPointFactor, gameResultTotalExperience,
                gameResultDiamondCnt, gameResultLevelText,
                gameResultAddFriend, gameResultDuelOthers, gameResultReport);


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
            bonus = earnedDiamond + 120;
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
        gameResultAddFriend = (Button) findViewById(R.id.game_result_add_friend);
        gameResultDuelOthers = (Button) findViewById(R.id.game_result_duel_others);
        gameResultReport = (Button) findViewById(R.id.game_result_report);
    }

    private void readArguments() {
        Bundle params = getIntent().getExtras();
        if (params == null)
            return;

        String json = params.getString(ARGUMENT_OPPONENT, "");
        resultInfo = params.getString(ARGUMENT_RESULT_INFO, "");
        collectedDiamond = params.getInt("collectedDiamond", 0);

        if (!json.isEmpty()) {
            opponentUser = BaseModel.deserialize(json, User.class);
        }
    }

    public void setTextView(int viewId, String s) {
        ((TextView) findViewById(viewId)).setText(s);
    }

    public void addToFriends(View v) {
        boolean everythingOK = true;
        JSONObject query = new JSONObject();
        try {
            query.put("code", "AF");
            query.put("user_number", opponentUser.getId());

            DuelApp.getInstance().sendMessage(query.toString());
        } catch (JSONException e) {
            everythingOK = false;
        }

        if (everythingOK == true) {
            String msg = "به لیست دوستان شما اضافه شد.";
            Toast toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP, 0, 10);
            toast.show();
        }
    }

    public void duelWithOthers(View v) {
        JSONObject query = new JSONObject();
        try {
            query.put("code", "WP");
            query.put("category", category);

            DuelApp.getInstance().sendMessage(query.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        startActivity(new Intent(this, WaitingActivity.class));
        this.finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
